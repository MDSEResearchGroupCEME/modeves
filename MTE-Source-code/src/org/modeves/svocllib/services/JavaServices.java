package org.modeves.svocllib.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Vertex;
import org.modeves.svocllib.main.Launcher;

/**
 * 
 * @author Yasir
 *
 */
public class JavaServices
{
	private static int emptyPropCtr = 0;
	private static int emptyFsmNameCtr = 0;
	private static String propsFileName;
	private static String propertyFileName;
	private static String behFileName;
	private static String timedAutomataFileName;
	private static String methodSelection;
	private static HashMap<String, String> currentStateVars = new HashMap<String, String>();
	private static HashMap<String, String> nextStateVars = new HashMap<String, String>();

	private static HashMap<String, String> initStates = new HashMap<String, String>();
	private static HashMap<String, ArrayList<Vertex>> vertexTargets = new HashMap<String, ArrayList<Vertex>>();
	private static HashMap<String, ArrayList<Transition>> targetTrns = new HashMap<String, ArrayList<Transition>>();

	public static void reset()
	{
		emptyPropCtr = 0;
		emptyFsmNameCtr = 0;
		propsFileName = null;
		behFileName = null;
		methodSelection= null;
		currentStateVars = new HashMap<String, String>();
		nextStateVars = new HashMap<String, String>();
		initStates = new HashMap<String, String>();
		vertexTargets = new HashMap<String, ArrayList<Vertex>>();
		targetTrns = new HashMap<String, ArrayList<Transition>>();
	}

	public static String getEngineVersion()
	{
		return Launcher.SVOCL_ENGINE_VER;
	}
	
	public static String getSvoclBuild()
	{
		return ""+Launcher.SVOCL_BUILD;
	}
	
	public static void setAssertFileName(String name)
	{
		propsFileName = name;
	}
	public static void setSelectionMethod(String name)
	{
		methodSelection = name;
	}
	public static String getSelectionMethod()
	{
		System.out.println("value is: "+methodSelection);
		return methodSelection;
	}
	
	public static void setBehFileName(String name)
	{
		behFileName = name;
	}
	
	public static String getAssertFileName()
	{
		return propsFileName;
	}
	
	public static String getBehFileName()
	{
		return behFileName;
	}
	public static void setTimedAutomataFileName(String name)
	{
		timedAutomataFileName = name;
	}
	public static void setPropertyFileName(String name)
	{
		propertyFileName = name;
	}
	public static String getPropertyFileName()
	{
		return propertyFileName;
	}
	
	public static String getTimedAutomataFileName()
	{
		return timedAutomataFileName;
	}
	public String parseToSVL(String oclExpr, String fileId)
	{
		return SvoclToSvlParser.getInstance().execute(oclExpr, fileId);
	}
	
	public static String getDate()
	{
		String actual;
		DateFormat dateFormatter = DateFormat.getDateTimeInstance();
		dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+5"));
		actual=dateFormatter.format(new Date());
		return actual;
	}

	public String getPropName(String str)
	{
		if (str != null) {
			str = str.trim();
			if (str.isEmpty()) {
				return ("property"+(++emptyPropCtr));
			}
			else {
				return str.replaceAll(" ", "_");
			}
		}
		else {
			return ("property"+(++emptyPropCtr));
		}
	}

	public String makeIdentifier(String str, boolean toLowerCase)
	{
		if (str != null) {
			str = str.trim();
			if (str.isEmpty()) {
				return "";
			}
			else {
				if(toLowerCase) {
					str = str.toLowerCase();
				}
				return str.replaceAll(" ", "_");
			}
		}
		else {
			return "";
		}
	}
	
	public String getInitialState(State fsm)
	{
		if(!initStates.containsKey(fsm)) {
			Region region = fsm.getRegions().get(0);
			EList<EObject> contents = region.eContents();
			
			for(EObject eObj : contents) {
				if(eObj.eClass().getInstanceClass() == Pseudostate.class)
				{
					Pseudostate ps = (Pseudostate) eObj;
					if(ps.getKind().getValue() == PseudostateKind.INITIAL) {
						String initStateName = ps.getOutgoings().get(0).getTarget().getName();
						if(initStateName == null || initStateName.isEmpty()) {
							System.out.println("UNNAMED initial state while adding initial state.");
							initStateName = "UNNAMED_INIT";
						}
						initStates.put(getId(fsm), initStateName);
						return initStateName;
					}
				}
			}
		}
		
		return initStates.get(getId(fsm));
	}

