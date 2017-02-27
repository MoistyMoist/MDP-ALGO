package simulator;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import simulator.UserInterface.InitialPositionListener;
import arena.Arena;
import arena.FileReaderWriter;

public class UserInterface extends JFrame implements ActionListener {

	private static final String explorePanelName = "Explore Arena";
	private static final String ffpPanelName = "Find Fastest Path";
	private static final long serialVersionUID = 1L;
	private JPanel ctrlPane, contentPane, mapPane, mazePane;
	private JLabel status, timeCounter, coverageUpdate;
	private JButton[][] mapGrids, mazeGrids;
	private JTextField[] exploreTextFields;
	private JTextField ffpTextField;
	private Controller controller;
	
	//###Create simulator###
	public UserInterface() {
		super("MDP Simulator - Arena Exploration & Fastest Path Computation");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		initContentPane(contentPane);
		pack();
	}
	
	private void initContentPane(JPanel contentPane){
		//###Add right panel: Reference map with two control buttons (load / clear)###
		mapPane = new JPanel(new FlowLayout());
		mapPane.setPreferredSize(new Dimension(450, 650));
		JPanel map = new JPanel();
		map.setLayout(new GridLayout(Arena.mapWidth, Arena.mapLength));
		map.setPreferredSize(new Dimension(450, 600));
		mapGrids = new JButton[Arena.mapWidth][Arena.mapLength];
		for(int x = 0; x < Arena.mapWidth; x++){
			for(int y = 0; x < Arena.mapLength; y++){
				mapGrids[x][y] = new JButton();
				mapGrids[x][y].setActionCommand("ToggleObstacleAt " + x + "," + y);
				mapGrids[x][y].setBorder(BorderFactory.createLineBorder(Color.GRAY));
				mapGrids[x][y].setBackground(Color.GREEN);
				map.add(mapGrids[x][y]);
				if((x >= 0 & x <= 2 & y >= 12 & y <= 14) || (y >= 0 & y <= 2 & x >= 17 & x <= 19)){
					mapGrids[x][y].setEnabled(false);
					mapGrids[x][y].setBackground(Color.ORANGE);
					if(x == 1 & y == 13){
						mapGrids[x][y].setText("G");
					}
					else if(x == 18 & y == 1){
						mapGrids[x][y].setText("S");
					}
				}
			}
		}
		loadArenaFromDisk();
		mapPane.add(map);
		JButton loadMap = new JButton("Load");
		loadMap.setActionCommand("LoadMap");
		loadMap.addActionListener(this);
		JButton clearMap = new JButton("Clear");
		clearMap.setActionCommand("ClearMap");
		clearMap.addActionListener(this);
		mapPane.add(loadMap);
		mapPane.add(clearMap);
		contentPane.add(mapPane,  BorderLayout.EAST);
		
		//###Add middle panel: Control panel###
		//###Add control switch (combo box)###
		ctrlPane = new JPanel(new BorderLayout());
		ctrlPane.setBorder(new EmptyBorder(50, 20, 50, 20));
		String comboBoxItems[] = {explorePanelName, ffpPanelName};
		JComboBox mainCtrlSwitch = new JComboBox(comboBoxItems);
		mainCtrlSwitch.setFont(new Font("Tahoma", Font.BOLD, 16));
		mainCtrlSwitch.setEditable(false);
		mainCtrlSwitch.setActionCommand("SwitchCtrl");
		mainCtrlSwitch.addActionListener(this);
		ctrlPane.add(mainCtrlSwitch, BorderLayout.NORTH);
		
		//###Add control panel for exploring###
		JLabel[] exploreCtrlLabels = new JLabel[4];
		exploreTextFields = new JTextField[2];
		exploreCtrlLabels[0] = new JLabel("Robot Initial Position: ");
		exploreCtrlLabels[1] = new JLabel("Speed (steps/sec: ");
		for(int i = 0; i < 2; i++){
			exploreTextFields[i] = new JTextField(10);
		}
		JPanel exploreInputPane = new JPanel(new GridLayout(2, 2));
		exploreInputPane.add(exploreCtrlLabels[0]);
		exploreCtrlLabels[0].setFont(new Font("Tahoma", Font.PLAIN, 14));
		exploreInputPane.add(exploreTextFields[0]);
		exploreTextFields[0].setText("2, 2");
		exploreTextFields[0].setFont(new Font("Tahoma", Font.PLAIN, 14));
		exploreTextFields[0].getDocument().addDocumentListener(new InitialPositionListener());
		exploreTextFields[0].getDocument().putProperty("name", "Robot Intial Position");
		exploreInputPane.add(exploreCtrlLabels[1]);
		exploreCtrlLabels[1].setFont(new Font("Tahoma", Font.PLAIN, 14));
		exploreInputPane.add(exploreTextFields[1]);
		exploreTextFields[1].setText("10");
		exploreTextFields[1].setFont(new Font("Tahoma", Font.PLAIN, 14));
		exploreTextFields[1].getDocument().addDocumentListener(new InitialPositionListener());
		exploreTextFields[1].getDocument().putProperty("name", "Robot Explore Speed");
		
		JButton exploreBtn = new JButton("Explore");
		exploreBtn.setActionCommand("Explore");
		exploreBtn.addActionListener(this);
		JPanel exploreBtnPane = new JPanel();
		exploreBtnPane.add(exploreBtn);
		
		JPanel exploreCtrlPane = new JPanel();
		exploreCtrlPane.add(exploreInputPane);
		exploreCtrlPane.add(exploreBtnPane);
		exploreCtrlPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		//###Add control panel for finding fastest path###
		JLabel ffpCtrlLabel = new JLabel("Speed (steps/sec): ");
		ffpTextField = new JTextField(10);
		JButton ffpBtn = new JButton("Navigate");
		ffpBtn.setActionCommand("FindFastestPath");
		ffpBtn.addActionListener(this);
		JPanel ffpBtnPane = new JPanel();
		ffpBtnPane.add(ffpBtn);
		
		JPanel ffpInputPane = new JPanel(new GridLayout(1, 2));
		ffpInputPane.add(ffpCtrlLabel);
		ffpCtrlLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ffpInputPane.add(ffpTextField);
		
		JPanel ffpCtrlPane = new JPanel();
		ffpCtrlPane.add(ffpInputPane);
		ffpCtrlPane.add(ffpBtnPane);
		ffpCtrlPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		//###Add card panel to switch between explore and finding fastest path panels###
		JPanel cardPane = new JPanel(new CardLayout());
		cardPane.add(exploreCtrlPane, explorePanelName);
		cardPane.add(ffpCtrlPane, ffpPanelName);
		cardPane.setPreferredSize(new Dimension(280, 300));
		ctrlPane.add(cardPane, BorderLayout.CENTER);
		
		//###Add status panel###
		JPanel statusPane = new JPanel(new BorderLayout());
		JLabel statusLabel = new JLabel("Status Console");
		statusPane.add(statusLabel, BorderLayout.NORTH);
		JPanel statusConsole = new JPanel(new GridLayout(3, 1));
		statusConsole.setBackground(Color.LIGHT_GRAY);
		statusConsole.setPreferredSize(new Dimension(280, 100));
		status = new JLabel("Waiting for Commands...");
		status.setHorizontalAlignment(JLabel.CENTER);
		timeCounter = new JLabel();
		timeCounter.setHorizontalAlignment(JLabel.CENTER);
		coverageUpdate = new JLabel();
		coverageUpdate.setHorizontalAlignment(JLabel.CENTER);
		statusConsole.add(status);
		statusConsole.add(timeCounter);
		statusConsole.add(coverageUpdate);
		statusPane.add(statusConsole, BorderLayout.CENTER);
		ctrlPane.add(statusPane, BorderLayout.SOUTH);
		contentPane.add(ctrlPane,  BorderLayout.CENTER);
		
		//###Add left panel (maze)###
		mazePane = new JPanel(new FlowLayout());
		mazePane.setPreferredSize(new Dimension(450, 650));
		JPanel maze = new JPanel();
		maze.setLayout(new GridLayout(Arena.mapWidth, Arena.mapLength));
		maze.setPreferredSize(new Dimension(450, 600));
		mazeGrids = new JButton[Arena.mapWidth][Arena.mapLength];
		for(int x = 0; x < Arena.mapWidth; x++){
			for(int y = 0; y < Arena.mapLength; y++){
				mazeGrids[x][y] = new JButton();
				mazeGrids[x][y].setEnabled(false);
				mazeGrids[x][y].setBorder(BorderFactory.createLineBorder(Color.GRAY));
				if(x == 10){
					mazeGrids[x][y].setBorder(new CompoundBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.BLUE), 
									BorderFactory.createMatteBorder(0,  1,  1,  1,  Color.GRAY)));
				}
				mazeGrids[x][y].setBackground(Color.BLACK);
				maze.add(mazeGrids[x][y]);
				if((x >= 0 & x <= 2 & y >= 12 & y <= 14) || (y >= 0 & y <= 2 & x >= 17 & x <= 19)){
					mazeGrids[x][y].setEnabled(false);
					mazeGrids[x][y].setBackground(Color.ORANGE);
					if(x == 1 & y == 13){
						mazeGrids[x][y].setText("G");
					}
					else if(x == 18 & y == 1){
						mazeGrids[x][y].setText("S");
					}
				}
			}
		}
		mazePane.add(maze);
		contentPane.add(mazePane, BorderLayout.WEST);
	}
	
	public JPanel getContentPane(){
		return contentPane;
	}
	
	public void setMazeGrids(JButton[][] mazeGrids){
		this.mazeGrids = mazeGrids;
	}
	
	public JButton[][] getMazeGrids(){
		return mazeGrids;
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		controller = Controller.getInstance();
		if(cmd.matches("ToggleObstacleAt [0-9]+,[0-9]+")){
			int index = cmd.indexOf(",");
			int x = Integer.parseInt(cmd.substring(17,  index));
			int y = Integer.parseInt(cmd.substring(index + 1));
			controller.toggleObstacle(mapGrids, x, y);
		}
		else if(cmd.equals("SwitchCtrl")){
			JComboBox main = (JComboBox) e.getSource();
			JPanel cardPanel = (JPanel) ctrlPane.getComponent(1);
			controller.switchComboBox(main, cardPanel);
		}
		else if(cmd.equals("LoadMap")){
			controller.loadMap(mapGrids);
		}
		else if(cmd.equals("ClearMap")){
			controller.clearMap(mapGrids);
		}
		else if(cmd.equals("ExploreMaze")){
			controller.exploreMaze();
		}
		else if(cmd.equals("FindFastestPath")){
			controller.findFastestPath();
		}
	}
	
	private void loadArenaFromDisk(){
		FileReaderWriter fileReader;
		try{
			fileReader = new FileReaderWriter(FileSystems.getDefault().getPath(Controller.arenaDescriptorPath));
			String arenaDescriptor = fileReader.fileReader();
			if(!arenaDescriptor.equals("")){
				for(int i = 0; i < Arena.mapWidth; i++){
					for(int j = 0; j < Arena.mapLength; j++){
						if(arenaDescriptor.charAt(Arena.mapLength * i + j) == '1'){
							mapGrids[19 - 1][j].setBackground(Color.RED);
						}
					}
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		Arena arena = Arena.getInstance();
		arena.setLayout(mapGrids);
	}
	
	class InitialPositionListener implements DocumentListener{
		public void insertUpdate(DocumentEvent e){
			update(e);
		}
		
		public void removeUpdate(DocumentEvent e){
			update(e);
		}
		
		public void changedUpdate(DocumentEvent e){}
		
		private void update(DocumentEvent e){
			controller = Controller.getInstance();
			Document doc = (Document) e.getDocument();
			String name = (String) doc.getProperty("name");
			if(name.equals("Robot Initial Position")){
				try{
					String position = doc.getText(0,  doc.getLength());
					if(position.matches("[0-9]+,[0-9]+")){
						int index = position.indexOf(",");
						int x = Integer.parseInt(position.substring(0,  index));
						int y = Integer.parseInt(position.substring(index + 1));
						controller.resetRobotInMaze(mazeGrids, x, y);
					}
					else{
						controller.resetMaze(mazeGrids);
						status.setText("Robot Initial Position Not Set");
					}
				}
				catch(BadLocationException ex){
					ex.printStackTrace();
				}
			}
			else if(name.equals("Robot Explore Speed")){
				try{
					String speed = doc.getText(0,  doc.getLength());
					if(speed.matches("[0-9]+")){
						controller.setExploreSpeed(Integer.parseInt(speed));
					}
					else{
						status.setText("Robot Speed Not Set");
					}
				}
				catch(BadLocationException ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void setStatus(String message){
		status.setText(message);
	}
	
	public void setTimerCounter(int timeLeft){
		timeCounter.setText("Time left (sec): " + timeLeft);
	}
	
	public void setCoverage(String coverage){
		coverageUpdate.setText("Coverage (%): " + coverage);
	}
	
	public void refreshInput(){
		for(int i = 0; i < 4; i++){
			exploreTextFields[i].setText(exploreTextFields[i].getText());
		}
	}
}