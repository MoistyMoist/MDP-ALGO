package com.sg.ntu.mdp;

import java.util.ArrayList;
import java.util.LinkedList;

import com.sg.ntu.mdp.simulator.MapUI;

import model.AlgoNode;
import model.Direction;
import model.RobotCallback;
import model.AlgoEdge;
import model.AlgoGraph;

public class Algothrim {
	private float coverageLimit = 100f;
	
	private static boolean isGoalReached = false;
	
	public static Direction currentDirection = Direction.East;
	private float sensorTrashold = 1.00f;
	
	public static int[][]exploredData = new int[20][15];
	public static int[][]obstacleData = new int[20][15];
		
	public static int currentLocationFrontRow;
	public static int currentLocationFrontCol;
	
	public Algothrim(int[][] exploredData, int[][]obstacleData, int currentLocationFrontRow, int currentLocationFrontCol){
		//set the goal and start area to explored
		this.exploredData[19][0] = 1; this.exploredData[0][14] = 1;
		this.exploredData[19][1] = 1; this.exploredData[0][13] = 1;
		this.exploredData[19][2] = 1; this.exploredData[0][12] = 1;
		this.exploredData[18][0] = 1; this.exploredData[1][14] = 1;
		this.exploredData[18][1] = 1; this.exploredData[1][13] = 1;
		this.exploredData[18][2] = 1; this.exploredData[1][12] = 1;
		this.exploredData[17][0] = 1; this.exploredData[2][14] = 1;
		this.exploredData[17][1] = 1; this.exploredData[2][13] = 1;
		this.exploredData[17][2] = 1; this.exploredData[2][12] = 1;
		
		if(currentLocationFrontRow != -1 && currentLocationFrontCol!=-1){
			//TODO:change the starting direction base on the row,col (17,2[east),etc
			this.currentLocationFrontRow = currentLocationFrontRow;
			this.currentLocationFrontCol = currentLocationFrontCol;
		}
	}
	
	//***************************************//
	//  EXPLORATION METHODS FOR GOD			 //
	//***************************************//
	AlgoNode[][] explorationNodes = new AlgoNode[19][13];
	int[][]obstacles = new int[20][15];
	AlgoNode currentTarget;
	AlgoNode currentNode = new AlgoNode(18,1);
	boolean hasGoalReached=false;
	
