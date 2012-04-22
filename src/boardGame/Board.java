package boardGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import java.util.Vector;
import javax.swing.JPanel;

/**
 * A generic board that, together with <code>Piece</code>, can be used to display board
 * games.  This class maintains information about board positions, but does
 * not include any game-specific logic.
 * <p>
 * Note: The diamond operator, <>, is not used in this class in order to
 * make the class usable to pre-Java 7 programs.
 * 
 * @author Copyright &copy; 2012, David Matuszek
 * @version March 9, 2012
 */
public class Board extends Observable implements Observer {
    private Vector<Piece>[][] board;
    private Vector<Piece> allPieces = new Vector<Piece>();
    private int selectedRow = -1;
    private int selectedColumn = -1;
    private Piece selectedPiece = null;
    private int rows;
    private int columns;
    private int defaultSpeed = 10;
    private Board thisBoard;
    private JPanel display;
    protected boolean panelHasBeenResized = false;
    
    /**
     * To be notified when a user has used the mouse to drag a piece on this
     * board, a program can add an <code>Observer</code> to this field, and
     * override its <code>public void update(Observable o, Object arg)</code>
     * method. When the method is invoked, <code>o</code> will hold the
     * <code>Board.DragEvent</code> object, and <code>arg</code> will hold
     * the piece that was dragged. 
     */
    public final DragEvent dragEvent;
    
