package com.sg.ntu.mdp;

public class Algothrim {
	private float coverageLimit = 100f;
	private float timeLimit = 500f;//TODO: change this later
	
	public static Direction currentDirection = Direction.East;
	private float sensorTrashold = 4.00f;
	
	public static int[][]exploredData = new int[20][15];
	public static int[][]obstacleData = new int[20][15];
	public static int currentLocationFrontRow;
	public static int currentLocationFrontCol;
	
	public Algothrim(int[][] exploredData, int[][]obstacleData, int currentLocationFrontRow, int currentLocationFrontCol){
		//set the goal and start area to explored
		exploredData[19][0] = 1; exploredData[0][14] = 1;
		exploredData[19][1] = 1; exploredData[0][13] = 1;
		exploredData[19][2] = 1; exploredData[0][12] = 1;
		exploredData[18][0] = 1; exploredData[1][14] = 1;
		exploredData[18][1] = 1; exploredData[1][13] = 1;
		exploredData[18][2] = 1; exploredData[1][12] = 1;
		exploredData[17][0] = 1; exploredData[2][14] = 1;
		exploredData[17][1] = 1; exploredData[2][13] = 1;
		exploredData[17][2] = 1; exploredData[2][12] = 1;
		if(currentLocationFrontRow != -1 && currentLocationFrontCol!=-1){
			//TODO:change the starting direction base on the row,col (17,2[east),etc
			this.currentLocationFrontRow = currentLocationFrontRow;
			this.currentLocationFrontCol = currentLocationFrontCol;
		}
	}
	
	
	//***************************************//
	//EXPLORATION METHODS FOR REAL SITUATION //
	//***************************************//
	public void explore(float frontMidSensor, float frontLeftSensor, float frontRightSensor, float rightSensor, float leftSensor, final RobotCallback callback){
		if(leftSensor<=sensorTrashold){
			//obstacle on robot left
			//update the obstacle data
			switch(currentDirection){
				case North: 
					if(currentLocationFrontCol-2>=-1)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case South: 
					if(currentLocationFrontCol+2>=15)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case East: 
					if(currentLocationFrontRow-2>=-1)
						addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
				case West: 
					if(currentLocationFrontRow+2>=20)
						addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
			}
		}
		if(rightSensor<=sensorTrashold){
			//obstacle on robot right
			//update the obstacle data
			switch(currentDirection){
				case North:
					if(currentLocationFrontCol+2>=15)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case South:
					if(currentLocationFrontCol-2>=-1)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case East:
					if(currentLocationFrontRow+2>=20)
						addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
				case West:
					if(currentLocationFrontRow-2>=-1)
						addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
			}
		}
		
		if(isRobotWithinGoalArea()){
			returnToStart();
		}else{
			//continue exploring
			//main decision making depending on the direction and current location of the robot(goal direction is always north east)
			if(currentDirection == Direction.North){
				if(currentLocationFrontRow==0){
					//if east path has not obstacle
					if(obstacleData[0][currentLocationFrontCol+2]!=1&&
							obstacleData[currentLocationFrontRow+1][currentLocationFrontCol+2]!=1&&
							obstacleData[currentLocationFrontCol+2][currentLocationFrontCol+2]!=1){
//						callback.changeDirection(Direction.East);
						this.currentDirection = Direction.East;
						callback.moveForward(1);
						//TODO update the current location after moving forward
					}
					else{
						int distance = 0;
						//we move backwards (x distance) till we can move east
						while(obstacleData[currentLocationFrontRow+1][currentLocationFrontCol+2]==1&&
								obstacleData[currentLocationFrontCol+2][currentLocationFrontCol+2]==1&&
								obstacleData[currentLocationFrontCol][currentLocationFrontCol+2]==1){
							distance++;
						}
//						callback.changeDirection(Direction.South);
						this.currentDirection = Direction.South;
						callback.moveForward(distance);
					}
					
				}
				else if(frontMidSensor>sensorTrashold && frontRightSensor>sensorTrashold && frontLeftSensor>sensorTrashold){
					callback.moveForward(1);
				}
			}
			if(currentDirection == Direction.South){
				//for some reason we are facing south means north direction is blocked we move to the east
				if(frontMidSensor>sensorTrashold && frontRightSensor>sensorTrashold && frontLeftSensor>sensorTrashold){
					//nothing infront move forward
				}
			}
			if(currentDirection == Direction.East){
				if(frontMidSensor>sensorTrashold && frontRightSensor>sensorTrashold && frontLeftSensor>sensorTrashold){
					//nothing infront move forward
				}
			}
			if(currentDirection == Direction.West){
				if(frontMidSensor>sensorTrashold && frontRightSensor>sensorTrashold && frontLeftSensor>sensorTrashold){
					//nothing infront move forward
				}
			}
		}	
	}

	
	//***************************************//
	//  EXPLORATION METHODS FOR SIMULATION	 //
	//***************************************//
	//@Notes: focus on facing east going row by row towards 0
	//@Notes: we focus on this first since it is a requirement
	public void exploreSimulation(float frontMidSensor, float frontLeftSensor, float frontRightSensor, float rightSensor, float leftSensor, final RobotCallback callback){
		
		if(leftSensor<=sensorTrashold){
			//obstacle on robot left
			//update the obstacle data
			switch(currentDirection){
				case North: 
					if(currentLocationFrontCol-2>=-1)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case South: 
					if(currentLocationFrontCol+2>=15)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case East: 
					if(currentLocationFrontRow-2>=-1)
						addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
				case West: 
					if(currentLocationFrontRow+2>=20)
						addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
			}
		}
		if(rightSensor<=sensorTrashold){
			//obstacle on robot right
			//update the obstacle data
			switch(currentDirection){
				case North:
					if(currentLocationFrontCol+2>=15)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol+2);
					break;
				case South:
					if(currentLocationFrontCol-2>=-1)
						addObstacle(currentLocationFrontRow,currentLocationFrontCol-2);
					break;
				case East:
					if(currentLocationFrontRow+2>=20)
						addObstacle(currentLocationFrontRow+2,currentLocationFrontCol);
					break;
				case West:
					if(currentLocationFrontRow-2>=-1)
						addObstacle(currentLocationFrontRow-2,currentLocationFrontCol);
					break;
			}
		}
		
