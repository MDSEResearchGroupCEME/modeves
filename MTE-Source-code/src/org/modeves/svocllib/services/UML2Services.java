package org.modeves.svocllib.services;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FunctionBehavior;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;


public class UML2Services {
	public static String path="";
	
	
	public String prepareRequirementFile(Model model)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle"); 
		try (BufferedWriter bwreq = new BufferedWriter(new FileWriter(path))) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@path: "+path);
		String[] req=bundle.getString(model.getName()).split("&");
		for(int i=0;i<req.length;i++)
		{
			 String[] reqsplit=req[i].split(";");
		   	 bwreq.write("/*"+"\n");
			 bwreq.write("ReqID: "+model.getName().concat("_Req "+i)+"\n");
			 String property=reqsplit[0];
			 String orignalreqtext=property;
			 bwreq.write("Mutex: "+property+"\n");
			 bwreq.write("*/\n");
			 String reqproperty="";
			 if(property.equalsIgnoreCase("System is deadlock free"))
			 {
				 reqproperty="not deadlock";
			 }else if(property.equalsIgnoreCase("System has deadlock"))
			 {
				 reqproperty="deadlock";
			 }else
			 {
				 if(property.contains("Never "))
				 {
					 property=property.replaceAll("Never ", "not");
				 }
				 if(property.contains(" state is "))
				 {
					 property=property.replaceAll(" state is ", ".");
				 }
				 
				 if(property.contains("If"))
				 {
					 property=property.replaceAll("If", "");
				 }
				 if(property.contains(" then at next state "))
				 {
					 property=property.replaceAll(" then at next state ", " --> ");
				 }
				 if(property.contains(" then "))
				 {
					 property=property.replaceAll(" then ", " imply ");
				 }
				 if(property.contains(" greater than equal to "))
				 {
					 property=property.replaceAll(" greater than equal to ", " >= ");
				 }
				 if(property.contains(" equal equal to "))
				 {
					 property=property.replaceAll(" equal equal to ", " == ");
				 }
				 if(property.contains(" less than equal to"))
				 {
					 property=property.replaceAll(" less than equal to", " <= ");
				 }
				 if(property.contains(" less than "))
				 {
					 property=property.replaceAll(" less than ", " < ");
				 }
				 if(property.contains(" not "))
				 {
					 property=property.replaceAll(" not ", " ! ");
				 }
				 reqproperty=property;
			 }
			 String reqtype=reqsplit[1];
			 if(reqtype.equalsIgnoreCase("Possibly"))
			 {
				 bwreq.write("E<> "+reqproperty+"\n");
				 TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"E<> "+reqproperty);
			 }else if(reqtype.equalsIgnoreCase("Invariantly"))
			 {
				 bwreq.write("A[] "+reqproperty+"\n");
				 TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"A[] "+reqproperty);
			 }else if(reqtype.equalsIgnoreCase("Potentially always"))
			 {
				 bwreq.write("E[] "+reqproperty+"\n");
				 TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"E[] "+reqproperty);
			 }else if(reqtype.equalsIgnoreCase("Eventually "))
			 {
				 bwreq.write("A<> "+reqproperty+"\n");
				 TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"A<> "+reqproperty);
			 }else
			 {
				 bwreq.write(""+reqproperty+"\n");
				 TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+reqproperty);
			 }
			 
		}
		//bwreq.close();
		} catch (IOException e) {

			e.printStackTrace();

		}
		System.out.println("ends time: "+System.currentTimeMillis());	
		return null;
	}
	
	
	public String globalDeclPreparation(Model model) throws IOException
	{
		try (BufferedWriter pw = new BufferedWriter(new FileWriter(path+"\\StateMachine.xta"))) {
		pw.write("after");
		List<Element> elements = new ArrayList<Element>(model.getOwnedElements());
		for(int i=0;i<elements.size();i++)
		{
			// Global Declaration
			 List<Stereotype> stereotypes = elements.get(i).getAppliedStereotypes();
		     for (Stereotype stereotype : stereotypes) {
		       if (stereotype.getName().equals("GlobalDeclaration")) {
		    	   List<Property> propertyarr=(List<Property>) elements.get(i).getValue(elements.get(i).getAppliedStereotype("Profile::GlobalDeclaration"), "Properties");
		    	   for (Property property : propertyarr) {
		    		   if(property.isStatic()==true)
		    		   {
		    			   pw.write("const "+ property.getType().getName()+" "+property.getName());
		    			   System.out.println("Global : "+property.getName());
		    			   if(property.getDefault()!=null && property.getType().getName()=="bool")
		    			   {
		    				   pw.write(" = "+ property.getDefaultValue());
		    			   }else if(property.getDefault()!=null && property.getType().getName()=="int")
		    			   {
		    				   pw.write(" = "+ property.getDefaultValue());
		    			   }
		    			   pw.write(";\n");
		    		   }else
		    		   {
		    			   pw.write( property.getType().getName()+" "+property.getName());
		    			   System.out.println("Global : "+property.getName());
		    			   if(property.getDefault()!=null && property.getType().getName()=="bool")
		    			   {
		    				   pw.write(" = "+ property.getDefaultValue());
		    			   }else if(property.getDefault()!=null && property.getType().getName()=="int")
		    			   {
		    				   pw.write(" = "+ property.getDefaultValue());
		    			   }  
		    			   pw.write(";\n");
		    		   }
		    	  
		    	   }
		    		
		       }
		   }
		}
		} catch (IOException e) {

			e.printStackTrace();

		}
		return null;
	}
	
	public String systemDeclPreparation(Model model) throws IOException
	{
		try (BufferedWriter pw = new BufferedWriter(new FileWriter(path+"\\StateMachine.xta",true))) {
		System.out.println("path: "+path);
		
		List<Element> elements = new ArrayList<Element>(model.getOwnedElements());  
		
		  // System Definition
		     
		     for(int i=0;i<elements.size();i++)
				{
					// System Declaration
					 List<Stereotype> stereotypes = elements.get(i).getAppliedStereotypes();
					 for (Stereotype stereotype : stereotypes) {
						 if (stereotype.getName().equals("SystemDeclaration")) {
						 List<Property> propertyarr=(List<Property>) elements.get(i).getValue(elements.get(i).getAppliedStereotype("Profile::SystemDeclaration"), "Properties");
			    	     for (Property property : propertyarr) 
			    	     {
			    	    	 if(property.isStatic()==true)
			    		     {
			    			   pw.write("const "+ property.getType().getName()+" "+property.getName());
			    			   System.out.println("Global : "+property.getName());
			    			   if(property.getDefault()!=null && property.getType().getName()=="bool")
			    			   {
			    				   pw.write(" = "+ property.getDefaultValue());
			    			   }else if(property.getDefault()!=null && property.getType().getName()=="int")
			    			   {
			    				   pw.write(" = "+ property.getDefaultValue());
			    			   }
			    			   pw.write(";\n");
			    		     }else
			    		     {
			    			   pw.write( property.getType().getName()+" "+property.getName());
			    			   System.out.println("Global : "+property.getName());
			    			   if(property.getDefault()!=null && property.getType().getName()=="bool")
			    			   {
			    				   pw.write(" = "+ property.getDefaultValue());
			    			   }else if(property.getDefault()!=null && property.getType().getName()=="int")
			    			   {
			    				   pw.write(" = "+ property.getDefaultValue());
			    			   }  
			    			   pw.write(";\n");
			    		     }
			    	  
			    	   }
			    	   System.out.println("initialize sys: "+elements.get(i).getValue(elements.get(i).getAppliedStereotype("Profile::SystemDeclaration"), "initilizeSystem"));
			    	   Operation operation= (Operation) elements.get(i).getValue(elements.get(i).getAppliedStereotype("Profile::SystemDeclaration"), "initilizeSystem");
			    	   
			    	   //for (Operation operation : operationsarr) {
			    	   System.out.println("method out"+operation.getName());
			    		   if(operation.getName().equalsIgnoreCase("initilizeSystem"))
			    		   {
			    			   FunctionBehavior fbahaviour=(FunctionBehavior) operation.getMethods().get(0);
			    			   pw.write(fbahaviour.getBodies().get(0));
			    		   }
			    		   
			    	   //}
			    	   
			    	   
			    		
			       }
			   }	
		}
		
		} catch (IOException e) {

			e.printStackTrace();

		}
		return null;
	}
	public static void writeIntoFile(String str) throws IOException
    {
		try (BufferedWriter pw = new BufferedWriter(new FileWriter(path+"\\StateMachine.xta",true))) {
	
			pw.write("str\n");
	    } catch (IOException e) {
	
			e.printStackTrace();
	
		}
		
	}


}
