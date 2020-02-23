package cg_lab1;

import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TheProthean
 */
public class LAB {
    
    private javax.swing.JSlider mLABSliderL;
    private javax.swing.JSlider mLABSliderA;
    private javax.swing.JSlider mLABSliderB;
    private javax.swing.JTextField mLABTextFieldL;
    private javax.swing.JTextField mLABTextFieldA;
    private javax.swing.JTextField mLABTextFieldB;
    private javax.swing.JFrame frame;
    RGB rgb;
    
    public LAB(javax.swing.JSlider mLABSliderL,
        javax.swing.JSlider mLABSliderA,
        javax.swing.JSlider mLABSliderB,
        javax.swing.JTextField mLABTextFieldL,
        javax.swing.JTextField mLABTextFieldA,
        javax.swing.JTextField mLABTextFieldB,
        javax.swing.JFrame frame,
        RGB rgb) {
        this.mLABSliderL = mLABSliderL;
        this.mLABSliderA = mLABSliderA;
        this.mLABSliderB = mLABSliderB;
        this.mLABTextFieldL = mLABTextFieldL;
        this.mLABTextFieldA = mLABTextFieldA;
        this.mLABTextFieldB = mLABTextFieldB;
        this.frame = frame;
        this.rgb = rgb;
    }
    
    public void textChanged() {
        double bufLS = mLABSliderL.getValue() / mLABSliderL.getMaximum() * 100;
        double bufAS = (mLABSliderA.getValue() / mLABSliderA.getMaximum() * 184.439 - 86.185);
        double bufBS = (mLABSliderB.getValue() / mLABSliderB.getMaximum() * 202.345 - 107.863);
        try {
            double bufL = Double.parseDouble(mLABTextFieldL.getText().replace(",", "."));
            double bufA = Double.parseDouble(mLABTextFieldA.getText().replace(",", "."));
            double bufB = Double.parseDouble(mLABTextFieldB.getText().replace(",", "."));
            if (bufL >= 0 && bufL <= 100 && bufA >= -86.185 && bufA <= 98.254 && bufB >= -107.863 && bufB <= 94.482) {
                this.convertLABtoRGB(new float[]{(float)bufL, (float)bufA, (float)bufB});
                mLABSliderL.setValue((int) Math.round(bufL));
                mLABSliderA.setValue((int) Math.round(bufA));
                mLABSliderB.setValue((int) Math.round(bufB));
            } else {
                throw new NumberFormatException("Values are incorrect for LAB.");
            }
        } catch (NumberFormatException e) {
            mLABTextFieldL.setText(String.format(".0f", bufLS));
            mLABTextFieldA.setText(String.format(".0f", bufAS));
            mLABTextFieldB.setText(String.format(".0f", bufBS));
            JOptionPane.showMessageDialog(frame, e.getMessage() + " Reverting changes.");
            System.out.println(e.getMessage());
        }
    }

    public void sliderChanged() {
        float L = (float) mLABSliderL.getValue();
        float A = (float) mLABSliderA.getValue();
        float B = (float) mLABSliderB.getValue();
        this.convertLABtoRGB(new float[]{L, A, B});
        mLABTextFieldL.setText(String.valueOf(Math.round(L)));
        mLABTextFieldA.setText(String.valueOf(Math.round(A)));
        mLABTextFieldB.setText(String.valueOf(Math.round(B)));
    }

    public void updateAll() {
        float[] updated = this.convertRGBtoLAB();
        
        mLABSliderL.setValue(Math.round(updated[0]));
        mLABSliderA.setValue(Math.round(updated[1]));
        mLABSliderB.setValue(Math.round(updated[2]));

        mLABTextFieldL.setText(String.valueOf(Math.round(updated[0])));
        mLABTextFieldA.setText(String.valueOf(Math.round(updated[1])));
        mLABTextFieldB.setText(String.valueOf(Math.round(updated[2])));
    }
    
