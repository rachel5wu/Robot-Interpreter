package parser;

import java.util.Stack;

import tokenizer.Token;
import tokenizer.TokenType;
import tokenizer.Tokenizer;
import tree.Tree;

/**
 * This class consists of a number of methods that parse strings
 * composed of Tokens that follow the indicated grammar rules for each
 * method, and build trees corresponding to each nonterminal type.
 * <p>Each method may have one of three outcomes:
 * <ul>
 *   <li>The method may succeed, returning <code>true</code> ,
 *      consuming the tokens that make up that particular
 *      nonterminal.</li>
 *   <li>The method may fail, returning <code>false</code> and not
 *       consuming any tokens.</li>
 *   <li>(Some methods only) The method may determine that an
 *       unrecoverable error has occurred and throw a
 *       <code>RuntimeException</code></li>.
 * </ul>
 * @author Weizhuo Wu
 * @author David Matuszek
 * @version February 21, 2012
 */
public class Parser {
	private Tokenizer tokenizer = null;
	private Stack<Tree<Token>> stack = new Stack<Tree<Token>>();

	/**
	 * Constructs a Parser for the given string.
	 * @param text The string to be parsed.
	 */
	public Parser(String text) {
		tokenizer = new Tokenizer(text);
	}

	/**
	 * Returns the Tokenizer being used; needed for testing.
	 * @return The current Tokenizer.
	 */
	public Tokenizer getTokenizer() {
		return tokenizer;
	}

	/**
	 * Tries to parse a &lt;variable&gt; (which is just a &lt;name&gt;).
	 * <pre>&lt;variable&gt; ::= &lt;name&gt;</pre>
	 * @return <code>true</code> if a &lt;variable&gt; is parsed.
	 */
	public boolean variable() {
		// <variable> ::= <name>
		return name();
	}
	/**
	 * Tries to recognize a &lt;thing&gt;.
	 * <pre>&lt;thing&gt; ::= &lt;NAME&gt;</pre>
	 * @return <code>true</code> if a &lt;thing&gt; is recognized.
	 */
	public boolean thing(){
		return name();
	}
	/**
	 * Tries to recognize a &lt;command&gt;.
	 * <pre>&lt;command&gt; ::= &lt;thought&gt; | &lt;action&gt;</pre>
	 * @return <code>true</code> if a &lt;command&gt; is recognized.
	 */
	public boolean command(){
		return thought()||action();
	}
	/**
	 * Tries to recognize a &lt;program&gt;.
	 * <pre>&lt;program&gt; ::= "program" &lt;block&gt; {&lt;procedure&gt;}</pre>
	 * @return <code>true</code> if a &lt;program&gt; is recognized.
	 */
	public boolean program(){
		if(keyword("program")){
			if(!block()){
				error("Missing block for program");
			}else{
				makeTree();
			}
			while(procedure()){
				makeTree();
			}
			return true;
		}
		return false;
	}
	/**
	 * Tries to recognize a &lt;thought&gt;.
	 * <pre>&lt;thought&gt; ::= "set" &lt;variable&gt; &lt;expression&gt;";"
	 *             |"repeat"&lt;expression&gt;&lt;block&gt;
	 *             |"if" &lt;condition&gt; &lt;block&gt; [ "else" &lt;block&gt; ]
	 *             |"call" &lt;name&gt; { &lt;expression&gt; }  ";"</pre>
	 * @return <code>true</code> if a &lt;thought&gt; is recognized.
	 */
	public boolean thought(){
		if(keyword("set")){
			if(!variable()){
				error("Missing variable after set!");
			}else{
				makeTree();
				if(!expression()){
					error("Missing expression to set variable");
				}else{
					makeTree();
				}
			}
			if(!matchButDontKeep(";")){error("Missing \';\' after expression");}
			return true;
		}else if(keyword("repeat")){
			if(!expression()){
				error("Missing expression after repeat");
			}else{
				makeTree();
				if(!block()){
					error("Missing block after");
				}else{
					makeTree();
				}
			}
			return true;
		}else if(keyword("while")){
			if(!condition()){
				error("Mission while condition");
			}else{
				makeTree();
				if(!block()){
					error("No block for while");
				}else{
					makeTree();
				}
			}
			return true;
		}else if(keyword("if")){
			if(!condition()){
				error("Missing conditions for if statement");
			}else{
				makeTree();
				if(!block()){
					error("No block for if statement");
				}else{
					makeTree();
				}
			}
			if(matchButDontKeep("else")){
				if(!block()){
					error("Missing block after else statement");
				}else{
					makeTree();
				}
			}
			return true;
		}else if(keyword("call")){
			if(!name()){
				error("Missing name to call");
			}else{
				makeTree();
			}
			while(expression()){
				makeTree();
			}
			if(!matchButDontKeep(";")){error("Missing \';\' at the end of call statement");}
			return true;
		}
		return false;
	}

