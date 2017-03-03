package com.sg.ntu.mdp.communication;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sg.ntu.mdp.Algothrim;
import com.sg.ntu.mdp.instructions.MoveRobotForwardThread;
import com.sg.ntu.mdp.instructions.TurnRobotThread;
import com.sg.ntu.mdp.simulator.MapUI;

import model.Direction;
import model.RobotCallback;

//communication between the robot and algo outputting json
public class AlgoContoller {
	
	static Algothrim algothrim = new Algothrim(null,null,18,2); 
	static RobotCallback callback;
	static boolean isExplorin=true;
	static boolean isReturning=false;
	static Timer explorationTimer;
	static int intervial =180;
	
	
	public AlgoContoller (){
		
	}
	
	public static void returnToStart(int inputLeftSensor, int inputRightSensor, int inputFrontMidSensor, int inputFrontLeftSensor, int inputFrontRightSensor){
		ArrayList<String> jsonInstruction = new ArrayList<String>();
		algothrim.returnGodHome(new RobotCallback(){
			@Override
			public void moveForward(int distance) {
				updateRobotUI(null,distance,true);
				jsonInstruction.add(wrapMoveForwardChangeJson(distance));
			}
			@Override
			public void changeDirection(Direction direction, int times) {
				updateRobotUI(direction,times,false);
				jsonInstruction.add(wrapDirectionChangeJson(direction,times));
			}
			@Override
			public void readyForFastestPath(){
				isReturning=false;
			}
			@Override
			public void sendRobotInstruction(String jsonInstructions){
				sendRobotInstructions(jsonInstruction);
			}
		});
		
	}
	
	public static void computeFastestPath(){
		ArrayList<String> jsonInstruction = new ArrayList<String>();
		
		algothrim.findPath(new RobotCallback(){
			@Override
			public void moveForward(int distance) {
				jsonInstruction.add(wrapMoveForwardChangeJson(distance));
			}
			@Override
			public void changeDirection(Direction direction, int times) {
				jsonInstruction.add(wrapDirectionChangeJson(direction,times));
			}
			@Override
			public void readyForFastestPath(){
				sendRobotInstructions(jsonInstruction);
			}	
			@Override
			public void sendRobotInstruction(String jsonInstructions){
			}
		});
	}
	
	public static void explore(int inputLeftSensor, int inputRightSensor, int inputFrontMidSensor, int inputFrontLeftSensor, int inputFrontRightSensor){
		ArrayList<String> jsonInstruction = new ArrayList<String>();
		
		algothrim.godsExploration(inputFrontMidSensor, inputFrontLeftSensor, inputFrontRightSensor, inputRightSensor, inputLeftSensor, new RobotCallback(){
			@Override
			public void moveForward(int distance) {
				System.out.println("moving for");
				updateRobotUI(null,distance,true);
				jsonInstruction.add(wrapMoveForwardChangeJson(distance));
			}
			@Override
			public void changeDirection(Direction direction, int times) {
				updateRobotUI(direction,times,false);
				jsonInstruction.add(wrapDirectionChangeJson(direction,times));
			}
			@Override
			public void readyForFastestPath(){
				//TOdo:position the robot
				computeFastestPath();
			}
			@Override
			public void sendRobotInstruction(String jsonInstructions){
				sendRobotInstructions(jsonInstruction);
			}
		});
		
		
	}
	
	public static void parseMessageFromRobot(String json, RobotCallback inCallback){
		callback=inCallback;
		String message=json.replace("{", "");
		message=message.replace("}", "");
		List<String> sensorList = Arrays.asList(message.split(","));
		for(int i=0;i<sensorList.size();i++){
			System.out.println("received "+sensorList.get(i));
		}
		
		if(isExplorin==true){
			explore(Integer.parseInt(sensorList.get(0)),Integer.parseInt(sensorList.get(1)),Integer.parseInt(sensorList.get(2)),Integer.parseInt(sensorList.get(3)),Integer.parseInt(sensorList.get(4)));
		}else if(isReturning==true){
			returnToStart(Integer.parseInt(sensorList.get(0)),Integer.parseInt(sensorList.get(1)),Integer.parseInt(sensorList.get(2)),Integer.parseInt(sensorList.get(3)),Integer.parseInt(sensorList.get(4)));
		}else{
			computeFastestPath();
		}
		
	}
	
