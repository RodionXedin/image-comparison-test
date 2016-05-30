package com.rodionshkrobot.test.image.drawers;

import com.rodionshkrobot.test.image.markers.RectangleMarker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class RectangleDrawer {

    private static final int BORDER_MARGIN_Y = 5;
    private static final int BORDER_MARGIN_Y_DEFAULT = 0;
    private static final int BORDER_MARGIN_X = 5;
    private static final int BORDER_MARGIN_X_DEFAULT = 0;
    private static final double MINIMAL_BORDERS_DISTANCE = 0.01;

    public static BufferedImage markRectangleAres(BufferedImage image, List<RectangleMarker.Rectangle> areasToMark) {
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.RED);

        for (RectangleMarker.Rectangle rectangle : areasToMark) {

            // add some margins so in case of single pixel we still have something
            // we can actually see and just for..  aesthetics
            // check for size is added due to too many possible false-intersections on garbage-filled examples

            int currentBorderMarginY = rectangle.getYTop() - rectangle.getYBottom() <
                    image.getHeight() * MINIMAL_BORDERS_DISTANCE ?
                    BORDER_MARGIN_Y : BORDER_MARGIN_Y_DEFAULT;

            int currentBorderMarginX = (rectangle.getXRight() - rectangle.getXLeft() <
                    image.getWidth() * MINIMAL_BORDERS_DISTANCE) ?
                    BORDER_MARGIN_X : BORDER_MARGIN_X_DEFAULT;

            int xLeft = rectangle.getXLeft() - currentBorderMarginX;
            int xRight = rectangle.getXRight() + currentBorderMarginX;
            int yBottom = rectangle.getYBottom() - currentBorderMarginY;
            int yTop = rectangle.getYTop() + currentBorderMarginY;

            graphics.drawRect(xLeft, yBottom, xRight - xLeft, yTop - yBottom);
        }

        return image;
    }

}
