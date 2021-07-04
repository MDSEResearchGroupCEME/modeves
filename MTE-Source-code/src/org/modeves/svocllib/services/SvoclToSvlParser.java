
package org.modeves.svocllib.services;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;

import org.modeves.svocllib.main.Launcher;


/**
 * 
 * @author Yasir
 *
 */
public class SvoclToSvlParser
{
	private String functions[];
	private String structs[];
	private String structEnds[];
	private String keyWords[];
	
	private char spChars[];
	private char operators[];

	private String str;

	private Vector<Token> gtokens;
	private String lastToken;
	private String convertedSt;
	
	private static SvoclToSvlParser instance;

	public static SvoclToSvlParser getInstance()
	{
		if (instance == null) {
			instance = new SvoclToSvlParser();
		}
		return instance;
	}
	
	private SvoclToSvlParser()
	{}

	public String execute(String str, String fileId)
	{
		init();
		this.str = str;
		
		try {
			tokenize();
			extractFuncs();
			
			convertedSt = convert(gtokens);
		}
		catch (EmptyStackException e) {
			boolean behavior = fileId.equals("Beh")? true:false;
			Launcher.getInstance().setStatus(Launcher.CREATED_WITH_ERRORS, behavior);
			return "ERROR: Number of '(' and ')' NOT equal in the constraint string.";
		}
		catch (ArrayIndexOutOfBoundsException e) {
			boolean behavior = fileId.equals("Beh")? true:false;
			Launcher.getInstance().setStatus(Launcher.CREATED_WITH_ERRORS, behavior);
			return "ERROR: Constraint String contains an error. Check no. of opening and closing delimiters.";
		}
		catch (Exception e) {
			e.printStackTrace();
			boolean behavior = fileId.equals("Beh")? true:false;
			Launcher.getInstance().setStatus(Launcher.CREATED_WITH_ERRORS, behavior);
		}
		
		if(convertedSt != null) {
			convertedSt = convertedSt.replaceAll("\n", System.lineSeparator());
			return convertedSt;
		}
		else {
			return "ERROR: Constraint String contains errors.";
		}
		
	}
	
	private void init()
	{
		gtokens = new Vector<Token>();
		functions = new String[]{"SVSeq", "SVRep", "SVImplication", "SVStable", "SVChanged", "SVPast",
				  "SVRose", "SVFell"};
		structs = new String[]{"Disif"};
		structEnds = new String[]{"endDisif"};
		keyWords = new String[]{"not", "and", "or", "true", "false", "null"};
		
		spChars = new char[]{'(', ')', ',', '[', ']'};
		operators = new char[]{'+', '-', '*', '/', '<', '>', '='};
		
		str = null;
		lastToken = null;
		convertedSt = null;
	}

	private String convert(Vector<Token> tokens) throws Exception
	{
		String str = "";
		try {
		for(Token token : tokens) {
			if(!token.isEvaluated()) {
				
				if(token.isFunc()) {
					
					Vector<Token>[] params = token.getParams();
					
					if(params == null || params.length == 0) {
						str += "^^"+token.getName()+": No Param^^";
						// TODO: No params
					}
					else {
						String[] strParams = new String[token.getNoOfParams()];
						for(int i=0; i<strParams.length; i++) {
							strParams[i] = "";
						}
						
						for(int i=0; i<params.length; i++) {
							
							Vector<Token> pTokens = params[i];
							
							if(pTokens == null || pTokens.size() == 0) {
								str += "^^"+token.getName()+": Empty Param^^";
								// TODO: an empty param
							}
							else {
								for(int j=0; j<pTokens.size(); j++) {
									Token ptoken = pTokens.get(j);
									if (ptoken.isFunc()) {
										strParams[i] += convert(getSubVector(ptoken));
									}
									else {
										if(!ptoken.isEvaluated()) {
//											System.out.println("Adding: "+ptoken.getStrRep());
											strParams[i] += ptoken.getName();
											ptoken.setEvaluated(true);
										}
									}
								}
							}
						}
						
						str += decide(token.getName(), strParams);						
					}
					token.setEvaluated(true);
				}
				else if (token.isStruct()) {
					if(token.getName().equals("Disif")) {			// TODO: Hardcoded value may be changed
						String[] strParams = {"", ""};
						int startInd = gtokens.indexOf(token.getLp());
						int endInd = gtokens.indexOf(token.getRp());
						if(endInd-startInd <= 1) {
							str += "^^"+token.getName()+": Empty Expression^^";
							// TODO: an empty expression
						}
						else {
							for (int i=startInd+1; i < endInd; i++) {
								Token ptoken = gtokens.get(i);
								strParams[0] += ptoken.getName();
								ptoken.setEvaluated(true);
							}
						}
						
						startInd = gtokens.indexOf(token.getLb())+1;
						endInd = gtokens.indexOf(token.getRb())-1;
						
						strParams[1] += convert(getSubVector(startInd, endInd));
						
						str += decide(token.getName(), strParams);
						token.setEvaluated(true);
					}
				}
				
				else {
					int ind = gtokens.indexOf(token);
					if(ind+1 < gtokens.size()) {
						if(!gtokens.get(ind+1).isEvaluated()) {
							str += token.getName();
						}
					}
					else {
						str += token.getName();
					}
					token.setEvaluated(true);
				}
			}
		}
		
		if(str.endsWith("\n")) {
			str = str.trim()+"\n";
		}
		else {
			str = str.trim();
		}
		}
		catch(Exception e) {
			throw e;
		}
		return str;
	}
	
