package com.rodionshkrobot.test;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Pair<Integer, Integer>> diffList = new ArrayList<>();
        int[][][] ch = new int[4][4][4];
        BufferedImage image1 = ImageIO.read(new File("1.png"));
        BufferedImage image2 = ImageIO.read(new File("2.png"));
        BufferedImage image3 = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());
        for (int x = 0; x < image1.getWidth(); x++)
            for (int y = 0; y < image1.getHeight(); y++) {
                int firstImageColor = image1.getRGB(x, y);
                int secondImageColor = image2.getRGB(x, y);

                int alpha = (firstImageColor >> 24) & 255;
                int red = (firstImageColor >> 16) & 0xFF;
                int green = (firstImageColor >> 8) & 0xFF;
                int blue = (firstImageColor) & 0xFF;

                int a1 = (secondImageColor >> 24) & 0xFF;
                int r1 = (secondImageColor >> 16) & 0xFF;
                int g1 = (secondImageColor >> 8) & 0xFF;
                int b1 = (secondImageColor) & 0xFF;

                int aDiff = Math.abs(a1 - alpha);
                int rDiff = Math.abs(r1 - red);
                int gDiff = Math.abs(g1 - green);
                int bDiff = Math.abs(b1 - blue);

                int diff =
                        (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
                if (diff != 0) {
                    diffList.add(new Pair<>(x, y));
                }

                image3.setRGB(x, y, diff);

//                color = Math.abs(image2.getRGB(x, y) - image1.getRGB(x, y));
//                image3.setRGB(x, y, color);
            }
        ImageIO.write(image3, "bmp", new File("image.bmp"));
        System.out.println(diffList);
    }


    public int extractAlpha(int color) {
        return (color >> 24) & 255;
    }

    public int extractRed(int color) {
        return (color >> 16) & 255;
    }

    public int extractGreen(int color) {
        return (color >> 8) & 255;
    }

    public int extractBlue(int color) {
        return color & 255;
    }

    public int compareColors(int firstColor, int secondColor) {
        int aDiff = Math.abs(extractAlpha(secondColor) - extractAlpha(firstColor));
        int rDiff = Math.abs(extractRed(secondColor) - extractRed(firstColor));
        int gDiff = Math.abs(extractGreen(secondColor) - extractGreen(firstColor));
        int bDiff = Math.abs(extractBlue(secondColor) - extractGreen(firstColor));

        return (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
    }
}