	private void addStateVars(EObject obj)
	{
		String key = getId(obj);
		String fsmName = null;
		boolean ok = true;
		if(obj.eClass().getInstanceClass() == StateMachine.class) {
			fsmName = ((StateMachine)obj).getName();
		}
		else if (obj.eClass().getInstanceClass() == State.class) {
			fsmName = ((State)obj).getName();
		}
		if(fsmName.equalsIgnoreCase("FSM")) {
			ok = false;
		}
		if(fsmName == null || fsmName.isEmpty()) {
			fsmName = "fsm"+(++emptyFsmNameCtr);
			ok = false;
		}
		String curr = makeIdentifier(fsmName, true)+"_state";
		String next = "next_"+makeIdentifier(fsmName, true)+"_state";
		if (ok) {													// Not Standard. Reconsider.
			curr = curr.replace("fsm", "").replace("fsM", "").replace("fSm", "").replace("fSM", "").replace("Fsm", "").
					replace("FsM", "").replace("FSm", "").replace("FSM", "");
			next = next.replace("fsm", "").replace("fsM", "").replace("fSm", "").replace("fSM", "").replace("Fsm", "").
					replace("FsM", "").replace("FSm", "").replace("FSM", "");
		}
		currentStateVars.put(key, curr.replace("__", "_"));
		nextStateVars.put(key, next.replace("__", "_"));
	}
	
	private String getCurrentVarName(EObject fsm)
	{
		String key = getId(fsm);
		if(!currentStateVars.containsKey(key)) {
			addStateVars(fsm);
		}
		return currentStateVars.get(key);
	}
	
	public String getCurrentVar(StateMachine fsm)
	{
		return getCurrentVarName(fsm);
	}

	public String getCurrentVar(State fsm)
	{
		return getCurrentVarName(fsm);
	}
	
	private String getNextVarName(EObject fsm)
	{
		String key = getId(fsm);
		if(!nextStateVars.containsKey(key)) {
			addStateVars(fsm);
		}
		return nextStateVars.get(key);
	}
	
	public String getNextVar(StateMachine fsm)
	{
		return getNextVarName(fsm);
	}
	
	public String getNextVar(State fsm)
	{
		return getNextVarName(fsm);
	}

	public String getFsmCurrentVar(Vertex vertex)
	{
		State state = vertex.getContainer().getState();
		StateMachine sm = vertex.getContainer().getStateMachine();
		String varName = "UNDEFINED_FSM_state";
		if(sm != null && state == null) {
			varName = getCurrentVar(sm);
		}
		else if(sm == null && state != null) {
			varName = getCurrentVar(state);
		}
		return varName;
	}
	
	public String getFsmNextVar(Vertex vertex)
	{
		State state = vertex.getContainer().getState();
		StateMachine sm = vertex.getContainer().getStateMachine();
		String varName = "UNDEFINED_FSM_next_state";
		if(sm != null && state == null) {
			varName = getNextVar(sm);
		}
		else if(sm == null && state != null) {
			varName = getNextVar(state);
		}
		return varName;
	}
	
	public String toBinary(String str)
	{
		return Integer.toBinaryString(Integer.parseInt(str));
	}
	
