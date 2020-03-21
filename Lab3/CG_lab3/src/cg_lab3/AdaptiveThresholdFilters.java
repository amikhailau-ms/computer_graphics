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
public class AdaptiveThresholdFilters {
    
    private static final int windowSize = 9;
    
    public static BufferedImage niblekAdaptiveThreshold(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int windowHalfSize = (int)((double)windowSize / 2.0);
        for (int i = windowHalfSize; i < image.getWidth() - windowHalfSize; i++) {
            for (int j = windowHalfSize; j < image.getHeight() - windowHalfSize; j++) {
                double mean = 0;
                double dev = 0;
                int[][] window = new int[windowSize][windowSize];
                for (int p = -windowHalfSize; p <= windowHalfSize; p++) {
                    for (int q = -windowHalfSize; q <= windowHalfSize; q++) {
                        int luminance = CommonUtils.convertRGBtoLum(image.getRGB(i+p, j+q));
                        window[p+windowHalfSize][q+windowHalfSize] = luminance;
                        mean += luminance;
                    }
                }
                mean /= windowSize*windowSize;
                for (int p = -windowHalfSize; p <= windowHalfSize; p++) {
                    for (int q = -windowHalfSize; q <= windowHalfSize; q++) {
                        dev += Math.pow((window[p+windowHalfSize][q+windowHalfSize] - mean), 2);
                    }
                }
                dev = Math.sqrt(1 / (windowSize*windowSize - 1) * dev);
                int threshold = (int)(mean - 0.2 * dev);
                if (window[windowHalfSize][windowHalfSize] > threshold) {
                    newImage.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    newImage.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return newImage;
    }
    
}
