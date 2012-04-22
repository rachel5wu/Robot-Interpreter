package weizhuo;

import boardGame.Piece;
import java.util.ArrayList;
import examples.Block;

public class RobotController {
	
	private RobotGui robotGui;
	ArrayList<Piece> freeItemList  = new ArrayList<Piece>();

	RobotController(RobotGui robotGui){
		this.robotGui = robotGui;
	}
	/**
	 * Set the robot to face a special direction
	 * @param dir --Direction robot is going to face to
	 */
	public void faceTo(Direction dir){
		(RobotPiece.getInstance()).setDirection(dir);
		(RobotPiece.getInstance()).redraw();
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nFace to:"+ dir);
	}
	/**
	 * Make the Robot turn left according to current direction
	 */
	public void turnLeft(){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			(RobotPiece.getInstance()).setDirection(Direction.WEST);
			break;
		case WEST:
			(RobotPiece.getInstance()).setDirection(Direction.SOUTH);
			break;
		case SOUTH:
			(RobotPiece.getInstance()).setDirection(Direction.EAST);
			break;
		case EAST:
			(RobotPiece.getInstance()).setDirection(Direction.NORTH);
			break;
		}
		(RobotPiece.getInstance()).redraw();
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nTurn Left");
	}
	/**
	 * Make the Robot turn right according to current direction
	 */
	public void turnRight(){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			(RobotPiece.getInstance()).setDirection(Direction.EAST);
			break;
		case EAST:
			(RobotPiece.getInstance()).setDirection(Direction.SOUTH);
			break;
		case SOUTH:
			(RobotPiece.getInstance()).setDirection(Direction.WEST);
			break;
		case WEST:
			(RobotPiece.getInstance()).setDirection(Direction.NORTH);
			break;
		}
		(RobotPiece.getInstance()).redraw();
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nTurn Right");
	}
	/**
	 * Make the Robot turn Backwards according to current Direction
	 */
	public void turnBack(){
		Direction d = RobotPiece.getInstance().getDirection();
		switch(d){
		case NORTH:
			(RobotPiece.getInstance()).setDirection(Direction.SOUTH);
			break;
		case EAST:
			(RobotPiece.getInstance()).setDirection(Direction.WEST);
			break;
		case SOUTH:
			(RobotPiece.getInstance()).setDirection(Direction.NORTH);
			break;
		case WEST:
			(RobotPiece.getInstance()).setDirection(Direction.EAST);
			break;
		}
		(RobotPiece.getInstance()).redraw();
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nTurn Back");
	}
	/**
	 * Move the robot s steps given by parameter, in the current direction 
	 * It will get blocked if there are blocks in the way  
	 * @param s steps robot is going to move
	 */
	public void move(String s){
		try{
			int steps = Integer.parseInt(s);
			int posOldX = RobotPiece.getInstance().getRow();
			int posOldY = RobotPiece.getInstance().getColumn();
			int posNewX = posOldX;
			int posNewY = posOldY;
			int x = posOldX;
			int y = posOldY;
			boolean routeOK = true;
			Piece p = this.robotGui.getViewBoard().getPiece(posOldX, posOldY);
			Direction d = RobotPiece.getInstance().getDirection();

			switch(d){
			case NORTH:
				posNewX = posOldX - steps;

				while((x>=posNewX)){
					p = this.robotGui.getViewBoard().getPiece(x, posOldY);
					if((p != null)&&(p instanceof Block)){routeOK = false;}
					x -= 1;
				}

				if(this.robotGui.getViewBoard().isLegalPosition(posNewX, posNewY)){
					if(!routeOK){
						String oldString = this.robotGui.getMessageArea().getText();
						this.robotGui.setMessageArea(oldString+"\nOops.there's a wall!");
					}else{
						RobotPiece.getInstance().moveTo(posNewX, posNewY);		
					}
				}else{
					String oldString = this.robotGui.getMessageArea().getText();
					this.robotGui.setMessageArea(oldString+"\nIndex Out of Bound!");
				}	

				break;
			case EAST:
				posNewY = posOldY + steps;

				while((y<=posNewY)){
					p = this.robotGui.getViewBoard().getPiece(posOldX, y);
					if((p != null)&&(p instanceof Block)){routeOK = false;}
					y += 1;
				}

				if(this.robotGui.getViewBoard().isLegalPosition(posNewX, posNewY)){
					if(!routeOK){
						String oldString = this.robotGui.getMessageArea().getText();
						this.robotGui.setMessageArea(oldString+"\nOops.there's a wall!");
					}else{
						RobotPiece.getInstance().moveTo(posNewX, posNewY);		
					}
				}else{
					String oldString = this.robotGui.getMessageArea().getText();
					this.robotGui.setMessageArea(oldString+"\nIndex Out of Bound!");
				}	

				break;
			case SOUTH:
				posNewX = posOldX + steps;

				while((x<=posNewX)){
					p = this.robotGui.getViewBoard().getPiece(x, posOldY);
					if((p != null)&&(p instanceof Block)){routeOK = false;}
					x += 1;
				}

				if(this.robotGui.getViewBoard().isLegalPosition(posNewX, posNewY)){
					if(!routeOK){
						String oldString = this.robotGui.getMessageArea().getText();
						this.robotGui.setMessageArea(oldString+"\nOops.there's a wall!");
					}else{
						RobotPiece.getInstance().moveTo(posNewX, posNewY);		
					}
				}else{
					String oldString = this.robotGui.getMessageArea().getText();
					this.robotGui.setMessageArea(oldString+"\nIndex Out of Bound!");
				}	
				break;
			case WEST:
				posNewY = posOldY - steps;

				while(y>=posNewY){
					p = this.robotGui.getViewBoard().getPiece(posOldX, y);
					if((p != null)&&(p instanceof Block)){routeOK = false;}
					y -= 1;
				}

				if(this.robotGui.getViewBoard().isLegalPosition(posNewX, posNewY)){
					if(!routeOK){
						String oldString = this.robotGui.getMessageArea().getText();
						this.robotGui.setMessageArea(oldString+"\nOops.there's a wall!");
					}else{
						RobotPiece.getInstance().moveTo(posNewX, posNewY);		
					}
				}else{
					String oldString = this.robotGui.getMessageArea().getText();
					this.robotGui.setMessageArea(oldString+"\nIndex Out of Bound!");
				}	
				break;
			}

			String oldString = this.robotGui.getMessageArea().getText();
			oldString += "\nMove From"+posOldX+","+posOldY;
			oldString += "\nMove To"+RobotPiece.getInstance().getRow()+","+RobotPiece.getInstance().getColumn();
			this.robotGui.setMessageArea(oldString);

		}catch(Exception e){
			String oldString = this.robotGui.getMessageArea().getText();
			this.robotGui.setMessageArea(oldString+"\nIllegal Input!");
			this.robotGui.setStepsField("Steps:");
		}

		this.robotGui.updateUnpickedItems();
		this.robotGui.updatePickedItems();
	}
	/**
	 * Place the robot to a specified square if and only if robot now is off-board
	 * @param xPos  --target square's x-coordinate
	 * @param yPos  --target square's y-coordinate
	 */
	public void place(String xPos, String yPos){
		try{
			if(RobotPiece.getInstance().isOnBoard()){
				String oldString = this.robotGui.getMessageArea().getText();
				this.robotGui.setMessageArea(oldString+"\nRobot is onBoard!");
			}else{
				int x = Integer.parseInt(xPos);
				int y = Integer.parseInt(yPos);
				if(this.robotGui.getViewBoard().isLegalPosition(x, y)){
					Piece p = this.robotGui.getViewBoard().getPiece(x, y);
					if((p != null)&&( p instanceof Block)){
						String oldString = this.robotGui.getMessageArea().getText();
						this.robotGui.setMessageArea(oldString+"\nOops.there's a wall!");
					}else{	
						this.robotGui.getViewBoard().place(RobotPiece.getInstance(), x, y);
					}
				}else{					
					String oldString = this.robotGui.getMessageArea().getText();
					this.robotGui.setMessageArea(oldString+"\nIndex Out of Bound!");
				}
				this.robotGui.updateUnpickedItems();
				this.robotGui.updatePickedItems();
			}

			String oldString = this.robotGui.getMessageArea().getText();
			oldString += "\nRobot is at "+RobotPiece.getInstance().getRow()+","+RobotPiece.getInstance().getColumn();
			this.robotGui.setMessageArea(oldString);

		}catch(Exception e){
			String oldString = this.robotGui.getMessageArea().getText();
			this.robotGui.setMessageArea(oldString+"\nIllegal Input!");
			this.robotGui.setXPosField("X:");
			this.robotGui.setYPosField("Y:");
		}

	}
	/**
	 * Remove the robot from board
	 */
	public void remove(){
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nOff Board!");
		this.robotGui.getViewBoard().remove(RobotPiece.getInstance());
	}
	/**
	 * Pick up a selected item from current square
	 * @param p -- the item robot want to pick
	 */
	public void pickUp(Piece p){
		RobotPiece.getInstance().pickItem(p);
		this.robotGui.getViewBoard().remove(p);
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nPicks up "+p.getName());
		this.robotGui.updateUnpickedItems();
		this.robotGui.updatePickedItems();
	}
	/**
	 * Drop down a selected item from Robot's picked item list to current square
	 * @param p  --item robot want to drop down
	 */
	public void dropPiece(Piece p){
		RobotPiece.getInstance().removeItem(p);
		this.robotGui.getViewBoard().place(p, RobotPiece.getInstance().getRow(), RobotPiece.getInstance().getColumn());
		String oldString = this.robotGui.getMessageArea().getText();
		this.robotGui.setMessageArea(oldString+"\nDrops "+p.getName());
		this.robotGui.updateUnpickedItems();
		this.robotGui.updatePickedItems();
		RobotPiece.getInstance().moveToTop();
	}
	/**
	 * Clear Message Display Area
	 */
	public void clearMessage(){
		this.robotGui.setMessageArea("");
	}
}
