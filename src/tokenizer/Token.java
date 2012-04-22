/**
 * 
 */
package tokenizer;

/**
 *
 * A Token has a type and a text.
 * The type is one of the TokenType values defined in TokenType.java. 
 * The text is a String containing the exact characters that make up the Token. 
 * @author weizhuowu
 * @version Feb. 27 2012
 */
public class Token {

    public final TokenType type;
    public final String text;
    /**
     * 
     * @param type -one of the TokenType values defined in TokenType.java.
     * @param text -a String containing the exact characters that make up the Token.
     */
    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj - the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj==null) return false;
        if(obj instanceof Token){
            Token that = (Token)obj;
            if((that.type==null)||(that.text==null)){ throw new ClassCastException();}
            return (that.type==this.type) && (that.text.equals(this.text));
        }
        return false;
    }
    /**
     * Returns a hash code value for the object. 
     * If two objects are equal according to the equals(Object) method, 
     * then calling the hashCode method on each of the two objects must produce the same integer result.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode(){
        return this.text.hashCode()+this.type.hashCode();
    }
    /**
     * Returns a string representation of the object
     * @return a string of the form type:text (no spaces).
     */
    @Override
    public String toString(){
        return type+":"+text;
    }

}
