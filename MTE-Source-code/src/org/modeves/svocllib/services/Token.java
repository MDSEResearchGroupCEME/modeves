package org.modeves.svocllib.services;

import java.util.Vector;

/**
 * 
 * @author Yasir
 *
 */
class Token
{
	private String name;
	private String type;
	private Token lp;
	private Token rp;
	private Token lb;
	private Token rb;
	private Token assoc;
	private Token end;
	private int noOfParam;
	private Vector<Token>[] params;
	private Vector<Token> commas;
	private boolean evaluated = false;
	
	private static int count = 0;
	private int id; 
	
	Token(String name, String type)
	{
		this.name = name;
		this.type = type;
		count++;
		id = count;
	}

	int getId()
	{
		return id;
	}

	String getStrRep()
	{
		if(name.equals("\n")) {
			return Character.toString((char) 172);
		}
		else if(name.equals(" ")) {
			return Character.toString((char) 168);
		}
		else {
			return name;
		}
	}
	
	void setAssoc(Token assoc)
	{
		this.assoc = assoc;
	}

	void setEnd(Token end)
	{
		this.end = end;
	}

	Token getEnd()
	{
		return end;
	}
	
	String getName()
	{
		return name;
	}

	void setName(String name)
	{
		this.name = name;
	}

	String getType()
	{
		return type;
	}

	void setType(String type)
	{
		this.type = type;
	}

	Token getLp()
	{
		return lp;
	}

	void setLp(Token lp)
	{
		this.lp = lp;
	}

	Token getRp()
	{
		return rp;
	}

	void setRp(Token rp)
	{
		this.rp = rp;
	}

	Token getLb()
	{
		return lb;
	}

	void setLb(Token lb)
	{
		this.lb = lb;
	}

	Token getRb()
	{
		return rb;
	}

	void setRb(Token rb)
	{
		this.rb = rb;
	}

	Token getAssoc()
	{
		return assoc;
	}

	Vector<Token>[] getParams()
	{
		return params;
	}

	void setEvaluated(boolean evaluated)
	{
//		System.out.println("Evaluated: "+toString());
		this.evaluated = evaluated;
		if(lp != null) {
			lp.setEvaluated(evaluated);
		}
		if(rp != null) {
			rp.setEvaluated(evaluated);
		}
		if(lb != null) {
			lb.setEvaluated(evaluated);
		}
		if(rb != null) {
			rb.setEvaluated(evaluated);
		}
		if(commas != null && commas.size()>0) {
			for(Token c : commas) {
				c.setEvaluated(evaluated);
			}
		}
		if(end != null) {
			end.setEvaluated(evaluated);
		}
	}

	void setParams(Vector<Token>[] params)
	{
		this.params = params;
		noOfParam = params.length;
	}
	
	void addComma(Token comma)
	{
		if(commas == null) {
			commas = new Vector<Token>();
		}
		commas.add(comma);
	}
	
	boolean isFunc()
	{
		return type.equals("Func");
	}
	
	boolean isStruct()
	{
		return type.equals("Struct");
	}

	boolean isStructEnd()
	{
		return type.equals("StructE");
	}
	
	boolean isKeyword()
	{
		return type.equals("KeyW");
	}
	
	boolean isPort()
	{
		return type.equals("Port");
	}
	
	boolean isOperator()
	{
		return type.equals("Oper");
	}
	
	boolean isLeftP()
	{
		return name.equals("(");
	}
	
	boolean isRightP()
	{
		return name.equals(")");
	}
	
	boolean isLeftB()
	{
		return name.equals("[");
	}
	
	boolean isRightB()
	{
		return name.equals("]");
	}
	
	boolean isComma()
	{
		return name.equals(",");
	}
	
	boolean isWhiteSpace()
	{
		return type.equals("WSpace");
	}
	
	boolean isEvaluated()
	{
		return evaluated;
	}
	
	int getNoOfParams()
	{
		return noOfParam;
	}
	
	void printFunc()
	{
		String str = name;
		
		if(type.equals("Func")) {
			str += "(";
			for(int i=0; i<params.length; i++) {
				if(i>0) {
					str += ",";
				}
				for(int j=0; j<params[i].size(); j++) {
					Token t = params[i].get(j);
					str += t.getName();
				}
			}
			str += ")";
		}
		
		System.out.println(str);
	}
	
	public String toString()
	{
		return getStrRep()+": "+id;
	}
}