    /**
     * Creates a playing board with the given number of rows and columns. This
     * board contains a Swing <code>JPanel</code> that may be used in a GUI.
     * 
     * @param rows
     *        Desired number of rows.
     * @param columns
     *        Desired number of columns.
     */
    public Board(int rows, int columns) {
        display = new DisplayPanel();
        this.rows = rows;
        this.columns = columns;
        thisBoard = this;
        board = new Vector[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = new Vector<Piece>(1);
            }
        }
        display.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedRow = yToRow(e.getY());
                selectedColumn = xToColumn(e.getX());
                setSelectedPiece(getPiece(selectedRow, selectedColumn));
                setChanged();
                notifyObservers(new int[] { selectedRow, selectedColumn });
            }
        });        
        display.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent arg0) {
                panelHasBeenResized  = true;
            }
        });
        addDragListener();
        dragEvent = new DragEvent();
    }

    /**
     * Makes it possible for the user to drag pieces on this board.
     */
    protected void addDragListener() {
            Piece.MouseDragger listener = new Piece.MouseDragger(this);
            display.addMouseListener(listener);
            display.addMouseMotionListener(listener);
    }
    
    /**
     * Returns the JPanel on which this board is displayed.
     * 
     * @return The JPanel on which this board is displayed.
     */
    public JPanel getJPanel() {
        return display;
    }

    /**
     * Returns the number of rows in this board.
     * 
     * @return The number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in this board.
     * 
     * @return The number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the topmost piece at the given row and column in this board, or
     * null if the given location is empty.
     * 
     * @param row
     *        The row number.
     * @param column
     *        The column number.
     * @return The piece in the given [row][column], or
     *         <code>null</code> if that location is empty. If the board
     *         location contains more than one piece, the "topmost" piece is
     *         returned.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    public Piece getPiece(int row, int column) {
        if (board[row][column].isEmpty()) {
            return null;
        }
        return board[row][column].lastElement();
    }

    /**
     * Returns a (possibly empty) Stack of all the pieces in the given position.
     * The top element of the stack is the topmost element in that board
     * location.
     * 
     * @param row
     *        A row number on this board.
     * @param column
     *        A column number on this board.
     * @return The pieces in this board location.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    public Stack<Piece> getPieces(int row, int column) {
        Stack<Piece> pieces = new Stack<Piece>();
        for (Iterator<Piece> iter = board[row][column].iterator(); iter.hasNext();) {
            pieces.push(iter.next());
        }
        return pieces;
    }
    
    /**
     * Returns <code>true</code> if the given row and column on this board
     * contains no pieces.
     * 
     * @param row The row to examine.
     * @param column The column to examine.
     * @return <code>true</code> if this location is empty.
     */
    public boolean isEmpty(int row, int column) {
        return board[row][column].isEmpty();
    }

    /**
     * Given x-y coordinates, finds and returns the topmost piece at that
     * location on this board, or null if there is no such piece.
     * 
     * @param x
     *        The local x coordinate.
     * @param y
     *        The local y coordinate.
     * @return The piece in the [row][column] containing the
     *         given (x, y) coordinates, or <code>null</code> if that location
     *         is empty. If the board location contains more than one piece, the
     *         "topmost" piece is returned.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    protected Piece findPiece(int x, int y) {
        return getPiece(yToRow(y), xToColumn(x));
    }

    /**
     * Given an x coordinate, determines which column it is in.
     * 
     * @param x
     *        A local x coordinate.
     * @return The number of the column containing the given x coordinate.
     */
    public int xToColumn(int x) {
        return Math.min(columns - 1, (x * columns) / display.getWidth());
    }

    /**
     * Given a y coordinate, determines which row it is in.
     * 
     * @param y
     *        A local y coordinate.
     * @return The number of the row containing the given y coordinate.
     */
    public int yToRow(int y) {
        return Math.min(rows - 1, (y * rows) / display.getHeight());
    }

    /**
     * Returns the X coordinate of the left side of cells in the given column of
     * this board.
     * 
     * @param columnNumber
     *        A column number.
     * @return The X coordinate of the left side of that column.
     */
    public int columnToX(int columnNumber) {
        return (columnNumber * (display.getWidth() - 1)) / columns;
    }

    /**
     * Returns the Y coordinate of the top side of cells in the given column of
     * this board.
     * 
     * @param rowNumber
     *        A row number.
     * @return The Y coordinate of the top side of that row.
     *      */
    public int rowToY(int rowNumber) {
        return (rowNumber * (display.getHeight() - 1)) / rows;
    }

    /**
     * Places the given piece at the given location in this board.
     * It is possible to place more than one piece in a given board
     * location, in which case later pieces go "on top of" earlier
     * pieces.
     * 
     * @param piece
     *        The piece to be placed on this board.
     * @param row
     *        The row in which to place the piece.
     * @param column
     *        The column in which to place the piece.
     * @throws ArrayIndexOutOfBoundsException
     *         If the specified location does not exist.
     */
    public void place(Piece piece, int row, int column) {
        if (piece.getBoard() != null) {
            throw new IllegalArgumentException("Piece " + piece + " is already on a board");
        }
        board[row][column].add(piece);
        synchronized (allPieces) {
            allPieces.add(piece);
        }
        piece.placeHelper(this, row, column);
    }
    
    /**
     * Removes all pieces from this board.
     */
    public void clear() {
        for (int i = allPieces.size() - 1; i >= 0; i--) {
            remove (allPieces.get(i));
        }
    }

    /**
     * Removes the top piece at the given row and column on this board.
     * 
     * @param row The row of the piece to be removed.
     * @param column The column of the piece to be removed.
     * @return The top piece at the given location, or <code>null</code>
     *         if the given location is empty.
     * @throws ArrayIndexOutOfBoundsException If the specified location does not exist.
     */
    public Piece remove(int row, int column) {
        Piece piece = getPiece(row, column);
        if (piece == null) {
            return null;
        }
        remove(piece);
        return piece;
    }
    
    /**
     * For internal use only! (Used by Piece.changePosition.)
     * 
     * @param piece The piece to be moved.
     * @param oldRow Where the piece is being moved from.
     * @param oldColumn Where the piece is being moved from.
     * @param newRow Where the piece is being moved to.
     * @param newColumn Where the piece is being moved to.
     */
    final void changePositionOnBoard(Piece piece,
                                     int oldRow, int oldColumn,
                                     int newRow, int newColumn) {
        board[oldRow][oldColumn].remove(piece);
        board[newRow][newColumn].add(piece);
    }

    /**
     * Removes this piece from the board. Does nothing if the piece
     * is not, in fact, on the board.
     * 
     * @param piece
     *        The piece to remove.
     * @return <code>true</code> if anything has been changed.
     */
    public boolean remove(Piece piece) {
        if (piece == null || piece.getBoard() != this) {
            return false;
        }
        board[piece.getRow()][piece.getColumn()].remove(piece);
        synchronized (allPieces) {
            allPieces.remove(piece);
        }
        piece.removeHelper();
        return true;
    }

    /**
     * Ensures that the given piece will be drawn on top of any other pieces
     * in the same board location.
     * 
     * @param piece
     *        The piece to promote to the top.
     */
    protected void moveToTop(Piece piece) {
        synchronized (allPieces) {
            allPieces.remove(piece);
            allPieces.add(piece);
        }
    }

    /**
     * Sets the default speed of movement for pieces on this board, in squares
     * per second. This value is used only for pieces that do not specify their
     * own speed.
     * 
     * @param speed
     *        The default speed for pieces on this board.
     */
    public void setSpeed(int speed) {
        if (speed > 0)
            defaultSpeed = speed;
    }

    /**
     * Returns the default speed (in squares per second) of pieces on this
     * board.
     * 
     * @return The default speed for pieces on this board.
     */
    public int getSpeed() {
        return defaultSpeed;
    }

    /**
     * Returns the current width, in pixels, of a single cell on this board. The
     * value will change if this board is resized.
     * 
     * @return The current width of a cell on this board.
     */
    protected int getCellWidth() {
        return display.getWidth() / columns;
    }

    /**
     * Returns the current height, in pixels, of a single cell on this
     * board. The value will change if this board is resized.
     * 
     * @return The current height of a cell on this board.
     */
    protected int getCellHeight() {
        return display.getHeight() / rows;
    }

    /**
     * Determines whether the given row and column denote a legal position on
     * this board.
     * 
     * @param row
     *        The given row number.
     * @param column
     *        The given column number.
     * @return <code>true</code> if the given row and column number represent
     *         a valid location on this board
     */
    public boolean isLegalPosition(int row, int column) {
        if (row < 0 || row >= rows)
            return false;
        if (column < 0 || column >= columns)
            return false;
        return true;
    }
    
    /**
     * Redraws this board whenever a piece is modified.
     * 
     * @param changedPiece
     *        The piece that needs to be redrawn.
     * @param rectangle
     *        The area in which to redraw the piece.
     */
    @Override
    public final void update(Observable changedPiece, Object rectangle) {
        if (rectangle == null) {
            display.repaint();
        } else {
            Rectangle r = (Rectangle)rectangle;
            display.repaint(r.x, r.y, r.width, r.height);
        }
    }
    
    /**
     * Paints this board, not including any pieces that may be on it.
     * (Pieces are requested to paint themselves by the <code>paint</code>
     * method that is defined in the <code>DisplayPanel</code> inner class.)
     * 
     * @param g
     *        The Graphics context on which this board is painted.
     */
    public void paint(Graphics g) {
        int height = display.getHeight();
        int width = display.getWidth();
        int x, y;
        Color oldColor = g.getColor();
        Color backgroundColor = Color.white;
        Color lineColor = new Color(192, 192, 255);

        // Fill background with solid color
        g.setColor(backgroundColor);
        g.fillRect(0, 0, display.getWidth(), display.getHeight());
       
        // Paint vertical lines
        g.setColor(lineColor);
        for (int i = 0; i <= columns; i++) {
            x = columnToX(i);
            g.drawLine(x, 0, x, height);
        }
        // Paint horizontal lines
        for (int i = 0; i <= rows; i++) {
            y = rowToY(i);
            g.drawLine(0, y, width, y);
        }
        // Mark selected square
        if (selectedRow >= 0) {
            g.setColor(Color.BLACK);
            int left = columnToX(selectedColumn);
            int top = rowToY(selectedRow);
            int right = columnToX(selectedColumn + 1);
            int bottom = rowToY(selectedRow + 1);
            g.drawRect(left, top, right - left, bottom - top); 
        }
        g.setColor(oldColor);
    }


    /**
     * Displays the board contents (for debugging).
     */
    public void dump() {
        System.out.println("----------- Board is " + rows + " rows, "
                           + columns + " columns.");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!board[i][j].isEmpty()) {
                    System.out.println("Board [" + i + "][" + j + "] contains:");
                    for (Iterator<?> iter = board[i][j].iterator(); iter.hasNext();) {
                        Piece piece = (Piece) iter.next();
                        System.out.println("    " + piece.toString());
                    }
                }
            }
        }
        synchronized (allPieces) {
            System.out.println("Vector allPieces:");
            for (Iterator<Piece> iter = allPieces.iterator(); iter.hasNext();) {
                Piece piece = iter.next();
                System.out.println("    " + piece.toString());
            }
//            System.out.println("Selected piece = " + selectedPiece);
            System.out.println("----------- Pieces: ");
            for (Iterator<Piece> iter = allPieces.iterator(); iter.hasNext();) {
                Piece piece = iter.next();
                System.out.print(piece.toString());
                piece.dump();
            }
        }
    }
    
