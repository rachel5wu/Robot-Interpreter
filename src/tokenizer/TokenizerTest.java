package tokenizer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TokenizerTest {
	Tokenizer intTokenizer;
	Tokenizer floatTokenizer;
	Tokenizer nameTokenizer;
	Tokenizer groupTokenizer;
	Tokenizer operatorTokenizer;
	Tokenizer stringTokenizer;
	Tokenizer commentTokenizer;
	Tokenizer emptyTokenizer;
	Tokenizer errorTokenizer;
	Tokenizer complexTokenizer;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIntegers() {
		String integers="11 22 33";
		intTokenizer = new Tokenizer(integers);
		Token token1=new Token(TokenType.INT,"11");
		Token token2=new Token(TokenType.INT,"22");
		Token token3=new Token(TokenType.INT,"33");
		assertEquals(intTokenizer.next(),token1);
		assertEquals(intTokenizer.next(),token2);
		assertEquals(intTokenizer.next(),token3);
	}

	@Test 
	public void testFloats() {
		String floats="0.1 1. .1 1e1 1.1e111";
		floatTokenizer=new Tokenizer(floats);
		Token token1=new Token(TokenType.FLOAT,"0.1");
		Token token2=new Token(TokenType.FLOAT,"1.");
		Token token3=new Token(TokenType.FLOAT,".1");
		Token token4=new Token(TokenType.FLOAT,"1e1");
		Token token5=new Token(TokenType.FLOAT,"1.1e111");
		assertEquals(floatTokenizer.next(),token1);
		assertEquals(floatTokenizer.next(),token2);
		assertEquals(floatTokenizer.next(),token3);
		assertEquals(floatTokenizer.next(),token4);
		assertEquals(floatTokenizer.next(),token5);
	}

	@Test
	public void testNames() {
		String names="aaa      A_A     _aAAa       ";
		nameTokenizer=new Tokenizer(names);

		Token token1=new Token(TokenType.NAME,"aaa");
		Token token2=new Token(TokenType.NAME,"A_A");
		Token token3=new Token(TokenType.NAME,"_aAAa");

		assertEquals(nameTokenizer.next(),token1);
		assertEquals(nameTokenizer.next(),token2);
		assertEquals(nameTokenizer.next(),token3);

	}

	public void testGrouping() {
		String groups="(aaa)_aAAa[1.1e111]";
		groupTokenizer=new Tokenizer(groups);
		Token token1=new Token(TokenType.GROUPING_SYMBOL,"(");
		Token token2=new Token(TokenType.NAME,"aaa");
		Token token3=new Token(TokenType.GROUPING_SYMBOL,")");
		Token token4=new Token(TokenType.NAME,"_aAAa");
		Token token5=new Token(TokenType.GROUPING_SYMBOL,"[");
		Token token6=new Token(TokenType.FLOAT,"1.1e111");
		Token token7=new Token(TokenType.GROUPING_SYMBOL,"]");

		assertEquals(nameTokenizer.next(),token1);
		assertEquals(nameTokenizer.next(),token2);
		assertEquals(nameTokenizer.next(),token3);
		assertEquals(nameTokenizer.next(),token4);
		assertEquals(nameTokenizer.next(),token5);
		assertEquals(nameTokenizer.next(),token6);
		assertEquals(nameTokenizer.next(),token7);
	}

	@Test
	public void testOperators() {
		String operators="  aaa!a+  -=  aa _aA  *=  EE\\";
		operatorTokenizer=new Tokenizer(operators);

		Token token1=new Token(TokenType.NAME,"aaa");
		Token token2=new Token(TokenType.OPERATOR,"!");
		Token token3=new Token(TokenType.NAME,"a");
		Token token4=new Token(TokenType.OPERATOR,"+");
		Token token5=new Token(TokenType.OPERATOR,"-=");
		Token token6=new Token(TokenType.NAME,"aa");
		Token token7=new Token(TokenType.NAME,"_aA");
		Token token8=new Token(TokenType.OPERATOR,"*=");
		Token token9=new Token(TokenType.NAME,"EE");
		Token token10=new Token(TokenType.OPERATOR,"\\");

		assertEquals(operatorTokenizer.next(),token1);
		assertEquals(operatorTokenizer.next(),token2);
		assertEquals(operatorTokenizer.next(),token3);
		assertEquals(operatorTokenizer.next(),token4);
		assertEquals(operatorTokenizer.next(),token5);
		assertEquals(operatorTokenizer.next(),token6);
		assertEquals(operatorTokenizer.next(),token7);
		assertEquals(operatorTokenizer.next(),token8);
		assertEquals(operatorTokenizer.next(),token9);
		assertEquals(operatorTokenizer.next(),token10);

	}

	@Test
	public void testStrings() {
		String strings="\"aaa\"   \"_ss\"\'1/2*322=*4   9e33.ddd\'";
		stringTokenizer=new Tokenizer(strings);
		Token token1=new Token(TokenType.STRING,"\"aaa\"");
		Token token2=new Token(TokenType.STRING,"\"_ss\"");
		Token token3=new Token(TokenType.STRING,"\'1/2*322=*4   9e33.ddd\'");
		assertEquals(stringTokenizer.next(),token1);
		assertEquals(stringTokenizer.next(),token2);
		assertEquals(stringTokenizer.next(),token3);

	}

	@Test
	public void testComments() {
		String comments="# sdjd*dw90e)()*))(#&#&&#& kdkskd   sdasda\n";
		commentTokenizer=new Tokenizer(comments);
		Token token1=new Token(TokenType.COMMENT,"# sdjd*dw90e)()*))(#&#&&#& kdkskd   sdasda");
		assertEquals(commentTokenizer.next(),token1);
	}

	@Test
	public void testError() {
		String error="å›§";
		commentTokenizer=new Tokenizer(error);
		Token token1=new Token(TokenType.ERROR,"å");
		assertEquals(commentTokenizer.next(),token1);
	}

	
	@Test
	public void testOperatorsFloatsIntegersNamesGroupingsStringsComments() {
		String complex="[111+\"test+string-test=string\"     {(}  +  [*A_-aa]) #++_()*&*^ \n ";
		complexTokenizer=new Tokenizer(complex);
		Token token1=new Token(TokenType.GROUPING_SYMBOL,"[");
		Token token2=new Token(TokenType.INT,"111");
		Token token3=new Token(TokenType.OPERATOR,"+");
		Token token4=new Token(TokenType.STRING,"\"test+string-test=string\"");
		Token token5=new Token(TokenType.GROUPING_SYMBOL,"{");
		Token token6=new Token(TokenType.GROUPING_SYMBOL,"(");
		Token token7=new Token(TokenType.GROUPING_SYMBOL,"}");
		Token token8=new Token(TokenType.OPERATOR,"+");
		Token token9=new Token(TokenType.GROUPING_SYMBOL,"[");
		Token token10=new Token(TokenType.OPERATOR,"*");
		Token token11=new Token(TokenType.NAME,"A_");
		Token token12=new Token(TokenType.OPERATOR,"-");
		Token token13=new Token(TokenType.NAME,"aa");
		Token token14=new Token(TokenType.GROUPING_SYMBOL,"]");
		Token token15=new Token(TokenType.GROUPING_SYMBOL,")");
		Token token16=new Token(TokenType.COMMENT,"#++_()*&*^");

		assertEquals(complexTokenizer.next(),token1);
		assertEquals(complexTokenizer.next(),token2);
		assertEquals(complexTokenizer.next(),token3);
		assertEquals(complexTokenizer.next(),token4);
		assertEquals(complexTokenizer.next(),token5);
		assertEquals(complexTokenizer.next(),token6);
		assertEquals(complexTokenizer.next(),token7);
		assertEquals(complexTokenizer.next(),token8);
		assertEquals(complexTokenizer.next(),token9);
		assertEquals(complexTokenizer.next(),token10);
		assertEquals(complexTokenizer.next(),token11);
		assertEquals(complexTokenizer.next(),token12);
		assertEquals(complexTokenizer.next(),token13);
		assertEquals(complexTokenizer.next(),token14);
		assertEquals(complexTokenizer.next(),token15);
		assertEquals(complexTokenizer.next(),token16);

	}



	@Test
	public void testHasNext() {
		String string="aaa   AAA";
		nameTokenizer=new Tokenizer(string);
		assertTrue(nameTokenizer.hasNext());
		nameTokenizer.next();
		assertTrue(nameTokenizer.hasNext());
		nameTokenizer.next();
		assertFalse(nameTokenizer.hasNext());
	}

	@Test
	public void testBackUp() {
		String backupstring="aaa bbb 1111";
		Token token1=new Token(TokenType.NAME,"aaa");
		Token token2=new Token(TokenType.NAME,"bbb");
		Token token3=new Token(TokenType.INT,"1111");
		nameTokenizer=new Tokenizer(backupstring);
		assertEquals(nameTokenizer.next(),token1);
		nameTokenizer.backUp();
		assertEquals(nameTokenizer.next(),token1);
		nameTokenizer.backUp();
		nameTokenizer.backUp();
		assertTrue(nameTokenizer.hasNext());
		assertTrue(nameTokenizer.hasNext());
		assertEquals(nameTokenizer.next(),token1);
		assertEquals(nameTokenizer.next(),token2);
		assertTrue(nameTokenizer.hasNext());
		assertTrue(nameTokenizer.hasNext());
		assertEquals(nameTokenizer.next(),token3);
		nameTokenizer.backUp();
		nameTokenizer.backUp();
		assertEquals(nameTokenizer.next(),token3);
		assertFalse(nameTokenizer.hasNext());
	}
}