	public static void startExploreTimer(){
		explorationTimer = new Timer();
		explorationTimer.scheduleAtFixedRate(new TimerTask() {
		        public void run() {
		        	--intervial;
		        	if(intervial<=0){
		        		endExploreTImer();
		        	}
		        }
		    }, 1000, 1000);
	}
	public static void endExploreTImer(){
		isExplorin=false;
		isReturning=true;
	}
	
	
	public static void sendRobotInstructions(ArrayList<String> jsonInstructions){
		String jsonInstructionsWraper="H[";
		for(int i=0;i<jsonInstructions.size();i++){
			jsonInstructionsWraper+=jsonInstructions.get(i);
			if((i+1)!=jsonInstructions.size()){
				jsonInstructionsWraper+=",";
			}
		}
//		
		jsonInstructionsWraper+="]";
		MapUI.saveMapToDescriptor();
		Descriptor descriptor = new Descriptor();
		String p1 = descriptor.readDescriptorFromFile(0);
		String p2 = descriptor.readDescriptorFromFile(1);
		
//		callback.sendRobotInstruction(jsonInstructionsWraper);

		jsonInstructionsWraper="";
		jsonInstructionsWraper+="Agrid{"+p1+p2+"}";
		System.out.println("jsonInstructionsWraper");
		callback.sendRobotInstruction(jsonInstructionsWraper);
		
		jsonInstructionsWraper="";
		
		String robotBody="";
		switch(algothrim.currentDirection){
			case North:
				robotBody = (algothrim.currentLocationFrontRow-1)+","+algothrim.currentLocationFrontCol;
				break;
			case South:
				robotBody = (algothrim.currentLocationFrontRow+1)+","+algothrim.currentLocationFrontCol;
				break;
			case East:
				robotBody = (algothrim.currentLocationFrontRow)+","+(algothrim.currentLocationFrontCol-1);
				break;
			case West:
				robotBody = (algothrim.currentLocationFrontRow)+","+(algothrim.currentLocationFrontCol+1);
				break;
		}
		
		jsonInstructionsWraper ="robotPosition:"+robotBody+","+algothrim.currentLocationFrontRow+","+algothrim.currentLocationFrontCol;
		callback.sendRobotInstruction(jsonInstructionsWraper);
	}
	public static String wrapDirectionChangeJson(Direction direction, int times){
		String jsonString="{";
		jsonString +="InstructionType=DirectionChange,"+direction.toString()+","+times+";";
		
		return jsonString+="}";
	}
	public static String wrapMoveForwardChangeJson(int times){
		String jsonString="{";
		jsonString +="InstructionType=MoveForward,"+times+";";
		return jsonString+="}";
	}

