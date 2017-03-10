package com.sg.ntu.mdp;

import java.awt.EventQueue;
import java.util.Scanner;

import com.sg.ntu.mdp.communication.AlgoContoller;
import com.sg.ntu.mdp.communication.CommMgr;
import com.sg.ntu.mdp.simulator.MapUI;

import model.Direction;
import model.RobotCallback;

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
		
/*	
		while(commmgr.isConnected()==false){
			System.out.println("reconnecting...");
			commmgr.setConnection(999999999);
		}
		
		while(true){
			String message = commmgr.recvMsg();
			if(message!=null&&!message.equals("")){
				System.out.println("received Message :"+message);
				
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
						System.out.println("SENDING Message :"+Jsoninstructions);
						commmgr.sendMsg(Jsoninstructions, "", false);
					}
					
				});
			}
		}
		*/

		
		
		

		
		
		Scanner sc= new Scanner(System.in);
		String input= sc.nextLine();
		boolean loop=true;
		while(input!="-1"){
			
//			if(loop==true){
//				for(int i=0;i<129;i++){
//					input="0|0|0|0|0";
////					if(i==0)
////						input="1|1|1|0|0";
//					controller.parseMessageFromRobot(input, new RobotCallback(){
//						@Override
//						public void moveForward(int distance) {
//						}
//						@Override
//						public void changeDirection(Direction direction, int times) {
//						}
//						@Override
//						public void readyForFastestPath(){
//						}
//						@Override
//						public void sendRobotInstruction(String Jsoninstructions){
//							System.out.println("SENDING Message :"+Jsoninstructions);
////							commmgr.sendMsg(Jsoninstructions, "", false);
//						}
//						
//					});
//				}
//				loop=false;
//			}
//			
			
			controller.parseMessageFromRobot(input, new RobotCallback(){
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
					System.out.println("SENDING Message :"+Jsoninstructions);
//					commmgr.sendMsg(Jsoninstructions, "", false);
				}
				
			});
			input = sc.nextLine();
		}
		
		
		
	}
	
	
	
	
	
	
	
	
}
