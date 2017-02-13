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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class MapUI {

	private JFrame frame;
	private static JButton exploreBtn;
	public static JLayeredPane mapPanel;
	public static JLabel[][] labels = new JLabel[20][15];
	public static JPanel[][] panels = new JPanel[20][15];
	
	public static JPanel robotBody;
	public static GridBagConstraints robotBodyConstrain;
	public static JPanel robotHead;
	public static GridBagConstraints robotHeadConstrain;
	
	public static Queue instructionQueue = new ArrayBlockingQueue(1000);
	
	
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
		
		exploreBtn = new JButton("Start Expore");
		splitPane_1.setLeftComponent(exploreBtn);
		exploreBtn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)  
		    {  
				startExploreSimulatorTask();
		    } 
		});
		
		buildMapEnvironment(splitPane);		
	}


	//////////////////////////////////////////////////////////////
	private void buildMapEnvironment(JSplitPane splitPane){
		JLayeredPane panel = new JLayeredPane();
		mapPanel = panel;
		Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
		panel.setBorder(border);
		
		splitPane.setLeftComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0,0,0,0);
		gbl_panel.setConstraints(panel, c);
//		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
//		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
//		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		buildWall();
		buildMapFields();
		buildRobot();
	}
	private void buildWall(){
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
			mapPanel.add(panel_1, gbc_TopWall,0);
			
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
			mapPanel.add(panel_1, gbc_BottomWall,0);
			
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
			mapPanel.add(panel_1, gbc_LeftWall,0);
			
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
			mapPanel.add(panel_1, gbc_RightWall,0);
			
			JLabel RightWall = new JLabel("9.99");
			RightWall.setForeground(Color.WHITE);
			panel_1.add(RightWall);
		}
		//bottom wall
		
	}
	private void buildMapFields(){
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
				mapPanel.add(panel_1, gbc_field,0);
				JLabel field = new JLabel("  "+i+","+j+"  ");
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
					field.setText("1");
				}
				if((i+1)==2&&(j+1)==1){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==3&&(j+1)==1){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==1&&(j+1)==2){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==2&&(j+1)==2){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==3&&(j+1)==2){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==1&&(j+1)==3){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==2&&(j+1)==3){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				if((i+1)==3&&(j+1)==3){
					panel_1.setBackground(Color.BLUE);
					field.setText("1");
				}
				//highlight the end area
				if((i+1)==18&&(j+1)==13){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==19&&(j+1)==13){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==20&&(j+1)==13){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==18&&(j+1)==14){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==19&&(j+1)==14){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==20&&(j+1)==14){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==18&&(j+1)==15){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==19&&(j+1)==15){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
				if((i+1)==20&&(j+1)==15){
					panel_1.setBackground(Color.RED);
					field.setText("1");
				}
			}	
		}
	}
	private void buildRobot(){
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		panel_1.setOpaque(true);
		
		ImageIcon image = new ImageIcon("/MDP-ALGO/src/Simulator_Robot_Editor_1.png");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		panel_1.add( label, BorderLayout.CENTER );
		
		GridBagConstraints gbc_robot = new GridBagConstraints();
		gbc_robot.fill = GridBagConstraints.BOTH;
		gbc_robot.weightx = 2;
		gbc_robot.weighty = 2;
		gbc_robot.gridx = 1;
		gbc_robot.gridy = 1;
		gbc_robot.gridheight=3;
		gbc_robot.gridwidth=3;
		mapPanel.add(panel_1, gbc_robot,2);
		robotBody = panel_1;
		robotBodyConstrain = gbc_robot;
		panel_1.setVisible(false);
		
		//set the front head to yellow
		GridBagConstraints gbc_robot2 = new GridBagConstraints();
		gbc_robot2.fill = GridBagConstraints.BOTH;
		gbc_robot2.weightx = 2;
		gbc_robot2.weighty = 2;
		gbc_robot2.gridx = 2;
		gbc_robot2.gridy = 3;
		gbc_robot2.gridheight=1;
		gbc_robot2.gridwidth=1;
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		panel_2.setBackground(Color.YELLOW);
		panel_2.setOpaque(true);
		mapPanel.add(panel_2, gbc_robot2,1);
		robotHead = panel_2;
		robotHeadConstrain = gbc_robot2;
		panel_2.setVisible(false);
		
	}
	//////////////////////////////////////////////////////////
	
	private void resetMap(){
		
	}
	
	//////////////////////////////////////////////////////////
	///					DESCRIPTOR TASK					   ///
	//////////////////////////////////////////////////////////
	public void loadMapFromDescriptor(){
		loadExploredData();
		loadObstacleData();
	}
	public void loadExploredData(){
		Descriptor descriptor = new Descriptor();
		int[][] data = descriptor.getExploredDataToSimulator();//
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
		descriptor.saveDataFromSimulatorToFile(exploredData,obstacledData);
		
	}
	public static int[][] saveExploredData(JLabel[][] labels){
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
	public static int[][] saveObstacleData(JLabel[][] labels){
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
	
	public static void updateMap(int[][] data, int type){
		if(type==0)
			updateExploredDataOnMap(data);
		else
			updateObstacleDataOnMap(data);
	}
	public static void updateExploredDataOnMap(int[][] data){
		int row = 0;
		int col = 19;
		for(int i=0;i<=14;i++){
			for(int j=0;j<=19;j++){
				if(data[j][i]==1){
					labels[col][row].setText("1");
				}
				col--;
			}
			col=19;
			row++;
		}
	}
	public static void updateObstacleDataOnMap(int[][] data){
		int row = 0;
		int col = 19;
		for(int i=0;i<=14;i++){
			for(int j=0;j<=19;j++){
				if(data[j][i]==1){
					labels[col][row].setText("1");
				}
				col--;
			}
			col=19;
			row++;
		}
	}
	
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////////
	///					MOVEMENT THREAD 			 	   ///
	//////////////////////////////////////////////////////////
	public static void runRobotInstruction(){
		Thread b = ((Thread) instructionQueue.poll());
		if(b!=null){
			b.start();
	        synchronized(b){
	            try{
	                System.out.println("Waiting for instruction to complete...");
	                b.wait();
	            }catch(InterruptedException e){
	                e.printStackTrace();
	            }
	            if(instructionQueue.size()==0){
	            	System.out.println("QUEUE NOW EMPTY");
	            	explore();
	            }
	            	
	        }
		}else{
			System.out.println("QUEUE is EMPTY");
			explore();
		}
	}
	
	
	//////////////////////////////////////////////////////////
	///					EXPLORATION TASK				   ///
	//////////////////////////////////////////////////////////
	public static void startExploreSimulatorTask(){
		showRobot();
		Boolean stopExploration = false;
		startExplorationTimer(stopExploration);
	}
	public static void showRobot(){
		robotBody.setVisible(true);
		robotHead.setVisible(true);
		mapPanel.repaint();
	}
	
	private static int interval;
	private static Timer explorerTimer;
	public static void startExplorationTimer(boolean stopExploration){
		int delay = 1000;
	    int period = 1000;
	    interval = 100;//10sec for exploration
	    explorerTimer = new Timer();
	    explorerTimer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	        	clockTick(stopExploration);
	        }
	    }, delay, period);
	    
	    exploreBtn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)  
		    {  
				stopExploration(stopExploration);
		    } 
		});
	}
	private static final int clockTick(boolean stopExploration) {
	    if (interval == 1){
	    	explorerTimer.cancel();
	    	exploreBtn.setText("Start Exploration");
	    	exploreBtn.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)  
			    {  
					startExploreSimulatorTask();
			    } 
			});
	    }else{
	    	
	    	--interval;
	    	runRobotInstruction();
		    exploreBtn.setText(interval+"sec left");
	    }
	   return interval;
	}
	
	
	
	public static void explore(){
//		instructionQueue.add(new MoveRobotForwardThread(1));
//		instructionQueue.add(new MoveRobotForwardThread(1));
//		instructionQueue.add(new TurnRobotThread(Direction.LEFT,1));
//		instructionQueue.add(new MoveRobotForwardThread(1));
//		instructionQueue.add(new MoveRobotForwardThread(1));
//		instructionQueue.add(new MoveRobotForwardThread(1));
//		instructionQueue.add(new MoveRobotForwardThread(1));
		
		Algothrim algothrim = new Algothrim(null,null,17,2); 
		
		int currentLocationFrontRow = Algothrim.currentLocationFrontRow;//up to 0 - 19
		int currentLocationFrontCol = Algothrim.currentLocationFrontCol;//up to 0 - 14
		int frontMidSensor = 0;
		int frontLeftSensor = 0;
		int frontRightSensor = 0;
		int rightSensor = 0;
		int leftSensor = 0;
		switch(Algothrim.currentDirection){
			case North:
				frontMidSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow+1].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol][19-currentLocationFrontRow+1].getBackground()==Color.BLACK?1:0;
				
				frontLeftSensor = panels[currentLocationFrontCol-1][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol-1][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				frontRightSensor = panels[currentLocationFrontCol+1][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol+1][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				rightSensor = panels[currentLocationFrontCol+2][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol+2][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				leftSensor = panels[currentLocationFrontCol-2][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol-2][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				break;
			case South:
				frontMidSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow-1].getBackground()==Color.LIGHT_GRAY||
				 		panels[currentLocationFrontCol][19-currentLocationFrontRow-1].getBackground()==Color.BLACK?1:0;
				
				frontLeftSensor = panels[currentLocationFrontCol+1][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol+1][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				frontRightSensor = panels[currentLocationFrontCol-1][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol-1][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				rightSensor = panels[currentLocationFrontCol-2][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol-2][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				leftSensor = panels[currentLocationFrontCol+2][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol+2][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				break;
			case East:
				frontMidSensor = panels[currentLocationFrontCol+1][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
		 				panels[currentLocationFrontCol+1][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				frontLeftSensor = panels[currentLocationFrontCol+1][19-currentLocationFrontRow-1].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol+1][19-currentLocationFrontRow-1].getBackground()==Color.BLACK?1:0;
				
				frontRightSensor = panels[currentLocationFrontCol+1][19-currentLocationFrontRow+1].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol+1][19-currentLocationFrontRow+1].getBackground()==Color.BLACK?1:0;
				
				rightSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow-2].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol][19-currentLocationFrontRow-2].getBackground()==Color.BLACK?1:0;
				
				leftSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow+2].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol][19-currentLocationFrontRow+2].getBackground()==Color.BLACK?1:0;
				
				break;
			case West:
				frontMidSensor = panels[currentLocationFrontCol-1][19-currentLocationFrontRow].getBackground()==Color.LIGHT_GRAY||
 						panels[currentLocationFrontCol-1][19-currentLocationFrontRow].getBackground()==Color.BLACK?1:0;
				
				frontLeftSensor = panels[currentLocationFrontCol-1][19-currentLocationFrontRow-1].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol-1][19-currentLocationFrontRow-1].getBackground()==Color.BLACK?1:0;
				
				rightSensor = panels[currentLocationFrontCol-1][19-currentLocationFrontRow+1].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol-1][19-currentLocationFrontRow+1].getBackground()==Color.BLACK?1:0;
				
				rightSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow+2].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol][19-currentLocationFrontRow+2].getBackground()==Color.BLACK?1:0;
				
				leftSensor = panels[currentLocationFrontCol][19-currentLocationFrontRow-2].getBackground()==Color.LIGHT_GRAY||
						 panels[currentLocationFrontCol][19-currentLocationFrontRow-2].getBackground()==Color.BLACK?1:0;
				break;
		}
		
	
		algothrim.explore(frontMidSensor, frontLeftSensor, frontRightSensor, rightSensor, leftSensor, new RobotCallback(){
			@Override
			public void moveForward(int distance) {
				instructionQueue.add(new MoveRobotForwardThread(distance));
			}
			@Override
			public void changeDirection(Direction direction, int times) {
				instructionQueue.add(new TurnRobotThread(direction,times));
			}
		});
		
		
		
	}
	public static void stopExploration(boolean stopExploration){
		stopExploration = true;
		explorerTimer.cancel();
		exploreBtn.setText("Start Exploration");
    	exploreBtn.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)  
		    {  
				startExploreSimulatorTask();
		    } 
		});
    	
    	//TODO: move robot to start area
    	
	}
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////

}
