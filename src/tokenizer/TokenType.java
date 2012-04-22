/**
 * 
 */
package tokenizer;

/**
 * 
 *A token is one "thing" in the input string.
 *A Token has a type and a text. The type is one of the TokenType values.
 *INT, FLOAT, KEYWORD, NAME, OPERATOR, GROUPING_SYMBOL, STRING, COMMENT, ERROR
 *@author Weizhuo Wu
 */

public enum TokenType {
	/**
	 * <code>INT</code> -- An unsigned integer, consisting of one or more digits.
	 */
    INT, 
    /**
     * <code>FLOAT</code> -- An unsigned floating-point number, containing either a decimal point, an exponent, or both. 
     * An exponent, if present, consists of the letter e or E, an optional sign, and up to three digits.
     */
    FLOAT,
    /**
     *<code>KEYWORD</code> -- Any token that matches the pattern for NAME, but also one of the words (program, set, repeat, etc.)
     */
    KEYWORD, 
    /**
     *<code>NAME</code> -- Begins with a letter or an underscore, followed by zero or more letters, digits, and underscores.
     */
    NAME, 
    /**
     *<code>OPERATOR<code> --Any of the following:
     */
    OPERATOR, 
    /**
     *<code>GROUPING_SYMBOL</code> -- Any one of the following six symbols: ( ) [ ] { } .
     *Single characters: ~ ` ! @ $ % ^ & * - + = | \ < , > . / ? (but not a grouping symbol, or #)
     *Two-character sequences: += -= *= /= %= == != <= >= (but not ++ or --).
     */
    GROUPING_SYMBOL, 
    /**
     *<code>STRING</code> -- Zero or more characters, enclosed in either single quotes (') or double quotes ("). There are no escaped characters, therefore:
     *a single quote cannot occur within a string demarcated by single quotes, and
     *a double quote cannot occur within a string demarcated by double quotes. 
     */
    STRING, 
    /**
     *<code>COMMENT</code> -- A Python-style string, beginning with # and extending to the end of the line.
     */
    COMMENT, 
    /**
     *<code>ERROR</code> -- Anything that isn't one of the above, such as a non-ASCII character.
     */
    ERROR;
}
