package weizhuo;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;

import boardGame.Piece;

public class RobotPiece extends Piece{

	private static RobotPiece robot = null;
	private Color color = Color.green;
	private Direction faceTo;
	private ArrayList<Piece> itemList = new ArrayList<Piece>();

	public ArrayList<Piece> getPieces(){
		return itemList;
	}
	public void pickItem(Piece item){
		itemList.add(item);
	}
	
	public Piece removeItem(Piece item){
		 itemList.remove(item);
		 return item;
	}
	
	public static synchronized RobotPiece getInstance() {
		if (robot == null) {
			robot = new RobotPiece();
		}
		return robot;
	}
	/**
	 * Constructs a <code>RoundPiece</code>.
	 **/
	public RobotPiece() {
		faceTo = Direction.NORTH;
	}

	/**
	 * Constructs a <code>RoundPiece</code> of the given color.
	 * 
	 * @param color The <code>Color</code> of the new piece.
	 **/
	public RobotPiece(Color color) {
		this.color = color;
		faceTo = Direction.NORTH;
	}

	/**
	 * Constructs a <code>RoundPiece</code> with the given name and color.
	 * 
	 * @param name A name for the new Piece.
	 * @param color The <code>Color</code> of the new Piece.
	 **/
	public RobotPiece(String name, Color color) {
		super(name);
		this.color = color;
		faceTo = Direction.NORTH;
	}

	public void setDirection(Direction d){
		this.faceTo = d;
	}
	
	public Direction getDirection(){
        return this.faceTo;
	}
	
	public int getColumn(){
		return super.getColumn();
	}
	
	public int getRow(){
		return super.getRow();
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
		if(faceTo == Direction.NORTH){
			int[] xCordN = {(r.x+r.width/2),(r.x+r.width-5),(r.x+r.width-5),(r.x+5),(r.x+5),(r.x+r.width/2)};
			int[] yCordN = {(r.y+5),(r.y+25),(r.y+r.height-5),(r.y+r.height-5),(r.y+25),(r.y+5)};
			g.fillPolygon(xCordN, yCordN, 5);
		}else if(faceTo == Direction.SOUTH){
			int[] xCordS = {(r.x+r.width/2),(r.x+r.width-5),(r.x+r.width-5),(r.x+5),(r.x+5),(r.x+r.width/2)};
			int[] yCordS = {(r.y+r.height-5),(r.y+r.height-25),(r.y+5),(r.y+5),(r.y+r.height-25),(r.y+r.height-5)};
			g.fillPolygon(xCordS, yCordS,5);
		}else if(faceTo == Direction.EAST){
			int[] xCordE = {(r.x+r.width-5),(r.x+r.width-25),(r.x+5),(r.x+5),(r.x+r.width-25),(r.x+r.width-5)};
			int[] yCordE = {(r.y+r.height/2),(r.y+r.height-5),(r.y+r.height-5),(r.y+5),(r.y+5),(r.y+r.height/2)};
			g.fillPolygon(xCordE, yCordE,5);
		}else if(faceTo == Direction.WEST){
			int[] xCordW = {(r.x+5),(r.x+25),(r.x+r.width-5),(r.x+r.width-5),(r.x+25),(r.x+5)};
			int[] yCordW = {(r.y+r.height/2),(r.y+r.height-5),(r.y+r.height-5),(r.y+5),(r.y+5),(r.y+r.height/2)};
			g.fillPolygon(xCordW, yCordW,5);
		}
		g.setColor(Color.magenta);
		g.drawString("ROBOT", r.x+12, r.y+20);
	}


}
