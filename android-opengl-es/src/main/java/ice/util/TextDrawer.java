package ice.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tosmart Date: 2010-9-29 Time: 9:14:29
 */
public class TextDrawer {

    public static void drawChar(Canvas canvas, Paint paint, char aChar, Rect region) {
        drawChar(canvas, paint, Character.toString(aChar), 0, region);
    }

    public static void drawHollowChar(Canvas canvas, Paint paint, char aChar, Rect region) {
        drawHollowChar(canvas, paint, Character.toString(aChar), 0, region);
    }

    public static void drawChar(
            Canvas canvas, Paint paint, String text, int charIndex, Rect region) {

        paint.setTextSize(getMatchTextSize(region));
        /* todo:
        if (MjResources.isPrepared()) {
            paint.setTypeface(MjResources.get().getTypeface());
        }
        */
        Rect bounds = new Rect();
        paint.getTextBounds(text, charIndex, charIndex + 1, bounds);
        Rect output = adjustPosition(region, bounds);

        canvas.drawText(
                text,
                charIndex,
                charIndex + 1,
                output.left - bounds.left,
                output.bottom - bounds.bottom,
                paint
        );
    }

    public static void drawHollowChar(
            Canvas canvas, Paint paint, String text, int charIndex, Rect region) {

        paint.setTextSize(getMatchTextSize(region));

        Rect bounds = new Rect();
        paint.getTextBounds(text, charIndex, charIndex + 1, bounds);
        Rect output = adjustPosition(region, bounds);

        Path path = new Path();

        paint.getTextPath(
                text,
                charIndex,
                charIndex + 1,
                output.left - bounds.left,
                output.bottom - bounds.bottom,
                path
        );

        final Paint.Style style = paint.getStyle();
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawPath(path, paint);

        paint.setStyle(style);
    }

    public static void drawTextInRegion(Canvas canvas, Paint paint, String text, Rect region) {

        int textSize = (int) paint.getTextSize();
        List<Integer> lines = split(text, paint, region);

        int startPos = 0;
        for (int row = 0; row < lines.size() && (row + 1) * textSize < region.height(); row++) {

            int endPos = startPos + lines.get(row);
            String line = text.substring(startPos, endPos);

            drawTextInLine(
                    canvas,
                    paint,
                    line,
                    new Rect(
                            region.left,
                            region.top + row * textSize,
                            region.right,
                            region.top + (row + 1) * textSize
                    ),
                    false
            );

            startPos = endPos;
        }
    }

    public static List<Integer> split(String text, Paint paint, Rect region) {

        if (region.width() == 0) {
            throw new RuntimeException("Region's width equals '0'.");
        }

        int textSize = (int) paint.getTextSize();
        int minWordsInLine = region.width() / textSize;

        List<Integer> lines = new ArrayList<Integer>();

        Rect bounds = new Rect();
        int startPos = 0;

        while (true) {

            int wordsInLine = minWordsInLine;
            if (startPos + wordsInLine >= text.length()) {
                lines.add(text.length() - startPos);
                break;
            }

            while (true) {

                int end = startPos + wordsInLine + 1;
                if (end > text.length()) break;

                paint.getTextBounds(text, startPos, end, bounds);
                if (bounds.width() > region.width()) break;

                wordsInLine++;
            }

            lines.add(wordsInLine);
            startPos += wordsInLine;
        }

        return lines;
    }

    /**
     * 单行文字绘制.
     *
     * @param canvas
     * @param paint
     * @param text
     * @param region
     * @param alignCenter
     * @return 文字真正占用的宽度
     */
    public static float drawTextInLine(
            Canvas canvas, Paint paint, String text, Rect region, boolean alignCenter) {

        paint = new Paint(paint);
        /* todo:
        if (MjResources.isPrepared()) {
            paint.setTypeface(MjResources.get().getTypeface());
        }
        */
        int regionWidth = region.width();
        int fontSize = region.height();

        if (regionWidth <= 0 || fontSize <= 0) {
            throw new IllegalArgumentException("region width or height must be positive !");
        }

        paint.setTextSize(fontSize);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > regionWidth) {
            paint.setTextSize(--fontSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        Rect output = new Rect(bounds);

        if (alignCenter) {
            output.offsetTo(
                    region.left + (regionWidth - bounds.width()) / 2,
                    region.top + (region.height() - bounds.height()) / 2
            );
        }
        else {
            output.offsetTo(region.left, region.top);
        }

        canvas.drawText(text, output.left - bounds.left, output.bottom - bounds.bottom, paint);

        return bounds.width();
    }

    private static Rect adjustPosition(Rect region, Rect bounds) {

        Rect output = new Rect(bounds);

        output.offsetTo(
                region.left + (region.width() - bounds.width()) / 2,
                region.top + (region.height() - bounds.height()) / 2
        );

        return output;
    }

    private static int getMatchTextSize(Rect region) {
        return Math.min(region.width(), region.height());
    }
}