	public void godsExploration(float frontMidSensor, float frontLeftSensor, float frontRightSensor, float rightSensor, float leftSensor, final RobotCallback callback){
		updateObstacle(frontMidSensor, frontLeftSensor, frontRightSensor, rightSensor, leftSensor);
		
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(buildGraphForExploration());
        dijkstra.execute(currentNode);
        
        currentNode = updateCurrentNode();
        
        
        if(currentNode.getNodeRowIndex()==1&&currentNode.getNodeColIndex()==13)
        	hasGoalReached=true;
        if(hasGoalReached==false)
        	currentTarget = explorationNodes[17][12];
        
        LinkedList<AlgoNode> path = dijkstra.getPath(currentTarget);
        
		if(path==null){
//			System.out.println("in LOOP");
			exploredData[currentNode.getNodeRowIndex()][currentNode.getNodeColIndex()]=1;
			//TOdo:test this
			boolean escloop=false;
			for(int i=explorationNodes.length-1;i>0;i--){
				for(int j=0;j<explorationNodes[i].length;j++){
					if(explorationNodes[i][j]!=null){
						if(exploredData[explorationNodes[i][j].getNodeRowIndex()][explorationNodes[i][j].getNodeColIndex()]==0){
							currentTarget = explorationNodes[i][j];
							dijkstra.execute(currentNode);
							path = dijkstra.getPath(currentTarget);
//							System.out.println("target="+currentTarget);
//							System.out.println("FACING"+currentDirection.toString());
							escloop=true;
							break;
						}	
					}
				}
				if(escloop)
					break;
			}
		}
		
		//finally decide the command to give to the robot/UI only the index(1) command	
		if(path==null){
			System.out.println("returning god");
			returnGodHome(callback);
		}else{
			AlgoNode nextNodeToTravel =  path.get(1);
//			System.out.println("NODE TO TRAVEL"+nextNodeToTravel);
//			System.out.println("CURRENT POSITION"+currentNode);
			int tempRow = -1;
	        int tempCol = -1;
			switch(currentDirection){
				case North:
					tempRow = currentLocationFrontRow+1;
					tempCol = currentLocationFrontCol;
					if(nextNodeToTravel.getNodeRowIndex()<tempRow){
						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
						callback.changeDirection(Direction.LEFT, 2);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
						callback.changeDirection(Direction.RIGHT, 1);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
						callback.changeDirection(Direction.LEFT, 1);
//						callback.moveForward(1);
					}
					break;
				case South:
					tempRow = currentLocationFrontRow-1;
					tempCol = currentLocationFrontCol;
					if(nextNodeToTravel.getNodeRowIndex()<tempRow){
						callback.changeDirection(Direction.LEFT, 2);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
						callback.changeDirection(Direction.LEFT, 1);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
						callback.changeDirection(Direction.RIGHT, 1);
//						callback.moveForward(1);
					}
					break;
				case East:
					tempRow = currentLocationFrontRow;
					tempCol = currentLocationFrontCol-1;
					if(nextNodeToTravel.getNodeRowIndex()<tempRow){
						callback.changeDirection(Direction.LEFT, 1);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
						callback.changeDirection(Direction.RIGHT, 1);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
						callback.changeDirection(Direction.LEFT, 2);
//						callback.moveForward(1);
					}
					break;
				case West:
					tempRow = currentLocationFrontRow;
					tempCol = currentLocationFrontCol+1;
					if(nextNodeToTravel.getNodeRowIndex()<tempRow){
						callback.changeDirection(Direction.RIGHT, 1);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
						callback.changeDirection(Direction.LEFT, 1);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
						callback.changeDirection(Direction.LEFT, 2);
//						callback.moveForward(1);
					}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
						callback.moveForward(1);
						
					}
					break;
			}
		}
		
	}
	public AlgoNode updateCurrentNode(){
		AlgoNode node = null;
		switch(currentDirection){
		case North:
				node = new AlgoNode(currentLocationFrontRow+1,currentLocationFrontCol);
			break;
		case South:
			node = new AlgoNode(currentLocationFrontRow-1,currentLocationFrontCol);
			break;
		case East:
			node = new AlgoNode(currentLocationFrontRow,currentLocationFrontCol-1);
			break;
		case West:
			node = new AlgoNode(currentLocationFrontRow,currentLocationFrontCol+1);
			break;
		}
		return node;
		
	}
	public AlgoGraph buildGraphForExploration(){
		ArrayList<AlgoEdge> edges = new ArrayList<>();
		ArrayList<AlgoNode> nodes = new ArrayList<>();
		explorationNodes = trimGraphForExploration(generateNodePool());
	
		
		for(int i=0;i<explorationNodes.length;i++){
			for(int j=0;j<explorationNodes[i].length;j++){
				if(explorationNodes[i][j]!=null){
//					System.out.println(nodePool[i][j]);
					nodes.add(explorationNodes[i][j]);
				}
			}
		}
		//blind the edges
		for(int i=0;i<explorationNodes.length;i++){
			for(int j=0;j<explorationNodes[i].length;j++){

				//set front node
				if((j+1)<explorationNodes[i].length){
					if(explorationNodes[i][j+1]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i][j+1].getNodeRowIndex()+","+explorationNodes[i][j+1].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i][j+1],1));
					}
				}
					
				//set left node
				if((i-1)>=0){
					if(explorationNodes[i-1][j]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i-1][j].getNodeRowIndex()+","+explorationNodes[i-1][j].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i-1][j],2));
					}
				}
					
				//set right node
				if((i+1)<explorationNodes.length){
					if(explorationNodes[i+1][j]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i+1][j].getNodeRowIndex()+","+explorationNodes[i+1][j].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i+1][j],2));
					}
				}
				
				//set back node
				if((j-1)>=0){
					if(explorationNodes[i][j-1]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i][j-1].getNodeRowIndex()+","+explorationNodes[i][j-1].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i][j-1],3));
					}
				}
			}
		}
		
		
		return new AlgoGraph(nodes, edges);
	}
	public AlgoNode[][] trimGraphForExploration(AlgoNode[][] nodePool){
		for(int i=0;i<nodePool.length;i++){
			for(int j=0;j<nodePool[i].length;j++){
				int actualrow = 18-i;
				int actualcol = j+1;
				
				//check center is obstacle
				if(obstacles[actualrow][actualcol]==1){
					nodePool[i][j]=null;
				}
				//check forward is obstacle
				if(obstacles[actualrow][actualcol+1]==1){
					nodePool[i][j]=null;
				}
				//check forwardright is obsacle
				if(obstacles[actualrow-1][actualcol+1]==1){
					nodePool[i][j]=null;
				}
				//check forwardleft is obstacle
				if(obstacles[actualrow+1][actualcol+1]==1){
					nodePool[i][j]=null;
				}
				//check back isobstacle
				if(obstacles[actualrow][actualcol-1]==1){
					nodePool[i][j]=null;
				}
				//check backright is obstacle
				if(obstacles[actualrow-1][actualcol-1]==1){
					nodePool[i][j]=null;
				}
				//check backleft is obstacle
				if(obstacles[actualrow+1][actualcol-1]==1){
					nodePool[i][j]=null;
				}
				//check right is obstacle
				if(obstacles[actualrow-1][actualcol]==1){
					nodePool[i][j]=null;
				}
				//check left is obstacle
				if(obstacles[actualrow-1][actualcol]==1){
					nodePool[i][j]=null;
				}
				
			}
		}
		return nodePool;
	}
	public void updateObstacle(float frontMidSensor, float frontLeftSensor, float frontRightSensor, float rightSensor, float leftSensor){
		if(leftSensor>=sensorTrashold){
			switch(currentDirection){
				case North: 
					if(currentLocationFrontCol-2>=0)
						updateObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case South: 
					if(currentLocationFrontCol+2<=14)
						updateObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case East: 
					if(currentLocationFrontRow-2>=0)
						updateObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
				case West: 
					if(currentLocationFrontRow+2<=19)
						updateObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
			}
		}else{
			switch(currentDirection){
			case North: 
					if(currentLocationFrontCol-2>=0)
						updateExploredArea(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case South: 
					if(currentLocationFrontCol+2<=14)
						updateExploredArea(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case East: 
					if(currentLocationFrontRow-2>=0)
						updateExploredArea(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
				case West: 
					if(currentLocationFrontRow+2<=19)
						updateExploredArea(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
			}
		}
		if(rightSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontCol+2<=14)
						updateObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case South:
					if(currentLocationFrontCol-2>=0)
						updateObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case East:
					if(currentLocationFrontRow+2<=19)
						updateObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
				case West:
					if(currentLocationFrontRow-2>=0)
						updateObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
			}
		}
		else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontCol+2<=14)
						updateExploredArea(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case South:
					if(currentLocationFrontCol-2>=0)
						updateExploredArea(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case East:
					if(currentLocationFrontRow+2<=19)
						updateExploredArea(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
				case West:
					if(currentLocationFrontRow-2>=0)
						updateExploredArea(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
			}
		}
		if(frontMidSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						updateObstacle(currentLocationFrontRow-1,currentLocationFrontCol);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						updateObstacle(currentLocationFrontRow+1,currentLocationFrontCol);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						updateObstacle(currentLocationFrontRow,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						updateObstacle(currentLocationFrontRow,currentLocationFrontCol-1);
					break;
			}
		}else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						updateExploredArea(currentLocationFrontRow-1,currentLocationFrontCol);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						updateExploredArea(currentLocationFrontRow+1,currentLocationFrontCol);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						updateExploredArea(currentLocationFrontRow,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						updateExploredArea(currentLocationFrontRow,currentLocationFrontCol-1);
					break;
			}
		}
		if(frontLeftSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						updateObstacle(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						updateObstacle(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						updateObstacle(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						updateObstacle(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
			}
		}else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						updateExploredArea(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						updateExploredArea(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						updateExploredArea(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						updateExploredArea(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
			}
		}
		
		if(frontRightSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						updateObstacle(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						updateObstacle(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						updateObstacle(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						updateObstacle(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
			}
		}else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						updateExploredArea(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						updateExploredArea(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						updateExploredArea(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						updateExploredArea(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
			}
		}
	}
	public void updateObstacle(int row, int col){
		this.obstacles[row][col] = 1;
		int newData[][] = new int[20][15];//17,3 17,11
		for(int i=0;i<20;i++){
			for(int j=0;j<15;j++){
				newData[i][j]=this.obstacles[i][Math.abs(14-j)];
			}
		}
		MapUI.updateMap(newData, 1);
	}
	public void updateExploredArea(int row, int col){
		this.exploredData[row][col] = 1;
		int newData[][] = new int[20][15];//17,3 17,11
		for(int i=0;i<20;i++){
			for(int j=0;j<15;j++){
				newData[i][j]=this.exploredData[i][Math.abs(14-j)];
			}
		}
		MapUI.updateMap(newData, 0);	
	}
	//***********************************************//
	// 			GODS RETURN FROM EXPLORATION		 //
	//***********************************************//
	boolean hasReturnedHome=false;
	public void returnGodHome(RobotCallback callback){
		
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(buildGraphForReturningHome());
        dijkstra.execute(currentNode);
        
        currentNode = updateCurrentNode();
        
        
        if(currentNode.getNodeRowIndex()==18&&currentNode.getNodeColIndex()==1)
        	hasReturnedHome=true;
        if(hasReturnedHome==false){
        	System.out.println("finding way home");
        	currentTarget = explorationNodes[0][0];
        	LinkedList<AlgoNode> path = dijkstra.getPath(currentTarget);
    		
    		//finally decide the command to give to the robot/UI only the index(1) command	
    		
    		AlgoNode nextNodeToTravel =  path.get(1);
    		System.out.println("RETURNING NODE TO TRAVEL"+nextNodeToTravel);
    		System.out.println("RETURNING CURRENT POSITION"+currentNode);
    		int tempRow = -1;
            int tempCol = -1;
    		switch(currentDirection){
    			case North:
    				tempRow = currentLocationFrontRow+1;
    				tempCol = currentLocationFrontCol;
    				if(nextNodeToTravel.getNodeRowIndex()<tempRow){
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
    					callback.changeDirection(Direction.LEFT, 2);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
    					callback.changeDirection(Direction.RIGHT, 1);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
    					callback.changeDirection(Direction.LEFT, 1);
    					callback.moveForward(1);
    				}
    				break;
    			case South:
    				tempRow = currentLocationFrontRow-1;
    				tempCol = currentLocationFrontCol;
    				if(nextNodeToTravel.getNodeRowIndex()<tempRow){
    					callback.changeDirection(Direction.LEFT, 2);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
    					callback.changeDirection(Direction.LEFT, 1);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
    					callback.changeDirection(Direction.RIGHT, 1);
    					callback.moveForward(1);
    				}
    				break;
    			case East:
    				tempRow = currentLocationFrontRow;
    				tempCol = currentLocationFrontCol-1;
    				if(nextNodeToTravel.getNodeRowIndex()<tempRow){
    					callback.changeDirection(Direction.LEFT, 1);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
    					callback.changeDirection(Direction.RIGHT, 1);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
    					callback.changeDirection(Direction.LEFT, 2);
    					callback.moveForward(1);
    				}
    				break;
    			case West:
    				tempRow = currentLocationFrontRow;
    				tempCol = currentLocationFrontCol+1;
    				if(nextNodeToTravel.getNodeRowIndex()<tempRow){
    					callback.changeDirection(Direction.RIGHT, 1);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeRowIndex()>tempRow){
    					callback.changeDirection(Direction.LEFT, 1);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()>tempCol){
    					callback.changeDirection(Direction.LEFT, 2);
    					callback.moveForward(1);
    				}else if(nextNodeToTravel.getNodeColIndex()<tempCol){
    					callback.moveForward(1);
    					
    				}
    				break;
    		}
        }else{
        	System.out.println("AT HOME LIAO");
        	callback.readyForFastestPath();
//			callback.sendRobotInstruction(null);
        }
	}
	public AlgoGraph buildGraphForReturningHome(){
		ArrayList<AlgoEdge> edges = new ArrayList<>();
		ArrayList<AlgoNode> nodes = new ArrayList<>();
		explorationNodes = trimGraphForReturningHome(generateNodePool());
		
		for(int i=0;i<explorationNodes.length;i++){
			for(int j=0;j<explorationNodes[i].length;j++){
				if(explorationNodes[i][j]!=null){
//					System.out.println(nodePool[i][j]);
					nodes.add(explorationNodes[i][j]);
				}
			}
		}
		//blind the edges
		for(int i=0;i<explorationNodes.length;i++){
			for(int j=0;j<explorationNodes[i].length;j++){

				//set front node
				if((j+1)<explorationNodes[i].length){
					if(explorationNodes[i][j+1]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i][j+1].getNodeRowIndex()+","+explorationNodes[i][j+1].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i][j+1],1));
					}
				}
					
				//set left node
				if((i-1)>=0){
					if(explorationNodes[i-1][j]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i-1][j].getNodeRowIndex()+","+explorationNodes[i-1][j].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i-1][j],2));
					}
				}
					
				//set right node
				if((i+1)<explorationNodes.length){
					if(explorationNodes[i+1][j]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i+1][j].getNodeRowIndex()+","+explorationNodes[i+1][j].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i+1][j],2));
					}
				}
				
				//set back node
				if((j-1)>=0){
					if(explorationNodes[i][j-1]!=null&&explorationNodes[i][j]!=null){
						edges.add(new AlgoEdge(explorationNodes[i][j].getNodeRowIndex()+","+explorationNodes[i][j].getNodeColIndex()+"TO"+explorationNodes[i][j-1].getNodeRowIndex()+","+explorationNodes[i][j-1].getNodeColIndex(),explorationNodes[i][j],explorationNodes[i][j-1],3));
					}
				}
			}
		}
		
		
		return new AlgoGraph(nodes, edges);
	}
	public AlgoNode[][] trimGraphForReturningHome(AlgoNode[][] nodePool){
		for(int i=0;i<nodePool.length;i++){
			for(int j=0;j<nodePool[i].length;j++){
				int actualrow = 18-i;
				int actualcol = j+1;
				
				//check center is obstacle
				if(obstacles[actualrow][actualcol]==1||exploredData[actualrow][actualcol]==0){
					nodePool[i][j]=null;
				}
				//check forward is obstacle
				if(obstacles[actualrow][actualcol+1]==1||exploredData[actualrow][actualcol+1]==0){
					nodePool[i][j]=null;
				}
				//check forwardright is obsacle
				if(obstacles[actualrow-1][actualcol+1]==1||exploredData[actualrow-1][actualcol+1]==0){
					nodePool[i][j]=null;
				}
				//check forwardleft is obstacle
				if(obstacles[actualrow+1][actualcol+1]==1||exploredData[actualrow+1][actualcol+1]==0){
					nodePool[i][j]=null;
				}
				//check back isobstacle
				if(obstacles[actualrow][actualcol-1]==1||exploredData[actualrow][actualcol-1]==0){
					nodePool[i][j]=null;
				}
				//check backright is obstacle
				if(obstacles[actualrow-1][actualcol-1]==1||exploredData[actualrow-1][actualcol-1]==0){
					nodePool[i][j]=null;
				}
				//check backleft is obstacle
				if(obstacles[actualrow+1][actualcol-1]==1||exploredData[actualrow+1][actualcol-1]==0){
					nodePool[i][j]=null;
				}
				//check right is obstacle
				if(obstacles[actualrow-1][actualcol]==1||exploredData[actualrow-1][actualcol]==0){
					nodePool[i][j]=null;
				}
				//check left is obstacle
				if(obstacles[actualrow-1][actualcol]==1||exploredData[actualrow-1][actualcol]==0){
					nodePool[i][j]=null;
				}
				
			}
		}
		return nodePool;
	}
	
	//***************************************//
	//  EXPLORATION METHODS FOR SIMULATION	 //
	//***************************************//
	