	public String getConditionStr(Transition trn, String fileId)
	{
		String expr = "";
		if(trn.eContents() != null) {
			for(EObject eobj : trn.eContents())
			{
				if(eobj instanceof Constraint) {
					Constraint constr = (Constraint) eobj;
					String localExpr = "";
					if (constr.getConstrainedElements() != null &&
							constr.getConstrainedElements().size() > 0) {
						Element element = constr.getConstrainedElements().get(0);
						if(element != null && element instanceof Port) {
							Port port = (Port) element;
							String portName = port.getName();
							if(portName != null && !portName.isEmpty()) {
								localExpr += portName + " == ";
							}
						}
					}
					if (constr.getSpecification() != null) {
						String spec = constr.getSpecification().stringValue();
						if(spec != null && !spec.isEmpty()) {
							localExpr += spec;
							expr += localExpr + " && ";
						}
					} 
				}
			}
			
		}
		for (Trigger trigger : trn.getTriggers()) {
			for (Port port : trigger.getPorts()) {
				if(port.getType() instanceof Signal) {
					
					State state = getLastStateForTranstion(trn);
					
					if(state != null && state.getDoActivity() != null && state.getDoActivity().getName() != null &&
							state.getDoActivity().getName().trim().equalsIgnoreCase("Call Timer")) {

						Class cls = null;
						int max = 0;
						Model model= trn.getModel();
						TreeIterator<EObject> iterator = model.eAllContents();
						while(iterator.hasNext()) {
							EObject obj = iterator.next();
							if (obj instanceof Class) {
								if (((Class)obj).getName() != null && ((Class)obj).getName().equals("Timer")) {
									cls = (Class)obj;
									break;
								}
							}
						}
						if(cls != null) {
							for(Element elem : cls.getOwnedElements()) {
								if(elem instanceof Property) {
									Property prop = (Property)elem;
									if(prop.getName() != null && prop.getName().equals("TimerMaxCount")) {
										max = Integer.parseInt(prop.getDefault());
										break;
									}
								}
							}
							expr += port.getName()+ " == 2'b"+Integer.toBinaryString(max)+" &&";
						}
					}
				}
			}
		}
		expr = replaceTrueFalse(expr.trim());
		if(expr.endsWith("&&")) {
			expr = expr.substring(0, expr.length()-3).trim();
		}

		return expr;
	}

	private void addSourceVertex(Vertex vertex)
	{
		EList<Transition> trns = vertex.getOutgoings();
		
		String stateKey = String.valueOf(vertex.hashCode());

		if(!vertexTargets.containsKey(stateKey)) {
			vertexTargets.put(stateKey, new ArrayList<Vertex>());
		}
		
		ArrayList<Vertex> targets = vertexTargets.get(stateKey);
		
		if(trns == null || trns.size() <= 0) {
			return;
		}
		else {
			for(Transition trn : trns) {
				Vertex target = trn.getTarget();
				if(target == null) {
					continue;
				}
				String targetKey = stateKey+"$"+target.hashCode();
				
				if(!targets.contains(target)) {
					targets.add(target);
					targetTrns.put(targetKey, new ArrayList<Transition>());
				}
				targetTrns.get(targetKey).add(trn);
			}
		}
	}
	
	public ArrayList<Vertex> getTargets(Vertex source)
	{
		if(!vertexTargets.containsKey(getId(source))) {
			addSourceVertex(source);
		}
		
		return vertexTargets.get(getId(source));
	}
	
	public ArrayList<Transition> getTrnsTo(Vertex source, Vertex target)
	{
		if(!vertexTargets.containsKey(getId(source))) {
			addSourceVertex(source);
		}
		
		return targetTrns.get(getId(source)+"$"+getId(target));
	}
	
	private String replaceTrueFalse(String str)
	{
		if(str == null) {
			return "INVALID";
		}
		String temp;
		temp = str.replace("true", "1\'bl").replace("True", "1\'bl").replace("false", "1\'b0")
				.replace("False", "1\'b0");

		return temp;
	}

	public String polishStatement(String str, String fileId)
	{
		String stmnt = replaceTrueFalse(str.trim());
		stmnt = stmnt.replace(" &&", "&&").replace("&& ", "&&").replace("&&", "\n").
				replace(";", "\n").replace("\n", ";").replace(";;", ";");
		
		return stmnt;
	}
	
	private State getLastStateForTranstion(Transition trn)
	{
		Vertex vertex = trn.getSource();
		if(vertex instanceof State) {
			return ((State) vertex);
		}
		else {
			for(Transition tr : ((Pseudostate)vertex).getIncomings()) {
				return getLastStateForTranstion(tr);
			}
		}
		return null;
	}
	
