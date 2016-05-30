package com.rodionshkrobot.test.image.processors;


public class DifferenceProcessor {

    public static final double MAX_ALLOWED_DIFFERENCE = 255 * 0.10;

    /**
     * Returns the color difference between two colors if this difference is significant.
     * Difference is considered significant is it exceeds {@link DifferenceProcessor#MAX_ALLOWED_DIFFERENCE}.
     *
     * @param firstColor  first color to compare. Has to be an int representation of the color.
     * @param secondColor second color to compare. Has to be an int representation of the color.
     * @return Difference as an int representation or 0 if difference is non-significant.
     */
    public static int compareColors(int firstColor, int secondColor) {
        int alphaDifference = Math.abs(extractAlpha(secondColor) - extractAlpha(firstColor)),
                redDifference = Math.abs(extractRed(secondColor) - extractRed(firstColor)),
                greenDifference = Math.abs(extractGreen(secondColor) - extractGreen(firstColor)),
                blueDifference = Math.abs(extractBlue(secondColor) - extractBlue(firstColor));

        return MAX_ALLOWED_DIFFERENCE < (redDifference + greenDifference + blueDifference + alphaDifference) ?
                (alphaDifference << 24) | (redDifference << 16) | (greenDifference << 8) | blueDifference
                : 0;
    }

    private static int extractAlpha(int color) {
        return (color >> 24) & 255;
    }

    private static int extractRed(int color) {
        return (color >> 16) & 255;
    }

    private static int extractGreen(int color) {
        return (color >> 8) & 255;
    }

    private static int extractBlue(int color) {
        return color & 255;
    }
}
