/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg_lab1;

import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author lenovo
 */
public class RGB {

    private javax.swing.JSlider mRGBSliderR;
    private javax.swing.JSlider mRGBSliderG;
    private javax.swing.JSlider mRGBSliderB;
    private javax.swing.JTextField mRGBTextFieldR;
    private javax.swing.JTextField mRGBTextFieldG;
    private javax.swing.JTextField mRGBTextFieldB;
    private javax.swing.JFrame frame;

    float r = 0;
    float g = 0;
    float b = 0;

    public float getR() {
        return r;
    }

    public void setR(float r) {
        if (r < 0) {
            this.r = 0;
        } else if (r > 1) {
            this.r = 1;
        } else {
            this.r = r;
        }
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        if (g < 0) {
            this.g = 0;
        } else if (g > 1) {
            this.g = 1;
        } else {
            this.g = g;
        }
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        if (b < 0) {
            this.b = 0;
        } else if (b > 1) {
            this.b = 1;
        } else {
            this.b = b;
        }
    }

    public Color getColor() {
        return new Color(Math.round(r * 255),
                Math.round(g * 255),
                Math.round(b * 255));
    }

    public RGB(javax.swing.JSlider mRGBSliderR,
            javax.swing.JSlider mRGBSliderG,
            javax.swing.JSlider mRGBSliderB,
            javax.swing.JTextField mRGBTextFieldR,
            javax.swing.JTextField mRGBTextFieldG,
            javax.swing.JTextField mRGBTextFieldB,
            javax.swing.JFrame frame) {
        this.mRGBSliderR = mRGBSliderR;
        this.mRGBSliderG = mRGBSliderG;
        this.mRGBSliderB = mRGBSliderB;
        this.mRGBTextFieldR = mRGBTextFieldR;
        this.mRGBTextFieldG = mRGBTextFieldG;
        this.mRGBTextFieldB = mRGBTextFieldB;
        this.frame = frame;
    }

    public void textChanged() {
        double bufRS = (double) mRGBSliderR.getValue() / mRGBSliderR.getMaximum();
        double bufGS = (double) mRGBSliderG.getValue() / mRGBSliderG.getMaximum();
        double bufBS = (double) mRGBSliderB.getValue() / mRGBSliderB.getMaximum();
        
        try {
            double bufR = Double.parseDouble(mRGBTextFieldR.getText().replace(",", "."));
            double bufB = Double.parseDouble(mRGBTextFieldB.getText().replace(",", "."));
            double bufG = Double.parseDouble(mRGBTextFieldG.getText().replace(",", "."));
            if (bufG >= 0 && bufG <= 255 && bufB >= 0 && bufB <= 255 && bufR >= 0 && bufR <= 255) {
                r = (float) bufR / 255;
                g = (float) bufG / 255;
                b = (float) bufB / 255;
                mRGBSliderR.setValue(Math.round(r * mRGBSliderR.getMaximum()));
                mRGBSliderG.setValue(Math.round(g * mRGBSliderG.getMaximum()));
                mRGBSliderB.setValue(Math.round(b * mRGBSliderB.getMaximum()));
            } else {
                throw new NumberFormatException("Values are incorrect for RGB.");
            }
        } catch (NumberFormatException e) {
            mRGBTextFieldR.setText(String.format("%.0f", bufRS));
            mRGBTextFieldG.setText(String.format("%.0f", bufGS));
            mRGBTextFieldB.setText(String.format("%.0f", bufBS));
            JOptionPane.showMessageDialog(frame, e.getMessage() + " Reverting changes.");
            System.out.println(e.getMessage());
        }
    }

    public void sliderChanged() {
        r = (float) mRGBSliderR.getValue() / mRGBSliderR.getMaximum();
        g = (float) mRGBSliderG.getValue() / mRGBSliderG.getMaximum();
        b = (float) mRGBSliderB.getValue() / mRGBSliderB.getMaximum();
        mRGBTextFieldR.setText(String.valueOf(Math.round(r * 255)));
        mRGBTextFieldG.setText(String.valueOf(Math.round(g * 255)));
        mRGBTextFieldB.setText(String.valueOf(Math.round(b * 255)));
    }

    public void updateAll() {
        mRGBTextFieldR.setText(String.valueOf(Math.round(r * 255)));
        mRGBSliderR.setValue(Math.round(r * mRGBSliderR.getMaximum()));
        mRGBTextFieldB.setText(String.valueOf(Math.round(b * 255)));
        mRGBSliderB.setValue(Math.round(b * mRGBSliderG.getMaximum()));
        mRGBTextFieldG.setText(String.valueOf(Math.round(g * 255)));
        mRGBSliderG.setValue(Math.round(g * mRGBSliderB.getMaximum()));
    }
}
