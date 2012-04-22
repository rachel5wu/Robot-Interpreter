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
public class OilCan extends Piece{

private Color color = Color.orange;
    
    /**
     * Constructs a <code>RoundPiece</code>.
     **/
    public OilCan() {
    }
    
    /**
     * Constructs a <code>RoundPiece</code> of the given color.
     * 
     * @param name The name of the new piece.
     **/
     public OilCan(String name) {
        super(name);
    }
     
     /**
      * Constructs a <code>RoundPiece</code> with the given name and color.
      * 
      * @param name A name for the new Piece.
      * @param color The <code>Color</code> of the new Piece.
      **/
     public OilCan(String name, Color color) {
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
        int[] xCord = {r.x +15, r.x + r.width-15, r.x + r.width-10, r.x+r.width-10, r.x + r.width-15,r.x +15,r.x +10,r.x +10, r.x +15};
        int[] yCord = {(r.y + 5), (r.y + 5), (r.y + 10), (r.y + r.height-10), (r.y + r.height-5), (r.y + r.height-5), (r.y + r.height-10),(r.y + 10), (r.y + 5)};
        g.fillPolygon(xCord, yCord, 9);
        g.setColor(Color.red);
        g.drawString("OilCan", r.x+12, r.y+20);
    }

}
