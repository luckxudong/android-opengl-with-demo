package ice.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;

import java.io.*;

/**
 * User: tosmart
 * Date: 11-5-24
 */
public final class BitmapUtils {

    private static final int PNG_SAVE_QUALITY = 100;

    private BitmapUtils() {
    }

    public static Bitmap loadResource(Context context, int resourceId) {

        Resources resources = context.getResources();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = resources.getDisplayMetrics().densityDpi;
        options.inPurgeable = true;

        return BitmapFactory.decodeResource(
                resources,
                resourceId,
                options
        );
    }

    public static byte[] toBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, PNG_SAVE_QUALITY, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap loadFromBytes(byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap slices(Bitmap bitmap, int left, int top, int width, int height) {

        if (left < 0) left = 0;
        if (top < 0) top = 0;

        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        if (width > bitmap.getWidth()) width = bitmap.getWidth();
        if (height > bitmap.getWidth()) height = bitmap.getHeight();

        return Bitmap.createBitmap(bitmap, left, top, width, height);
    }

    public static Bitmap slices(Bitmap bitmap, Rect rect) {
        return slices(bitmap, rect.left, rect.top, rect.width(), rect.height());
    }

    public static Bitmap filtrateColor(Bitmap source, int color, int colorReplace) {

        final int width = source.getWidth();
        final int height = source.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (source.getPixel(x, y) == color) {
                    source.setPixel(x, y, colorReplace);
                }
            }
        }

        return source;
    }

    public static Bitmap loadFile(Context context, String fileName) {

        Resources resources = context.getResources();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = resources.getDisplayMetrics().densityDpi;

        return BitmapFactory.decodeFile(fileName, options);
    }

    public static boolean writePng(Bitmap bitmap, File file) {

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, PNG_SAVE_QUALITY, os);
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (os != null) try {
                os.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {

        Matrix matrix = new Matrix();

        matrix.setRotate(
                degrees,
                (float) bitmap.getWidth() / 2,
                (float) bitmap.getHeight() / 2
        );

        return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                true
        );
    }

    public static Bitmap scale(Bitmap org, float xRate, float yRate) {

        final int orgWidth = org.getWidth();
        final int orgHeight = org.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(xRate, yRate);

        return Bitmap.createBitmap(
                org,
                0,
                0,
                orgWidth,
                orgHeight,
                matrix,
                true
        );
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param original 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap original) {

        int width = original.getWidth();
        int height = original.getHeight();

        Bitmap grayscale = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.RGB_565
        );

        Canvas canvas = new Canvas(grayscale);
        Paint paint = new Paint();

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // 饱和度

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);

        canvas.drawBitmap(original, 0, 0, paint);

        return grayscale;
    }

    /**
     * 把图片变成圆角
     *
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(0xFF424242);

        canvas.drawARGB(0, 0, 0, 0);

        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        canvas.drawRoundRect(rectF, pixels, pixels, paint);

        paint.setXfermode(
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        );

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
