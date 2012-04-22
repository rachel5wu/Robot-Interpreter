/**
 * 
 */
package weizhuo;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import boardGame.Board;
import boardGame.Piece;
import examples.Block;

/**
 * @author weizhuowu
 *
 */
public class RobotGui extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;

	private RobotGui robotGui = this;
	private RobotController controller = new RobotController(robotGui);
	private Board viewBoard;
	private JPanel viewPanel;
	private JPanel controlPanel;
	private JTextArea messageArea = new JTextArea("Robot is born!");
	private JScrollPane messagePanel = new JScrollPane(messageArea);

	private JButton northButton = new JButton("North");
	private JButton southButton = new JButton("South");
	private JButton westButton = new JButton("West");
	private JButton eastButton = new JButton("East");

	private JButton rightButton = new JButton("Right>>");
	private JButton leftButton = new JButton("<<Left");
	private JButton reverseButton = new JButton("Back");

	private JTextField stepsField = new JTextField("Steps:");
	private JButton moveButton = new JButton("Move");

	private JTextField xPosField = new JTextField("X:");
	private JTextField yPosField = new JTextField("Y:");
	private JButton placeButton = new JButton("Place");
	
	private JButton clearButton = new JButton("clear Message");
	private JComboBox unPickeditems = new JComboBox();
	private JButton pickButton = new JButton("Pick up");
	private JComboBox pickeditems = new JComboBox();
	private JButton dropButton = new JButton("Drop");

	private JButton removeButton = new JButton("Remove");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RobotGui robotGui = new RobotGui();
		robotGui.layOutGui();
		robotGui.addButtonListeners();
	}
	/**
	 * Creates, but does not display, the JPanel used in this program.
	 */
	private void layOutGui() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		controlPanel = new JPanel();
		viewPanel = new JPanel();

		/*View Panel*/
		viewPanel.setBorder(new TitledBorder("View:"));
		viewBoard = new Board(12, 12);
		viewPanel.setLayout(new GridLayout(1, 1));
		viewPanel.add(viewBoard.getJPanel());
		createNewPiece();
		/*Control Panel*/
		JPanel directPanel = new JPanel();
		directPanel.setLayout(new GridLayout(3,3));
		directPanel.add(new JPanel());
		directPanel.add(northButton);
		directPanel.add(new JPanel());
		directPanel.add(westButton, BorderLayout.WEST);
		directPanel.add(new JPanel());
		directPanel.add(eastButton, BorderLayout.EAST);
		directPanel.add(new JPanel());
		directPanel.add(southButton);
		directPanel.add(new JPanel());


		JPanel lrPanel = new JPanel();
		lrPanel.setLayout(new GridLayout(1, 3));
		lrPanel.add(leftButton);
		lrPanel.add(reverseButton);
		lrPanel.add(rightButton);

		JPanel movePanel = new JPanel();
		movePanel.setLayout(new GridLayout(1, 2));
		movePanel.add(stepsField);
		movePanel.add(moveButton);

		JPanel moveToPanel = new JPanel();
		moveToPanel.setLayout(new BorderLayout());
		JPanel xyPanel = new JPanel();
		xyPanel.setLayout(new GridLayout(2, 1));
		xyPanel.add(xPosField);
		xyPanel.add(yPosField);
		moveToPanel.add(xyPanel, BorderLayout.CENTER);
		moveToPanel.add(placeButton, BorderLayout.EAST);

		JPanel allMovePanel = new JPanel();
		allMovePanel.setLayout(new GridLayout(3, 1));
		allMovePanel.add(lrPanel);
		allMovePanel.add(movePanel);
		allMovePanel.add(moveToPanel);

		Iterator<Piece> iter1 = viewBoard.getPieces(RobotPiece.getInstance().getRow(), RobotPiece.getInstance().getColumn()).iterator();
		while(iter1.hasNext()){
			Piece p = iter1.next();
			if(! (p instanceof RobotPiece)){
				unPickeditems.addItem(p);
			}
		}
		Iterator<Piece> iter2 = RobotPiece.getInstance().getPieces().iterator();
		while(iter2.hasNext()){
			pickeditems.addItem(iter2.next());
		}

		JPanel actPanel = new JPanel();
		actPanel.setLayout(new GridLayout(6, 1));
		actPanel.add(clearButton);
		actPanel.add(unPickeditems);
		actPanel.add(pickButton);
		actPanel.add(pickeditems);
		actPanel.add(dropButton);
		actPanel.add(removeButton);

		controlPanel.setBorder(new TitledBorder("Control Panel:"));
		controlPanel.setLayout(new GridLayout(1, 3));
		controlPanel.add(directPanel);
		controlPanel.add(allMovePanel);
		controlPanel.add(actPanel);

		//messagePanel

		messagePanel.setBorder(new TitledBorder("Message:"));

		this.setLayout(new BorderLayout());
		this.add(viewPanel, BorderLayout.CENTER);
		this.add(controlPanel, BorderLayout.SOUTH);
		this.add(messagePanel, BorderLayout.EAST);

		setVisible(true);
		setTitle("Robot");
		setSize(1000, 800);
	}

	/**
	 * Creates pieces and puts it on the dock.
	 */
	protected void createNewPiece() {

		Piece robot = RobotPiece.getInstance();
		Piece[] coins = new Coin[4];
		Piece[] diamonds = new Diamond[4];
		Piece[] oilCans = new OilCan[4];
		Piece[] tazers = new Tazer[4];
		Piece[] blocks = new Block[4];

		for(int i=0; i<4; i++){
			coins[i] = new Coin("Coin");
			placePieceOnBoard(coins[i]);
			diamonds[i] = new Diamond("Diamond");
			placePieceOnBoard(diamonds[i]);
			oilCans[i] = new OilCan("OilCan");
			placePieceOnBoard(oilCans[i]);
			tazers[i] = new Tazer("Tazer");
			placePieceOnBoard(tazers[i]);
			blocks[i] = new Block();
			placePieceOnBoard(blocks[i]);
		}

		placePieceOnBoard(robot);

	}

	/**
	 * Moves a Piece to the board randomly.
	 */
	private void placePieceOnBoard(Piece piece) {

		Random random = new Random();
		int row = random.nextInt(12);
		int column = random.nextInt(12);
		if(viewBoard.getPiece(row, column) != null){
			Piece oldPiece = viewBoard.getPiece(row, column);

			while((oldPiece instanceof Block)){
				row = random.nextInt(12);
				column = random.nextInt(12);
				oldPiece = viewBoard.getPiece(row, column);
			}
		}
		viewBoard.place(piece, row, column);

	}

	/**
	 * Adds listeners to the buttons.
	 */
	private void addButtonListeners() {
		// Create new piece
		northButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.faceTo(Direction.NORTH);
			}
		});
		southButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.faceTo(Direction.SOUTH);
			}
		});
		eastButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.faceTo(Direction.EAST);
			}
		});
		westButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.faceTo(Direction.WEST);
			}
		});
		rightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				controller.turnRight();
			}
		});
		leftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				controller.turnLeft();
			}
		});
		reverseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				controller.turnBack();
			}
		});
		moveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				controller.move(stepsField.getText());
			}
		});
		placeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				controller.place(xPosField.getText(), yPosField.getText());
			}
		});
		removeButton.addActionListener(new ActionListener(){
			@Override 
			public void actionPerformed(ActionEvent e){
				controller.remove();
			}
		});
		pickButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				controller.pickUp((Piece)unPickeditems.getSelectedItem());
			}
		});
		dropButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				controller.dropPiece((Piece)pickeditems.getSelectedItem());
			}
		});
		clearButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				controller.clearMessage();
			}
		});


	}


	public Board getViewBoard(){
		return this.viewBoard;
	}
	public JPanel getViewPanel(){
		return this.viewPanel;
	}
	public JPanel getcontrolPanel(){
		return this.controlPanel;
	}
	public JTextArea getMessageArea(){
		return messageArea;
	}
	public void setMessageArea(String s){
		messageArea.setText(s);
	}
	public void setXPosField(String s){
		xPosField.setText(s);
	}
	public void setYPosField(String s){
		yPosField.setText(s);
	}
	public JTextField getXPosField(){
		return xPosField;
	}
	public JTextField getYPosField(){
		return yPosField;
	}
	public void setStepsField(String s){
		stepsField.setText(s);
	}
	@Override
	public void run() {
		throw new UnsupportedOperationException("Operation Unsupported!");
	}
	/**
	 * Update unPicked Items in the square where robot is in
	 */
	public void updateUnpickedItems(){
		unPickeditems.removeAllItems();
		Iterator<Piece> iter1 = viewBoard.getPieces(RobotPiece.getInstance().getRow(), RobotPiece.getInstance().getColumn()).iterator();
		while(iter1.hasNext()){
			Piece p = iter1.next();
			if(! (p instanceof RobotPiece)){
				unPickeditems.addItem(p);
			}
		}
	}
	/**
	 * Update Robot's list of items picked
	 */
	public void updatePickedItems(){
		pickeditems.removeAllItems();
		Iterator<Piece> iter2 = RobotPiece.getInstance().getPieces().iterator();
		while(iter2.hasNext()){
			pickeditems.addItem(iter2.next());
		}
	}
}
