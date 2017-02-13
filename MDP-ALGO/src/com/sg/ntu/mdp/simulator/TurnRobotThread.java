package com.sg.ntu.mdp.simulator;

import com.sg.ntu.mdp.Algothrim;
import com.sg.ntu.mdp.Direction;

public class TurnRobotThread extends Thread{

	private Direction direction;
	private int times;
	
	public TurnRobotThread(Direction direction, int times){
		this.direction = direction;
		this.times = times;
	}
	 public void run(){
	        synchronized(this){
	        	
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
	        	
	        	notify();
	        }
	 }
}