    public float[] convertRGBtoLAB() {
        
        float[] RGBn = new float[3];
        RGBn[0] = rgb.getR();
        RGBn[1] = rgb.getG();
        RGBn[2] = rgb.getB();
        for (int i = 0; i < 3; i++) {
            float In = RGBn[i];
            if (In >= 0.04045f) {
                In = (float)(Math.pow((In + 0.055)/ 1.055, 2.4));
            } else {
                In /= 12.92;
            }
            In *= 100;
            RGBn[i] = In;
        }
        float[] XYZ = new float[3];
        XYZ[0] = (float)(0.412453 * RGBn[0] + 0.357580 * RGBn[1] + 0.180423 * RGBn[2]);
        XYZ[1] = (float)(0.212671 * RGBn[0] + 0.715160 * RGBn[1] + 0.072169 * RGBn[2]);
        XYZ[2] = (float)(0.019334 * RGBn[0] + 0.119193 * RGBn[1] + 0.950227 * RGBn[2]);

        float[] XYZwhite = new float[3];
        XYZwhite[0] = 95.047f;
        XYZwhite[1] = 100.0f;
        XYZwhite[2] = 108.883f;
        
        for (int i = 0; i < 3; i++) {
            float In = XYZ[i] / XYZwhite[i];
            if (In >= 0.008856f) {
            	In = (float)Math.pow((double)In, 0.3333333333);
            } else {
                In = (float)(7.787 * In + (16.0/116.0));
            }
            XYZ[i] = In;
        }
        
        float[] LAB = new float[3];
        LAB[0] = 116.0f * XYZ[1] - 16.0f;
        LAB[1] = 500.0f * (XYZ[0] - XYZ[1]);
        LAB[2] = 200.0f * (XYZ[1] - XYZ[2]);

        return LAB;
    }
    
    public void convertLABtoRGB(float[] LAB) {
        
        float[] XYZwhite = new float[3];
        XYZwhite[0] = 95.047f;
        XYZwhite[1] = 100.0f;
        XYZwhite[2] = 108.883f;
        
        float[] XYZtmp = new float[3];
        XYZtmp[1] = (LAB[0] + 16.0f) / 116.0f;
        XYZtmp[0] = LAB[1] / 500.0f + XYZtmp[1];
        XYZtmp[2] = XYZtmp[1] - LAB[2] / 200.0f;
        
        for (int i = 0; i < 3; i++) {
            float In = XYZtmp[i];
            if (Math.pow(In, 3) >= 0.008856f) {
                In = (float)Math.pow(In, 3.0);
            } else {
                In = (float)((In - 16.0f/116.0f) / 7.787);
            }
            XYZtmp[i] = In;
        }
        
        float[] XYZ = new float[3];
        for (int i = 0; i < 3; i++) {
        	XYZ[i] = XYZtmp[i] * XYZwhite[i] / 100.0f;
        }
        
        float[] RGBn = new float[3];
        RGBn[0] = (float)(3.2404542 * XYZ[0] + -1.5371385 * XYZ[1] + -0.4985314 * XYZ[2]);
        RGBn[1] = (float)(-0.9692660 * XYZ[0] + 1.8760108 * XYZ[1] + 0.0415560 * XYZ[2]);
        RGBn[2] = (float)(0.0556434 * XYZ[0] + -0.2040259 * XYZ[1] + 1.0572252 * XYZ[2]);
        
        float[] RGB = new float[3];
       
        for (int i = 0; i < 3; i++) {
            float In = RGBn[i];
            if (In >= 0.0031308f) {
                In = (float)(1.055 * Math.pow(In, 1.0 / 2.4) - 0.055);
            } else {
                In *= 12.92;
            }
            RGB[i] = In;
        }
        
        rgb.setR(RGB[0]);
        rgb.setG(RGB[1]);
        rgb.setB(RGB[2]);
    }
}
