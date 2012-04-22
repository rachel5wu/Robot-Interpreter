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
public class Diamond extends Piece{

	private Color color = Color.pink;
    
    /**
     * Constructs a <code>RoundPiece</code>.
     **/
    public Diamond() {
    }
    
    /**
     * Constructs a <code>RoundPiece</code> of the given color.
     * 
     * @param name The <code>Name</code> of the new piece.
     **/
     public Diamond(String name) {
        super(name);
    }
     
     /**
      * Constructs a <code>RoundPiece</code> with the given name and color.
      * 
      * @param name The name for the new Piece.
      * @param color The <code>Color</code> of the new Piece.
      **/
     public Diamond(String name, Color color) {
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
        int[] xCord = {r.x + 15, r.x +r.width-15, r.x +r.width-1,r.x + r.width/2, r.x + 1, r.x+3};
        int[] yCord = {r.y +5, r.y +5, r.y +20, r.y+r.height-1, r.y +20, r.y +5};
        g.fillPolygon(xCord, yCord, 5);
        g.setColor(Color.cyan);
        g.drawString("Diamond", r.x+2, r.y+20);

    }

}
