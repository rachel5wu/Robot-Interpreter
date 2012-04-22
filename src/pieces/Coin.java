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
public class Coin extends Piece{

	private Color color = Color.gray;
	
	public Coin(){}
    
     /**
      * Constructs a <code>Coin</code> with the given name.
      * 
      * @param name A name for the new Piece.
      **/
     public Coin(String name) {
         super(name);
     }
    
    /**
     * Draws this <code>Coin</code> on the given <code>Graphics</code>.
     * Drawing should be limited to the provided <code>java.awt.Rectangle</code>.
     * 
     * @param g The graphics on which to draw.
     * @param r The rectangle in which to draw.
     */
    @Override
    public void paint(Graphics g, Rectangle r) {
        
        g.setColor(color);
        g.fillOval(r.x + 1, r.y + 1, r.width - 2, r.height - 2);

        g.setColor(Color.black);
        g.drawString("Coin", r.x+12, r.y+20);
    }

}
