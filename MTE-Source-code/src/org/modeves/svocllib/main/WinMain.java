package org.modeves.svocllib.main;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;

import java.awt.SystemColor;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Insets;

import org.modeves.svocllib.main.Launcher;
import java.awt.Panel;
import java.awt.FlowLayout;

/**
 * 
 * @author Yasir
 *
 */
public class WinMain
{
	private JFrame frame;
	private JTextField inputFileField;
	private JTextField behaviorFileField;
	private JTextField taFileField;
	private JTextField propertyFileField;
	private JTextField destField;
	private JCheckBox openFileCheck;
	private JCheckBox behaviorCheck;
	private JCheckBox propsCheck;
	private JCheckBox chckbxTimedAutomataCode;
	private JComboBox<String> behaviorTypeCombo;
	private JComboBox<String> taTypeCombo;
	private JComboBox<String> propertyTypeCombo;
	private JComboBox<String> propsTypeCombo;
	private JComboBox<String> selectionpropsTypeCombo;
	private JTextArea msgTextArea;
	private Launcher launcher;
	private JTextField propsFileField;
	private JButton generateBtn;
	private JCheckBox propertyCheck;
	public static String SelectionMethod;


	/**
	 * Create the application.
	 */
	public WinMain(Launcher launcher)
	{
		this.launcher = launcher;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					frame = new JFrame();
					frame.setTitle("MODEVES Transformation Engine");
					frame.setBounds(100, 100, 795, 497);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					initialize();
					frame.setVisible(true);
					inputFileField.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		JPanel centerPanel = new JPanel();
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(null);
		centerPanel.requestFocusInWindow();
		
		JButton aboutButton = new JButton("i");
		aboutButton.setForeground(new Color(51, 153, 0));
		aboutButton.setRequestFocusEnabled(false);
		aboutButton.setOpaque(false);
		aboutButton.setBackground(centerPanel.getBackground());
		aboutButton.setMargin(new Insets(2, 2, 2, 2));
		aboutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "MODEVES Transformation Engine Version: "+
						Launcher.SVOCL_ENGINE_VER+"\nSVOCL Build: "+Launcher.SVOCL_BUILD, "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aboutButton.setToolTipText("About");
		aboutButton.setFont(new Font("Bookman Old Style", Font.BOLD, 18));
		aboutButton.setBounds(678, 0, 26, 23);
		centerPanel.add(aboutButton);
		
		JLabel inputFileLabel = new JLabel("Input Model:");
		inputFileLabel.setFont(new Font("Thoma", Font.PLAIN, 14));
		inputFileLabel.setBounds(63, 26, 90, 20);
		centerPanel.add(inputFileLabel);
	
		JLabel inputFileLabel1 = new JLabel("Select Method:");
		inputFileLabel1.setFont(new Font("Thoma", Font.PLAIN, 14));
		inputFileLabel1.setBounds(63, 74, 96, 27);
		centerPanel.add(inputFileLabel1);
		
		generateBtn = new JButton("Generate");
		generateBtn.setFont(new Font("Thoma", Font.PLAIN, 12));
		generateBtn.setBounds(565, 270, 96, 23);
		centerPanel.add(generateBtn);
		
		inputFileField = new JTextField();
		inputFileField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		inputFileField.setBounds(163, 25, 378, 24);
		centerPanel.add(inputFileField);
		inputFileField.setColumns(10);
		
		JButton inputBrowseBtn = new JButton("Browse...");
		inputBrowseBtn.setFont(new Font("Thoma", Font.PLAIN, 12));
		inputBrowseBtn.setBounds(551, 26, 97, 23);
		centerPanel.add(inputBrowseBtn);
		
		JButton destBrowseBtn = new JButton("Browse...");
		destBrowseBtn.setFont(new Font("Thoma", Font.PLAIN, 12));
		destBrowseBtn.setBounds(564, 215, 97, 23);
		centerPanel.add(destBrowseBtn);
		
		destField = new JTextField();
		destField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		destField.setColumns(10);
		destField.setBounds(194, 214, 347, 24);
		centerPanel.add(destField);
		
		JLabel destFolderLabel = new JLabel("Destination Folder:");
		destFolderLabel.setFont(new Font("Thoma", Font.PLAIN, 14));
		destFolderLabel.setBounds(63, 218, 121, 14);
		centerPanel.add(destFolderLabel);
	
		JButton resetBtn = new JButton("Reset");
		resetBtn.setFont(new Font("Thoma", Font.PLAIN, 12));
		resetBtn.setBounds(458, 270, 97, 23);
		centerPanel.add(resetBtn);
		
		JPanel msgPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) msgPanel.getLayout();
		flowLayout.setVgap(2);
		flowLayout.setHgap(2);
		msgPanel.setFont(new Font("Thoma", Font.PLAIN, 14));
		msgPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Status", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Vernada", Font.PLAIN, 14), Color.BLACK));
		msgPanel.setBackground(SystemColor.control);
		msgPanel.setPreferredSize(new Dimension(40, 140));
		msgPanel.setBounds(38, 305, 623, 108);
		centerPanel.add(msgPanel);
		
		JButton openFolderBtn = new JButton("Open Folder");
		openFolderBtn.setFont(new Font("Thoma", Font.PLAIN, 12));
		openFolderBtn.setBounds(446, 424, 109, 23);
		centerPanel.add(openFolderBtn);
		
		JButton closeBtn = new JButton("Close");
		closeBtn.setFont(new Font("Thoma", Font.PLAIN, 12));
		closeBtn.setBounds(564, 424, 97, 23);
		centerPanel.add(closeBtn);
		
		openFileCheck = new JCheckBox("Open Generated Files");
		openFileCheck.setSelected(true);
		openFileCheck.setFont(new Font("Thoma", Font.PLAIN, 14));
		openFileCheck.setBounds(194, 269, 180, 23);
		centerPanel.add(openFileCheck);
		
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputFileField.setText("");
				propsCheck.setSelected(true);
				behaviorCheck.setSelected(true);
				propsFileField.setText("");
				propsTypeCombo.setSelectedIndex(0);
				selectionpropsTypeCombo.setSelectedIndex(0);
				behaviorFileField.setText("");
				behaviorTypeCombo.setSelectedIndex(0);
				destField.setText("");
				openFileCheck.setSelected(true);
				msgTextArea.setText("");
			}
		});
		
		inputBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("uml", "uml"));
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("xmi", "xmi"));
				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					inputFileField.setText(file.getPath());
				}
			}
		});
		
		destBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  destField.setText(file.getPath());
				  // load from file
				}
			}
		});
		
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		
		openFolderBtn.addActionListener(new ActionListener() {
			String path = null;
			public void actionPerformed(ActionEvent event) {
				if(propsCheck.isSelected()) {
					path = getDestFolder()+"\\"+getPropsFileName();
				}
				else {
					path = getDestFolder()+"\\"+getBehaviorFileName();
				}
				
				if(path != null && (new File(path)).exists()) {
					try {
						Runtime.getRuntime().exec("explorer.exe /select," + path);
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
			}
		});
		selectionpropsTypeCombo = new JComboBox<String>();
		selectionpropsTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"SystemVerilog", "TimedAutomata", "Both"}));
		selectionpropsTypeCombo.setFont(new Font("Dialog", Font.PLAIN, 13));
		selectionpropsTypeCombo.setBounds(88, 112, 167, 29);
		centerPanel.add(selectionpropsTypeCombo);
		
		Panel panel = new Panel();
		panel.setBounds(270, 55, 499, 142);
		centerPanel.add(panel);
		panel.setLayout(null);
			propsCheck = new JCheckBox("Assertions Code");
			propsCheck.setBounds(6, 5, 129, 27);
			panel.add(propsCheck);
			propsCheck.setSelected(true);
			propsCheck.setFont(new Font("Dialog", Font.PLAIN, 14));
		
		
		propsCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean status = propsCheck.isSelected();
				//propsLabel.setEnabled(status);
				propsFileField.setEnabled(status);
				propsTypeCombo.setEnabled(status);
				refreshGenerateBtnStatus();
			}
		});
		
		JLabel propsLabel = new JLabel("File Name:");
		propsLabel.setBounds(185, 9, 67, 19);
		panel.add(propsLabel);
		propsLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		
		propsFileField = new JTextField();
		propsFileField.setBounds(272, 7, 162, 24);
		panel.add(propsFileField);
		propsFileField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		propsFileField.setColumns(10);
		
		propsFileField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void removeUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void insertUpdate(DocumentEvent e) {
				textChangedAction();
			}
			
			public void textChangedAction() {
				refreshGenerateBtnStatus();
			}
		});
		
		propsTypeCombo = new JComboBox<String>();
		propsTypeCombo.setBounds(437, 7, 48, 24);
		panel.add(propsTypeCombo);
		propsTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {".sv", ".txt"}));
		propsTypeCombo.setFont(new Font("Dialog", Font.PLAIN, 13));
		
		behaviorCheck = new JCheckBox("Behavior Code");
		behaviorCheck.setBounds(6, 35, 129, 27);
		panel.add(behaviorCheck);
		behaviorCheck.setSelected(true);
		behaviorCheck.setFont(new Font("Dialog", Font.PLAIN, 14));
		
		JLabel behaviorLabel = new JLabel("File Name:");
		behaviorLabel.setBounds(185, 39, 67, 19);
		panel.add(behaviorLabel);
		behaviorLabel.setFont(new Font("Thoma", Font.PLAIN, 14));
		
		behaviorFileField = new JTextField();
		behaviorFileField.setBounds(272, 37, 162, 24);
		panel.add(behaviorFileField);
		behaviorFileField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		behaviorFileField.setColumns(10);
		
		behaviorTypeCombo = new JComboBox<String>();
		behaviorTypeCombo.setBounds(437, 37, 48, 24);
		panel.add(behaviorTypeCombo);
		behaviorTypeCombo.setFont(new Font("Thoma", Font.PLAIN, 13));
		behaviorTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {".sv", ".txt"}));
		
		chckbxTimedAutomataCode = new JCheckBox("Timed Automata Code");
		chckbxTimedAutomataCode.setBounds(6, 67, 175, 23);
		panel.add(chckbxTimedAutomataCode);
		chckbxTimedAutomataCode.setSelected(false);
		chckbxTimedAutomataCode.setFont(new Font("Dialog", Font.PLAIN, 14));
		chckbxTimedAutomataCode.setEnabled(false);
		
		JLabel taLabel = new JLabel("File Name:");
		taLabel.setBounds(185, 69, 67, 19);
		panel.add(taLabel);
		taLabel.setFont(new Font("Thoma", Font.PLAIN, 14));
		taLabel.setEnabled(false);
		
		taFileField = new JTextField();
		taFileField.setBounds(272, 67, 162, 24);
		panel.add(taFileField);
		taFileField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		taFileField.setColumns(10);
		taFileField.setEnabled(false);
		
		taTypeCombo = new JComboBox<String>();
		taTypeCombo.setBounds(437, 67, 48, 24);
		panel.add(taTypeCombo);
		taTypeCombo.setFont(new Font("Thoma", Font.PLAIN, 13));
		taTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {".xta"}));
		taTypeCombo.setEnabled(false);
		
		propertyCheck = new JCheckBox("Property Code");
		propertyCheck.setBounds(6, 95, 129, 23);
		panel.add(propertyCheck);
		propertyCheck.setSelected(false);
		propertyCheck.setFont(new Font("Dialog", Font.PLAIN, 14));
		propertyCheck.setEnabled(false);
		
		JLabel propertyLabel = new JLabel("File Name:");
		propertyLabel.setBounds(185, 97, 67, 19);
		panel.add(propertyLabel);
		propertyLabel.setFont(new Font("Thoma", Font.PLAIN, 14));
		propertyLabel.setEnabled(false);
		
		propertyFileField = new JTextField();
		propertyFileField.setBounds(272, 102, 162, 24);
		panel.add(propertyFileField);
		propertyFileField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		propertyFileField.setColumns(10);
		propertyFileField.setEnabled(false);
		
		propertyTypeCombo = new JComboBox<String>();
		propertyTypeCombo.setBounds(437, 102, 48, 24);
		panel.add(propertyTypeCombo);
		propertyTypeCombo.setFont(new Font("Thoma", Font.PLAIN, 13));
		propertyTypeCombo.setModel(new DefaultComboBoxModel<String>(new String[] {".q"}));
		propertyTypeCombo.setEnabled(false);
		
		msgTextArea = new JTextArea();
		msgTextArea.setBounds(48, 326, 600, 93);
		centerPanel.add(msgTextArea);
		msgTextArea.setFocusable(false);
		msgTextArea.setForeground(new Color(47, 79, 79));
		msgTextArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		msgTextArea.setBackground(SystemColor.control);
		msgTextArea.setEditable(false);
		msgTextArea.setPreferredSize(new Dimension(583, 55));
		behaviorFileField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void removeUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void insertUpdate(DocumentEvent e) {
				textChangedAction();
			}
			
			public void textChangedAction() {
				refreshGenerateBtnStatus();
			}
		});
		behaviorCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean status = behaviorCheck.isSelected();
				behaviorLabel.setEnabled(status);
				behaviorFileField.setEnabled(status);
				behaviorTypeCombo.setEnabled(status);
				refreshGenerateBtnStatus();
			}
		});
		selectionpropsTypeCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Text Changes "+selectionpropsTypeCombo.getSelectedItem());
				if(selectionpropsTypeCombo.getSelectedItem()=="SystemVerilog")
				{
					behaviorCheck.setEnabled(true);
					behaviorFileField.setEnabled(true);
					behaviorLabel.setEnabled(true);
					behaviorTypeCombo.setEnabled(true);
					propsTypeCombo.setEnabled(true);
					propsLabel.setEnabled(true);
					propsCheck.setEnabled(true);
					propsFileField.setEnabled(true);
					behaviorCheck.setSelected(true);
					propsCheck.setSelected(true);
					
					chckbxTimedAutomataCode.setEnabled(false);
					taFileField.setEnabled(false);
					taTypeCombo.setEnabled(false);
					taLabel.setEnabled(false);
					propertyLabel.setEnabled(false);
					propertyCheck.setEnabled(false);
					propertyFileField.setEnabled(false);
					propertyTypeCombo.setEnabled(false);
					chckbxTimedAutomataCode.setSelected(false);
					propertyCheck.setSelected(false);
				}else if(selectionpropsTypeCombo.getSelectedItem()=="TimedAutomata")
				{
					chckbxTimedAutomataCode.setEnabled(true);
					taFileField.setEnabled(true);
					taTypeCombo.setEnabled(true);
					taLabel.setEnabled(true);
					propertyLabel.setEnabled(true);
					propertyCheck.setEnabled(true);
					propertyFileField.setEnabled(true);
					propertyTypeCombo.setEnabled(true);
					chckbxTimedAutomataCode.setSelected(true);
					propertyCheck.setSelected(true);
	
					behaviorCheck.setEnabled(false);
					behaviorFileField.setEnabled(false);
					behaviorLabel.setEnabled(false);
					behaviorTypeCombo.setEnabled(false);
					propsTypeCombo.setEnabled(false);
					propsLabel.setEnabled(false);
					propsCheck.setEnabled(false);
					propsFileField.setEnabled(false);
					behaviorCheck.setSelected(false);
					propsCheck.setSelected(false);
				}else if(selectionpropsTypeCombo.getSelectedItem()=="Both")
				{
					chckbxTimedAutomataCode.setEnabled(true);
					taFileField.setEnabled(true);
					taTypeCombo.setEnabled(true);
					taLabel.setEnabled(true);
					propertyLabel.setEnabled(true);
					propertyCheck.setEnabled(true);
					propertyFileField.setEnabled(true);
					propertyTypeCombo.setEnabled(true);
					chckbxTimedAutomataCode.setSelected(true);
					propertyCheck.setSelected(true);
	
					behaviorCheck.setEnabled(true);
					behaviorFileField.setEnabled(true);
					behaviorLabel.setEnabled(true);
					behaviorTypeCombo.setEnabled(true);
					propsTypeCombo.setEnabled(true);
					propsLabel.setEnabled(true);
					propsCheck.setEnabled(true);
					propsFileField.setEnabled(true);
					behaviorCheck.setSelected(true);
					propsCheck.setSelected(true);
				
				}
				refreshGenerateBtnStatus();
				// TODO Auto-generated method stub
				
			}
		});
		
		destField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void removeUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void insertUpdate(DocumentEvent e) {
				textChangedAction();
			}
			
			public void textChangedAction() {
				refreshGenerateBtnStatus();
			}
		});
		
		inputFileField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void removeUpdate(DocumentEvent e) {
				textChangedAction();
			}
			public void insertUpdate(DocumentEvent e) {
				textChangedAction();
			}
			
			public void textChangedAction() {
				String fname = inputFileField.getText();
				if (!fname.isEmpty()){
					int ind = fname.lastIndexOf('\\');
					if(ind != -1) {
						fname = fname.substring(ind+1);
					}
					ind = fname.lastIndexOf('.');
					if(ind != -1) {
						fname = fname.substring(0, ind);
					}
					propsFileField.setText(fname+"_asserts");
					behaviorFileField.setText(fname+"_behavior");
					propertyFileField.setText(fname+"_properties");
					taFileField.setText(fname+"_timed_automata");
				}
				refreshGenerateBtnStatus();
			}
		});
		
		generateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean launch = true;
				if(getInputPath().trim().isEmpty() || getDestFolder().trim().isEmpty() 
						|| (propsCheck.isSelected() && propsFileField.getText().trim().isEmpty())
						|| (behaviorCheck.isSelected() && behaviorFileField.getText().trim().isEmpty())
						|| (propertyCheck.isSelected() && propertyFileField.getText().trim().isEmpty())
						|| (chckbxTimedAutomataCode.isSelected() && taFileField.getText().trim().isEmpty())) {
					launcher.setStatus(Launcher.EMPTY_REQUIRED_FIELDS);
					launch = false;
				}
				else {
					String propsFileName="";
					String behaviorFileName="";
					String propertyFileName="";
					String timedAutomataFileName="";
					if(selectionpropsTypeCombo.getSelectedIndex()==0)
					{
						SelectionMethod="SystemVerilog";
						propsFileName = getPropsFileName();
						behaviorFileName = getBehaviorFileName();
					}else if(selectionpropsTypeCombo.getSelectedIndex()==1)
					{
						SelectionMethod="TimedAutomata";
						propertyFileName = getPropertyFileName();
						timedAutomataFileName = getTimedAutomataFileName();
					}else if(selectionpropsTypeCombo.getSelectedIndex()==2)
					{
						SelectionMethod="Both";
						propsFileName = getPropsFileName();
						behaviorFileName = getBehaviorFileName();
						propertyFileName = getPropertyFileName();
						timedAutomataFileName = getTimedAutomataFileName();
					}
					
					if(!(new File (getInputPath().replace('\\', '/')).exists())) {
						launcher.setStatus(Launcher.MODEL_NOT_FOUND);
						launch = false;
					}
					else if(!(getInputPath().endsWith("uml") || getInputPath().endsWith("UML") ||
							getInputPath().endsWith("xmi") || getInputPath().endsWith("XMI")) ) {
						launcher.setStatus(Launcher.MODEL_NOT_SUPPORTED);
						launch = false;
					}
					else if((propsCheck.isSelected() && !isAValidFileName(propsFileName)) ||
							(behaviorCheck.isSelected() && !isAValidFileName(behaviorFileName))) {
						launcher.setStatus(Launcher.ILLEGAL_FILE_NAME);
						launch = false;
					}
					else if (!(new File (getDestFolder().replace('\\', '/')).exists())) {
						launcher.setStatus(Launcher.FOLDER_DOES_NOT_EXIST);
						launch = false;
					}
					else {
						if(propsCheck.isSelected()) {
							launch = launch && getFileStatus(propsFileName);
						}
						if(behaviorCheck.isSelected()) {
							launch = launch && getFileStatus(behaviorFileName);
						}
						if(propertyCheck.isSelected()) {
							launch = launch && getFileStatus(propertyFileName);
						}
						if(chckbxTimedAutomataCode.isSelected()) {
							launch = launch && getFileStatus(timedAutomataFileName);
						}
						if(!launch) {
							launcher.setStatus(Launcher.CLEAR);
						}
					}
					if (launch){
						launcher.setStatus(Launcher.CLEAR);
						launcher.setStatus(Launcher.CREATING);
						String args[] = new String[2];
						args[0] = getInputPath().replace('\\', '/');
						args[1] = getDestFolder().replace('\\', '/');
						System.out.println("Args1: "+args[0]);
						System.out.println("Args2: "+args[1]);
						launcher.start(args);
					}
				}
			}
		});
		
		refreshGenerateBtnStatus();
	}

	private void refreshGenerateBtnStatus()
	{
		if(	(!inputFileField.getText().trim().isEmpty()) && (!destField.getText().trim().isEmpty()) &&
				(propsCheck.isSelected() || behaviorCheck.isSelected() || propertyCheck.isSelected() || chckbxTimedAutomataCode.isSelected()) ) {
			if((propsCheck.isSelected() && propsFileField.getText().trim().isEmpty()) ||
				(behaviorCheck.isSelected() && behaviorFileField.getText().trim().isEmpty()) && (propertyCheck.isSelected() && propertyFileField.getText().trim().isEmpty()) && (chckbxTimedAutomataCode.isSelected() && taFileField.getText().trim().isEmpty())){
				generateBtn.setEnabled(false);
			}
			else {
				generateBtn.setEnabled(true);
			}
		}
		else {
			generateBtn.setEnabled(false);
		}
	}
	
	private boolean isAValidFileName(String name)
	{
		if(name.contains("/") ||name.contains("\\") || name.contains("*") ||
				name.contains("?") ||name.contains("<") ||name.contains(">") ||
				name.contains("|") ||name.contains(":")) {
			return false;
		}
		
		return true;
	}
	
	private boolean fileExistsInDest(String name)
	{
		if(new File (getDestFolder().replace('\\', '/')+"/"+name).exists()) {
			return true;
		}

		return false;
	}
	
	private boolean getFileStatus(String fileName)
	{
		boolean status = true;
		if(fileExistsInDest(fileName)) {
			int op = JOptionPane.showConfirmDialog(frame,
			  "A file with name \""+fileName+ "\" already exists.\nDo you want to replace it?", "",
			  JOptionPane.YES_NO_OPTION);
			if(op == JOptionPane.YES_OPTION) {
				if(fileExistsInDest(fileName)) {
					if(!(new File(getDestFolder().replace('\\', '/')+"/"+fileName)).delete()) {
						status = false;
						JOptionPane.showMessageDialog(frame,
								fileName+" cannot be replaced. If file is open, close it first.",
						   "", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else {
				status = false;
			}
		}
		return status;
	}
	
	public String getInputPath()
	{
		return inputFileField.getText();
	}

	public String getPropsFileName()
	{
		return propsFileField.getText().trim()+propsTypeCombo.getSelectedItem().toString();
	}
	public String getPropertyFileName()
	{
		return propertyFileField.getText().trim()+propertyTypeCombo.getSelectedItem().toString();
	}
	public String getTimedAutomataFileName()
	{
		return taFileField.getText().trim()+taTypeCombo.getSelectedItem().toString();
	}

	public String getBehaviorFileName()
	{
		return behaviorFileField.getText().trim()+behaviorTypeCombo.getSelectedItem().toString();
	}
	
	public String getDestFolder()
	{
		return destField.getText();
	}

	public boolean isOpenFiles()
	{
		return openFileCheck.isSelected();
	}

	public boolean isGenerateProps()
	{
		return propsCheck.isSelected();
	}
	public boolean isGenerateProperty()
	{
		return propertyCheck.isSelected();
	}
	public boolean isGenerateTimedAutomata()
	{
		return chckbxTimedAutomataCode.isSelected();
	}
	public boolean isGenerateBehavior()
	{
		return behaviorCheck.isSelected();
	}
	
	public void setMessage(String msg)
	{
		msgTextArea.setText(msg);
		msgTextArea.update(msgTextArea.getGraphics());
	}
}
