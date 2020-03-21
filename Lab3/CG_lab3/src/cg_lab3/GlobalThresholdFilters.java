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
public class GlobalThresholdFilters {
    
    private static final int levelsOfBrightness = 256;
    
    public static BufferedImage manualThreshold(BufferedImage image, int threshold) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int luminance = CommonUtils.convertRGBtoLum(image.getRGB(i, j));
                if (luminance <= threshold) {
                    newImage.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    newImage.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return newImage;
    }
    
    public static BufferedImage otsuThreshold(BufferedImage image) {
        int[] hist = makeBrightnessHistogram(image);
        int histSum = 0;
        for (int i = 0; i < hist.length; i++) {
            histSum += hist[i];
        }
        int bestThreshold = calcThreshold(hist);
        BufferedImage newImage = manualThreshold(image, bestThreshold);
        return newImage;
    }
    
    private static int[] makeBrightnessHistogram(BufferedImage image) {
        int[] hist = new int[256];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int luminance = CommonUtils.convertRGBtoLum(image.getRGB(i, j));
                hist[luminance] += 1;
            }
        }
        return hist;
    }
    
    private static int calcThreshold(int[] hist) {
        int k,kStar;
        double N1, N;
        double BCV, BCVmax;
        double num, denom;
        double Sk;
        double S, L= levelsOfBrightness;

        S = N = 0;
	for (k=0; k<L; k++){
            S += (double)k * hist[k];
            N += hist[k];
	}

	Sk = 0;
	N1 = hist[0];
	BCV = 0;
	BCVmax=0;
	kStar = 0;

	for (k=1; k<L-1; k++) {
		Sk += (double)k * hist[k];
		N1 += hist[k];

		denom = (double)( N1) * (N - N1);
		if (denom != 0 ){
                    num = ( (double)N1 / N ) * S - Sk; 	
                    BCV = (num * num) / denom;
		}
		else
                    BCV = 0;

                if (BCV >= BCVmax){
                    BCVmax = BCV;
                    kStar = k;
                }
	}
	return kStar;
   }
}