	private Vector<Token> getSubVector(Token func)
	{
		int stInd = gtokens.indexOf(func);
		int endInd = gtokens.indexOf(func.getRp())-1;
		Vector<Token> newVect = new Vector<Token>();

		for(int i=stInd; i <= endInd; i++) {
			newVect.add(gtokens.get(i));
		}
		
		return newVect;
	}

	private Vector<Token> getSubVector(int startInd, int endInd)
	{
		Vector<Token> newVect = new Vector<Token>();

		for(int i=startInd; i <= endInd; i++) {
			newVect.add(gtokens.get(i));
		}
		
		return newVect;
	}
	
 	private void tokenize()  throws Exception
	{
 		String str = this.str;
 
		StringBuilder buffer = new StringBuilder();
		char prevChar = '\0';

		try {
		while(!str.isEmpty()) {
			char ch = str.charAt(0);

			if(Character.isWhitespace(ch) && ch != '\n') {		// TODO: how to deal white spaces?
				ch = ' ';
			}

			if (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_') {
				buffer.append(ch);
			}
			else {
				if(isOperator(ch)) {
					if(ch == '>' && prevChar == '<') {
						gtokens.remove(gtokens.size()-1);
						addToken("!=", "Oper");
					}
					else {
						validateBuffer(buffer);
						addToken(ch, "Oper");
					}
				}
				else if(isSpChar(ch)) {
					validateBuffer(buffer);
					addToken(ch);
				}
				else if (Character.isWhitespace(ch)) {

					if(buffer.length() > 0) {
						validateBuffer(buffer);
						buffer.setLength(0);
						addToken(ch);
					}
					else if(ch != prevChar) {
						if(ch == '\n'){
							if(lastToken.charAt(0) != '\n') {
								addToken(ch);
							}
						}
						else {
							if (prevChar != '\n') {
								addToken(ch);
							}
						}
					}
				}

				if(ch != ' ' && ch != '\n') {
					buffer.setLength(0);
				}
			}

			prevChar = ch;
			str = str.substring(1);
			if(str.isEmpty()) {
				if(buffer.length() > 0) {
					validateBuffer(buffer);
					buffer.setLength(0);
				}
			}
		}
		}
		catch (Exception e) {
			throw e;
		}
	}

