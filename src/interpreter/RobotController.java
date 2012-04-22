/**
 * 
 */
package interpreter;

import java.util.ArrayList;
import java.util.Stack;

import examples.Block;

import weizhuo.Direction;
import weizhuo.RobotPiece;
import boardGame.Board;
import boardGame.Piece;

/**
 * @author Weizhuo Wu
 * @version Apr 10, 2012
 */
public class RobotController {
	private static final int ROWS = 12;
	private static final int COLUMNS = 15;
	private Board board;

	RobotController(Board board){
		this.board = board;
	}
	/**
	 * Get the Square's Row number on which robot is standing
	 * @return Row number
	 */
	public int getRobotRow(){
		return RobotPiece.getInstance().getRow();
	}
	/**
	 * Get the Square's column number on which robot is standing
	 * @return Column number
	 */
	public int getRobotCol(){
		return RobotPiece.getInstance().getColumn();
	}
	/**
	 * Get distance from Robot to the nearest piece in facing direction
	 * @return integer distance to whatever it is that the robot is seeing
	 */
	public int getDistance(){
		Direction d = RobotPiece.getInstance().getDirection();
		int row = RobotPiece.getInstance().getRow();
		int col = RobotPiece.getInstance().getColumn();

		if(d.equals(Direction.NORTH)){
			int r=row-1;
			int c=col;
			while((r >= 0)&&(board.getPieces(r, c).size()==0)){r--;}
			if(r >= 0){ return (row-r);}
			return row;
		}else if(d.equals(Direction.SOUTH)){
			int r=row+1;
			int c=col;
			while((r < ROWS)&&(board.getPieces(r, c).size()==0)){r++;}
			if(r< ROWS){return (r-row);}
			return (ROWS-row);
		}else if(d.equals(Direction.WEST)){
			int r=row;
			int c=col-1;
			while((c >= 0)&&(board.getPieces(r, c).size()==0)){c--;}
			if(c >= 0){return (col-c);}
			return col;
		}else{//facing EAST
			int r=row;
			int c=col+1;
			while((c < COLUMNS)&&(board.getPieces(r, c).size()==0)){c++;}
			if(c < COLUMNS){return (c-col);}
			return (COLUMNS-c);
		}
	}
	/**
	 * Find the first nonempty square in the direction the robot is facing
	 * (not including the square that the robot is on),
	 *  
	 * @param thing the thing to be found in the direction Robot is facing
	 * @return <code>true</code> if that square contains an object of the given type.
	 *         <code>false</code> otherwise
	 */
	public boolean findPieceOnTheWay(String thing){
		Direction d = RobotPiece.getInstance().getDirection();
		int row = RobotPiece.getInstance().getRow();
		int col = RobotPiece.getInstance().getColumn();

		if(d.equals(Direction.NORTH)){
			int r=row-1;
			int c=col;
			while(r >= 0){
				if(board.getPieces(r, c).size()==0){r--;}
				else{break;}
			}
			if(r>=0){
				Stack<Piece> items = board.getPieces(r, c) ;  
				while(items.size()>0){
					Piece p = items.pop();
					if(p.getName().equals(thing)){return true;}
				}
			}
			return false;
		}else if(d.equals(Direction.SOUTH)){
			int r=row+1;
			int c=col;
			while(r < COLUMNS){
				if(board.getPieces(r, c).size()==0){r++;}
				else{break;}
			}
			if(r< COLUMNS){
				Stack<Piece> items = board.getPieces(r, c) ;  
				while(items.size()>0){
					Piece p = items.pop();
					if(p.getName().equals(thing)){return true;}
				}
			}
			return false;
		}else if(d.equals(Direction.WEST)){
			int r=row;
			int c=col-1;
			while(c >= 0){
				if(board.getPieces(r, c).size()==0){c--;}
				else{break;}
			}
			if(c >= 0){
				Stack<Piece> items = board.getPieces(r, c) ;  
				while(items.size()>0){
					Piece p = items.pop();
					if(p.getName().equals(thing)){return true;}
				}
			}
			return false;
		}else{//facing EAST
			int r=row;
			int c=col+1;
			while(c < ROWS){
				if(board.getPieces(r, c).size()==0){c++;}
				else{break;}
			}
			if(c < ROWS){
				Stack<Piece> items = board.getPieces(r, c) ;  
				while(items.size()>0){
					Piece p = items.pop();
					if(p.getName().equals(thing)){return true;}
				}
			}
			return false;
		}
	}
	/**
	 * Check whether the Robot is holding the specific thing
	 * @param thing --to be checked 
	 * @return <code>true</code> if Robot is holding the given thing
	 */
	public boolean isHolding(String thing){
		ArrayList<Piece> items = RobotPiece.getInstance().getPieces();
		Piece p;
		for(int i=0; i<items.size();i++){
			p = items.get(i);
			if(p.getName().equals(thing)){return true;}
		}
		return false;
	}
	/**
	 * Move the Robot forward the specific steps, if blocked, move as far as possible
	 * @param d --Direction toward which robot is moving
	 * @param steps --Robot is expected to be moved
	 */
	public void moveInDirection(Direction d, int steps){

		int posOldX = RobotPiece.getInstance().getRow();
		int posOldY = RobotPiece.getInstance().getColumn();
		int posNewX = posOldX;
		int posNewY = posOldY;
		int x = posOldX;
		int y = posOldY;
		Piece p = this.board.getPiece(x, y);

		switch(d){
		case NORTH:
			posNewX = posOldX - steps;
			if(posNewX <0){posNewX = 0;}
			while(x > posNewX){  
				x -= 1;
				p = this.board.getPiece(x, y);
				if((p != null)&&(p instanceof Block)){x += 1;break;}
			}
			RobotPiece.getInstance().moveTo(x, y);
			break;
		case EAST:
			posNewY = posOldY + steps;
			if(posNewY >= COLUMNS){posNewY = COLUMNS-1;}
			while(y<posNewY){
				y += 1;
				p = this.board.getPiece(x, y);
				if((p != null)&&(p instanceof Block)){y -= 1;break;}
			}
			RobotPiece.getInstance().moveTo(x, y);
			break;
		case SOUTH:
			posNewX = posOldX + steps;
			if(posNewX >= ROWS){posNewX = ROWS-1;}
			while(x<posNewX){
				x += 1;
				p = this.board.getPiece(x, y);
				if((p != null)&&(p instanceof Block)){x -= 1;break;}
			}
			RobotPiece.getInstance().moveTo(x, y);
			break;
		case WEST:
			posNewY = posOldY - steps;
			if(posNewY<0){posNewY = 0;}
			while(y>posNewY){
				y -= 1;
				p = this.board.getPiece(x, y);
				if((p != null)&&(p instanceof Block)){y += 1;break;}
			}
			RobotPiece.getInstance().moveTo(x, y);
			break;
		}
	}
	/**
	 * Move Robot in forward direction with given steps
	 * @param steps  --Robot is expected to be moved
	 */
	public void moveForward(int steps){
		Direction d = RobotPiece.getInstance().getDirection();
		this.moveInDirection(d, steps);
	}
	/**
	 * Move Robot in backward direction with given steps
	 * @param steps  --Robot is expected to be moved
	 */
	public void moveBackward(int steps){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			this.moveInDirection(Direction.SOUTH, steps);
	        break;
		case SOUTH:
			this.moveInDirection(Direction.NORTH, steps);
			break;
		case WEST:
			this.moveInDirection(Direction.EAST, steps);
			break;
		case EAST:
			this.moveInDirection(Direction.WEST, steps);
			break;
		}
	}
	/**
	 * Turn Robot 90 degree left according to current direction
	 */
	public void turnLeft(){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			RobotPiece.getInstance().setDirection(Direction.WEST);
			break;
		case SOUTH:
			RobotPiece.getInstance().setDirection(Direction.EAST);
			break;
		case WEST:
			RobotPiece.getInstance().setDirection(Direction.SOUTH);
			break;
		case EAST:
			RobotPiece.getInstance().setDirection(Direction.NORTH);
		}
	}
	/**
	 * Turn Robot 90 degrees right according to current direction
	 */
	public void turnRight(){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			RobotPiece.getInstance().setDirection(Direction.EAST);
			break;
		case SOUTH:
			RobotPiece.getInstance().setDirection(Direction.WEST);
			break;
		case WEST:
			RobotPiece.getInstance().setDirection(Direction.NORTH);
			break;
		case EAST:
			RobotPiece.getInstance().setDirection(Direction.SOUTH);
		}
	}
	/**
	 * Turn Robot 180 degrees from current direction
	 */
	public void trunAround(){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			RobotPiece.getInstance().setDirection(Direction.SOUTH);
			break;
		case SOUTH:
			RobotPiece.getInstance().setDirection(Direction.NORTH);
			break;
		case WEST:
			RobotPiece.getInstance().setDirection(Direction.EAST);
			break;
		case EAST:
			RobotPiece.getInstance().setDirection(Direction.WEST);
		}
	}
	/**
	 * Let robot pick up the specific thing on the square on which robot is standing
	 * @param s  name of the thing robot is expected to take
	 */
	public void pickUp(String s){
		Stack<Piece> items = this.board.getPieces(getRobotRow(), getRobotCol());
		while(items.size()>0){
			Piece p = items.pop();
			if(s.equals(p.getName())){
				RobotPiece.getInstance().pickItem(p);
				this.board.remove(p);
				break;
			}
		}
	}
	/**
	 * Let robot drop the specific thing on the current square
	 * @param s  --name of the thing robot is expected to drop
	 */
	public void dropPiece(String s){
		ArrayList<Piece> items = RobotPiece.getInstance().getPieces();
		Piece p;
		for(int i=0; i<items.size(); i++){
			p = items.get(i);
			if(s.equals(p.getName())){
				items.remove(i);
				this.board.place(p, RobotPiece.getInstance().getRow(), RobotPiece.getInstance().getColumn());
			    break;
			}
		}
		RobotPiece.getInstance().moveToTop();
		RobotPiece.getInstance().redraw();
	}
}