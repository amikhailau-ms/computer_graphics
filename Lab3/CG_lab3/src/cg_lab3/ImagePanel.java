package cg_lab3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private BufferedImage image;
    private Stack<BufferedImage> stack;

    private GroupLayout jPanel1Layout;

    public ImagePanel() {
        jPanel1Layout = new javax.swing.GroupLayout(this);
        stack = new Stack<>();
        this.setLayout(jPanel1Layout);
    }

    public void setImage(File imageFile) throws IOException {
        this.image = ImageIO.read(imageFile);
        stack.push(image);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, image.getWidth(), Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, image.getHeight(), Short.MAX_VALUE)
        );
        this.repaint();
    }
    
    public void setBuffImage(BufferedImage image) {
        this.image = image;
        stack.push(image);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, image.getWidth(), Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, image.getHeight(), Short.MAX_VALUE)
        );
        this.repaint();
    }

    public boolean undo() {
        BufferedImage retimage = stack.pop();
        if (this.image == retimage) {
            this.image = stack.pop();
        } else {
            this.image = retimage;
        }
        this.repaint();
        if (stack.size() == 0) {
            stack.push(image);
            return false;
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        }
    }

}
