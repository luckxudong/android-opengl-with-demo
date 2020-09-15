package ice.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;

/**
 * User: Mike.Hu
 * Date: 11-12-29
 * Time: 下午5:21
 */
public class XML2BeansUtil {

    public static final String CHARACTER_ENCODING = "GBK";

    public static final int DEFAULT_BUFFER_SIZE = 1024*100;



  // public volatile static boolean stopEnforce;

    private static XmlPullParserFactory factory;

    static {
        try {
            factory = XmlPullParserFactory.newInstance();
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 填充某个实体bean
     *
     * @param entity 将被填充的实体
     * @param in
     */
    public void fill(Entity entity, InputStream in) {

        if (entity == null || in == null)
            return;

        entity.beforeParse();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, CHARACTER_ENCODING), DEFAULT_BUFFER_SIZE);

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(br);

            parseDocument(entity, parser);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null) br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fill(Entity entity, Reader input)
	{
		if (null == entity || null == input)
			return;

		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(input, DEFAULT_BUFFER_SIZE);

			XmlPullParser parser = factory.newPullParser();
			parser.setInput(reader);

			parseDocument(entity, parser);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != reader)
					reader.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

    private void parseDocument(Entity entity, XmlPullParser parser) throws XmlPullParserException, IOException {

        String tag = null;
        //stopEnforce=false;


        for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {

            switch (eventType) {

                case XmlPullParser.START_TAG:// 元素开始
                    tag = onStart(entity, parser);
                    break;

                case XmlPullParser.TEXT:// text内容
                    onText(entity, tag, parser);
                    break;

                case XmlPullParser.END_TAG:
                    entity.noticeEnd(parser.getName());
                    break;

                default:
                    break;
            }

        }

    }

    private String onStart(Entity entity, XmlPullParser parser) {
        String statue = parser.getName();
        entity.onStartTag(statue);

        int attNum = parser.getAttributeCount();
        for (int i = 0; i < attNum; i++) {
            entity.setAttributeByAtt(
                    parser.getAttributeName(i),
                    parser.getAttributeValue(i)
            );
        }

        return statue;
    }

    private void onText(Entity entity, String statue, XmlPullParser parser) {
        String text = parser.getText().trim();
        if (text.length() > 0)
            entity.setAttributeByTag(statue, text);

    }


}
