package com.sg.ntu.mdp.simulator;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.Border;

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
import javax.swing.JLabel;

public class MapUI {

	private JFrame frame;
	private JLabel[][] labels = new JLabel[20][15];
	private JPanel[][] panels = new JPanel[20][15];
	
	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public MapUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
		
		JButton btnStartExpore = new JButton("Start Expore");
		splitPane_1.setLeftComponent(btnStartExpore);
		
		buildMapEnvironment(splitPane);		
	}


	private void buildMapEnvironment(JSplitPane splitPane){
		JPanel panel = new JPanel();
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
		buildRobot(panel);
	}
	private void buildWall(JPanel panel){
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
			panel.add(panel_1, gbc_TopWall);
			
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
			panel.add(panel_1, gbc_BottomWall);
			
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
			panel.add(panel_1, gbc_LeftWall);
			
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
			panel.add(panel_1, gbc_RightWall);
			
			JLabel RightWall = new JLabel("9.99");
			RightWall.setForeground(Color.WHITE);
			panel_1.add(RightWall);
		}
		//bottom wall
		
	}
	private void buildMapFields(JPanel panel){
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
				panel.add(panel_1, gbc_field);
				JLabel field = new JLabel("  0  ");
				field.setName(i+j+"");
				field.setForeground(Color.BLACK);
				panel_1.add(field);
				
				//store the object so we can reference it and update the UI
				panels[i][j] = panel;
				labels[i][j]=field;
				
				
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

	//TODO: not done
	private void buildRobot(JPanel panel){
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
		panel_1.setBackground(Color.BLUE);
		GridBagConstraints gbc_robot = new GridBagConstraints();
		gbc_robot.fill = GridBagConstraints.BOTH;
		gbc_robot.weightx = 2;
		gbc_robot.weighty = 2;
		gbc_robot.gridx = 1;
		gbc_robot.gridy = 1;
		gbc_robot.gridheight=2;
		gbc_robot.gridwidth=2;
		panel.add(panel_1, gbc_robot);
	}
	//TODO: not done
	private void setAsBlock(JPanel panel, JLabel label){
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	}
}