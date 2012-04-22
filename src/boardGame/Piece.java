package boardGame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

/**
 * An abstract class that represents a playing piece. This class, together with
 * Board, can be used to display board games. This class maintains information
 * about board positions, but does not include any game-specific logic.
 * 
 * @author Copyright &copy; 2012, David Matuszek
 * @version March 9, 2012
 */
public abstract class Piece extends Observable {
    
    /** The board on which this piece has been placed (if any). */
    protected Board board;
    
    private String name;
    private int row = -1;  // -1 means not on any board
    private int column = -1;
    private boolean moving = false;
    private boolean draggable = false;
    private boolean selectable = true;
    private int x;
    private int y;
    private int speed = -1; // Negative means to use board default
    private static final int PAUSE_MS = 15;
    private static final int FRAME_RATE = 1000 / PAUSE_MS;
    private static Piece pieceBeingDragged = null;

    /**
     * Creates a piece. The piece is not placed on a
     * board; for that, see the instance methods
     * <code>board.place(piece, row, column)</code> and
     * <code>piece.place(board, row, column)</code>.
     */
    public Piece() {
        name = getClass().getName();
    }

    /**
     * Creates a piece with the given name. The piece is not
     * placed on a board; for that, see the instance methods
     * <code>board.place(piece, row, column)</code> and
     * <code>piece.place(board, row, column)</code>.
     * 
     * @param name The user-defined name of this piece.
     */
    public Piece(String name) {
        this.name = name;
    }
    
    /**
     * Returns the name of this piece; possibly useful for debugging.
     * 
     * @return The name of this piece.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the board on which this piece has been placed.
     * 
     * @return The board (if any) containing this piece.
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Returns a printable String representing this piece.
     * 
     * @return A printable representation of this piece.
     */
    @Override
    public String toString() {
        return name + "[" + row + "][" + column + "]";
    }

    /**
     * Determines whether the piece can be dragged by the mouse.
     * 
     * @param draggable
     *        Tell whether the piece can be dragged by user mouse
     *        movement.
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * Returns <code>true</code> if the piece can be dragged by the mouse.
     * 
     * @return <code>true</code> if the piece is draggable.
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Sets whether this piece can be selected by the mouse.
     * 
     * @param selectable
     *  A value of <code>true</code> means that the piece can
     *  be selected with the mouse.
     *  
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Returns <code>true</code> if the piece can be selected by the mouse.
     * 
     * @return <code>true</code> if the piece is selectable.
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Returns the x-coordinate at which this piece will be painted.
     * 
     * @return The x-coordinate.
     */
    protected int getX() {
        return moving ? x : board.columnToX(column);
    }

    /**
     * Returns the y-coordinate at which this piece will be painted.
     * 
     * @return The y-coordinate.
     */
    protected int getY() {
            return moving ? y : board.rowToY(row);
    }

    /**
     * Returns the rectangle in which this piece should be painted.
     * 
     * @return The rectangle in which to paint this piece.
     */
    protected Rectangle getRectangle() {
        int leftEdge = getX() + 1;
        int topEdge = getY() + 1;
        int width;
        int height;
        if (moving) {
            width = board.getCellWidth() - 1;
            height = board.getCellHeight() - 1;
        } else {
            width = board.columnToX(column + 1) - leftEdge;
            height = board.rowToY(row + 1) - topEdge;
        }
        return new Rectangle(leftEdge, topEdge, width, height);
    }

    /**
     * Returns the row that this piece is in, or -1 if this piece is not
     * currently on some board.
     * 
     * @return The row number.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column that this piece is in, or -1 if this piece is not 
     * currently on some board.
     * 
     * @return The column number.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Tests whether this piece is placed in a legal position on some board.
     * 
     * @return <code>true</code> if this piece is at a legal position.
     */
    protected boolean atLegalPosition() {
        return board.isLegalPosition(row, column);
    }

    /**
     * Places this piece at the given row and column on the board.
     * @param board 
     *        The board on which to place this piece.
     * @param row
     *        The row in which to place this piece.
     * @param column
     *        The column in which to place this piece.
     */
    public void place(Board board, int row, int column) {
        board.place(this, row, column);
    }
    
    /** For internal use only! 
     * 
     * @param board The board on which to place this piece.
     * @param row The row number.
     * @param column The column number.
     */
    final void placeHelper(Board board, int row, int column) {
        // When a piece is placed on a board, both the piece and
        // the board must be updated. This method is called from
        // Board.place to update the piece.  
        this.board = board;
        this.row = row;
        this.column = column;
        x = board.columnToX(column);
        y = board.rowToY(row);

        addObserver(board);
        redraw(getRectangle());
    }

    /**
     * Removes this piece from whatever board it may be on,
     * but does not delete the piece itself.
     */
    public void remove() {
        if (!atLegalPosition())
            return;
        board.remove(this);
    }
    
    /** For internal use only! */
    final void removeHelper() {
        redraw();
        board = null;
        row = column = -1;
    }

