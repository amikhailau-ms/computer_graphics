/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg_lab3;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author TheProthean
 */
public class NonlinearFilters {
    
    public static BufferedImage averageFilter(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 3; i < image.getWidth() - 3; i++) {
            for (int j = 3; j < image.getHeight() - 3; j++) {
                double mean = 0;
                for (int p = -3; p <= 3; p++) {
                    for (int q = -3; q <= 3; q++) {
                        mean += CommonUtils.convertRGBtoLum(image.getRGB(i+p, j+q));
                    }
                }
                mean /= 7*7;
                newImage.setRGB(i, j, CommonUtils.changeBrightness((int)mean, image.getRGB(i, j)));
            }
        }
        return newImage;
    }
            
    public static BufferedImage minimumFilter(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 3; i < image.getWidth() - 3; i++) {
            for (int j = 3; j < image.getHeight() - 3; j++) {
                int minimum = 255;
                for (int p = -3; p <= 3; p++) {
                    for (int q = -3; q <= 3; q++) {
                        int luminance = CommonUtils.convertRGBtoLum(image.getRGB(i+p, j+q));
                        if (luminance < minimum) {
                            minimum = luminance;
                        }
                    }
                }
                newImage.setRGB(i, j, CommonUtils.changeBrightness(minimum, image.getRGB(i, j)));
            }
        }
        return newImage;
    }
    
}