	/**
	 * Tries to recognize a &lt;action&gt;.
	 * <pre>&lt;action&gt; ::= &lt;move&gt; &lt;expression&gt; ";"
	 *            |"turn" &lt;direction&gt; ";"
	 *            |"take" &lt;thing&gt; ";"
	 *            |"drop" &lt;thing&gt; ";"
	 *            |"stop" ";"</pre>
	 * @return <code>true</code> if a &lt;action&gt; is recognized.
	 */
	public boolean action(){
		if(move()){
			if(!expression()){
				error("Missing expression after move statement");
			}else{
				makeTree();
				if(!matchButDontKeep(";")){error("Missing \';\' at the end of action statement");}
			}
			return true;
		}else if(keyword("turn")){
			if(!direction()){
				error("Missing direction after keyword turn");
			}else{
				makeTree();
				if(!matchButDontKeep(";")){error("Missing \';\' at the end of action statement");}
			}
			return true;
		}else if(keyword("take")){
			if(!thing()){
				error("Missing the thing to take");
			}else{
				makeTree();
				if(!matchButDontKeep(";")){error("Missing \';\' at the end of action statement");}
			}
			return true;
		}else if(keyword("drop")){
			if(!thing()){
				error("Missing the thing to drop");
			}else{
				makeTree();
				if(!matchButDontKeep(";")){error("Missing \';\' at the end of action statement");}
			}
			return true;
		}else if(keyword("stop")){
			if(!matchButDontKeep(";")){error("Missing \';\' at the end of action statement");}
			return true;
		}
		return false;
	}
	/**
	 * Tries to recognize a &lt;move&gt;.
	 * <pre>&lt;move&gt; ::= "forward" | "back" </pre>
	 * @return <code>true</code> if a &lt;move&gt; is recognized.
	 */
	public boolean move(){
		return keyword("forward")||keyword("back");
	}
	/**
	 * Tries to recognize a &lt;direction&gt;.
	 * <pre>&lt;direction&gt; ::= "right" | "left" | "around"</pre>
	 * @return <code>true</code> if a &lt;direction&gt; is recognized.
	 */
	public boolean direction(){
		return keyword("right")||keyword("left")||keyword("around");
	}

	/**
	 * Tries to recognize a &lt;block&gt;.
	 * <pre>&lt;block&gt; ::= "{" { &lt;command&gt; } "}"</pre>
	 * @return <code>true</code> if a &lt;block&gt; is recognized.
	 */
	public boolean block(){
		if(matchButDontKeep("{")){
			Token blockToken = new Token(TokenType.NAME, "block");
			Tree<Token> t = new Tree<Token>(blockToken);
			stack.push(t);
			while(command()){
				makeTree();
			}
			if(!matchButDontKeep("}")){
				error("Unclosed parenthetical expression");
			}
			return true;
		}
		return false;
	}

