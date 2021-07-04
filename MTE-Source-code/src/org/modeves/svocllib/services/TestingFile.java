package org.modeves.svocllib.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestingFile {

	public static String destpath="";
	public static List<String> reqText=new ArrayList<String>();
	public static List<String> resultText=new ArrayList<String>();
	public static HashMap<String, String> hashmap=new HashMap<String, String>();
	public static void verifyRequirements() throws IOException {
		// TODO Auto-generated method stub
			String modelpath=destpath+"\\StateMachine.xta ";
			String querypath=destpath+"\\StateMachine.q";
			String cmdquery="cd \"E:\\Aamir Data\\uppaal-4.0.14\\bin-Win32\"&&verifyta.exe -q -s "+"\""+modelpath+"\" \""+querypath+"\"";
			System.out.println("cmd query: "+cmdquery);		
			ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmdquery);
	        builder.redirectErrorStream(true);
	        Process p = builder.start();
	        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            else
	            {
	            	System.out.println("uppaal results: "+line);
	            	if(line.startsWith("Verifying property"))
	            	{
	            		String reqid=line.substring(line.indexOf("Verifying property "), line.indexOf(" at line")).replace("Verifying property ", "");
	            		String results=r.readLine();
	            		System.out.println(results+" for Requirement "+reqid);
	            		resultText.add(results);
	            	}
	            
	            }
	        }
	        if(!reqText.isEmpty())
	        {
		        for(int i=0;i<reqText.size();i++)
		        {
		        	System.out.println("ReqText");
		        	hashmap.put(reqText.get(i), resultText.get(i));
		        }
		        
	        }
	        
	}
	public static void printresults()
	{
		System.out.println("printing full: "+hashmap);
	}

}
