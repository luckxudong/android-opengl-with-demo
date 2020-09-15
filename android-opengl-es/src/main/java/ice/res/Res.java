package ice.res;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import ice.util.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tosmart
 * Date: 11-10-20
 * Time: 下午4:38
 */
public class Res {
    private static Context context;
    private static Map<Integer, Bitmap> bitmapBuffer;
    private static Map<Integer, Bitmap[]> bitmapArrayBuffer;


    public static void built(Context context) {
        Res.context = context;
        bitmapBuffer = new HashMap<Integer, Bitmap>();
        bitmapArrayBuffer = new HashMap<Integer, Bitmap[]>();
    }

    public static Bitmap getBitmap(int resourceId) {

        Bitmap bitmap = bitmapBuffer.get(resourceId);

        if (bitmap != null) {
            bitmap.prepareToDraw();
        }
        else {
            bitmap = load(resourceId, true);
        }

        return bitmap;
    }

    public static Drawable getDrawable(int resourceId) {
        return null;
    }

    public static Typeface getTypeface() {
        return null;
    }

    private static Bitmap load(int resourceId, boolean pooled) {

        Bitmap bitmap = BitmapUtils.loadResource(context, resourceId);
        if (pooled)
            bitmapBuffer.put(resourceId, bitmap);

        return bitmap;
    }

    public static Bitmap[] loadArray(int resourceId, int totalCols) {
        return loadArray(resourceId, totalCols, totalCols);
    }

    public static Bitmap[] loadArray(int resourceId, int toBeLoadedCols, int totalCols) {

        if (bitmapArrayBuffer.containsKey(resourceId)) {
            return bitmapArrayBuffer.get(resourceId);
        }

        Bitmap bitmap = BitmapUtils.loadResource(context, resourceId);

        float cellWidth = bitmap.getWidth() * 1f / totalCols;
        int cellHeight = bitmap.getHeight();

        Bitmap[] array = new Bitmap[toBeLoadedCols];

        for (int i = 0; i < array.length; i++) {
            array[i] = BitmapUtils.slices(bitmap, (int) (cellWidth * i), 0, (int) cellWidth, cellHeight);
        }

        bitmap.recycle();

        bitmapArrayBuffer.put(resourceId, array);

        return array;
    }


    public static InputStream openAssets(String fileName) {

        AssetManager assets = context.getAssets();

        try {
            return assets.open(fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Context getContext() {
        return context;
    }


    public static String getText(int id) {
        return context.getText(id).toString();
    }
}
