/**
 * 
 */
package interpreter;
import boardGame.Board;
import tree.Tree;
import tokenizer.Token;
import tokenizer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * @author Weizhuo Wu
 * @version Apr 10, 2012
 */
public class Interpreter extends Thread{
	
	RobotController controller;
	Board board;
	Tree<Token> program;
	HashMap<String, Tree<Token>> procedureMap;
	boolean isStopped, pause;
	Stack<HashMap<String, Integer>> varStack;
	/**
	 * constructor, to create a RobotController,
	 * and tell it what program to interpret and where to display its actions.
	 * @param program
	 * @param board
	 */
	Interpreter(Tree<Token> program, Board board){
		controller = new RobotController(board);
		this.board = board;
		this.program = program;
		isStopped = false;
		procedureMap = new HashMap<String, Tree<Token>>();
		varStack = new Stack<HashMap<String, Integer>>();
		varStack.push(new HashMap<String, Integer>());
	}
	/**
	 * Constructor for Test use
	 * @param program
	 */
	Interpreter(Tree<Token> program){
		this.program = program;
		isStopped = false;
		procedureMap = new HashMap<String, Tree<Token>>();
		varStack = new Stack<HashMap<String, Integer>>();
		varStack.push(new HashMap<String, Integer>());
	}
	/**
	 * To interpret the program. 
	 * This method isn't called directly; the GUI calls the Thread's start() method.
	 */
	public void run(){
		try {
			interprete(this.program);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * To terminate interpretation of the current robot program.
	 */
	public void stopProgram(){
		isStopped = true;
	}
	/**
	 * Pause/Resume interpreting program
	 * 
	 * @param pause <code>true</code> should pause the interpreter 
	 *             <code>false</code> should resume interpretation.
	 */
	public void setPaused(boolean pause){
		this.pause = pause;
	}
	/**
	 * Check whether the interpreter is paused.
	 * @return <code>true</code> if interpreting program is paused, 
	 *         <code>false</code>if interpreting program is not paused
	 */
	public boolean isPaused(){
		return this.pause;
	}
	/**
	 * Given the root of a Tree representing an arithmetic expression,
	 * evaluate the expression and return the numerical result.
	 * @param expression --a Tree representing an arithmetic expression
	 * @return numerical result of an arithmetic expression
	 * @throws Exception 
	 */
	public int evaluateExpression(Tree<Token> expression) throws Exception{
		if(expression.isLeaf()){
			if(expression.getValue().type == TokenType.INT){
				return Integer.parseInt(expression.getValue().text);
			}else if("row".equals(expression.getValue().text)){
				return controller.getRobotRow();	
			}else if("column".equals(expression.getValue().text)){
				return controller.getRobotCol();
			}else if("distance".equals(expression.getValue().text)){
				return controller.getDistance();
			}else{
				return findValue(expression.getValue().text);
			}
		}else{
			if("+".equals(expression.getValue().text)){
				if(expression.children().size() == 1){
					return evaluateExpression(expression.firstChild());
				}else{
					int number1 = evaluateExpression(expression.firstChild());
					int number2 = evaluateExpression(expression.firstChild().nextSibling());
					return number1+number2;
				}
			}else if("-".equals(expression.getValue().text)){
				if(expression.children().size() == 1){
					return 0-evaluateExpression(expression.firstChild());
				}else{
					int number1 = evaluateExpression(expression.firstChild());
					int number2 = evaluateExpression(expression.firstChild().nextSibling());
					return number1-number2;
				}
			}else if("*".equals(expression.getValue().text)){

				int number1 = evaluateExpression(expression.firstChild());
				int number2 = evaluateExpression(expression.firstChild().nextSibling());
				return number1*number2;

			}else if("/".equals(expression.getValue().text)){
				try{
					int number1 = evaluateExpression(expression.firstChild());
					int number2 = evaluateExpression(expression.firstChild().nextSibling());
					return number1/number2;
				}catch(Exception e){

				}
			}else if("%".equals(expression.getValue().text)){
				try{
					int number1 = evaluateExpression(expression.firstChild());
					int number2 = evaluateExpression(expression.firstChild().nextSibling());
					return number1%number2;
				}catch(Exception e){

				}
			}else{
				throw new Exception("Not Expression");
			}
		}
		return 0;
	}
	/**
	 * Given the root of a Tree representing a condition,
	 * evaluate the condition and return the boolean result.
	 * @param root --a Tree representing a condition
	 * @return boolean result of evaluating given condition
	 * @throws Exception 
	 */
	public boolean evaluateCondition(Tree<Token> root) throws Exception{
		if("not".equals(root.getValue().text)){
			return !evaluateCondition(root.firstChild());
		}else if("==".equals(root.getValue().text)){
			int number1 = evaluateExpression(root.firstChild());
			int number2 = evaluateExpression(root.firstChild().nextSibling());
			return number1 == number2;
		}else if("!=".equals(root.getValue().text)){
			int number1 = evaluateExpression(root.firstChild());
			int number2 = evaluateExpression(root.firstChild().nextSibling());
			return number1 != number2;
		}else if("<".equals(root.getValue().text)){
			int number1 = evaluateExpression(root.firstChild());
			int number2 = evaluateExpression(root.firstChild().nextSibling());
			return number1 < number2;
		}else if("<=".equals(root.getValue().text)){
			int number1 = evaluateExpression(root.firstChild());
			int number2 = evaluateExpression(root.firstChild().nextSibling());
			return number1 <= number2;
		}else if(">".equals(root.getValue().text)){
			int number1 = evaluateExpression(root.firstChild());
			int number2 = evaluateExpression(root.firstChild().nextSibling());
			return number1 > number2;
		}else if(">=".equals(root.getValue().text)){
			int number1 = evaluateExpression(root.firstChild());
			int number2 = evaluateExpression(root.firstChild().nextSibling());
			return number1 >= number2;
		}else if("seeing".equals((root.getValue().text))){
			String thing = root.firstChild().getValue().text;
			return controller.findPieceOnTheWay(thing);
		}else if("holding".equals(root.getValue().text)){
			String thing = root.firstChild().getValue().text;
			return controller.isHolding(thing);
		}
		return false;
	}
	/**
	 * Given the root of a Tree representing a program or a command, 
	 * interpret the program or command 
	 * @param root --a Tree representing a program or a command
	 * @throws Exception 
	 */
	public void interprete(Tree<Token> root) throws Exception{
		if(!isStopped){
			while (pause) {
				try { sleep(100); }
				catch (InterruptedException e) { }
			} 
			if("program".equals(root.getValue().text)){
				ArrayList<Tree<Token>> children = root.children();
				for(int i=children.size()-1;i>=0;i--){
					interprete(children.get(i));
				}
			}else if("def".equals(root.getValue().text)){
				ArrayList<Tree<Token>> children = root.children();
				if("header".equals(children.get(0).getValue().text)){
					String name = children.get(0).children().get(0).getValue().text;
					procedureMap.put(name, root);
				}
			}else if("block".equals(root.getValue().text)){
				//interpret each statement in the block, one after the other from the first.
				ArrayList<Tree<Token>> children = root.children();
				for(int i=0; i<children.size();i++){
					interprete(children.get(i));
				}
			}else if("set".equals(root.getValue().text)){//command -> thought
				//"set" <variable> <expression>
				ArrayList<Tree<Token>> children = root.children();
				int var = evaluateExpression(children.get(1));
				varStack.peek().put(children.get(0).getValue().text, var);
			}else if("repeat".equals(root.getValue().text)){
				//"repeat" <expression> <block>
				ArrayList<Tree<Token>> children = root.children();
				int var = evaluateExpression(children.get(0));
				while(var>0){
					interprete(children.get(1));
					var--;
				}
			}else if("while".equals(root.getValue().text)){
				//"while" <condition> <block>
				ArrayList<Tree<Token>> children = root.children();	
				while(evaluateCondition(children.get(0))){
					interprete(children.get(1));
				}
			}else if("if".equals(root.getValue().text)){
				//"if" <condition> <block> [ "else" <block> ]
				ArrayList<Tree<Token>> children = root.children();
				if(evaluateCondition(children.get(0))){
					interprete(children.get(1));
				}else if(children.size()>2){
					interprete(children.get(2));
				}

			}else if("call".equals(root.getValue().text)){
				String name = root.children().get(0).getValue().text;//procedure name
				/*Look up the procedure, by name, in hash table of procedure names.*/
				Tree<Token> procedureCalled = procedureMap.get(name);
				if(procedureCalled != null){
					/*Create a new HashMap and put it on the Stack of HashMaps.*/
					varStack.push(new HashMap<String, Integer>());
					HashMap<String, Integer> tempMap = varStack.peek();
					/*For each <variable> in the def, evaluate the corresponding <expression>s in the call*/
					String varName;
					Tree<Token> varInt;
					ArrayList<Tree<Token>> headerChildren = procedureCalled.firstChild().children();
					for(int i=1; i<headerChildren.size(); i++){//put all parameter into tempMap
						varName = headerChildren.get(i).getValue().text;
						varInt = root.children().get(i);
						if(varInt != null){
							tempMap.put(varName, evaluateExpression(varInt));
						}else{
							tempMap.put(varName, 0);
						}
					}
					/*Evaluate the procedure body, using the Stack of HashMaps.*/
					interprete(procedureCalled.children().get(1));
					/*When the procedure finishes, pop the new HashMap from the Stack.*/
					varStack.pop(); 
				}
			}else if("forward".equals(root.getValue().text)){//command->action
				//<action> ::= <move> <expression> 
				int var = evaluateExpression(root.firstChild());
				controller.moveForward(var);
				sleep(100);
			}else if("back".equals(root.getValue().text)){
				int var = evaluateExpression(root.firstChild());
				controller.moveBackward(var);
				sleep(100);
			}else if("turn".equals(root.getValue().text)){
				String direction = root.firstChild().getValue().text;
				if("right".equals(direction)){
					controller.turnRight();
				}else if("left".equals(direction)){
					controller.turnLeft();
				}else if("around".equals(direction)){
					controller.trunAround();
				}
				sleep(100);
			}else if("take".equals(root.getValue().text)){
				String thing = root.firstChild().getValue().text;
				controller.pickUp(thing);
				sleep(100);
			}else if("drop".equals(root.getValue().text)){
				String thing = root.firstChild().getValue().text;
				controller.dropPiece(thing);
				sleep(100);
			}else if("stop".equals(root.getValue().text)){
				//Stop interpreting; the program is finished.
				stopProgram();
			}
		}
	}

	private int findValue(String key){
		HashMap<String, Integer> tempMap;
		if(varStack.size()>0){
			for(int i=varStack.size()-1; i>=0; i--){
				tempMap = varStack.get(i);
				if(tempMap.get(key) != null){
					return tempMap.get(key);
				}
			}
		}
		return 0;
	}


}