/*
	public void exploreSimulation(float frontMidSensor, float frontLeftSensor, float frontRightSensor, float rightSensor, float leftSensor, final RobotCallback callback){
		if(isExporeCoverageReached()){
			returnToStart(callback);
		}else{
			
			if(leftSensor>=sensorTrashold){
//				System.out.println("OBSTICAL AT LEFT SENSOR");
				//obstacle on robot left
				//update the obstacle data
				switch(currentDirection){
					case North: 
						if(currentLocationFrontCol-2>=0)
//							if(currentLocationFrontRow>=0){
								addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
//							}
						break;
					case South: 
						if(currentLocationFrontCol+2<=14)
							addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
						break;
					case East: 
						if(currentLocationFrontRow-2>=0)
							addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
						break;
					case West: 
						if(currentLocationFrontRow+2<=19)
							addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
						break;
				}
			}else{
				switch(currentDirection){
					case North: 
						if(currentLocationFrontCol-2>=0)
//							if(currentLocationFrontRow>=0){
								addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol-2);
//							}
						break;
					case South: 
						if(currentLocationFrontCol+2<=14)
							addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol+2);
						break;
					case East: 
						if(currentLocationFrontRow-2>=0)
							addExploredAreaBySensor(currentLocationFrontRow-2,currentLocationFrontCol);
						break;
					case West: 
						if(currentLocationFrontRow+2<=19)
							addExploredAreaBySensor(currentLocationFrontRow+2,currentLocationFrontCol);
						break;
				}
			}
			if(rightSensor>=sensorTrashold){
				//obstacle on robot right
				//update the obstacle data
				switch(currentDirection){
					case North:
						if(currentLocationFrontCol+2<=14)
//							if(currentLocationFrontRow>=0){
								addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
//							}
						break;
					case South:
						if(currentLocationFrontCol-2>=0)
							addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
						break;
					case East:
						if(currentLocationFrontRow+2<=19)
							addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
						break;
					case West:
						if(currentLocationFrontRow-2>=0)
							addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
						break;
				}
			}else{
				switch(currentDirection){
					case North:
						if(currentLocationFrontCol+2<=14)
//							if(currentLocationFrontRow>=0){
								addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol+2);
//							}
						break;
					case South:
						if(currentLocationFrontCol-2>=0)
							addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol-2);
						break;
					case East:
						if(currentLocationFrontRow+2<=19)
							addExploredAreaBySensor(currentLocationFrontRow+2,currentLocationFrontCol);
						break;
					case West:
						if(currentLocationFrontRow-2>=0)
							addExploredAreaBySensor(currentLocationFrontRow-2,currentLocationFrontCol);
						break;
				}
			}
			if(frontMidSensor>=sensorTrashold){
				//obstacle on robot mid front
				//update the obstacle data
				switch(currentDirection){
					case North:
						if(currentLocationFrontRow-1>=0)
							addObstacle(currentLocationFrontRow-1,currentLocationFrontCol);
						break;
					case South:
						if(currentLocationFrontRow+1<=19)
							addObstacle(currentLocationFrontRow+1,currentLocationFrontCol);
						break;
					case East:
						if(currentLocationFrontCol+1<=14)
							addObstacle(currentLocationFrontRow,currentLocationFrontCol+1);
						break;
					case West:
						if(currentLocationFrontCol-1>=0)
							addObstacle(currentLocationFrontRow,currentLocationFrontCol-1);
						break;
				}
			}else{
				switch(currentDirection){
					case North:
						if(currentLocationFrontRow-1>=0)
							addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol);
						break;
					case South:
						if(currentLocationFrontRow+1<=19)
							addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol);
						break;
					case East:
						if(currentLocationFrontCol+1<=14)
							addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol+1);
						break;
					case West:
						if(currentLocationFrontCol-1>=0)
							addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol-1);
						break;
				}
			}
			if(frontLeftSensor>=sensorTrashold){
				//obstacle on robot front left
				//update the obstacle data
				switch(currentDirection){
					case North:
						if(currentLocationFrontRow-1>=0)
							addObstacle(currentLocationFrontRow-1,currentLocationFrontCol-1);
						break;
					case South:
						if(currentLocationFrontRow+1<=19)
							addObstacle(currentLocationFrontRow+1,currentLocationFrontCol+1);
						break;
					case East:
						if(currentLocationFrontCol+1<=14)
							addObstacle(currentLocationFrontRow-1,currentLocationFrontCol+1);
						break;
					case West:
						if(currentLocationFrontCol+1<=14)
							addObstacle(currentLocationFrontRow+1,currentLocationFrontCol-1);
						break;
				}
			}else{
				switch(currentDirection){
					case North:
						if(currentLocationFrontRow-1>=0)
							addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol-1);
						break;
					case South:
						if(currentLocationFrontRow+1<=19)
							addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol+1);
						break;
					case East:
						if(currentLocationFrontCol+1<=14)
							addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol+1);
						break;
					case West:
						if(currentLocationFrontCol-1>=0)
							addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol-1);
						break;
				}
			}
			if(frontRightSensor>=sensorTrashold){
//				System.out.println("OBSTICAL AT FRONT RIGHT SENSOR");
				//obstacle on robot front right
				//TODO:update the obstacle data
				switch(currentDirection){
					case North:
						if(currentLocationFrontRow-1>=0)
							addObstacle(currentLocationFrontRow-1,currentLocationFrontCol+1);
						break;
					case South:
						if(currentLocationFrontRow+1<=19)
							addObstacle(currentLocationFrontRow+1,currentLocationFrontCol-1);
						break;
					case East:
						if(currentLocationFrontCol+1<=14)
							addObstacle(currentLocationFrontRow+1,currentLocationFrontCol+1);
						break;
					case West:
						if(currentLocationFrontCol-1>=0)
							addObstacle(currentLocationFrontRow-1,currentLocationFrontCol-1);
						break;
				}
			}else{
				switch(currentDirection){
					case North:
						if(currentLocationFrontRow-1>=0)
							addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol+1);
						break;
					case South:
						if(currentLocationFrontRow+1<=19)
							addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol-1);
						break;
					case East:
						if(currentLocationFrontCol+1<=14)
							addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol+1);
						break;
					case West:
						if(currentLocationFrontCol-1>=0)
							addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol-1);
						break;
				}
			}
			if(currentLocationFrontRow<3&&currentLocationFrontCol>11){
				isGoalReached=true;
			}
			
			if(isGoalReached==true){
				boolean breakloop = false;
				
				
				
				for(int i=19;i>=0;i--){
					for(int j=0;j<=14;j++){
						if(exploredData[i][j]==0&&obstacleData[i][j]==0){
								breakloop=true;
								switch(currentDirection){
								case North:
									System.out.println("TARGETN"+i+","+j);
									makeDecisionFacingNorth(i,j,callback);
									break;
								case South:
									System.out.println("TARGETS"+i+","+j);
									makeDecisionFacingSouth(i,j,callback);
									break;
								case East:
									System.out.println("TARGETE"+i+","+j);
									makeDecisionFacingEast(i,j,callback);
									break;
								case West:
									System.out.println("TARGETW"+i+","+j);
									makeDecisionFacingWest(i,j,callback);
									break;
								}
								callback.sendRobotInstruction(null);
							
//							System.out.println(breakloop);
					
							if(breakloop==true)
								break;
						}
					}
					if(breakloop==true)
						break;
				}
			}else{
				switch(currentDirection){
				case North:
					makeDecisionFacingNorth(1,14,callback);
					break;
				case South:
					makeDecisionFacingSouth(1,14,callback);
					break;
				case East:
					makeDecisionFacingEast(1,14,callback);
					break;
				case West:
					makeDecisionFacingWest(1,14,callback);
					break;
				}
				callback.sendRobotInstruction(null);
			}
			
			
		}
	}
	
	private void makeDecisionFacingNorth(int row, int col,final RobotCallback callback){
		System.out.println("INSTRUCTION FACING NORTH");
		int counter = 0;
		int exploredCounter = 0;
		boolean isLeftWall = false;
		boolean isRightWall = false;
		boolean isFrontWall = false;
		boolean isBackWall = false;
		
		boolean isLeftExplored = false;
		int []leftUnExploredPositionCount = {0,0,0};
		boolean isRightExplored = false;
		int []rightUnExploredPositionCount = {0,0,0};
		boolean isBackExplored = false;
		int []backUnExploredPositionCount = {0,0,0};
		boolean isFrontExplored = false;
		int []frontUnExploredPositionCount = {0,0,0};
		
		boolean isRightObstacle = false;
		int []rightObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isLeftObstacle = false;
		int []leftObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isFrontObstacle = false;
		int []FrontObstaclePositionCount = {0,0,0};//index 0 = left of head
		boolean isBackObstacle = false;
		int []BackObstaclePositionCount = {0,0,0};//index 0 = left of head
		//check for walls
		if(currentLocationFrontRow<0){
			currentLocationFrontRow=0;
		}
		if(currentLocationFrontCol+2>=15){
			isRightWall = true;
		}
		if(currentLocationFrontCol-2<0){
			isLeftWall = true;
		}
		if(currentLocationFrontRow-1<0){
			System.out.println("FRONT IS WALL");
			isFrontWall = true;
		}
		if(currentLocationFrontRow+3>19){
			isBackWall = true;
		}
		//check for obstacle
		if(isRightWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol+2]==0){
					rightUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol+2]==0){
					rightUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol+2]==0){
					rightUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isRightObstacle = false;
			}else{
				isRightObstacle = true;
			}
			if(exploredCounter==3){
				isRightExplored =true;
			}else{
				isRightExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isLeftWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol-2]==0){
					leftUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol-2]==0){
					leftUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol-2]==0){
					leftUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isLeftObstacle = false;
			}else{
				isLeftObstacle = true;
			}
			if(exploredCounter==3){
				isLeftExplored =true;
			}else{
				isLeftExplored =false;
			}
			System.out.println("exporeed counter= "+exploredCounter);
			exploredCounter=0;
			counter = 0;
		}
		if(isFrontWall==false){
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol]==0){
					frontUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[1]=1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol+1]==0){
					frontUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[2]=1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol-1]==0){
					frontUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[0] = 1;
			}
			if(counter==3){
				isFrontObstacle = false;
			}else{
				isFrontObstacle = true;
			}
			if(exploredCounter==3){
				isFrontExplored =true;
			}else{
				isFrontExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isBackWall==false){
			if(obstacleData[currentLocationFrontRow+3][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+3][currentLocationFrontCol]==0){
					backUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow+3][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+3][currentLocationFrontCol+1]==0){
					backUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[2] = 1;
			}
			if(obstacleData[currentLocationFrontRow+3][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+3][currentLocationFrontCol-1]==0){
					backUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[0] = 1;
			}
			if(counter==3){
				isBackObstacle = false;
			}else{
				isBackObstacle = true;
			}
			if(exploredCounter==3){
				isBackExplored =true;
			}else{
				isBackExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		
		
		
		//FACING NORTH TARGET FRONT
		if((currentLocationFrontRow>row&&currentLocationFrontCol==col)||(currentLocationFrontRow>row&&currentLocationFrontCol-1==col)||(currentLocationFrontRow>row&&currentLocationFrontCol+1==col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET FRONT");
			if(isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isRightObstacle==false&&isRightWall==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					if(rightObstaclePositionCount[2]==1){
						callback.moveForward(1);
						callback.moveForward(1);
						callback.moveForward(1);
					}else if(rightObstaclePositionCount[1]==1){
						callback.moveForward(1);
						callback.moveForward(1);
					}else{
						callback.moveForward(1);
					}
					callback.changeDirection(Direction.RIGHT, 1);
				}
				else{
					System.out.println("ERROR: FACING NORTH TARGET FRONT");
					
				}
			}
		}
		
		//FACING NORTH TARGET LEFT
		else if((currentLocationFrontRow==row&&currentLocationFrontCol>col)||(currentLocationFrontRow+2==row&&currentLocationFrontCol>col)||(currentLocationFrontRow+1==row&&currentLocationFrontCol>col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET LEFT");
			if(isLeftWall==false&& isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING NORTH TARGET LEFT");
				}
				
			}
		}
				
		//FACING NORTH TARGET RIGHT
		else if((currentLocationFrontRow==row&&currentLocationFrontCol<col)||(currentLocationFrontRow==row+1&&currentLocationFrontCol<col)||(currentLocationFrontRow+2==row&&currentLocationFrontCol<col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET RIGHT");
			if(isRightWall==false && isRightObstacle ==false){
				callback.changeDirection(Direction.RIGHT, 1);
				callback.moveForward(1);
			}
			else if(isFrontWall==false && isFrontObstacle == false){
				callback.moveForward(1);
			}
			else if(isLeftWall==false && isLeftObstacle == false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}
			else if(isBackWall == false && isBackObstacle==false){
				callback.changeDirection(Direction.RIGHT, 2);
				callback.moveForward(1);
			}
		}
		//FACING NORTH TARGET BOTTOM
		else if((currentLocationFrontRow<row&&currentLocationFrontCol==col)||(currentLocationFrontRow<row&&currentLocationFrontCol-1==col)||(currentLocationFrontRow<row&&currentLocationFrontCol+1==col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET BOTTOM");
			if(isRightExplored==false&&isRightObstacle==false&&isRightWall==false){
					callback.changeDirection(Direction.RIGHT, 1);
			}
			else if(isLeftExplored==false&&isLeftObstacle==false&&isLeftWall==false){
					callback.changeDirection(Direction.LEFT, 1);
			}else{
				if(isFrontWall==false&& isFrontObstacle){
					callback.moveForward(1);
				}else{
					
				}
			}
		}
		
		//FACING NORTH TARGET FRONT LEFT
		else if((currentLocationFrontRow>row&&currentLocationFrontCol>col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET FRONT LEFT");
			if(isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING NORTH TARGET FRONT LEFT");
				}
			}
			
		}
		//FACING NORTH TARGET FRONT RIGHT
		else if((currentLocationFrontRow>row&&currentLocationFrontCol<col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET FRONT RIGHT");
			if(isFrontWall==false&&isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING NORTH TARGET FRONT RIGHT");
				}
			}
		}

		//FACING NORTH TARGET BOTTOM LEFT
		else if((currentLocationFrontRow<row&&currentLocationFrontCol>col)){
			System.out.println("INSTRUCTION FACING NORTH TARGET BOTTOM LEFT");
			if(isLeftWall==false&&isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING NORTH TARGET BOTTOM LEFT");
				}
			}
			
		}
		
		//FACING NORTH TARGET BOTTOM RIGHT
		else if(currentLocationFrontRow<row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING NORTH TARGET BOTTOM RIGHT");
			if(isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
				callback.moveForward(1);
			}
			else{
				if(isFrontObstacle==false&&isFrontWall==false){
					callback.moveForward(1);
				}else if(isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING NORTH TARGET BOTTOM RIGHT");
				}
				
				
			}
		}
	
		
	}
	private void makeDecisionFacingSouth(int row, int col, final RobotCallback callback){
		int counter = 0;
		int exploredCounter = 0;
		boolean isLeftWall = false;
		boolean isRightWall = false;
		boolean isFrontWall = false;
		boolean isBackWall = false;
		
		boolean isLeftExplored = false;
		int []leftUnExploredPositionCount = {0,0,0};
		boolean isRightExplored = false;
		int []rightUnExploredPositionCount = {0,0,0};
		boolean isBackExplored = false;
		int []backUnExploredPositionCount = {0,0,0};
		boolean isFrontExplored = false;
		int []frontUnExploredPositionCount = {0,0,0};
		
		boolean isRightObstacle = false;
		int []rightObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isLeftObstacle = false;
		int []leftObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isFrontObstacle = false;
		int []FrontObstaclePositionCount = {0,0,0};//index 0 = left of head
		boolean isBackObstacle = false;
		int []BackObstaclePositionCount = {0,0,0};//index 0 = left of head
		
		if(currentLocationFrontCol-2<0){
			isRightWall = true;
		}
		if(currentLocationFrontCol+2>14){
			isLeftWall = true;
		}
		if(currentLocationFrontRow+1>19){
			isFrontWall = true;
		}
		if(currentLocationFrontRow-3<0){
			isBackWall = true;
		}
		
		if(isRightWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol-2]==0){
					rightUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol-2]==0){
					rightUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol-2]==0){
					rightUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isRightObstacle = false;
			}else{
				isRightObstacle = true;
			}
			if(exploredCounter==3){
				isRightExplored =true;
			}else{
				isRightExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isLeftWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol+2]==0){
					leftUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol+2]==0){
					leftUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol+2]==0){
					leftUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isLeftObstacle = false;
			}else{
				isLeftObstacle = true;
			}
			if(exploredCounter==3){
				isLeftExplored =true;
			}else{
				isLeftExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isFrontWall==false){
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol]==0){
					frontUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[1]=1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol+1]==0){
					frontUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[0]=1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol-1]==0){
					frontUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isFrontObstacle = false;
			}else{
				isFrontObstacle = true;
			}
			if(exploredCounter==3){
				isFrontExplored =true;
			}else{
				isFrontExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isBackWall==false){
			if(obstacleData[currentLocationFrontRow-3][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-3][currentLocationFrontCol]==0){
					backUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-3][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-3][currentLocationFrontCol+1]==0){
					backUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow-3][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-3][currentLocationFrontCol-1]==0){
					backUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isBackObstacle = false;
			}else{
				isBackObstacle = true;
			}
			if(exploredCounter==3){
				isBackExplored =true;
			}else{
				isBackExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		
		//FACING SOUTH TARGET IS FRONT
		if((currentLocationFrontRow<row&&currentLocationFrontCol==col)||(currentLocationFrontRow<row&&currentLocationFrontCol==col-1)||(currentLocationFrontRow<row&&currentLocationFrontCol==col+1)){
			System.out.println("INSTRUCTION FACING SOUTH TARGET FRONT");
			if(isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING SOUTH TARGET FRONT");
				}
				
			}
		}
		//FACING SOUTH TARGET IS LEFT
		else if((currentLocationFrontRow==row&&currentLocationFrontCol<col)||(currentLocationFrontRow-1==row&&currentLocationFrontCol<col)||(currentLocationFrontRow-2==row&&currentLocationFrontCol<col)){
			System.out.println("INSTRUCTION FACING SOUTH TARGET LEFT");
			if(isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}
				else if(isBackWall=false&&isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}else{
					System.out.println("ERROR: FACING SOUTH TARGET LEFT");
				}
			}
		}
		
		//FACING SOUTH TARGET RIGHT
		else if((currentLocationFrontRow==row&&currentLocationFrontCol>col)||(currentLocationFrontRow-1==row&&currentLocationFrontCol>col)||(currentLocationFrontRow-2==row&&currentLocationFrontCol>col)){
			System.out.println("INSTRUCTION FACING SOUTH TARGET RIGHT");
			if(isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING SOUTH TARGET RIGHT");
				}
				
			}
		}
		//FACING SOUTH TARGET BOTTOM
		else if((currentLocationFrontRow>row&&currentLocationFrontCol==col)||(currentLocationFrontRow>row&&currentLocationFrontCol+1==col)||(currentLocationFrontRow>row&&currentLocationFrontCol-1==col)){
			System.out.println("INSTRUCTION FACING SOUTH TARGET BOTTOM");
			if(isBackObstacle==false){
				callback.changeDirection(Direction.RIGHT, 2);
				callback.moveForward(1);
			}else{
				System.out.println("ERROR: FACING SOUTH TARGET BOTTOM");
			}
		}
		
		
		
		//FACING SOUTH TARGET TOP RIGHT
		else if(currentLocationFrontRow<row&&currentLocationFrontCol>col){
			System.out.println("INSTRUCTION FACING SOUTH TARGET TOP RIGHT");
			if(isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isBackWall==false&& isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					if(rightObstaclePositionCount[2]==1){
						callback.moveForward(1);
						callback.moveForward(1);
						callback.moveForward(1);
						
					}else if(rightObstaclePositionCount[1]==1){
						callback.moveForward(1);
						callback.moveForward(1);
					}else{
						callback.moveForward(1);
					}
					callback.changeDirection(Direction.LEFT, 1);
				}
				else{
					System.out.println("ERROR: FACING SOUTH TARGET TOP RIGHT");
				}
				
			}
		}
		//FACING SOUTH TARGET TOP LEFT
		else if(currentLocationFrontRow<row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING SOUTH TARGET TOP LEFT");
			if(isFrontWall==false&&isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING SOUTH TARGET TOP LEFT");
				}
			}
		}
		
		//FACING SOUTH TARGET BOTTOM LEFT
		else if(currentLocationFrontRow>row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING SOUTH TARGET BOTTOM LEFT");
			if(isLeftWall==false&&isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}
				else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}else{
					System.out.println("ERROR: FACING SOUTH TARGET BOTTOM LEFT");
				}
			}
		}
		
		//FACING SOUTH TARGET BOTTOM RIGHT
		else if(currentLocationFrontRow>row&&currentLocationFrontCol>col){
			System.out.println("INSTRUCTION FACING SOUTH TARGET BOTTOM RIGHT");
			if(isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING SOUTH TARGET BOTTOM RIGHT");
				}
			}
		}
		
	}
	private void makeDecisionFacingEast(int row, int col, final RobotCallback callback){
		int counter = 0;
		int exploredCounter = 0;
		boolean isLeftWall = false;
		boolean isRightWall = false;
		boolean isFrontWall = false;
		boolean isBackWall = false;
		
		boolean isLeftExplored = false;
		int []leftUnExploredPositionCount = {0,0,0};
		boolean isRightExplored = false;
		int []rightUnExploredPositionCount = {0,0,0};
		boolean isBackExplored = false;
		int []backUnExploredPositionCount = {0,0,0};
		boolean isFrontExplored = false;
		int []frontUnExploredPositionCount = {0,0,0};
		
		boolean isRightObstacle = false;
		int []rightObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isLeftObstacle = false;
		int []leftObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isFrontObstacle = false;
		int []FrontObstaclePositionCount = {0,0,0};//index 0 = left of head
		boolean isBackObstacle = false;
		int []BackObstaclePositionCount = {0,0,0};//index 0 = left of head
		
		if(currentLocationFrontRow+2>19){
			isRightWall = true;
		}
		if(currentLocationFrontRow-2<0){
			isLeftWall = true;
		}
		if(currentLocationFrontCol+1>14){
			isFrontWall = true;
		}
		if(currentLocationFrontCol-3<0){
			isBackWall = true;
		}
		if(isRightWall==false){
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol]==0){
					rightUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol-1]==0){
					rightUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol-2]==0){
					rightUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isRightObstacle = false;
			}else{
				isRightObstacle = true;
			}
			if(exploredCounter==3){
				isRightExplored =true;
			}else{
				isRightExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isLeftWall==false){
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol]==0){
					leftUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol-1]==0){
					leftUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol-2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol-2]==0){
					leftUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isLeftObstacle = false;
			}else{
				isLeftObstacle = true;
			}
			if(exploredCounter==3){
				isLeftExplored =true;
			}else{
				isLeftExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isFrontWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol+1]==0){
					frontUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[1]=1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol+1]==0){
					frontUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[2]=1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol+1]==0){
					frontUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[0] = 1;
			}
			if(counter==3){
				isFrontObstacle = false;
			}else{
				isFrontObstacle = true;
			}
			if(exploredCounter==3){
				isFrontExplored =true;
			}else{
				isFrontExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isBackWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol-3]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol-3]==0){
					backUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol-3]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol-3]==0){
					backUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol-3]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol-3]==0){
					backUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isBackObstacle = false;
			}else{
				isBackObstacle = true;
			}
			if(exploredCounter==3){
				isBackExplored =true;
			}else{
				isBackExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		
		
		
		//FACING EAST TARGET FRONT
		if((currentLocationFrontRow==row&&currentLocationFrontCol<col)||(currentLocationFrontRow-1==row&&currentLocationFrontCol<col)||(currentLocationFrontRow+1==row&&currentLocationFrontCol<col)){					
			System.out.println("INSTRUCTION FACING EAST TARGET FRONT");
			if(isFrontWall==false && isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					if(leftObstaclePositionCount[2]==1){
						callback.moveForward(1);
						callback.moveForward(1);
						callback.moveForward(1);
					}else if(leftObstaclePositionCount[1]==1){
						callback.moveForward(1);
						callback.moveForward(1);
					}else{
						callback.moveForward(1);
					}
					callback.changeDirection(Direction.RIGHT, 1);
				}
				else{
					System.out.println("ERROR: FACING EAST TARGET INFRONT");
				}
			}
		}
		
		
		//FACING EAST TARGET LEFT
		else if((currentLocationFrontRow>row&& currentLocationFrontCol==col)||(currentLocationFrontRow>row&& currentLocationFrontCol-1==col)||(currentLocationFrontRow>row&& currentLocationFrontCol-2==col)){
			System.out.println("INSTRUCTION FACING EAST TARGET LEFT");
			if(isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING EAST TARGET LEFT");
				}
			}
		}
		
		//FACING EAST TARGET RIGHT
		else if((currentLocationFrontRow<row&& currentLocationFrontCol==col)||(currentLocationFrontRow<row&& currentLocationFrontCol-1==col)||(currentLocationFrontRow<row&& currentLocationFrontCol-2==col)){
			System.out.println("INSTRUCTION FACING EAST TARGET RIGHT");
			if(isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}else if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}
				else {
					System.out.println("ERROR: FACING EAST TARGET RIGHT");
				}
			}
		}
		//FACING EAST TARGET BOTTOM
		else if((currentLocationFrontRow==row&&currentLocationFrontCol>col)||(currentLocationFrontRow-1==row&&currentLocationFrontCol>col)||(currentLocationFrontRow+1==row&&currentLocationFrontCol>col)){
			System.out.println("INSTRUCTION FACING EAST TARGET BOTTOM");
			if(isBackWall==false&&isBackObstacle==false){
				callback.changeDirection(Direction.LEFT, 2);
				callback.moveForward(1);
			}else{
				System.out.println("ERROR: FACING EAST TARGET BOTTOM");
			}
		}
		
		
		
		//FACING EAST TARGET TOP RIGHT
		else if(currentLocationFrontRow<row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING EAST TARGET TOP RIGHT");
			if(isFrontWall==false && isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isLeftWall==false&&isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING EAST TARGET TOP RIGHT");
				}	
			}
		}
		
		//FACING EAST TARGET TOP LEFT
		else if(currentLocationFrontRow>row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING EAST TARGET TOP LEFT");
			if(isFrontWall==false&&isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isBackWall==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					
					System.out.println("ERROR: FACING EAST TARGET TOP LEFT");
				}
			}
			
		}
		
		//FACING EAST TARGET BOTTOM RIGHT
		else if(currentLocationFrontRow<row&&currentLocationFrontCol>col){
			System.out.println("INSTRUCTION FACING EAST TARGET BOTTOM RIGHT");
			if(isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
			
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackObstacle==false&&isBackWall==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING EAST TARGET BOTTOM RIGHT");
				}
				
			}
		}
		
		//FACING EAST TARGET BOTTOM LEFT
		else if(currentLocationFrontRow>row&&currentLocationFrontCol>col){
			System.out.println("INSTRUCTION FACING EAST TARGET BOTTOM LEFT");
			if(isLeftWall==false&& isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}else{
					System.out.println("ERROR: FACING EAST TARGET BOTTOM LEFT");
				}
			}
		}else{
			System.out.println(currentLocationFrontRow);
			System.out.println(currentLocationFrontCol);
			System.out.println("STUCK FACING EAST");
		}
		
	}
	private void makeDecisionFacingWest(int row, int col, final RobotCallback callback){
		int counter = 0;
		int exploredCounter = 0;
		boolean isLeftWall = false;
		boolean isRightWall = false;
		boolean isFrontWall = false;
		boolean isBackWall = false;
		
		boolean isLeftExplored = false;
		int []leftUnExploredPositionCount = {0,0,0};
		boolean isRightExplored = false;
		int []rightUnExploredPositionCount = {0,0,0};
		boolean isBackExplored = false;
		int []backUnExploredPositionCount = {0,0,0};
		boolean isFrontExplored = false;
		int []frontUnExploredPositionCount = {0,0,0};
		
		boolean isRightObstacle = false;
		int []rightObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isLeftObstacle = false;
		int []leftObstaclePositionCount = {0,0,0};//index 0 = nearest to head
		boolean isFrontObstacle = false;
		int []FrontObstaclePositionCount = {0,0,0};//index 0 = left of head
		boolean isBackObstacle = false;
		int []BackObstaclePositionCount = {0,0,0};//index 0 = left of head
		
		if(currentLocationFrontRow-2<0){
			isRightWall = true;
		}
		if(currentLocationFrontRow+2>19){
			isLeftWall = true;
		}
		if(currentLocationFrontCol-1<0){
			isFrontWall = true;
		}
		if(currentLocationFrontCol+3>14){
			isBackWall = true;
		}
		if(isRightWall==false){
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol]==0){
					rightUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol+1]==0){
					rightUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				rightObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-2][currentLocationFrontCol+2]!=1){
				counter++;
			}else{
				if(exploredData[currentLocationFrontRow-2][currentLocationFrontCol+2]==0){
					rightUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
				rightObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isRightObstacle = false;
			}else{
				isRightObstacle = true;
			}
			if(exploredCounter==3){
				isRightExplored =true;
			}else{
				isRightExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isLeftWall==false){
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol]==0){
					leftUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[0] = 1;
			}
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol+1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol+1]==0){
					leftUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow+2][currentLocationFrontCol+2]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+2][currentLocationFrontCol+2]==0){
					leftUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				leftObstaclePositionCount[2] = 1;
			}
			if(counter==3){
				isLeftObstacle = false;
			}else{
				isLeftObstacle = true;
			}
			if(exploredCounter==3){
				isLeftExplored =true;
			}else{
				isLeftExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isFrontWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol-1]==0){
					frontUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[1]=1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol-1]==0){
					frontUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[2]=1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol-1]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol-1]==0){
					frontUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				FrontObstaclePositionCount[0] = 1;
			}
			if(counter==3){
				isFrontObstacle = false;
			}else{
				isFrontObstacle = true;
			}
			if(exploredCounter==3){
				isFrontExplored =true;
			}else{
				isFrontExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		if(isBackWall==false){
			if(obstacleData[currentLocationFrontRow][currentLocationFrontCol+3]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow][currentLocationFrontCol+3]==0){
					backUnExploredPositionCount[1]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[1] = 1;
			}
			if(obstacleData[currentLocationFrontRow-1][currentLocationFrontCol+3]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow-1][currentLocationFrontCol+3]==0){
					backUnExploredPositionCount[2]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[2] = 1;
			}
			if(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol+3]!=1){
				counter++;
				if(exploredData[currentLocationFrontRow+1][currentLocationFrontCol+3]==0){
					backUnExploredPositionCount[0]=1;
					exploredCounter++;
				}
			}else{
				BackObstaclePositionCount[0] = 1;
			}
			if(counter==3){
				isBackObstacle = false;
			}else{
				isBackObstacle = true;
			}
			if(exploredCounter==3){
				isBackExplored =true;
			}else{
				isBackExplored =false;
			}
			exploredCounter=0;
			counter = 0;
		}
		
		
		
		
		
		
		
		//FACING WEST TARGET FRONT
		if((currentLocationFrontRow==row&&currentLocationFrontCol>col)||(currentLocationFrontRow-1==row&&currentLocationFrontCol>col)||(currentLocationFrontRow+1==row&&currentLocationFrontCol>col)){
			System.out.println("INSTRUCTION FACING WEST TARGET FRONT");
			if(isFrontWall==false&& isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isRightObstacle==false&&isRightWall==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
					
				}else if(isLeftObstacle==false&&isLeftWall==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					if(leftObstaclePositionCount[2]==1){
						callback.moveForward(1);
						callback.moveForward(1);
						callback.moveForward(1);
					}else if(leftObstaclePositionCount[1]==1){
						callback.moveForward(1);
						callback.moveForward(1);
					}else{
						callback.moveForward(1);
					}
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING WEST TARGET FRONTT");
				}
			}
		}
		
		//FACING WEST TARGET BACK
		else if((currentLocationFrontRow==row&&currentLocationFrontCol<col)||(currentLocationFrontRow-1==row&&currentLocationFrontCol<col)||(currentLocationFrontRow+1==row&&currentLocationFrontCol<col)){	
			System.out.println("INSTRUCTION FACING WEST TARGET BACK");
			if(isBackWall==false&&isBackObstacle==false){
				callback.changeDirection(Direction.LEFT, 2);
				callback.moveForward(1);
			}else{
				System.out.println(isBackWall);
				System.out.println("ERROR: FACING WEST TARGET BACK");
			}
		}
		//FACING WEST TARGET LEFT
		else if((currentLocationFrontRow<row&&currentLocationFrontCol==col)||(currentLocationFrontRow<row&&currentLocationFrontCol+1==col)||(currentLocationFrontRow<row&&currentLocationFrontCol+2==col)){
			System.out.println("INSTRUCTION FACING WEST TARGET LEFT");
			if(isLeftWall==false&&isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if((isFrontWall==true||isFrontObstacle==true)&&isLeftObstacle==true&&(isBackWall==false&&isBackObstacle==false)){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING WEST TARGET LEFT");
				}
				
			}
		}
		
		//FACING WEST TARGET RIGHT
		else if((currentLocationFrontRow>row&&currentLocationFrontCol==col)||(currentLocationFrontRow>row&&currentLocationFrontCol+1==col)||(currentLocationFrontRow>row&&currentLocationFrontCol+2==col)){
			System.out.println("INSTRUCTION FACING WEST TARGET RIGHT");
			if(isRightWall==false&&isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING WEST TARGET RIGHT");
				}
				
			}
		}
		
		//target grid top right of robot
		else if(currentLocationFrontRow>row&&currentLocationFrontCol>col){
			System.out.println("INSTRUCTION FACING WEST TARGET TOP RIGHT");
			if(isFrontWall==false&&isFrontObstacle==false){
				callback.moveForward(1);;
			}else{
				if(isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else if(isBackObstacle){
					
				}
				else if(isLeftObstacle==false&&isLeftWall==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}
				else{
					System.out.println("left wall "+isLeftWall);
					System.out.println("left OBS "+isLeftObstacle);
					System.out.println("ERROR: FACING WEST TARGET TOP RIGHT");
				}
			}
		}
		//target grid top left of robot
		else if(currentLocationFrontRow<row&&currentLocationFrontCol>col){
			System.out.println("INSTRUCTION FACING WEST TARGET TOP LEFT");
			if(isFrontWall==false&&isFrontObstacle==false){
				callback.moveForward(1);
			}else{
				if(isLeftObstacle==false){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isRightWall==false&&isRightObstacle==false){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}
				else if(isBackWall==false&&isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}
				else{
					System.out.println("ERROR: FACING WEST TARGET TOP LEFT");
				}
				
			}
			
		}
		
		//target grid bottom right of robot
		else if(currentLocationFrontRow>row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING WEST TARGET BOTTOM RIGHT");
			if(isRightWall==false&&isRightObstacle==false){
				callback.changeDirection(Direction.RIGHT, 1);
				callback.moveForward(1);
			}else{
				if(isFrontWall==false&&isFrontObstacle==false){
					callback.moveForward(1);
				}
				else if(isBackObstacle==false){
					callback.changeDirection(Direction.RIGHT, 2);
					callback.moveForward(1);
				}else{
					System.out.println("ERROR: FACING WEST TARGET BOTTOM RIGHT");
				}
			}
			
		}
		//target grid bottom left of robot
		else if(currentLocationFrontRow<row&&currentLocationFrontCol<col){
			System.out.println("INSTRUCTION FACING WEST TARGET BOTTOM LEFT");
			if(isLeftObstacle==false){
				callback.changeDirection(Direction.LEFT, 1);
				callback.moveForward(1);
			}else{
				if(isBackObstacle==false){
					callback.changeDirection(Direction.LEFT, 2);
					callback.moveForward(1);
				}else{
					System.out.println("ERROR: FACING WEST TARGET BOTTOM LEFT");
				}
			}
		}else{
			System.out.println("currentLocationFrontRow"+currentLocationFrontRow);
			System.out.println("currentLocationFrontCol"+currentLocationFrontCol);
			System.out.println("STUCK");
		}
	}
	
	private boolean isExporeCoverageReached(){
		int exploredCount= 0;
		for(int i=0;i<exploredData.length;i++){
			for(int j=0;j<exploredData[i].length;j++){
				if(exploredData[i][j]==1){
					exploredCount ++;
				}
			}
		}
		float coverage=exploredCount/300f*100f;
		System.out.println("EXPLORED GRIDS ="+coverage);
		if(coverage>=this.coverageLimit)
			return true;
		else
			return false;
	}
	
	public void addObstacle(int frontMidSensor, int frontLeftSensor, int frontRightSensor, int rightSensor, int leftSensor){
		if(leftSensor>=sensorTrashold){
			switch(currentDirection){
				case North: 
					if(currentLocationFrontCol-2>=0)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case South: 
					if(currentLocationFrontCol+2<=14)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case East: 
					if(currentLocationFrontRow-2>=0)
						addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
				case West: 
					if(currentLocationFrontRow+2<=19)
						addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
			}
		}else{
			switch(currentDirection){
			case North: 
					if(currentLocationFrontCol-2>=0)
						if(currentLocationFrontRow>=0){
							addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol-2);
						}
					break;
				case South: 
					if(currentLocationFrontCol+2<=14)
						addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case East: 
					if(currentLocationFrontRow-2>=0)
						addExploredAreaBySensor(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
				case West: 
					if(currentLocationFrontRow+2<=19)
						addExploredAreaBySensor(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
			}
		}
		if(rightSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontCol+2<=14)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case South:
					if(currentLocationFrontCol-2>=0)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case East:
					if(currentLocationFrontRow+2<=19)
						addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
				case West:
					if(currentLocationFrontRow-2>=0)
						addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
			}
		}
		else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontCol+2<=14)
						if(currentLocationFrontRow>=0){
							addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol+2);
						}
					break;
				case South:
					if(currentLocationFrontCol-2>=0)
						addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case East:
					if(currentLocationFrontRow+2<=19)
						addExploredAreaBySensor(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
				case West:
					if(currentLocationFrontRow-2>=0)
						addExploredAreaBySensor(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
			}
		}
		if(frontMidSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						addObstacle(currentLocationFrontRow-1,currentLocationFrontCol);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						addObstacle(currentLocationFrontRow+1,currentLocationFrontCol);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-1);
					break;
			}
		}else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						addExploredAreaBySensor(currentLocationFrontRow,currentLocationFrontCol-1);
					break;
			}
		}
		if(frontLeftSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						addObstacle(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						addObstacle(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						addObstacle(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						addObstacle(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
			}
		}else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
			}
		}
		
		if(frontRightSensor>=sensorTrashold){
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						addObstacle(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						addObstacle(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						addObstacle(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						addObstacle(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
			}
		}else{
			switch(currentDirection){
				case North:
					if(currentLocationFrontRow-1>=0)
						addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol+1);
					break;
				case South:
					if(currentLocationFrontRow+1<=19)
						addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol-1);
					break;
				case East:
					if(currentLocationFrontCol+1<=14)
						addExploredAreaBySensor(currentLocationFrontRow+1,currentLocationFrontCol+1);
					break;
				case West:
					if(currentLocationFrontCol-1>=0)
						addExploredAreaBySensor(currentLocationFrontRow-1,currentLocationFrontCol-1);
					break;
			}
		}
	}
	private void addObstacle(int row, int col){
		this.obstacleData[row][col] = 1;
		int newData[][] = new int[20][15];//17,3 17,11
		for(int i=0;i<20;i++){
			for(int j=0;j<15;j++){
				newData[i][j]=this.obstacleData[i][Math.abs(14-j)];
			}
		}
		MapUI.updateMap(newData, 1);
//		System.out.println("OBSTICAL DETECTED AT "+row+","+col);
	}
	private void addExploredAreaBySensor(int row, int col){
		this.exploredData[row][col] = 1;
		int newData[][] = new int[20][15];//17,3 17,11
		for(int i=0;i<20;i++){
			for(int j=0;j<15;j++){
				newData[i][j]=this.exploredData[i][Math.abs(14-j)];
			}
		}
		MapUI.updateMap(newData, 0);		
	}
	
	public void returnToStart(RobotCallback callback){
		
		if(currentLocationFrontRow==19&&currentLocationFrontCol==1){
			if(currentDirection==Direction.North){
				callback.changeDirection(Direction.RIGHT, 1);
			}
			else if(currentDirection==Direction.South){
				callback.changeDirection(Direction.LEFT, 1);
			}
			else if(currentDirection==Direction.West){
				callback.changeDirection(Direction.LEFT, 2);
			}
			callback.readyForFastestPath();
			callback.sendRobotInstruction(null);
			
		}else if(currentLocationFrontRow==18&&currentLocationFrontCol==0){
			if(currentDirection==Direction.North){
				callback.changeDirection(Direction.RIGHT, 1);
			}
			else if(currentDirection==Direction.South){
				callback.changeDirection(Direction.LEFT, 1);
			}
			else if(currentDirection==Direction.West){
				callback.changeDirection(Direction.LEFT, 2);
			}
			callback.readyForFastestPath();
			callback.sendRobotInstruction(null);
			
		}else if((currentLocationFrontRow==18&&currentLocationFrontCol==0)&&currentDirection==Direction.East){
			callback.readyForFastestPath();
			callback.sendRobotInstruction(null);
		}
		else{
			switch(currentDirection){
			case North:
				makeDecisionFacingNorth(19,0,callback);
				break;
			case South:
				makeDecisionFacingSouth(19,0,callback);
				break;
			case East:
				makeDecisionFacingEast(19,0,callback);
				break;
			case West:
				makeDecisionFacingWest(19,0,callback);
				break;
			}
			callback.sendRobotInstruction(null);
		}
	}
	*/
	
	//***********************************//
	//		SHORTEST PATH METHODS		 //
	//***********************************//
	public void findPath(RobotCallback callback){
		System.out.println("finiding fastest path");
		AlgoGraph tree1 = generateAlgoTree();
		
		
			AlgoGraph tree = generateAlgoTree();
			DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(tree);
            dijkstra.execute(tree.getVertexes().get(1));
            LinkedList<AlgoNode> path = dijkstra.getPath(tree.getVertexes().get(tree.getVertexes().size()-1));
            
            //from 18,2
//           MapUI.readyRobotAtStartPosition();
           //TODO:finish this
           
           
            	 int tempRow = 18;
                 int tempCol = 2;
                 Direction tempDirection = currentDirection;
                 callback.moveForward(1);
            	for(int i=1;i<path.size();i++){
                	System.out.println(path.get(i));
                	switch(tempDirection){
                		case North:
                			
                			if(path.get(i).getNodeRowIndex()<tempRow){
                				callback.moveForward(1);
                				tempDirection = Direction.North;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()>tempCol){
                				callback.changeDirection(Direction.RIGHT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.East;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()<tempCol){
                				callback.changeDirection(Direction.LEFT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.West;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}
                			else if(path.get(i).getNodeRowIndex()>tempRow){
                				callback.changeDirection(Direction.LEFT, 2);
                				callback.moveForward(1);
                			}
                			break;
                		case South:
                			if(path.get(i).getNodeRowIndex()<tempRow){
                				callback.changeDirection(Direction.LEFT, 2);
                				callback.moveForward(1);
                				tempDirection = Direction.North;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()>tempCol){
                				callback.changeDirection(Direction.LEFT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.East;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeRowIndex()>tempRow){
                				callback.moveForward(1);
                				tempDirection = Direction.South;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()<tempCol){
                				callback.changeDirection(Direction.RIGHT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.West;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}
                			break;
                		case East:
                			if(path.get(i).getNodeRowIndex()<tempRow){
                				callback.changeDirection(Direction.LEFT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.North;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()>tempCol){
                				callback.moveForward(1);
                				tempDirection = Direction.East;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeRowIndex()>tempRow){
                				callback.changeDirection(Direction.RIGHT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.South;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()<tempCol){
                				callback.changeDirection(Direction.RIGHT, 2);
                				callback.moveForward(1);
                				tempDirection = Direction.West;
                				tempRow=path.get(i).getNodeRowIndex();
                			}
                			break;
                		case West:
                			if(path.get(i).getNodeRowIndex()<tempRow){
                				callback.changeDirection(Direction.RIGHT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.North;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                				System.out.println("FAcing west turning right");
                			}else if(path.get(i).getNodeColIndex()>tempCol){
                				callback.changeDirection(Direction.RIGHT, 2);
                				callback.moveForward(1);
                				tempDirection = Direction.East;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeRowIndex()<tempRow){
                				callback.changeDirection(Direction.LEFT, 1);
                				callback.moveForward(1);
                				tempDirection = Direction.South;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}else if(path.get(i).getNodeColIndex()>tempCol){
                				callback.moveForward(1);
                				tempDirection = Direction.West;
                				tempRow=path.get(i).getNodeRowIndex();
                				tempCol=path.get(i).getNodeColIndex();
                			}
                			break;
                	}
                }
            	callback.readyForFastestPath();
            
            
		
	}	
	public AlgoGraph generateAlgoTree(){
		//create the nodes indivually so we can reference instead of recreating the leaf so the tree wont go into a loop
		AlgoNode[][] nodePool = trimNodePool(generateNodePool());
//		AlgoGraph algoTree = new AlgoGraph(nodePool[0][0]);
//		System.out.println(algoTree.getRoot().getNodeRowIndex());
		
		ArrayList<AlgoEdge> edges = new ArrayList<>();
		ArrayList<AlgoNode> nodes = new ArrayList<>();
		
		//building the nodes
		for(int i=0;i<nodePool.length;i++){
			for(int j=0;j<nodePool[i].length;j++){
				if(nodePool[i][j]!=null){
//					System.out.println(nodePool[i][j]);
					nodes.add(nodePool[i][j]);
				}
			}
		}
		//building the edges
		for(int i=0;i<nodePool.length;i++){
			for(int j=0;j<nodePool[i].length;j++){

				//set front node
				if((j+1)<nodePool[i].length){
					if(nodePool[i][j+1]!=null&&nodePool[i][j]!=null){
						edges.add(new AlgoEdge(nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+"TO"+nodePool[i][j+1].getNodeRowIndex()+","+nodePool[i][j+1].getNodeColIndex(),nodePool[i][j],nodePool[i][j+1],1));
//						System.out.println("parent=("+nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+") child=("+nodePool[i][j+1].getNodeRowIndex()+","+nodePool[i][j+1].getNodeColIndex()+")");
					}
				}else{
//					System.out.println(nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+" has no front node");
				}
					
				//set left node
				if((i-1)>=0){
					if(nodePool[i-1][j]!=null&&nodePool[i][j]!=null){
						edges.add(new AlgoEdge(nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+"TO"+nodePool[i-1][j].getNodeRowIndex()+","+nodePool[i-1][j].getNodeColIndex(),nodePool[i][j],nodePool[i-1][j],2));
					}
//					System.out.println("parent=("+nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+") child=("+nodePool[i-1][j].getNodeRowIndex()+","+nodePool[i-1][j].getNodeColIndex()+")");
				}else{
//					System.out.println(i+","+j+" has no left node");
				}
					
				//set right node
				if((i+1)<nodePool.length){
					if(nodePool[i+1][j]!=null&&nodePool[i][j]!=null){
						edges.add(new AlgoEdge(nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+"TO"+nodePool[i+1][j].getNodeRowIndex()+","+nodePool[i+1][j].getNodeColIndex(),nodePool[i][j],nodePool[i+1][j],2));
					}
//					System.out.println("parent=("+nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+") child=("+nodePool[i+1][j].getNodeRowIndex()+","+nodePool[i+1][j].getNodeColIndex()+")");
				}else{
//					System.out.println(i+","+j+" has no right node");
				}
				
				//set back node
				if((j-1)>=0){
					if(nodePool[i][j-1]!=null&&nodePool[i][j]!=null){
						edges.add(new AlgoEdge(nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+"TO"+nodePool[i][j-1].getNodeRowIndex()+","+nodePool[i][j-1].getNodeColIndex(),nodePool[i][j],nodePool[i][j-1],3));
//						System.out.println("chil2d=("+nodePool[i][j-1].getNodeRowIndex()+","+nodePool[i][j-1].getNodeColIndex()+")");
					}
						
//					System.out.println("parent=("+nodePool[i][j].getNodeRowIndex()+","+nodePool[i][j].getNodeColIndex()+") child=("+nodePool[i][j-1].getNodeRowIndex()+","+nodePool[i][j-1].getNodeColIndex()+")");
				}else{
//					System.out.println(i+","+j+" has no back node");
				}
			}
		}
		AlgoGraph graph = new AlgoGraph(nodes, edges);
		
		return graph;
	}
	
	public AlgoNode[][] generateNodePool(){
		AlgoNode[][] nodePool = new AlgoNode[18][13];
		for(int i=0;i<nodePool.length;i++){
			for(int j=0;j<nodePool[i].length;j++){
				AlgoNode child = new AlgoNode(18-i,j+1);
				nodePool[i][j] = child;
			}
		}
		return nodePool;
	}
	
	public AlgoNode[][] trimNodePool(AlgoNode[][] nodePool){
		
		for(int i=0;i<nodePool.length;i++){
			for(int j=0;j<nodePool[i].length;j++){
				int actualrow = 18-i;
				int actualcol = j+1;
				
				//check center is obstacle
				if(obstacles[actualrow][actualcol]==1||exploredData[actualrow][actualcol]==0){
					nodePool[i][j]=null;
				}
				//check forward is obstacle
				if(obstacles[actualrow][actualcol+1]==1||exploredData[actualrow][actualcol+1]==0){
					nodePool[i][j]=null;
				}
				//check forwardright is obsacle
				if(obstacles[actualrow-1][actualcol+1]==1||exploredData[actualrow-1][actualcol+1]==0){
					nodePool[i][j]=null;
				}
				//check forwardleft is obstacle
				if(obstacles[actualrow+1][actualcol+1]==1||exploredData[actualrow+1][actualcol+1]==0){
					nodePool[i][j]=null;
				}
				//check back isobstacle
				if(obstacles[actualrow][actualcol-1]==1||exploredData[actualrow][actualcol-1]==0){
					nodePool[i][j]=null;
				}
				//check backright is obstacle
				if(obstacles[actualrow-1][actualcol-1]==1||exploredData[actualrow-1][actualcol-1]==0){
					nodePool[i][j]=null;
				}
				//check backleft is obstacle
				if(obstacles[actualrow+1][actualcol-1]==1||exploredData[actualrow+1][actualcol-1]==0){
					nodePool[i][j]=null;
				}
				//check right is obstacle
				if(obstacles[actualrow-1][actualcol]==1||exploredData[actualrow-1][actualcol]==0){
					nodePool[i][j]=null;
				}
				//check left is obstacle
				if(obstacles[actualrow-1][actualcol]==1||exploredData[actualrow-1][actualcol]==0){
					nodePool[i][j]=null;
				}
				
			}
		}
		
		return nodePool;
	}
	
	public boolean isGoalReachable(){
		return isGoalReached;
	}
	
}
