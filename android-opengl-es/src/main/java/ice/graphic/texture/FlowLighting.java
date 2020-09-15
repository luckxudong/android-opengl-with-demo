package ice.graphic.texture;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * User: jason
 * Date: 12-2-16
 * Time: 下午12:24
 */
public class FlowLighting extends TextureModifier {

    public FlowLighting(long duration) {
        super(duration);

        lightingY = -halfWidth;
    }

    @Override
    protected boolean onStep(float interpolatedTime, Bitmap originalPixels) {

        int width = originalPixels.getWidth();
        int height = originalPixels.getHeight();

        lightingY = -halfWidth + (int) ((width + halfWidth * 2) * interpolatedTime);

        updatePixels(originalPixels, width, height);

        return true;
    }

    public void updatePixels(Bitmap originalPixels, int width, int height) {

        int[] newPixelsBuffer = new int[width * height];

        originalPixels.getPixels(newPixelsBuffer, 0, width, 0, 0, width, height);

        for (int i = Math.max(0, lightingY - halfWidth); i < Math.min(width, lightingY + halfWidth); i++) {

            for (int j = 0; j < height; j++) {
                int index = i + j * width;
                int originalPix = newPixelsBuffer[index];
                int alpha = Color.alpha(originalPix);

                if (alpha != 0) {
                    int adjustBrightness = 255 * (halfWidth - Math.abs(i - lightingY)) / halfWidth;

                    int red = Math.min(Color.red(originalPix) + adjustBrightness, 255);
                    int green = Math.min(Color.green(originalPix) + adjustBrightness, 255);
                    int blue = Math.min(Color.blue(originalPix) + adjustBrightness, 255);

                    newPixelsBuffer[index] = Color.argb(alpha, red, green, blue);
                }

            }
        }

        result.setPixels(newPixelsBuffer, 0, width, 0, 0, width, height);
    }

    private int halfWidth = 30;
    private int lightingY;
}
