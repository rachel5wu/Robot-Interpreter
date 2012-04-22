/**
 * 
 */
package tokenizer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.*;

/** 
 * Decomposes a String into a series of tokens
 * @author weizhuowu
 * @version February 25, 2012
 */
public class Tokenizer implements Iterator<Token> {

    private String input;
    private int position;
    private int lastPosition;

    private enum States{
        READY, ERROR	
    }
    /**
     * 
     * @param input -- the String to be tokenized
     */
    public Tokenizer(String input){
        this.input = input.trim() + " ";
        this.position = 0;
        this.lastPosition = this.position;
    }
    /**
     * Indicates whether there are more token in the tokenizer string.
     * @return <code>true</code> --if there are token in the tokenizer string. <code>false</code> --if isn't.
     */
    @Override
    public boolean hasNext() {
        return (this.position < this.input.length()-1);
    }
    /**
     *  Returns the next token from this string tokenizer
     *  @return the next token from this string tokenizer.
     *  @throws <code>NoSuchElementException</code> -- if there are no more tokens in this tokenizer's string.
     */
    @Override
    public Token next(){
        this.lastPosition = this.position;
        States state;
        String value = "";

        if(!this.hasNext()){
            throw new NoSuchElementException();
        }

        state = States.READY;
        //discard the space in front of a token
        while(input.substring(position).startsWith(" ")||input.substring(position).startsWith("\n")){
            position+=1;
        }

        switch (state){
        case READY:{

            //FLOAT 
            Pattern floatPattern2 = Pattern.compile("([0-9]{0,}[.][0-9]{1,}[eE][+-]{0,1}[0-9]{1,3})|([0-9]{1,}[.][0-9]{0,}[eE][+-]{0,1}[0-9]{1,3})");
            Matcher floatMatcher2 = floatPattern2.matcher(input.substring(position));
            if(floatMatcher2.lookingAt()){
                value = input.substring(floatMatcher2.start()+position, floatMatcher2.end()+position);
                position += floatMatcher2.end();
                return new Token(TokenType.FLOAT, value);
            }
            Pattern floatPattern1 = Pattern.compile("([0-9]{0,}[.][0-9]{1,})|([0-9]{1,}[.][0-9]{0,})|([0-9]{1,}[eE][+-]{0,1}[0-9]{1,3})");
            Matcher floatMatcher1 = floatPattern1.matcher(input.substring(position));
            if(floatMatcher1.lookingAt()){
                value = input.substring(floatMatcher1.start()+position, floatMatcher1.end()+position);
                position += floatMatcher1.end();
                return new Token(TokenType.FLOAT, value);
            }

            //INT --an unsigned integer, consisting of one or more digits
            Pattern intPattern = Pattern.compile("[0-9]{1,}");
            Matcher intMatcher = intPattern.matcher(input.substring(position));
            if(intMatcher.lookingAt()){
                value = input.substring(intMatcher.start()+position, intMatcher.end()+position);
                position += intMatcher.end();
                return new Token(TokenType.INT, value);
            }

            //NAME--begins with a letter or an underscore, followed by zero or more letters, digits, and underscores.
            Pattern namePattern = Pattern.compile("([a-zA-Z_])([a-zA-Z0-9_]){0,}") ;
            Matcher nameMatcher = namePattern.matcher(input.substring(position));
            if(nameMatcher.lookingAt()){
                value = input.substring(nameMatcher.start()+position, nameMatcher.end()+position);
                position += nameMatcher.end();

                //KEYWORD--Any token matches the pattern for NAME, but is also one of (program, set, repeat, etc.)
                Pattern keywordPattern1 = Pattern.compile("(program)|(set)|(repeat)|(while)|(if)|(else)|(call)");
                Matcher keywordMatcher1 = keywordPattern1.matcher(value);
                Pattern keywordPattern2 = Pattern.compile("(turn)|(take)|(drop)|(stop)|(forward)|(back)|(right)|(left)|(around)");
                Matcher keywordMatcher2 = keywordPattern2.matcher(value);
                Pattern keywordPattern3 = Pattern.compile("(seeing)|(holding)|(not)|(def)|(row)|(column)|(distance)");
                Matcher keywordMatcher3 = keywordPattern3.matcher(value);
                if(keywordMatcher1.matches()||keywordMatcher2.matches()||keywordMatcher3.matches()){
                    return new Token(TokenType.KEYWORD, value);
                }
                return new Token(TokenType.NAME, value);
            }

            //GROUPING_SYMBOL--any one of the following six symbols: ( ) [ ] { } 
            Pattern groupingPattern = Pattern.compile("[)(}{\\]\\[]{1}");
            Matcher groupingMatcher = groupingPattern.matcher(input.substring(position));
            if(groupingMatcher.lookingAt()){
                value = input.substring(groupingMatcher.start()+position, groupingMatcher.end()+position);
                position += groupingMatcher.end();
                return new Token(TokenType.GROUPING_SYMBOL, value);
            }

            //OPERATOR--Single characters, and Two-character sequences
            Pattern operatorPattern = Pattern.compile("([-+*/%=!<>]{1}[=]{1})|([;~`!@$%^&*+-=|\\\\<,>./?]{1})");
            Matcher operatorMatcher = operatorPattern.matcher(input.substring(position));
            if(operatorMatcher.lookingAt()){
                value = input.substring(operatorMatcher.start()+position, operatorMatcher.end()+position);
                position += operatorMatcher.end();
                return new Token(TokenType.OPERATOR, value);
            }

            //STRING -- zero or more characters, enclosed in either single quotes (') or double quotes (").
            Pattern stringPattern = Pattern.compile("([\"][^\"]{0,}[\"])|([\'][^\']{0,}[\'])");
            Matcher stringMatcher = stringPattern.matcher(input.substring(position));
            if(stringMatcher.lookingAt()){
                value = input.substring(stringMatcher.start()+position, stringMatcher.end()+position);
                position += stringMatcher.end();
                return new Token(TokenType.STRING, value);
            }

            //COMMENT -- A Python-style string, beginning with # and extending to the end of the line.
            Pattern commentPattern = Pattern.compile("[#][^\n]{0,}");
            Matcher commentMatcher = commentPattern.matcher(input.substring(position));
            if(commentMatcher.lookingAt()){
                value = input.substring(commentMatcher.start()+position, commentMatcher.end()+position).trim();
                position += commentMatcher.end();
                return new Token(TokenType.COMMENT, value);
            }
        }
        default:{

            value = input.substring(position, position+1);
            position +=1;
            return new Token(TokenType.ERROR, value);
        }
        }
    }
    /**
     * UnsupportedOperation
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    /**
     * Back up one token, so that whatever was returned from the most recent call to next()
     * will be returned again the next time next() is called;
     * Only one token is remembered,
     * if call backUp() multiple times, the second and subsequent calls don't make any difference.
     */
    public void backUp(){
        this.position = this.lastPosition;
    }

}