    /**
     * Tests whether this piece is on some (any) board.
     * 
     * @return <code>true</code> if this piece is currently on a board.
     */
    public boolean isOnBoard() {
        return board != null;
    }

    /**
     * Tests whether this piece is on the specified board.
     * 
     * @return <code>true</code> if this piece is currently on
     *  the given board.
     */
    public boolean isOnBoard(Board board) {
        return this.board == board;
    }

    /**
     * Sets the speed of movement (in squares/second) for this piece. Values of
     * 1 to 10 are reasonable; very large values will cause this piece to
     * "teleport" to the new location; a zero or negative value will cause the
     * default speed (set by the board) to be used.
     * 
     * @param speed
     *        The desired speed of movement, in pixels/redraw.
     */
    public void setSpeed(int speed) {
        this.speed = speed > 0 ? speed : board.getSpeed();
    }

    /**
     * Get the speed (in squares/second) at which this piece will move.
     * 
     * @return This piece's speed of movement, in pixels/redraw.
     */
    public int getSpeed() {
        if (speed <= 0) {
            if (board != null) {
                return board.getSpeed();
            }
            return 0;
        }
        return speed;
    }

    /**
     * Paints this piece on the board within the given rectangle;
     * must be implemented by a subclass.
     * 
     * @param g The Graphics object on which painting should be done.
     * @param r The rectangle in which to paint this piece.
     */
    public abstract void paint(Graphics g, Rectangle r);
    
    /**
     * Ensures that this piece will be drawn on top of any other pieces
     * in the same location on the board.
     */
    public void moveToTop() {
        if (board != null) {
            board.moveToTop(this);
        }
    }

    /**
     * Moves this piece the given number of rows and columns.
     * 
     * @param deltaRow
     *        The number of rows down to move this piece; a negative number will
     *        move the piece up.
     * @param deltaColumn
     *        The number of columns to move this piece to the right; a negative
     *        number will move the piece left.
     * @return False if the move would take the piece outside the boundaries of
     *         the board.
     */
    public boolean move(int deltaRow, int deltaColumn) {
        return moveTo(row + deltaRow, column + deltaColumn);
    }

    /**
     * Moves this piece to a new position on the board.
     * 
     * @param newRow
     *        The destination row.
     * @param newColumn
     *        The destination column.
     * @return <code>false</code> if the destination is not a legal board
     *         position, or if the piece is already moving.
     */
    public boolean moveTo(int newRow, int newColumn) {
        if (!board.isLegalPosition(newRow, newColumn))
            return false;
        if (moving)
            return false;
        int startX = board.columnToX(column);
        int startY = board.rowToY(row);
        int finishX = board.columnToX(newColumn);
        int finishY = board.rowToY(newRow);
        int changeInX = finishX - startX;
        int changeInY = finishY - startY;
        Rectangle oldRect = getRectangle();
        Rectangle newRect = new Rectangle(oldRect);
        
        // compensate for squares being slightly different sizes
        oldRect.width += 2;
        newRect.width += 2;
        oldRect.height += 2;
        newRect.height += 2;

        // Move smoothly towards new position
        moving = true;
        board.moveToTop(this);
        int deltaRow = Math.abs(row - newRow);
        int deltaColumn = Math.abs(column - newColumn);
        int distance = Math.max(deltaRow, deltaColumn)
                + Math.min(deltaRow, deltaColumn) / 2;
        int numberOfSteps = distance * FRAME_RATE / getSpeed();
        
        for (int i = 1; i <= numberOfSteps; i++) {
            oldRect.x = x;
            oldRect.y = y;
            x = startX + (i * changeInX) / numberOfSteps;
            y = startY + (i * changeInY) / numberOfSteps;
            newRect.x = x;
            newRect.y = y;
            board.getJPanel().paintImmediately(oldRect.union(newRect));
//            redraw(oldRect.union(newRect));

            try {
                Thread.sleep(PAUSE_MS);
            }
            catch (InterruptedException e) { /* Deliberately empty */ }
            
        }
        moving = false;
        if (canMoveTo(newRow, newColumn)) {
            changePosition(newRow, newColumn);
        }
        redraw(oldRect.union(newRect));
        return true;
    }

    /**
     * Determines whether this piece can be moved to the specified
     * location. The default behavior is to return <code>true</code>
     * if this piece is on some board, and the specified location
     * exists on that board.
     * <p>
     * This method delegates the decision to the <code>canMoveTo</code>
     * method. To ensure consistency, this method may not be overridden;
     * but overriding <code>canMoveTo</code> with more specific tests
     * will alter the behavior of both methods.
     * 
     * @param deltaRow The number of squares to move down.
     * @param deltaColumn The number of squares to move to the right.
     * @return <code>true</code> if the move can be made.
     */
    public final boolean canMove(int deltaRow, int deltaColumn) {
        return canMoveTo(row + deltaRow, column + deltaColumn);
    }
    
