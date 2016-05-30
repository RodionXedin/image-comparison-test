package com.rodionshkrobot.test.image.markers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RectangleMarker {

    //% of picture size that is considered to be small enough to add current point to the closest existing area
    private static final double COVERAGE_GRIP_PERCENT = 0.05;
    private List<Rectangle> rectangles;
    private boolean rectanglesMerged = false;
    private BufferedImage image;

    public RectangleMarker(BufferedImage image) {
        this.image = image;
        this.rectangles = new ArrayList<>();
    }

    public void addMark(int x, int y) {
        boolean rectangleFound = false;
        for (Rectangle rectangle : rectangles) {
            if (rectangle.addMark(x, y, image)) {
                // actually it's better to do this in case if the borders changed, but time lack & test task.
                // TBC in future - just save old values & recalculate borders & compare on point add.
                rectanglesMerged = false;
                rectangleFound = true;
                break;
            }
        }
        if (!rectangleFound) {
            rectanglesMerged = false;
            rectangles.add(new Rectangle(x, y));
        }
    }

    private void mergeRectangles() {

        // just a math algorithm to detect intersections. I'm sorry.
        // I wasn't allowed to use any libraries right? And creating a dozen or two of java.awt.Rectangles
        // seemed to be a little too much.
        // Still, due to time limits and since that it's a test task - this is far from a perfect algorithm covering all
        // possible options.
        rectangles.forEach(first -> {
            rectangles.stream().filter(second -> first.getXLeft() <= second.getXRight() &&
                    first.getXRight() >= second.getXLeft() &&
                    first.getYTop() >= second.getYBottom() &&
                    first.getYBottom() <= second.getYTop() && !second.equals(first)).forEach(second -> {
                if (!first.isMerged()) {
                    first.merge(second);
                    second.setMerged(true);
                }
            });
        });

        rectangles.removeIf(Rectangle::isMerged);

        rectanglesMerged = true;
    }

    public List<Rectangle> getRectangles() {
        if (!rectanglesMerged) {
            mergeRectangles();
        }
        return rectangles;
    }


    public class Rectangle {
        private Coverage coverage;
        private boolean merged = false;

        public Rectangle(int x, int y) {
            coverage = new Coverage(x, x, y, y);
        }

        public boolean addMark(int x, int y, BufferedImage image) {
            return coverage.updateCoverageArea(x, y, image);
        }

        public void merge(Rectangle rectangleToMerge) {
            Coverage newCoverage = new Coverage(
                    Math.min(this.getXLeft(), rectangleToMerge.getXLeft()),
                    Math.max(this.getXRight(), rectangleToMerge.getXRight()),
                    Math.min(this.getYBottom(), rectangleToMerge.getYBottom()),
                    Math.max(this.getYTop(), rectangleToMerge.getYTop()));
            this.coverage = newCoverage;
        }


        public boolean isMerged() {
            return merged;
        }

        public void setMerged(boolean merged) {
            this.merged = merged;
        }

        public int getXLeft() {
            return coverage.getXLeft();
        }

        public int getXRight() {
            return coverage.getXRight();
        }

        public int getYTop() {
            return coverage.getYTop();
        }

        public int getYBottom() {
            return coverage.getYBottom();
        }
    }


    private class Coverage {

        private int xLeft;
        private int xRight;
        private int yTop;
        private int yBottom;


        public Coverage(int xLeft, int xRight, int yBottom, int yTop) {
            this.xLeft = xLeft;
            this.xRight = xRight;
            this.yTop = yTop;
            this.yBottom = yBottom;
        }

        public boolean updateCoverageArea(int x, int y, BufferedImage image) {
            if (isInCoveredArea(x, y, image)) {

                xLeft = Math.min(xLeft, x);
                xRight = Math.max(xRight, x);
                yBottom = Math.min(yBottom, y);
                yTop = Math.max(yTop, y);

                return true;
            }
            return false;
        }

        private boolean isInCoveredArea(int x, int y, BufferedImage image) {
            int coverageAdditionX = (int) (image.getWidth() * COVERAGE_GRIP_PERCENT);
            int coverageAdditionY = (int) (image.getHeight() * COVERAGE_GRIP_PERCENT);

            if (x > xLeft - coverageAdditionX && x < xRight + coverageAdditionX &&
                    y > yBottom - coverageAdditionY && y < yTop + coverageAdditionY) {
                return true;
            }
            return false;
        }

        public int getXLeft() {
            return xLeft;
        }

        public int getXRight() {
            return xRight;
        }

        public int getYTop() {
            return yTop;
        }

        public int getYBottom() {
            return yBottom;
        }
    }


}
