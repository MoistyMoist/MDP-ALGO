package com.sg.ntu.mdp.simulator;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.Border;

import com.sg.ntu.mdp.Algothrim;
import com.sg.ntu.mdp.Direction;
import com.sg.ntu.mdp.RobotCallback;
import com.sg.ntu.mdp.communication.Descriptor;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class MapUI {

	private JFrame frame;
	private JLabel[][] labels = new JLabel[20][15];
	private JPanel[][] panels = new JPanel[20][15];
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapUI window = new MapUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MapUI() {
		initialize();
	}
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.9);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frame.getContentPane().add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.3);
		splitPane.setRightComponent(splitPane_1);
		
		JSplitPane splitPane_2 = new JSplitPane();
		
		splitPane_1.setRightComponent(splitPane_2);
		
		JButton btnStartSp = new JButton("Start SP");
		splitPane_2.setLeftComponent(btnStartSp);
		
		JButton btnSaveMd = new JButton("Save MD");
		splitPane_2.setRightComponent(btnSaveMd);
		btnSaveMd.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)  
		    {  
				saveMapToDescriptor();
		    } 
		});
		
		JButton btnStartExpore = new JButton("Start Expore");
		splitPane_1.setLeftComponent(btnStartExpore);
		
		buildMapEnvironment(splitPane);		
	}


	//////////////////////////////////////////////////////////////
	private void buildMapEnvironment(JSplitPane splitPane){
		JLayeredPane panel = new JLayeredPane();
		Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
		panel.setBorder(border);
		
		splitPane.setLeftComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,0,0,0);
		gbl_panel.setConstraints(panel, c);
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		buildWall(panel);
		buildMapFields(panel);
	}
	private void buildWall(JLayeredPane panel){
		//top wall
		for(int i=0;i<22;i++){
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
			panel_1.setBackground(Color.BLACK);
			GridBagConstraints gbc_TopWall = new GridBagConstraints();
			gbc_TopWall.fill = GridBagConstraints.BOTH;
			gbc_TopWall.weightx = 22;
			gbc_TopWall.weighty = 17;
			gbc_TopWall.gridx = i;
			gbc_TopWall.gridy = 0;
			panel.add(panel_1, gbc_TopWall,0);
			
			JLabel TopWall = new JLabel("9.99");
			TopWall.setForeground(Color.WHITE);
			panel_1.add(TopWall);
		}
		for(int i=0;i<22;i++){
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
			panel_1.setBackground(Color.BLACK);
			GridBagConstraints gbc_BottomWall = new GridBagConstraints();
			gbc_BottomWall.fill = GridBagConstraints.BOTH;
			gbc_BottomWall.weightx = 22;
			gbc_BottomWall.weighty = 17;
			gbc_BottomWall.gridx = i;
			gbc_BottomWall.gridy = 16;
			panel.add(panel_1, gbc_BottomWall,0);
			
			JLabel BottomWall = new JLabel("9.99");
			BottomWall.setForeground(Color.WHITE);
			panel_1.add(BottomWall);
		}
		//left wall
		for(int i=0;i<16;i++){
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
			panel_1.setBackground(Color.BLACK);
			GridBagConstraints gbc_LeftWall = new GridBagConstraints();
			gbc_LeftWall.fill = GridBagConstraints.BOTH;
			gbc_LeftWall.weightx = 1;
			gbc_LeftWall.weighty = 17;
			gbc_LeftWall.gridx = 0;
			gbc_LeftWall.gridy = i;
			panel.add(panel_1, gbc_LeftWall,0);
			
			JLabel LeftWall = new JLabel("9.99");
			LeftWall.setForeground(Color.WHITE);
			panel_1.add(LeftWall);
		}
		//right wall
		for(int i=0;i<16;i++){
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));
			panel_1.setBackground(Color.BLACK);
			GridBagConstraints gbc_RightWall = new GridBagConstraints();
			gbc_RightWall.fill = GridBagConstraints.BOTH;
			gbc_RightWall.weightx = 1;
			gbc_RightWall.weighty = 17;
			gbc_RightWall.gridx = 21;
			gbc_RightWall.gridy = i;
			panel.add(panel_1, gbc_RightWall,0);
			
			JLabel RightWall = new JLabel("9.99");
			RightWall.setForeground(Color.WHITE);
			panel_1.add(RightWall);
		}
		//bottom wall
		
	}
	private void buildMapFields(JLayeredPane panel){
		for(int i=0;i<20;i++){
			for(int j=0;j<15;j++){
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 1));

				GridBagConstraints gbc_field = new GridBagConstraints();
				gbc_field.fill = GridBagConstraints.BOTH;
				gbc_field.weightx = 19;
				gbc_field.weighty = 15;
				gbc_field.gridx = i+1;
				gbc_field.gridy = j+1;
				panel.add(panel_1, gbc_field,0);
				JLabel field = new JLabel("  0  ");
				field.addMouseListener(new MouseAdapter()  
				{  
				    public void mouseClicked(MouseEvent e)  
				    {  
				    	field.setText("1");//CHANGE THIS LATER
				    	panels[Integer.parseInt(field.getName().substring(0, field.getName().indexOf("/")))]
				    			[Integer.parseInt(field.getName().substring(field.getName().indexOf("/")+1,field.getName().length()))].
				    			setBackground(Color.LIGHT_GRAY);
				    }  
				});
				field.setName(i+"/"+j);
				field.setForeground(Color.BLACK);
				panel_1.add(field);
				
				//store the object so we can reference it and update the UI
				panels[i][j] = panel_1;
				labels[i][j] = field;
				
				
				//highlight the start area
				if((i+1)==1&&(j+1)==1){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==2&&(j+1)==1){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==3&&(j+1)==1){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==1&&(j+1)==2){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==2&&(j+1)==2){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==3&&(j+1)==2){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==1&&(j+1)==3){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==2&&(j+1)==3){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				if((i+1)==3&&(j+1)==3){
					panel_1.setBackground(Color.BLUE);
					field.setText("9.99");
				}
				//highlight the end area
				if((i+1)==18&&(j+1)==13){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==19&&(j+1)==13){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==20&&(j+1)==13){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==18&&(j+1)==14){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==19&&(j+1)==14){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==20&&(j+1)==14){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==18&&(j+1)==15){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==19&&(j+1)==15){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
				if((i+1)==20&&(j+1)==15){
					panel_1.setBackground(Color.RED);
					field.setText("9.99");
				}
			}	
		}
	}

	//TODO: not done call this method after start button is clicked
	private void buildRobot(JLayeredPane panel){
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		panel_1.setBackground(Color.BLUE);
		panel_1.setOpaque(true);
		GridBagConstraints gbc_robot = new GridBagConstraints();
		gbc_robot.fill = GridBagConstraints.BOTH;
		gbc_robot.weightx = 2;
		gbc_robot.weighty = 2;
		gbc_robot.gridx = 1;
		gbc_robot.gridy = 1;
		gbc_robot.gridheight=3;
		gbc_robot.gridwidth=3;
		panel.add(panel_1, gbc_robot,1);
	}
	//////////////////////////////////////////////////////////

	
	//////////////////////////////////////////////////////////
	///					DESCRIPTOR TASK					   ///
	//////////////////////////////////////////////////////////

	public void loadMapFromDescriptor(){
		loadExploredData();
		loadObstacleData();
	}
	public void loadExploredData(){
		Descriptor descriptor = new Descriptor();
		int[][] data = descriptor.getExploredDataToSimulator();
		updateMap(data,0);
	}
	public void loadObstacleData(){
		Descriptor descriptor = new Descriptor();
		int[][] data = descriptor.getObstacleDataToSimulator();
		updateMap(data,1);
	}
	
	public void saveMapToDescriptor(){
		int [][] exploredData = saveExploredData(labels);
		int [][] obstacledData = saveObstacleData(labels);
		
		Descriptor descriptor = new Descriptor();
		
	}
	public int[][] saveExploredData(JLabel[][] labels){
		int col=0;
		int row=0;
		int[][]binaryData = new int[20][15];
		
		for(int i=19;i>=0;i--){
			for(int j=0;j<=14;j++){
				if(labels[i][j].getText().equals("1")||labels[i][j].getText().equals("9.99")){
					binaryData[row][col]=1;
				}else{
					binaryData[row][col]=0;
				}
				col++;
			}
			row++;
			col=0;
		}
		return binaryData;
	}
	public int[][] saveObstacleData(JLabel[][] labels){
		int col=0;
		int row=0;
		int[][]binaryData = new int[20][15];
		
		for(int i=19;i>=0;i--){
			for(int j=0;j<=14;j++){
				if(labels[i][j].getText().equals("1")){
					binaryData[row][col]=1;
				}else{
					binaryData[row][col]=0;
				}
				col++;
			}
			row++;
			col=0;
		}
		return binaryData;
	}
	
	//TODO: update this add if else from descriptor or simulation start
	public void updateMap(int[][] data, int type){
		if(type==0)
			updateExploredDataOnMap(data);
		else
			updateObstacleDataOnMap(data);
	}
	public void updateExploredDataOnMap(int[][] data){
		int col=0;
		int row=14;
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				if(data[i][j]==1){
					labels[row][col].setText("1");
				}else{
					labels[row][col].setText("0");
				}
				col++;
			}
			col=0;
			row--;
		}
	}
	public void updateObstacleDataOnMap(int[][] data){
		int col=0;
		int row=14;
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[i].length;j++){
				if(data[i][j]==1){
					labels[row][col].setText("1");
				}else{
					labels[row][col].setText("0");
				}
				col++;
			}
			col=0;
			row--;
		}
	}
	
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	
	
	
	
	
	
	//////////////////////////////////////////////////////////
	///					EXPLORATION TASK				   ///
	//////////////////////////////////////////////////////////
	public void startExploreTask(){
		showRobot();
		Boolean stopExploration = false;
		startExplorationTimer(stopExploration);
		Algothrim algothrim = new Algothrim(null,null,17,2); 
		
		try{
			while(stopExploration!=true){
				try {
					//Do a 1 sec delay for every explore call;
					Thread.sleep(500);
	            } catch (InterruptedException x) {
	                System.out.println("hi");
	            }
				finally{
					int currentLocationFrontRow = Algothrim.currentLocationFrontRow;
					int currentLocationFrontCol = Algothrim.currentLocationFrontCol;
					int frontMidSensor = 0;
					int frontLeftSensor = 0;
					int frontRightSensor = 0;
					int rightSensor = 0;
					int leftSensor = 0;
					switch(Algothrim.currentDirection){
						case North:
							//TODO:check this
							frontMidSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY?1:0;
							break;
						case South:
							break;
						case East:
							break;
						case West:
							break;
					}
					
				
					algothrim.explore(frontMidSensor, frontLeftSensor, frontRightSensor, rightSensor, leftSensor, new RobotCallback(){
						
						@Override
						public void moveForward(int distance) {
							moveRobotForward(distance);
						}

						@Override
						public void changeDirection(Direction direction, int times) {
							turnRobot(direction, times);
							
						}
					});
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			//RETURN TO START POINT
		}
		
	}
	public void showRobot(){
		//TODO:show the robot on the ui
	}
	//TODO: add the timer function
	public void startExplorationTimer(boolean stopExploration){
		
	}
	//TODO: move the robot forward
	public  void moveRobotForward(int distance){
		//update the map updateMap();
	}
	//TODO:turn the robot
	public void turnRobot(Direction direction,int times){
		
	}
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////

}