    /**
     * Determines whether this piece can be moved to the specified
     * location.  This method returns <code>true</code> if this
     * piece is on a board and the location is legal for that board.
     * This method can be overridden with more specific tests.
     * 
     * @param newRow The desired row.
     * @param newColumn The desired column.
     * @return <code>true</code> if the move can be made.
     */
    public boolean canMoveTo(int newRow, int newColumn) {
        if (board == null) return false;
        return board.isLegalPosition(newRow, newColumn);
    }

    /**
     * Change the position of this piece on the board.
     * 
     * @param newRow
     *        The new row for this piece.
     * @param newColumn
     *        The new column for this piece.
     */
    private void changePosition(int newRow, int newColumn) {
        if (!atLegalPosition())
            return;
        int oldRow = row;
        int oldColumn = column;
        board.changePositionOnBoard(this, oldRow, oldColumn, newRow, newColumn);
        row = newRow;
        column = newColumn;
        x = board.columnToX(column);
        y = board.rowToY(row);
        redraw();
    }

    /**
     * Causes this piece to be redrawn.
     */
    public void redraw() {
        redraw(getRectangle());
    }

    /**
     * Causes the given rectangle to be redrawn.
     * 
     * @param rect The area to be redrawn.
     */
    public void redraw(Rectangle rect) {
        setChanged();
        notifyObservers(rect);
        try {
            Thread.sleep(PAUSE_MS);
        }
        catch (InterruptedException e) { /* Deliberately empty */ }
    }

//  ------------------------------ Inner class MouseDragger
    
    /**
     * Support for dragging Pieces on a board.
     * @author David Matuszek
     */
    static final class MouseDragger extends MouseAdapter {
        private Board board;

        /**
         * Constructor for a MouseDragger on the given board.
         * @param board The board to which this MouseDragger applies.
         */
        MouseDragger(Board board) {
            this.board = board;
        }

        /**
         * When the mouse button is pressed over a draggable piece,
         * begins the dragging process. Only one piece can be dragged
         * at a time.
         * 
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent e) {
            board.setSelectedSquare(board.yToRow(e.getY()), board.xToColumn(e.getX()));
            Piece chosenPiece = board.findPiece(e.getX(), e.getY());
            if (chosenPiece == null){
                return;
            }
            if (chosenPiece.isSelectable()) {
                board.setSelectedPiece(chosenPiece);
            }
            if (pieceBeingDragged != null) {
                return; // can only drag one piece at a time
            }
            if (!chosenPiece.draggable) {
                return;
            }
            pieceBeingDragged = chosenPiece;
            board = pieceBeingDragged.board;
            pieceBeingDragged.moving = true;
            board.moveToTop(pieceBeingDragged);
        }

        /**
         * Continues dragging a piece.
         * 
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            int x;
            int y;

            if (pieceBeingDragged == null)
                return;
            // Don't allow drag outside board boundaries
            int maxX = board.columnToX(board.getColumns() - 1);
            int maxY = board.rowToY(board.getRows() - 1);
            x = e.getX() - board.getCellWidth() / 2;
            if (x < 0) {
                x = 0;
            } else {
                if (x > maxX)
                    x = maxX;
            }
            y = e.getY() - board.getCellHeight() / 2;
            if (y < 0) {
                y = 0;
            } else {
                if (y > maxY)
                    y = maxY;
            }
            pieceBeingDragged.x = x;
            pieceBeingDragged.y = y;
            // Track mouse movement
            pieceBeingDragged.setChanged();
            pieceBeingDragged.notifyObservers();

        }

        /**
         * Terminates the drag process, putting the dragged piece in the
         * nearest square.
         * 
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (pieceBeingDragged == null) return;
            Rectangle oldRect = pieceBeingDragged.getRectangle();
            Rectangle newRect = oldRect;
            int newRow = board.yToRow(pieceBeingDragged.y
                    + board.getCellHeight() / 2);
            int newColumn = board.xToColumn(pieceBeingDragged.x
                    + board.getCellWidth() / 2);

            if (pieceBeingDragged.canMoveTo(newRow, newColumn)) {
                pieceBeingDragged.changePosition(newRow, newColumn);
            }
            pieceBeingDragged.moving = false;
            newRect = pieceBeingDragged.getRectangle();
            pieceBeingDragged.redraw(pieceBeingDragged.enlarge(oldRect.union(newRect)));  
            board.dragEvent.reportEvent(pieceBeingDragged);
            pieceBeingDragged = null;
        }
    }
    
    /**
     * Makes the given rectangle slightly larger. The given rectangle
     * is modified and also returned.
     * 
     * @param r A Rectangle to be enlarged.
     * @return The same Rectangle, after enlargement.
     */
    private static Rectangle enlarge(Rectangle r) {
        r.x -= 2;
        r.y -= 2;
        r.width += 4;
        r.height += 4;
        return r;
    }

    // ------------------------------ Debugging methods

    /**
     * Debugging method to print out the status of this piece.
     */
    public void dump() {
        System.out.println(" Piece " + this + ":  x = " + x + ", y = " + y);
        System.out.println("    draggable = " + draggable + ", selectable = " +
                           selectable + ", moving = " + moving);
    }
}