/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg_lab1;

import javax.swing.JOptionPane;

/**
 *
 * @author lenovo
 */
public class CMYK {

    private javax.swing.JSlider mCMYKSliderC;
    private javax.swing.JSlider mCMYKSliderM;
    private javax.swing.JSlider mCMYKSliderY;
    private javax.swing.JSlider mCMYKSliderK;
    private javax.swing.JTextField mCMYKTextFieldC;
    private javax.swing.JTextField mCMYKTextFieldM;
    private javax.swing.JTextField mCMYKTextFieldY;
    private javax.swing.JTextField mCMYKTextFieldK;
    private javax.swing.JFrame frame;
    RGB rgb;

    public CMYK(javax.swing.JSlider mCMYKSliderK,
            javax.swing.JSlider mCMYKSliderC,
            javax.swing.JSlider mCMYKSliderM,
            javax.swing.JSlider mCMYKSliderY,
            javax.swing.JTextField mCMYKTextFieldK,
            javax.swing.JTextField mCMYKTextFieldC,
            javax.swing.JTextField mCMYKTextFieldM,
            javax.swing.JTextField mCMYKTextFieldY,
            javax.swing.JFrame frame,
            RGB rgb) {
        this.mCMYKSliderC = mCMYKSliderC;
        this.mCMYKSliderM = mCMYKSliderM;
        this.mCMYKSliderY = mCMYKSliderY;
        this.mCMYKSliderK = mCMYKSliderK;
        this.mCMYKTextFieldC = mCMYKTextFieldC;
        this.mCMYKTextFieldM = mCMYKTextFieldM;
        this.mCMYKTextFieldY = mCMYKTextFieldY;
        this.mCMYKTextFieldK = mCMYKTextFieldK;
        this.frame = frame;
        this.rgb = rgb;

    }

    public void updateAll() {
        float r = rgb.getR();
        float g = rgb.getG();
        float b = rgb.getB();
        float chB = 1 - Math.max(Math.max(r, b), g);
        mCMYKSliderK.setValue(Math.round(chB * mCMYKSliderK.getMaximum()));
        mCMYKSliderC.setValue(Math.round((1 - r - chB) / (1 - chB) * mCMYKSliderC.getMaximum()));
        mCMYKSliderM.setValue(Math.round((1 - g - chB) / (1 - chB) * mCMYKSliderM.getMaximum()));
        mCMYKSliderY.setValue(Math.round((1 - b - chB) / (1 - chB) * mCMYKSliderY.getMaximum()));

        mCMYKTextFieldC.setText(String.valueOf(Math.round(mCMYKSliderC.getValue() * 100 / mCMYKSliderC.getMaximum())));
        mCMYKTextFieldM.setText(String.valueOf(Math.round(mCMYKSliderM.getValue() * 100 / mCMYKSliderM.getMaximum())));
        mCMYKTextFieldY.setText(String.valueOf(Math.round(mCMYKSliderY.getValue() * 100 / mCMYKSliderY.getMaximum())));
        mCMYKTextFieldK.setText(String.valueOf(Math.round(mCMYKSliderK.getValue() * 100 / mCMYKSliderK.getMaximum())));
    }

    public void sliderChanged() {
        float k = (float) mCMYKSliderK.getValue() / mCMYKSliderK.getMaximum();

        rgb.setR((1 - (float) mCMYKSliderC.getValue() / mCMYKSliderC.getMaximum()) * (1 - k));
        rgb.setG((1 - (float) mCMYKSliderM.getValue() / mCMYKSliderM.getMaximum()) * (1 - k));
        rgb.setB((1 - (float) mCMYKSliderY.getValue() / mCMYKSliderY.getMaximum()) * (1 - k));

        mCMYKTextFieldC.setText(String.valueOf(Math.round(mCMYKSliderC.getValue() * 100 / mCMYKSliderC.getMaximum())));
        mCMYKTextFieldM.setText(String.valueOf(Math.round(mCMYKSliderM.getValue() * 100 / mCMYKSliderM.getMaximum())));
        mCMYKTextFieldY.setText(String.valueOf(Math.round(mCMYKSliderY.getValue() * 100 / mCMYKSliderY.getMaximum())));
        mCMYKTextFieldK.setText(String.valueOf(Math.round(mCMYKSliderK.getValue() * 100 / mCMYKSliderK.getMaximum())));
    }

    public void textChanged() {
        float cS = (float) mCMYKSliderC.getValue() / mCMYKSliderC.getMaximum();
        float mS = (float) mCMYKSliderM.getValue() / mCMYKSliderM.getMaximum();
        float yS = (float) mCMYKSliderY.getValue() / mCMYKSliderY.getMaximum();
        float kS = (float) mCMYKSliderK.getValue() / mCMYKSliderK.getMaximum();
        try {
            double c = Double.parseDouble(mCMYKTextFieldC.getText().replace(",", ".")) / 100;
            double m = Double.parseDouble(mCMYKTextFieldM.getText().replace(",", ".")) / 100;
            double y = Double.parseDouble(mCMYKTextFieldY.getText().replace(",", ".")) / 100;
            double k = Double.parseDouble(mCMYKTextFieldK.getText().replace(",", ".")) / 100;
            if ((c >= 0 & c <= 1) && (m >= 0 & m <= 1) && (y >= 0 & y <= 1) && (k >= 0 & k <= 1)) {
                rgb.setR((float)((1 - c) * (1 - k)));
                rgb.setG((float)((1 - m) * (1 - k)));
                rgb.setB((float)((1 - y) * (1 - k)));
                mCMYKSliderK.setValue((int) Math.round(k * mCMYKSliderK.getMaximum()));
                mCMYKSliderC.setValue((int) Math.round(c * mCMYKSliderC.getMaximum()));
                mCMYKSliderM.setValue((int) Math.round(m * mCMYKSliderM.getMaximum()));
                mCMYKSliderY.setValue((int) Math.round(y * mCMYKSliderY.getMaximum()));
            } else {
                throw new NumberFormatException("Values are incorrect for CMYK.");
            }
        } catch (NumberFormatException e) {
            mCMYKTextFieldC.setText(String.format("%.0f", cS * 100));
            mCMYKTextFieldM.setText(String.format("%.0f", mS * 100));
            mCMYKTextFieldY.setText(String.format("%.0f", yS * 100));
            mCMYKTextFieldK.setText(String.format("%.0f", kS * 100));
            JOptionPane.showMessageDialog(frame, e.getMessage() + " Reverting changes.");
            System.out.println(e.getMessage());
        }
    }

}
