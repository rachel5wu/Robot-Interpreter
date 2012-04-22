/**
 * 
 */
package weizhuo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import boardGame.Piece;

/**
 * @author weizhuowu
 *
 */
public class Tazer extends Piece{

private Color color = Color.blue;
    
    /**
     * Constructs a <code>Tazer</code>.
     **/
    public Tazer() {
    }
    
    /**
     * Constructs a <code>Tazer</code> of the given color.
     * 
     * @param name The name of the new piece.
     **/
     public Tazer(String name) {
        super(name);
    }
     
     /**
      * Constructs a <code>Tazer</code> with the given name and color.
      * 
      * @param name The name for the new Piece.
      * @param color The <code>Color</code> of the new Piece.
      **/
     public Tazer(String name, Color color) {
         super(name);
         this.color = color;
     }
    
    /**
     * Draws this <code>RoundPiece</code> on the given <code>Graphics</code>.
     * Drawing should be limited to the provided <code>java.awt.Rectangle</code>.
     * 
     * @param g The graphics on which to draw.
     * @param r The rectangle in which to draw.
     */
    @Override
    public void paint(Graphics g, Rectangle r) {
        
        g.setColor(color);
        int[] xCord = {(r.x+5),(r.x+r.width-5), (r.x+r.width-5),(r.x+r.width-20),(r.x+r.width-20),(r.x+r.width/2),(r.x+r.width/2),(r.x+5),(r.x+5)};
        int[] yCord = {(r.y+5),(r.y+5),(r.y+r.height-5),(r.y+r.height-5),(r.y+r.height-20),(r.y+r.height-20),(r.y+20),(r.y+20),(r.y+5)};
        g.fillPolygon(xCord, yCord, 9);
        g.setColor(Color.magenta);
        g.drawString("Tazer", r.x+12, r.y+20);
    }

}
