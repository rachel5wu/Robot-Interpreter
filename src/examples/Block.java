package examples;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import boardGame.Piece;

/**
 * Example playing piece.
 */
public class Block extends Piece {
    
    /**
     * Constructs a <code>Block</code>.
     **/
     public Block() {
    }
    
    /**
     * Draws this <code>Block</code> on the given <code>Graphics</code>.
     * 
     * @param g The graphics on which to draw.
     */
    @Override
    public void paint(Graphics g, Rectangle r) {
        g.setColor(Color.black);
        g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(Color.white);
        g.drawString("Block", r.x+12, r.y+20);
    }
}