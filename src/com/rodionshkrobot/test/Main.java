package com.rodionshkrobot.test;

import com.rodionshkrobot.test.image.drawers.RectangleDrawer;
import com.rodionshkrobot.test.image.markers.RectangleMarker;
import com.rodionshkrobot.test.image.processors.DifferenceProcessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedImage image1 = ImageIO.read(new File("1.png"));
        BufferedImage image2 = ImageIO.read(new File("2-1.png"));
        BufferedImage image3 = new BufferedImage(image2.getWidth(), image2.getHeight(), image2.getType());

        RectangleMarker rectangleMarker = new RectangleMarker(image1);

        for (int x = 0; x < image1.getWidth(); x++) {
            for (int y = 0; y < image1.getHeight(); y++) {
                int firstColor = image1.getRGB(x, y);
                int secondColor = image2.getRGB(x, y);
                image3.setRGB(x, y, secondColor);

                int diff = DifferenceProcessor.compareColors(firstColor, secondColor);

                if (diff != 0) {
                    rectangleMarker.addMark(x, y);
                    //image3.setRGB(x, y, Color.CYAN.getRGB());
                }


//                color = Math.abs(image2.getRGB(x, y) - image1.getRGB(x, y));
//                image3.setRGB(x, y, color);
            }
        }

        RectangleDrawer.markRectangleAres(image3, rectangleMarker.getRectangles());
        ImageIO.write(image3, "png", new File("abcd.png"));
    }
}
