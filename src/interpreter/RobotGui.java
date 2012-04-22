package interpreter;

import interpreter.Interpreter;
import boardGame.Board;
import boardGame.Piece;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import examples.Block;

import parser.Parser;

import tokenizer.Token;
import tree.Tree;
import weizhuo.Coin;
import weizhuo.Diamond;
import weizhuo.Direction;
import weizhuo.OilCan;
import weizhuo.RobotPiece;
import weizhuo.Tazer;

/**
 * @author David Matuszek
 * @version Mar 28, 2012
 */
public class RobotGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int ROWS = 12;
    private static final int COLUMNS = 15;

    private Interpreter interpreter;
    private Board board;
    public Piece robot;
	public Piece[] coins;
	public Piece[] diamonds;
	public Piece[] oilCans;
	public Piece[] tazers;
	public Piece[] blocks;
	
    private JPanel controlPanel = new JPanel();
    private JTextArea programArea = new JTextArea(20, 20);
    private JScrollPane programPane = new JScrollPane(programArea);
    private JButton loadButton = new JButton("Load");
    private JButton saveButton = new JButton("Save");
    private JButton initializeButton = new JButton("Initialize");
    private JButton runButton = new JButton("Run");
    private JButton pauseButton = new JButton("Pause");
    private JButton stopButton = new JButton("Stop"); 
    
    /**
     * Creates the GUI for a robot, and turns control over to the GUI.
     * @param args Unused.
     */
    public static void main(String[] args) {
        new RobotGui().runRobotRun();
        
    }
    
    /**
     * Creates the GUI and runs the program.
     */
    private void runRobotRun() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createGui();
        attachListeners();
        enableButtons("Load Save Initialize");
    }
    
    /**
     * Creates the GUI for the Robot language, but does not
     * attach listeners to the buttons.
     */
    private void createGui() {
    	
        board = new Board(ROWS, COLUMNS);
        Container pane = getContentPane();
        JPanel display = board.getJPanel();
        pane.add(display, BorderLayout.CENTER);
        pane.add(programPane, BorderLayout.EAST);
        pane.add(controlPanel, BorderLayout.SOUTH);
        controlPanel.add(loadButton);
        controlPanel.add(saveButton);
        controlPanel.add(new JLabel("          "));
        controlPanel.add(initializeButton);
        controlPanel.add(runButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        pack();
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Attaches listeners to each of the buttons.
     */
    private void attachListeners() {
        ActionListener buttonListener = new ButtonListener();
        loadButton.addActionListener(buttonListener);
        saveButton.addActionListener(buttonListener);
        initializeButton.addActionListener(buttonListener);
        runButton.addActionListener(buttonListener);
        pauseButton.addActionListener(buttonListener);
        stopButton.addActionListener(buttonListener);
        
        Document doc = programArea.getDocument();
        doc.addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                setChanged();
            }
            
            @Override
            public void removeUpdate(DocumentEvent arg0) {
                setChanged();
            }
            
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                setChanged();
            }
            
            private void setChanged() {
                if (programArea.getText().trim().length() > 0) {
                    enableButtons("Load Save Init Run");
                }
                else {
                    enableButtons("Load Init");
                }
            }            
        });
    }
    
    /**
     * Clears everything, to make ready for a new Interpreter. 
     */
    private void initialize() {
        if (interpreter != null) {
            interpreter.stopProgram();
        }
        board.clear();
        interpreter = null;
        if (programArea.getText().trim().length() > 0) {
            enableButtons("Load Save Init Run");
        }
        else {
            enableButtons("Load Init");
        }    
        createNewPiece();
    }
    
    private void load() {
        JFileChooser chooser = new JFileChooser();
        BufferedReader reader;
        String program = "";
        
        chooser.setDialogTitle("Load which file?");
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                if (file == null) return;
                String fileName = file.getCanonicalPath();
                reader = new BufferedReader(new FileReader(fileName));
                String line = reader.readLine();
                while (line != null) {
                    program += line + "\n";
                    line = reader.readLine();
                }
                reader.close();
            }
            catch (IOException e) { /* empty */ }
        }
        programArea.setText(program);
    }
    
    private void save() {
        String fileName;
        PrintWriter writer;
        JFileChooser chooser = new JFileChooser();
        
        chooser.setDialogTitle("Save file as?");
        // Get the file chosen in a JChooser save file dialog
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            //...and prepare to use the file
            try {
                if (file == null) return;
                fileName = file.getCanonicalPath();
                writer =
                    new PrintWriter(new FileOutputStream(fileName), true);
                writer.write(programArea.getText());
                writer.close();
            }
            catch (IOException e) { /* empty */ }       
        }
    }
    
    private void executeProgram() {
        if (interpreter == null) {
            startProgram();
        }
        else if (interpreter.isPaused()) {
            interpreter.setPaused(false);
            enableButtons("Pause Stop");
        }
        else {
            startProgram();
        }
    }
    
    private void startProgram() {
        String programText = programArea.getText();
        Parser parser = new Parser(programText);
        try {
            if (parser.program()) {
                Tree<Token> program = parser.getParseTree();
                interpreter = new Interpreter(program, board);
                interpreter.start();
                enableButtons("Pause Stop");
            }
            else {
                JOptionPane.showMessageDialog(this, "Parsing error.");
            }
        }
        catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void pause() {
        interpreter.setPaused(true);
        enableButtons("Run Stop");
    }
    
    private void stop() {
        interpreter.stopProgram();
        enableButtons("Load Save Init Run");
    }
    
    private void enableButtons(String buttons) {
        loadButton.setEnabled(buttons.indexOf("Load") >= 0);
        saveButton.setEnabled(buttons.indexOf("Save") >= 0
                && programArea.getText().trim().length() > 0);
        initializeButton.setEnabled(buttons.indexOf("Init") >= 0);
        runButton.setEnabled(buttons.indexOf("Run") >= 0
                && programArea.getText().trim().length() > 0);
        pauseButton.setEnabled(buttons.indexOf("Pause") >= 0);
        stopButton.setEnabled(buttons.indexOf("Stop") >= 0);
    }
    
    /**
     * @author Dave Matuszek
     * @version March 28, 2012
     */
    public class ButtonListener implements ActionListener { 
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            JButton source = (JButton)event.getSource();
            if (source == loadButton) load();
            else if (source == saveButton) save();
            else if (source == initializeButton) initialize();
            else if (source == runButton) executeProgram();
            else if (source == pauseButton) pause();
            else if (source == stopButton) stop();   
        }
    }
    
	/**
	 * Creates pieces and puts it on the dock.
	 * @author weizhuowu
	 */
	protected void createNewPiece() {

		robot = RobotPiece.getInstance();
		coins = new Coin[4];
		diamonds = new Diamond[4];
		oilCans = new OilCan[4];
		tazers = new Tazer[4];
		blocks = new Block[4];

		for(int i=0; i<4; i++){
			coins[i] = new Coin("Coin");
			placePieceOnBoard(coins[i]);
//			board.place(coins[i], i, 0);
			diamonds[i] = new Diamond("Diamond");
			placePieceOnBoard(diamonds[i]);
//			board.place(diamonds[i], i, 1);
			oilCans[i] = new OilCan("OilCan");
			placePieceOnBoard(oilCans[i]);
//			board.place(oilCans[i], i, 2);
			tazers[i] = new Tazer("Tazer");
			placePieceOnBoard(tazers[i]);
//			board.place(tazers[i], i, 3);
			blocks[i] = new Block();
			placePieceOnBoard(blocks[i]);
//			board.place(blocks[i], i, 4);
		}

		board.place(RobotPiece.getInstance(), ROWS/2, COLUMNS/2);
		RobotPiece.getInstance().setDirection(Direction.NORTH);
	}

	/**
	 * Moves a Piece to the board randomly.
	 * @author weizhuowu
	 */
	private void placePieceOnBoard(Piece piece) {

		Random random = new Random();
		int row = random.nextInt(ROWS);
		int column = random.nextInt(COLUMNS);
		if(board.getPiece(row, column) != null){
			Piece oldPiece = board.getPiece(row, column);
			while((oldPiece instanceof Block)){
				row = random.nextInt(ROWS);
				column = random.nextInt(COLUMNS);
				oldPiece = board.getPiece(row, column);
			}
		}
		board.place(piece, row, column);
	}
	
	
}