	/**
	 * Tries to recognize a &lt;condition&gt;.
	 * <pre>&lt;condition&gt; ::= &lt;expression&gt; &lt;comparator&gt; &lt;expression&gt;
	 *               |"seeing" &lt;thing&gt;
	 *               |"holding" &lt;thing&gt;
	 *               |"not" &lt;condition&gt;</pre>
	 * @return <code>true</code> if a &lt;block&gt; is recognized.
	 */
	public boolean condition(){
		if(expression()){
			if(!comparator()){
				error("Missing comparator in condition expression");
			}else{
				swapTopTwoElementsOfStack();
				makeTree();
				if(!expression()){
					error("Missing expression after comdition comparator");
				}else{
					makeTree();
				}
			}
			return true;
		}else if(keyword("seeing")||keyword("holding")){
			if(!thing()){
				error("Missing thing after seeing/holding");
			}else{
				makeTree();
			}
			return true;
		}else if(keyword("not")){
			if(!condition()){
				error("Missing condition after not");
			}else{
				makeTree();
			}
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Tries to recognize a &lt;comparator&gt;.
	 * <pre>&lt;comparator&gt; ::= "<" | "<=" | "==" | "!=" | ">=" | ">"</pre>
	 * @return <code>true</code> if a &lt;comparator&gt; is recognized.
	 */
	public boolean comparator(){
		return operator("<")||operator("<=")||operator("==")||operator("!=")||operator(">=")||operator(">");
	}

	/**
	 * Tries to recognize a &lt;procedure&gt;.
	 * <pre>&lt;procedure&gt; ::= "def" &lt;name&gt; { &lt;variable&gt; } &lt;block&gt;</pre>
	 * @return <code>true</code> if a &lt;procedure&gt; is recognized.
	 */
	public boolean procedure(){
		if(keyword("def")){
			Token headerToken = new Token(TokenType.NAME, "header");
			Tree<Token> headerTree = new Tree<Token>(headerToken);
			stack.push(headerTree);
			if(!name()){
				error("Missing name after \"def\"");
			}else{
				makeTree();
			}
			while(variable()){
                makeTree();
            }
			makeTree();//header becomes child of def
			if(!block()){
				error("Unblocked procedure");
			}else{
				makeTree();
			}
			return true;
		}
		return false;
	}


	/**
	 * Tries to parse an &lt;expression&gt;.
	 * <pre>&lt;expression&gt; ::= [ &lt;add_operator&gt; ] &lt;term&gt; { &lt;add_operator&gt; &lt;expression&gt; }</pre>
	 * A <code>RuntimeException</code> will be thrown if an &lt;add_operator&gt;
	 * is present after the first &lt;term&gt; but not followed by a valid &lt;expression&gt;.
	 * @return <code>true</code> if an &lt;expression&gt; is parsed.
	 */    
	public boolean expression() {
		if (addOperator()) {
			if (term()) {
				makeTree();
			}
			else error("Unary operator not followed by term.");
		} else {
			if (!term()) return false;
		}
		while (addOperator()) {
			swapTopTwoElementsOfStack();
			makeTree();
			if (term()) {
				makeTree();
			}
			else {
				error("Error in expression after '+' or '-'");
			}
		}
		return true;
	}

	/**
	 * Tries to parse a &lt;term&gt;.
	 * <pre>&lt;term&gt; ::= &lt;factor&gt; { &lt;multiply_operator&gt; &lt;factor&gt; }</pre>
	 * A <code>RuntimeException</code> will be thrown if the &lt;multiply_operator&gt;
	 * is present but not followed by a valid &lt;term&gt;.
	 * @return <code>true</code> if a term is parsed.
	 */
	public boolean term() {
		if (!factor()) return false;
		while (multiplyOperator()) {
			swapTopTwoElementsOfStack();
			makeTree();
			if (factor()) {
				makeTree();
			}
			else {
				error("No term after '*' or '/'");
			}
		}
		return true;
	}


	/**
	 * Tries to parse a &lt;factor&gt;.
	 * <pre>&lt;factor&gt; ::= &lt;name&gt;
	 *           | &lt;number&gt;
	 *           | "row"
	 *           | "column"
	 *           | "distance"
	 *           | "(" &lt;expression&gt; ")"</pre>
	 * A <code>RuntimeException</code> will be thrown if the opening
	 * parenthesis is present but not followed by a valid
	 * &lt;expression&gt; and a closing parenthesis.
	 * @return <code>true</code> if a factor is parsed.
	 */
	public boolean factor() {
		if (variable()) {
			return true;
		}
		if (isNumber()) {
			return true;
		}
		if (keyword("row") || keyword("column") || keyword("distance")) {
			return true;
		}
		if (matchButDontKeep("(")) {
			if (!expression()) error("Error in parenthesized expression");
			if (!matchButDontKeep(")")) error("Unclosed parenthetical expression");
			return true;
		}
		return false;
	}
	/**
	 * Tries to parse an &lt;add_operator&gt;.
	 * <pre>&lt;add_operator&gt; ::= "+" | "-"</pre>
	 * @return <code>true</code> if an &lt;add_operator&gt; is parsed.
	 */
	public boolean addOperator() {
		return operator("+") || operator("-");
	}

	/**
	 * Tries to parse a &lt;multiply_operator&gt;.
	 * <pre>&lt;multiply_operator&gt; ::= "*" | "/" | "%"</pre>
	 * @return <code>true</code> if a &lt;multiply_operator&gt; is parsed.
	 */
	public boolean multiplyOperator() {
		return operator("*") || operator("/") || operator("%");
	}
	//----- Private "helper" methods

	/**
	 * Tests whether the next token is a number (only integer )
	 * If it is, the token is consumed, otherwise
	 * it is not.
	 *
	 * @return <code>true</code> if the next token is a number.
	 */
	private boolean isNumber() {
		return nextTokenMatches(TokenType.INT);
	}

	/**
	 * Tests whether the next token is a name. If it is, the token
	 * is consumed, otherwise it is not.
	 *
	 * @return <code>true</code> if the next token is a name.
	 */
	private boolean name() {
		return nextTokenMatches(TokenType.NAME);
	}

	/**
	 * Tests whether the next token is the expected keyword. If it is, the token
	 * is consumed, otherwise it is not.
	 *
	 * @param expectedKeyword The String value of the expected next token.
	 * @return <code>true</code> if the next token is a keyword with the expected value.
	 */
	private boolean keyword(String expectedKeyword) {
		return nextTokenMatches(TokenType.KEYWORD, expectedKeyword);
	}

	/**
	 * Tests whether the next token is the expected symbol. If it is,
	 * the token is consumed, otherwise it is not.
	 *
	 * @param expectedOperator The String value of the token we expect
	 *    to encounter next.
	 * @return <code>true</code> if the next token is the expected symbol.
	 */
	boolean operator(String expectedOperator) {
		return nextTokenMatches(TokenType.OPERATOR, expectedOperator);
	}


	/**
	 * Tests whether the next token is the expected grouping symbol. If it is,
	 * the token is consumed, otherwise it is not.
	 *
	 * @param expectedSymbol The String value of the token we expect
	 *    to encounter next.
	 * @return <code>true</code> if the next token is the expected symbol.
	 */
	boolean isGroupingSymbol(String expectedSymbol) {
		return nextTokenMatches(TokenType.GROUPING_SYMBOL, expectedSymbol);
	}
	/**
	 * Tests whether the next token has the expected type. If it does,
	 * the token is consumed from the input and made the value of a Tree
	 * node, which is pushed onto the stack. This method would
	 * normally be used only when the token's value is not relevant.
	 *
	 * @param type The expected type of the next token.
	 * @return <code>true</code> if the next token has the expected type.
	 */
	public boolean nextTokenMatches(TokenType type) {
		if (!tokenizer.hasNext()) return false;
		Token t = tokenizer.next();
		if (t.type == type) {
			stack.push(new Tree<Token>(t));
			return true;
		}
		tokenizer.backUp();
		return false;
	}

	/**
	 * Tests whether the next token has the expected type and value.
	 * If it does, the token is consumed, otherwise it is not. This
	 * method would normally be used when the token's value is
	 * important.
	 *
	 * @param type The expected type of the next token.
	 * @param text The expected text of the next token; must
	 *              not be <code>null</code>.
	 * @return <code>true</code> if the next token has the expected type.
	 */
	public boolean nextTokenMatches(TokenType type, String text) {
		if (!tokenizer.hasNext()) return false;
		Token t = tokenizer.next();
		if (type == t.type && text.equals(t.text)) {
			stack.push(new Tree<Token>(t));
			return true;
		}
		tokenizer.backUp();
		return false;
	}
	/**
	 * Utility routine to throw a <code>RuntimeException</code> with the
	 * given message.
	 * @param reason Why the <code>RuntimeException</code> was thrown.
	 */
	private void error(String reason) {
		final int TOKENS_TO_DISPLAY = 6;
		boolean needEllipsis = true;
		String message = reason + ": \"";

		for (int i = 0; i < TOKENS_TO_DISPLAY; i++) {
			if (nextTokenMatches(TokenType.OPERATOR, ";")) {
				message += "\"";
				needEllipsis = false;
				break;
			}
			if(tokenizer.hasNext()){
				message += " " + tokenizer.next().text;
			}
		}
		if (needEllipsis) message += " ...\"";
		throw new RuntimeException(message);
	}

	private void swapTopTwoElementsOfStack() {
		Tree<Token> tree1 = stack.pop();
		Tree<Token> tree2 = stack.pop();
		stack.push(tree1);
		stack.push(tree2);
	}

	/**
	 * Removes two Trees from the stack, makes a new Tree and
	 * puts it on the stack. The element on the top of the stack
	 * (the most recently seen element) is made the child of the
	 * element beneath it (which was seen earlier).
	 */
	private void makeTree() {
		Tree<Token> child = stack.pop();
		Tree<Token> parent = stack.pop();
		parent.addChild(child);
		stack.push(parent);
	}

	/**
	 * Returns the parse tree at the top of the stack. It is expected
	 * (but not enforced) that there be one and only one parse tree
	 * on the stack when this is called.
	 * @return The top element of the stack of parse trees.
	 */
	public Tree<Token> getParseTree() {
		return stack.peek();
	}

	/**
	 * Determines whether the next Token exists and has the given text.
	 * If so, the Token is made into a unit Tree and placed on the
	 * global stack. If not, the Token is consumed
	 * @param text The expected text of the next Token.
	 * @return <code>true</code> if the expected Token is matched.
	 */
	private boolean matchButDontKeep(String text) {
		if (!tokenizer.hasNext()) return false;
		Token token = tokenizer.next();
		if (token.text.equals(text)) return true;
		tokenizer.backUp();
		return false;
	}

	/**
	 * Determines whether the next Token exists and has the given text.
	 * If so, the Token is discarded. If not, a <code>RuntimeException</code>
	 * is thrown.
	 * @param text The text of the required Token.
	 */
//	private void requireButDontKeep(String text) {
//		if (!tokenizer.hasNext() || !tokenizer.next().text.equals(text)) {
//			error("Required \"" + text + "\" is missing.");
//		}
//	}
}