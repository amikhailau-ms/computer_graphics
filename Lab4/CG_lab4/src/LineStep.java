
import java.awt.Color;
import java.awt.Graphics;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TheProthean
 */
public class LineStep implements Figure {
    int x1;
    int y1;
    int x2;
    int y2;
    Color col;
    public LineStep(int x1, int y1, int x2, int y2, Color col){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.col = col;
    }
    
    public void paint(Graphics g){
        long start = System.nanoTime(); 
        float A = y1 - y2;
        float B = x2 - x1;
        float C = x1 * y2 - x2 * y1;
        float k = -A/B;
        float b = -C/B;
        int delta_x = Math.abs(x2 - x1);
        int delta_y = Math.abs(y2 - y1);
        int L = Math.max(delta_x, delta_y);

        g.setColor(col);

        if (x1 > x2){
            x1 = x2;
        }

        float x = x1;
        for (int i = 0; i < L; i++){
            x += (float)delta_x / L;
            int _x = Math.round(x);
            int _y = Math.round(k * x + b);
            g.drawLine(_x, _y, _x, _y);
        }
        long elapsedTime = System.nanoTime() - start;
        System.out.println("LineStep time: from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ") in " + elapsedTime + " ns.");
    }    
}
