/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg_lab3;

import java.awt.Color;

/**
 *
 * @author TheProthean
 */
public class CommonUtils {
    
    public static int convertRGBtoLum(int rgb) {
        int red   = (rgb >>> 16) & 0xFF;
        int green = (rgb >>>  8) & 0xFF;
        int blue  = (rgb >>>  0) & 0xFF;
        
        int luminance = (int)(red * 0.2126f + green * 0.7152f + blue * 0.0722f);
        return luminance;
    }
    
    public static int changeBrightness(int luminance, int rgb) {
        
        int red   = (rgb >>> 16) & 0xFF;
        int green = (rgb >>>  8) & 0xFF;
        int blue  = (rgb >>>  0) & 0xFF;
        
        float[] HSB = new float[3];
        
        Color.RGBtoHSB( red, green, blue, HSB);
        Color c = new Color(Color.HSBtoRGB(HSB[0], HSB[1], HSB[2] * ((float)luminance / 255.0f)));
        return c.getRGB();
    }
}