	private String getId(Object obj)
	{
		return String.valueOf(obj.hashCode());
	}
	public String prepareRequirementFile(Model model)
	{
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("@@@@@@@@@@@@ Getting Requirements @@@@@@@@@@@@");
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		List<Constraint> constraints = new ArrayList<Constraint>(model.getOwnedRules());
		System.out.println("constraints: "+constraints);
		String reqdata="";
		for(int i=0;i<constraints.size();i++)
		{
			OpaqueExpression opaq= (OpaqueExpression) constraints.get(i).getOwnedElements().get(0);
			 String reqvalue=opaq.stringValue();
			 System.out.println("reqvalue:"+reqvalue);
			 if(reqvalue.contains("property") && reqvalue.contains("type"))
			 {	String reqproperty="";
				 String reqtext="";
				 String[] reqvaluearr=reqvalue.split("\\r?\\n");
				 System.out.println(""+reqvaluearr[0].substring(11));
				 String property=reqvaluearr[0].substring(11);
				 reqtext=reqtext.concat("/*\nReqID: "+constraints.get(i).getName()+"\nMutex: "+property+"\n*/\n");
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
					 if(property.contains(" equal to "))
					 {
						 property=property.replaceAll(" equal to ", " == ");
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
				 System.out.println(""+reqvaluearr[1].substring(7));
				 String reqtype=reqvaluearr[1].substring(7);
				 if(reqtype.equalsIgnoreCase("Possibly"))
				 {
					 reqtext=reqtext.concat("E<> "+reqproperty+";");
					// TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"E<> "+reqproperty);
				 }else if(reqtype.equalsIgnoreCase("Invariantly"))
				 {
					 reqtext=reqtext.concat("A[] "+reqproperty+";");
					 //TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"A[] "+reqproperty);
				 }else if(reqtype.equalsIgnoreCase("Potentially always"))
				 {
					 reqtext=reqtext.concat("E[] "+reqproperty+";");
					 //TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"E[] "+reqproperty);
				 }else if(reqtype.equalsIgnoreCase("Eventually "))
				 {
					 reqtext=reqtext.concat("A<> "+reqproperty+";");
					 //TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+"A<> "+reqproperty);
				 }else
				 {
					 reqtext=reqtext.concat(""+reqproperty+";");
					 //TestingFile.reqText.add(model.getName().concat("_Req "+i)+"@"+orignalreqtext+"@"+reqproperty);
				 }
				 System.out.println("print @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+reqtext);
				 reqdata=reqdata.concat(reqtext);
				 System.out.println("printing req: "+reqdata);
			 }
			
			
		}
		
		System.out.println("@@@@@@@@@@@@@@@@ends time: "+System.currentTimeMillis());	
		return reqdata;
	}
	
	
	public void getTimedAutomataFile( ) throws IOException
	{
		System.out.println("Getting Timed Automata Files");
		System.out.println("Timed Automata File: "+timedAutomataFileName);
		System.out.println("Timed Query File: "+propertyFileName);
		File source = new File("C:\\Users\\AamirNaeem\\Desktop\\target\\Arbitor.xta");
		File dest = new File("C:\\Users\\AamirNaeem\\Desktop\\final_target_moves\\ArbitorModel.xta");
		FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	}
	public void getTimedQueryFile() throws IOException
	{
		System.out.println("Getting Timed Automata Files");
		File source = new File("C:\\Users\\AamirNaeem\\Desktop\\target\\Arbitor.q");
		File dest = new File("C:\\Users\\AamirNaeem\\Desktop\\final_target_moves\\ArbitorModel.q");
		FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	}
	public static void main(String[] args) throws IOException
	{
		//System.out.println(window.getPropsFileName());
		System.out.println("Timed Automata File: "+timedAutomataFileName);
		System.out.println("Timed Query File: "+propertyFileName);
		JavaServices js=new JavaServices();
		js.getTimedAutomataFile();
		js.getTimedQueryFile();
	}
	
	
	
}