//  -------------------------------------------------- inner class DisplayPanel
    
    /**
     * The JPanel upon which this board is displayed.
     * 
     * @author David Matuszek
     */
    private class DisplayPanel extends JPanel {
        
        /**
         * Repaints this board and everything on it.
         * 
         * @param g
         *        The Graphics context on which this board is painted.
         */
        @Override
        public void update(Graphics g) {
            paint(g);
        }

        /**
         * Repaints this board and everything on it.
         * 
         * @param g
         *        The Graphics context on which this board is painted.
         */
        @Override
        public void paint(Graphics g) {
            // Paint the board
            thisBoard.paint(g);
            // Paint the pieces
            synchronized (allPieces) {
                for (Iterator<Piece> iter = allPieces.iterator(); iter.hasNext();) {
                    Piece piece = iter.next();
                    piece.paint(g, piece.getRectangle());
                }
            }
        }
    } // end inner class DisplayPanel

    /**
     * Mark the given piece as the selected one; unmark any previously
     * selected piece.
     * 
     * @param selectedPiece The piece to be "selected."
     */
    public void setSelectedPiece(Piece selectedPiece) {
        if (selectedPiece == null) {
            return;
        }
        this.selectedPiece = selectedPiece;
    }
    
    /**
     * Returns the currently selected piece, or <code>null</code> if
     * no piece has been selected on this board.
     * 
     * @return The currently selected piece.
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    /**
     * Causes no piece on this board to be selected.
     */
    public void unselectPiece() {
        selectedPiece = null;
    }
    
    /**
     * Returns <code>true</code> to indicate that this square of the
     * board can be selected; override this method to provide different
     * behavior.
     * 
     * @param row The row index of the square to consider.
     * @param column The column index of the square to consider.
     * @return <code>true</code>.
     */
    public boolean isSelectable(int row, int column) {
        return true;
    }

    /**
     * Returns the row number (counting from zero) of the currently
     * selected square, or -1 if none is selected.
     * 
     * @return The selected row number.
     */
    public int getSelectedRow() {
        return selectedRow;
    }
    
    /**
     * Returns the column number (counting from zero) of the currently
     * selected square, or -1 if none is selected.
     * 
     * @return The selected column number.
     */
    public int getSelectedColumn() {
        return selectedColumn;
    }
       
    /**
     * If the given square is selectable, select it, otherwise
     * do nothing.
     * 
     * @param row The row number of the square being selected.
     * @param column The column number of the square being selected.
     */
    public void setSelectedSquare(int row, int column) {
        if (isSelectable(row, column)) {
            selectedRow = row;
            selectedColumn = column;
            display.repaint(); // needed to erase old selection lines
        }
    }
    
    /**
     * Unselects the currently selected square, if any.
     */
    public void unselectSquare() {
        selectedRow = selectedColumn = -1;
        display.repaint(); // needed to erase old selection lines
    }
    
//  -------------------------------------------------- inner class DragEvent   

    /**
     * Allows drag events to be observed. To use this class for a
     * given board, do the following:
     * <pre> board.dragEvent.addObserver(new Observer() {
     *     &#0064;Override
     *     public void update(Observable o, Object arg) {
     *         ...
     *     }
     * });</pre>
     * The <code>update</code> method will be called when a piece has
     * been released in a new (or possibly the same) location. The
     * parameter <code>o</code> will hold the <code>Board.DragEvent</code>
     * object, while <code>arg</code> will hold a reference to the
     * piece that was moved.
     * 
     * @author David Matuszek
     */
    public static class DragEvent extends Observable {
        void reportEvent(Piece piece) {
            setChanged();
            notifyObservers(piece);
        }
    }
}