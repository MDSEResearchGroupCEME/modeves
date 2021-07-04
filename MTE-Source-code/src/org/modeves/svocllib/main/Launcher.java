/**
 * 
 */
package org.modeves.svocllib.main;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.modeves.svocllib.main.WinMain;
import org.modeves.svocllib.services.JavaServices;
import org.modeves.svocllib.services.UML2Services;

/**
 * @author Yasir
 *
 */
public class Launcher
{
	public static final long SVOCL_BUILD = 20180717L;
	public static final String SVOCL_ENGINE_VER = "1.0";
	
	public static final int CLEAR = -1;
	public static final int CREATING = 0;
	public static final int CREATED = 1;
	public static final int CANNOT_OPEN = 2;
	public static final int NOT_CREATED = 3;
	public static final int CREATED_WITH_ERRORS = 4;
	public static final int MODEL_NOT_FOUND = 5;
	public static final int MODEL_NOT_SUPPORTED = 6;
	public static final int ILLEGAL_FILE_NAME = 7;
	public static final int FOLDER_DOES_NOT_EXIST = 8;
	public static final int EMPTY_REQUIRED_FIELDS = 9;

	private int status = CLEAR;
	private int propsGenStatus = CLEAR;
	private int behGenStatus = CLEAR;
	private WinMain window;
	private static Launcher instance;
	private Random random = new Random();
	
	private String propsPath;
	private String behPath;
	private String propsTempFileName;
	private String behTempFileName;
	private String propertyTempFileName;
	private String timedAutomataTempFileName;
	private String destFolder;
	
	private String propsGenStatusMsg = "";
	private String behGenStatusMsg = "";
	
	public static Launcher getInstance() {
		if(instance == null) {
			instance = new Launcher();
		}
		return instance;
	}
	
	private Launcher() {
	}

	public static void main(String[] args)
	{
		Launcher launcher = getInstance();
		launcher.launch();
		
		args = new String[2];
		args[0] = "C:/Users/Yasir/eclipse/workspace/current-papyrus-traffic/model.uml";
		args[1] = "D:/svtests";
		Generate.main(args);
		//window = new WinMain();
	}

	/**
	 * @param args
	 */
	public void launch()
	{
		if(window == null) {
			System.out.println("-----  Engine version: "+SVOCL_ENGINE_VER+" (SVOCL build: "+SVOCL_BUILD+")  -----");
			window = new WinMain(this);
		}
	}

	@SuppressWarnings("static-access")
	void start(String[] args)
	{
		
		JavaServices.reset();
		JavaServices.setSelectionMethod(window.SelectionMethod);
		System.out.println("Selection Method: "+JavaServices.getSelectionMethod());
		if(window.isGenerateProps()) {
			propsTempFileName = "tsv~"+random.nextInt(100000);
			JavaServices.setAssertFileName(propsTempFileName);
		}
		if(window.isGenerateBehavior()) {
			behTempFileName = "tsv~"+random.nextInt(100000);
			JavaServices.setBehFileName(behTempFileName);
		}
		if(window.isGenerateProperty()) {
			propertyTempFileName = "tsv~"+random.nextInt(100000);
			JavaServices.setPropertyFileName(propertyTempFileName);
		}
		if(window.isGenerateTimedAutomata()) {
			timedAutomataTempFileName = "tsv~"+random.nextInt(100000);
			JavaServices.setTimedAutomataFileName(timedAutomataTempFileName);
		}
		for(int i=0;i<args.length;i++)
		{
		System.out.println("Args in Main: "+args[i]);
		}
		UML2Services.path=window.getDestFolder().replace('\\', '/')+"/"+propertyTempFileName;
		System.out.println("Assert Files Names: "+JavaServices.getAssertFileName());
		System.out.println("Beh Files Names: "+JavaServices.getBehFileName());
		System.out.println("Timed Files Names: "+JavaServices.getTimedAutomataFileName());
		System.out.println("Property Files Names: "+JavaServices.getPropertyFileName());
		Generate.main(args);
		System.out.println("After min");
		destFolder = window.getDestFolder().replace('\\', '/')+"/";
		
		TextRefiner refiner = new TextRefiner();
		System.out.println("before GenerateProps");
		if(window.isGenerateProps()) {
			propsPath = destFolder + window.getPropsFileName();
			File propsFile = new File(destFolder+propsTempFileName);
			if(!propsFile.exists()) {
				setStatus(NOT_CREATED, false);
			}
			else {
				refiner.refinePropsFileText(propsFile, propsPath);
				if(window.isOpenFiles()) {
					try {
						Runtime.getRuntime().exec("cmd /c start "+propsPath);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						setStatus(CANNOT_OPEN, false);
						e1.printStackTrace();
					}
				}
			}
			if(!hasStatus(Launcher.CREATED_WITH_ERRORS, false) && !hasStatus(Launcher.CANNOT_OPEN, false)) {
				setStatus(Launcher.CREATED, false);
			}
		}
		if(window.isGenerateProperty()) {
			propsPath = destFolder + window.getPropertyFileName();
			File propsFile = new File(destFolder+propertyTempFileName);
			if(!propsFile.exists()) {
				setStatus(NOT_CREATED, false);
			}
			else {
				refiner.refinePropertyFileText(propsFile, propsPath);
				if(window.isOpenFiles()) {
					try {
						Runtime.getRuntime().exec("cmd /c start "+propsPath);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						setStatus(CANNOT_OPEN, false);
						e1.printStackTrace();
					}
				}
			}
			if(!hasStatus(Launcher.CREATED_WITH_ERRORS, false) && !hasStatus(Launcher.CANNOT_OPEN, false)) {
				setStatus(Launcher.CREATED, false);
			}
		}
		System.out.println("before GenerateBehav");
		if(window.isGenerateBehavior()) {
			behPath = destFolder + window.getBehaviorFileName();
			File behFile = new File(destFolder+behTempFileName);
			if(!behFile.exists()) {
				setStatus(NOT_CREATED, true);
			}
			else {
				refiner.refineBehaviorFileText(behFile, behPath);
				if(window.isOpenFiles()){
					try {
						Runtime.getRuntime().exec("cmd /c start "+behPath);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						setStatus(CANNOT_OPEN, true);
						e1.printStackTrace();
					}
				}
			}
			if(!hasStatus(Launcher.CREATED_WITH_ERRORS, true) && !hasStatus(Launcher.CANNOT_OPEN, true)) {
				setStatus(Launcher.CREATED, true);
			}
		}
		if(window.isGenerateTimedAutomata()) {
			behPath = destFolder + window.getTimedAutomataFileName();
			File behFile = new File(destFolder+timedAutomataTempFileName);
			if(!behFile.exists()) {
				setStatus(NOT_CREATED, true);
			}
			else {
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@"+window.getInputPath());
				refiner.refineTimedAutomataFileText(behFile, behPath);
				
				if(window.isOpenFiles()){
					try {
						Runtime.getRuntime().exec("cmd /c start "+behPath);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						setStatus(CANNOT_OPEN, true);
						e1.printStackTrace();
					}
				}
			}
			if(!hasStatus(Launcher.CREATED_WITH_ERRORS, true) && !hasStatus(Launcher.CANNOT_OPEN, true)) {
				setStatus(Launcher.CREATED, true);
			}
		}
	}
	