	public static void updateRobotUI(Direction direction, int times,boolean isForward){
		System.out.println("CURRENT ROW IS "+Algothrim.currentLocationFrontRow);
		if(isForward==true){
			moveRobotUIForward(times);
		}else{
			turnRobotUI(direction,times);
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	private static void moveRobotUIForward(int distance){
		MapUI.mapPanel.remove(MapUI.robotHead);
    	MapUI.mapPanel.remove(MapUI.robotBody);
		switch(Algothrim.currentDirection){
			case North:
				MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx+distance;
				MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy;
				MapUI.robotBodyConstrain.gridx = MapUI.robotBodyConstrain.gridx+distance;
				MapUI.robotBodyConstrain.gridy = MapUI.robotBodyConstrain.gridy;
				
				Algothrim.currentLocationFrontRow=Algothrim.currentLocationFrontRow-1;
				break;
			case South:
				MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-distance;
				MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy;
				MapUI.robotBodyConstrain.gridx = MapUI.robotBodyConstrain.gridx-distance;
				MapUI.robotBodyConstrain.gridy = MapUI.robotBodyConstrain.gridy;
				
				Algothrim.currentLocationFrontRow=Algothrim.currentLocationFrontRow+1;
				break;
			case East:
				MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx;
				MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+distance;
				MapUI.robotBodyConstrain.gridx = MapUI.robotBodyConstrain.gridx;
				MapUI.robotBodyConstrain.gridy = MapUI.robotBodyConstrain.gridy+distance;
				
				Algothrim.currentLocationFrontCol=Algothrim.currentLocationFrontCol+1;
				break;
			case West:
				MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx;
				MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-distance;
				MapUI.robotBodyConstrain.gridx = MapUI.robotBodyConstrain.gridx;
				MapUI.robotBodyConstrain.gridy = MapUI.robotBodyConstrain.gridy-distance;
				
				Algothrim.currentLocationFrontCol=Algothrim.currentLocationFrontCol-1;
				break;
		}
//		System.out.println("CUrrent row = "+Algothrim.currentLocationFrontRow);
//		System.out.println("CUrrent COl = "+Algothrim.currentLocationFrontCol);
	
		
		MapUI.labels[MapUI.robotBodyConstrain.gridx+1][MapUI.robotBodyConstrain.gridy].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx-1][MapUI.robotBodyConstrain.gridy].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx][MapUI.robotBodyConstrain.gridy+1].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx+1][MapUI.robotBodyConstrain.gridy+1].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx-1][MapUI.robotBodyConstrain.gridy+1].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx][MapUI.robotBodyConstrain.gridy-1].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx+1][MapUI.robotBodyConstrain.gridy-1].setText("1");
		MapUI.labels[MapUI.robotBodyConstrain.gridx-1][MapUI.robotBodyConstrain.gridy-1].setText("1");

		Algothrim.exploredData=MapUI.saveExploredData(MapUI.labels);
		
		MapUI.mapPanel.add(MapUI.robotHead, MapUI.robotHeadConstrain,1);
		MapUI.mapPanel.add(MapUI.robotBody, MapUI.robotBodyConstrain,2);
		MapUI.mapPanel.repaint();
		MapUI.mapPanel.revalidate();
	}
	
	@SuppressWarnings("incomplete-switch")
	private static void turnRobotUI(Direction direction, int times){
		MapUI.mapPanel.remove(MapUI.robotHead);
		switch(direction){
			case LEFT:
				switch(Algothrim.currentDirection){
					case North:
						if(times==1){
							Algothrim.currentDirection=Direction.West;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol -1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow +1;
						}else{
							Algothrim.currentDirection=Direction.South;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-2;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy;
							
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow +2;
						}
						break;
					case South:
						if(times==1){
							Algothrim.currentDirection=Direction.East;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx+1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol +1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow -1;
						}else{
							Algothrim.currentDirection=Direction.North;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-2;
							
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow -2;
						}
						break;
					case East:
						if(times==1){
							Algothrim.currentDirection=Direction.North;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx+1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol -1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow -1;
						}else{
							Algothrim.currentDirection=Direction.West;
							
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-2;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol -2;
						}
						break;
					case West:
						if(times==1){
							Algothrim.currentDirection=Direction.South;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol +1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow +1;
						}else{
							Algothrim.currentDirection=Direction.East;
							
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+2;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol +2;
						}
						break;
				}
				break;
			case RIGHT:
				switch(Algothrim.currentDirection){
					case North:
						if(times==1){
							Algothrim.currentDirection=Direction.East;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol +1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow +1;
						}else{
							Algothrim.currentDirection=Direction.South;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-2;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy;
							
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow +2;
						}
						break;
					case South:
						if(times==1){
							Algothrim.currentDirection=Direction.West;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx+1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol -1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow -1;
						}else{
							Algothrim.currentDirection=Direction.North;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx+2;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy;
							
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow -2;
						}
						break;
					case East:
						if(times==1){
							Algothrim.currentDirection=Direction.South;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx-1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol -1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow +1;
						}else{
							Algothrim.currentDirection=Direction.West;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy-2;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol -2;
						}
						break;
					case West:
						if(times==1){
							Algothrim.currentDirection=Direction.North;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx+1;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+1;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol +1;
							Algothrim.currentLocationFrontRow = Algothrim.currentLocationFrontRow -1;
						}else{
							Algothrim.currentDirection=Direction.East;
							
							MapUI.robotHeadConstrain.gridx = MapUI.robotHeadConstrain.gridx;
							MapUI.robotHeadConstrain.gridy = MapUI.robotHeadConstrain.gridy+2;
							
							Algothrim.currentLocationFrontCol = Algothrim.currentLocationFrontCol +2;
						}
						break;
				}
				break;
		}
		System.out.println("ROBOT FACING : "+Algothrim.currentDirection);
		MapUI.mapPanel.add(MapUI.robotHead, MapUI.robotHeadConstrain,1);
		MapUI.mapPanel.repaint();
		MapUI.mapPanel.revalidate();
	}
}
