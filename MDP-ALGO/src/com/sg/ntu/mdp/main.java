package com.sg.ntu.mdp;

import java.awt.EventQueue;
import java.util.Arrays;
import java.util.List;

import com.sg.ntu.mdp.communication.AlgoContoller;
import com.sg.ntu.mdp.communication.CommMgr;
import com.sg.ntu.mdp.simulator.MapUI;

public class main {
	static CommMgr commmgr = new CommMgr();
	static AlgoContoller controller = new AlgoContoller();
	
	
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
		
		
		
		
		while(commmgr.isConnected()==false){
			commmgr.setConnection(999999999);
		}
//		commmgr.sendMsg("AREADY", "", false);//todo: remove this later
		while(true){
			String message = commmgr.recvMsg();
			if(message!=null){
				System.out.println("received");
				controller.parseMessageFromRobot(message, new RobotCallback(){
					@Override
					public void moveForward(int distance) {
					}
					@Override
					public void changeDirection(Direction direction, int times) {
					}
					@Override
					public void readyForFastestPath(){
					}
					@Override
					public void sendRobotInstruction(String Jsoninstructions){
						commmgr.sendMsg(Jsoninstructions, "", false);
					}
					
				});
			}
		}
	}
}