	private String makeStatusMsg(String fileName, int status)
	{
		String msg = null;
		
		switch (status) {
		case CLEAR:
			msg = "";
			break;
		case CREATED:
			msg = fileName+" successfully generated.";
			break;
		case CANNOT_OPEN:
			msg = fileName+" successfully generated but cannot be opened.";
			break;
		case NOT_CREATED:
			msg = fileName+" could NOT be generated.";
			break;
		case CREATED_WITH_ERRORS:
			msg = fileName+" generated with errors.";
			break;
		}
		return msg;
	}

	@SuppressWarnings("static-access")
	public void setStatus(int status, boolean behavior)
	{
		if(window == null) {
			return;
		}
		
		if(behavior) {
			behGenStatus = status;
			if(window.SelectionMethod.equalsIgnoreCase("SystemVerilog"))
			{
				behGenStatusMsg = makeStatusMsg(window.getBehaviorFileName(), status);
			}else if(window.SelectionMethod.equalsIgnoreCase("TimedAutomata"))
			{
				behGenStatusMsg = makeStatusMsg(window.getTimedAutomataFileName(), status);
			}else if(window.SelectionMethod.equalsIgnoreCase("Both"))
			{
				behGenStatusMsg = makeStatusMsg(window.getBehaviorFileName(), status)+"\n"+makeStatusMsg(window.getTimedAutomataFileName(), status);
			}
			
		}
		else {
			propsGenStatus = status;
			
			if(window.SelectionMethod.equalsIgnoreCase("SystemVerilog"))
			{
				propsGenStatusMsg = makeStatusMsg(window.getPropsFileName(), status);
				
			}else if(window.SelectionMethod.equalsIgnoreCase("TimedAutomata"))
			{
				propsGenStatusMsg = makeStatusMsg(window.getPropertyFileName(), status);
			}else if(window.SelectionMethod.equalsIgnoreCase("Both"))
			{
				propsGenStatusMsg = makeStatusMsg(window.getPropsFileName(), status)+"\n"+makeStatusMsg(window.getPropertyFileName(), status);
			}
		}
		
		window.setMessage((propsGenStatusMsg+"\n"+behGenStatusMsg).trim());
	}
	
	public void setStatus(int status)
	{
		if(this.status == status) {
			return;
		}

		this.status = status;
		String msg;
		switch (status) {
		case CLEAR:
			msg = "";
			propsGenStatus = behGenStatus  = CLEAR;
			propsGenStatusMsg = behGenStatusMsg = "";
			break;
		case CREATING:
			msg = "Generating...";
			break;
		case CREATED_WITH_ERRORS:
			msg = "Files generated with errors.";
			break;
		case MODEL_NOT_FOUND:
			msg = "Input model not found. Please check the path and name.";
			break;
		case MODEL_NOT_SUPPORTED:
			msg = "Input model is not supported";
			break;
		case ILLEGAL_FILE_NAME:
			msg = "File name contains an illegal character. Please check the name.";
			break;
		case FOLDER_DOES_NOT_EXIST:
			msg = "Output folder does not exists. Please enter a valid location.";
			break;
		case EMPTY_REQUIRED_FIELDS:
			msg = "Some required fields are empty. Please enter required values.";
			break;
		default:
			msg = "";
		}
		
		window.setMessage(msg);
	}
	
	public boolean hasStatus(int status, boolean isBehavior)
	{
		if(isBehavior) {
			return (status == behGenStatus);
		}
		else {
			return (status == propsGenStatus);
		}
	}

}
