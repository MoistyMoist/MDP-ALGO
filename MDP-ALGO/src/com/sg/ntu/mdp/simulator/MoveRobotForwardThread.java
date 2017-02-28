package com.sg.ntu.mdp.simulator;

import com.sg.ntu.mdp.Algothrim;

public class MoveRobotForwardThread extends Thread{

	private int distance;
	
	public MoveRobotForwardThread(int distance){
		this.distance = distance;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
    public void run(){
        synchronized(this){
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
    		System.out.println("CUrrent row = "+Algothrim.currentLocationFrontRow);
    		System.out.println("CUrrent COl = "+Algothrim.currentLocationFrontCol);
    	
    		
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
        	notify();
        }
    }
}
