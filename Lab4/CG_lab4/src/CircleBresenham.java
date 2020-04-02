
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
public class CircleBresenham implements Figure{
    int x;
    int y;
    int r;
    Color col;
    public CircleBresenham(int x, int y, int r, Color col){
        this.x = x;
        this.y = y;
        this.r = r;
        this.col = col;
    }
    
    public void paint(Graphics g){
        long start = System.nanoTime(); 
        int sx=0;
        int sy=r;
        int d=3-2*r;
        g.setColor(col);
        while(sx<=sy) {
            g.drawLine(x+sx, y-sy, x+sx, y-sy);
            g.drawLine(x+sx, y+sy, x+sx, y+sy);
            g.drawLine(x-sx, y-sy, x-sx, y-sy);
            g.drawLine(x-sx, y+sy, x-sx, y+sy);
            g.drawLine(x+sy, y+sx, x+sy, y+sx);
            g.drawLine(x-sy, y+sx, x-sy, y+sx);
            g.drawLine(x+sy, y-sx, x+sy, y-sx);
            g.drawLine(x-sy, y-sx, x-sy, y-sx);
            if (d<0) {
                d = d + 4 * sx + 6;
            } else {
                d = d + 4 * (sx - sy) + 10;
                sy = sy - 1;
            }
            sx += 1;
        }
        long elapsedTime = System.nanoTime() - start;
        System.out.println("CircleBresenham time: center (" + x + "," + y + "), radius " + r + " in " + elapsedTime + " ns.");
    }    
}