	private void extractFuncs() throws Exception
	{
		int size = gtokens.size();
		Stack<Token> lpStack = new Stack<Token>();
		Stack<Token> lbStack = new Stack<Token>();
		
		try{
		for(int i=0; i<size; i++) {
			Token t = gtokens.get(i);
			if(t.isFunc() || t.isStruct()) {
				Token lp = null;
				do {
					if(i+1 < gtokens.size()) {
						lp = gtokens.get(++i);
					}
				} while(lp != null && lp.isWhiteSpace());
				
				if(lp != null && lp.isLeftP()) {
					t.setLp(lp);
					lp.setAssoc(t);
					lpStack.push(lp);
				}
				else {
					// ERROR function without (
				}
			}
			else if(t.isOperator()) {
				Token next = null;
				int k = i;
				do {
					if(k+1 < gtokens.size()) {
						next = gtokens.get(++k);
					}
				} while(next != null && next.isWhiteSpace());
				if(next != null && next.isLeftP()) {
					t.setLp(next);
					next.setAssoc(t);
					lpStack.push(next);
					i = k;
				}
			}
			else if(t.isLeftP()) {
				lpStack.push(t);
			}
			else if(t.isRightP()) {
				Token lp = lpStack.pop();
				Token assoc = lp.getAssoc();
				if(assoc != null) {
					t.setAssoc(assoc);
					assoc.setRp(t);
					
					if(assoc.isFunc()) {
						int startInd = gtokens.indexOf(lp);
						int endInd = gtokens.indexOf(t);
						
						int pCount = 0;

						if(endInd-startInd > 1) {
							pCount = 1;
							for(int j=startInd+1; j<endInd; j++) {
								Token cm = gtokens.get(j);
								if(cm.isComma() && cm.getAssoc() == null) {
									cm.setAssoc(assoc);
									assoc.addComma(cm);
									pCount++;
								}
							}
							
							Vector<Token>[] params = new Vector[pCount];
							
							pCount = 0;
							params[0] = new Vector<Token>();
							Vector<Token> temp = params[0];
							for(int j=startInd+1; j<endInd; j++) {
								Token tk = gtokens.get(j);
								if(tk.isComma() && tk.getAssoc() == assoc) {
									pCount++;
									params[pCount] = new Vector<Token>();
									temp = params[pCount];
								}
								else {
									temp.add(tk);
								}
							}
							assoc.setParams(params);
//							assoc.printFunc();
						}
					}
					else if(assoc.isStruct()) {
						Token lb = null;
						do {
							if(i+1 < gtokens.size()) {
								lb = gtokens.get(++i);
							}
						} while(lb != null && lb.isWhiteSpace());
						if(lb != null && lb.isLeftB()) {
							lbStack.push(lb);
							lb.setAssoc(assoc);
							assoc.setLb(lb);
							lbStack.push(lb);
						}
						else {
							// ERROR: [ not found after expression
						}
					}
				}
			}
			else if (t.isRightB()) {
				Token lb = lbStack.pop();
				Token assoc = lb.getAssoc();
				if(assoc != null && assoc.isStruct()) {
					t.setAssoc(assoc);
					assoc.setRb(t);
					
					Token end;
					do {
						end = gtokens.get(++i);
					} while(end.isWhiteSpace());
					if(end.isStructEnd()) {
						end.setAssoc(assoc);
						assoc.setEnd(end);
					}
					else {
						// ERROR: endStatement not found
					}
				}
			}
			else if(t.isPort()){
				// For now neglect.
			}
		}
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	private String decide(String fname, String[] pstrs)
	{		
		String conv = "";
		
		for(int i=0; i<pstrs.length; i++) {
			pstrs[i] = pstrs[i].trim();
		}
		
		switch(fname) {
		case "SVSeq":
			if(pstrs.length == 2) {
				conv = convertSVSeq(pstrs[0], pstrs[1]);
			}
			else {
				conv = convertSVSeq(pstrs[0], pstrs[1], pstrs[2]);
			}
			break;
		case "SVRep":
			conv = convertSVRep(pstrs[0], pstrs[1]);
			break;
		case "SVImplication":
			conv = convertSVImpl(pstrs[0], pstrs[1], pstrs[2]);
			break;
		case "SVStable":
			conv = convertSVStable(pstrs[0]);
			break;
		case "SVChanged":
			conv = convertSVChanged(pstrs[0]);
			break;
		case "SVPast":
			if(pstrs.length == 1) {
				conv = convertSVPast(pstrs[0]);
			}
			else {
				conv = convertSVPast(pstrs[0], pstrs[1]);
			}
			break;
		case "SVRose":
			conv = convertSVRose(pstrs[0]);
			break;
		case "SVFell":
			conv = convertSVFell(pstrs[0]);
			break;
		case "Disif":
			conv = convertDisif(pstrs[0], pstrs[1]);
			break;
		default:
			conv = "^^Invalid Function^^";
		}

		return conv;
	}
	
	private void validateBuffer(StringBuilder buffer)
	{
		if(buffer.length() > 0) {
			if(isFunc(buffer)) {
				addToken(buffer, "Func");
			}
			else if(isStruct(buffer)) {
				addToken(buffer, "Struct");
			}
			else if(isStructEnd(buffer)) {
				addToken(buffer, "StructE");
			}
			else if(isKeyWord(buffer)) {
//				if(buffer.toString().equals("not")) {
//					buffer.setLength(0);
//					buffer.append('!');
//				}
//				else 
				if(buffer.toString().equals("and")) {
					buffer.setLength(0);
					buffer.append("&&");
				}
				else if(buffer.toString().equals("or")) {
					buffer.setLength(0);
					buffer.append("||");
				}
					
				addToken(buffer, "KeyW");
			}
			else {
				addToken(buffer, "Port");
			}
		}
		else {
//			System.out.println("Buffer is Empty.");
		}
	}

	private void addToken(StringBuilder tokenName, String type)
	{
		lastToken = tokenName.toString();
		gtokens.add(new Token(tokenName.toString(), type));		
	}

	private void addToken(String tokenName, String type)
	{
		gtokens.add(new Token(tokenName, type));		
	}
	
	private void addToken(char ch, String type)
	{
		lastToken = Character.toString(ch);
		gtokens.add(new Token(Character.toString(ch), type));		
	}

	private void addToken(char ch)
	{
		String st = Character.toString(ch);
		lastToken = st;
		if(Character.isWhitespace(ch)) {
			gtokens.add(new Token(st, "WSpace"));
		}
		else {
			gtokens.add(new Token(st, st));
		}
	}

	private boolean isSpChar(char ch)
	{
		for(int i=0; i<spChars.length; i++) {
			if(spChars[i] == ch) {
				return true;
			}
		}
		return false;
	}

	private boolean isOperator(char ch)
	{
		for(int i=0; i<operators.length; i++) {
			if(operators[i] == ch) {
				return true;
			}
		}
		return false;
	}

	private boolean isFunc(StringBuilder str)
	{
		for(int i=0; i<functions.length; i++) {
			if(functions[i].equals(str.toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean isStruct(StringBuilder str)
	{
		for(int i=0; i<structs.length; i++) {
			if(structs[i].equals(str.toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean isStructEnd(StringBuilder str)
	{
		for(int i=0; i<structEnds.length; i++) {
			if(structEnds[i].equals(str.toString())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isKeyWord(StringBuilder str)
	{
		for(int i=0; i<keyWords.length; i++) {
			if(keyWords[i].equals(str.toString())) {
				return true;
			}
		}
		return false;
	}

	private String convertSVSeq(String s, String d, String p)
	{
		String str;

		str= p+" ##"+evalTime(d)+" "+s;
		
    	return str;
	}

	private String convertSVSeq(String s, String d)
	{
		String str;

		str= "  ##"+evalTime(d)+" "+s;
		
    	return str;
	}

	private String convertSVRep(String p, String r)
	{
		String str = p+"[*"+r+"]";
		return str;
	}

	private String convertSVImpl(String ant, String con, String t)
	{
		String str;
		if(t.equals("1")) {
			str = ant+" |-> "+con;
		}
		else {
			str = ant+" |=> "+con;
		}
		return str;
	}

	private String convertSVStable(String expr)
	{
		String str = "$stable("+expr+")";
		return str;
	}

	private String convertSVChanged(String expr)
	{
		String str = "$changed("+expr+")";
		return str;
	}
	
	private String convertSVPast(String past, String ct)
	{
		String str = "$past("+past+","+ct+")";
		return str;
	}
	
	private String convertSVPast(String past)
	{
		return convertSVPast(past, "1");
	}
	
	private String convertSVRose(String expr)
	{
		String str = "$rose("+expr+")";
		return str;
	}
	
	private String convertSVFell(String expr)
	{
		String str = "$fell("+expr+")";
		return str;
	}
	
	private String convertDisif(String reset, String conv_expr)
	{
		String str = "disable iff("+reset+")\n"+conv_expr;
		return str;
	}
	
	private String evalTime(String time)
	{
		String str;
		int len = time.length();
		if(len == 4) {
			String s1= time.substring(0, 2);
			String s2= time.substring(2);
			if(s1.startsWith("0")) {
				s1 = s1.substring(1);
			}
			if(s2.startsWith("0")) {
				s2 = s2.substring(1);
			}
			str = "["+s1+":"+s2+"]";
		}
		else if(time.startsWith("0")) {
			str = "["+time.substring(0, len-2)+":"+time.substring(len-2)+"]";
		}
		else {
			str = time;
		}
		return str;
	}
}