		if(isMapFullyExplored()){
			returnToStart();
		}else{
			//continue exploring row by row
			
			//check if row 19 is explored
			//check if that row/col area is blocked else move to that direction
			//else 18,17,16,
		}
		
		
	}
	
	
	
	private boolean isMapFullyExplored(){
		//@Notes: not sure of obstacle that surrounds an area making it unaccessable. need to check how the task is tested
		int exploredCount= 0;
		for(int i=0;i<exploredData.length;i++){
			for(int j=0;j<exploredData[i].length;j++){
				if(exploredData[i][j]==1){
					exploredCount ++;
				}
			}
		}
		if(exploredCount/300*100>=this.coverageLimit)
			return true;
		else
			return false;
	}
	//TODO:
	private boolean isRobotWithinGoalArea(){
		//within doesent mean the robot is fully in the goal area only means the head is in the goal area
		return false;
	}
	private void addObstacle(int row, int col){
		this.obstacleData[row][col] = 1;
	}
	private void addExploredArea(int midFrontRow, int midFrontCol, Direction robotDirection){
		this.exploredData[midFrontRow][midFrontCol] = 1;
		if(robotDirection == Direction.North){
			this.exploredData[midFrontRow][midFrontCol+1]=1;
			this.exploredData[midFrontRow][midFrontCol-1]=1;
		}
		if(robotDirection == Direction.South){
			this.exploredData[midFrontRow][midFrontCol+1]=1;
			this.exploredData[midFrontRow][midFrontCol-1]=1;
		}
		if(robotDirection == Direction.East){
			this.exploredData[midFrontRow-1][midFrontCol]=1;
			this.exploredData[midFrontRow+1][midFrontCol]=1;
		}
		if(robotDirection == Direction.West){
			this.exploredData[midFrontRow-1][midFrontCol]=1;
			this.exploredData[midFrontRow+1][midFrontCol]=1;
		}
	}
	//TODO:
	private void returnToStart(){
		//TODO: algo to make the robot go back to start line; can do by stack maybe?
	}
	
	
	
	//***********************************//
	//		SHORTEST PATH METHODS		 //
	//***********************************//
	private void findPath(int[][] currentLocation){
		//TODO: get the shortest path;
	}
	
	
}
