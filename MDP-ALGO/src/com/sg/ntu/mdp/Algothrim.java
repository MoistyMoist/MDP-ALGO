package com.sg.ntu.mdp;

import com.sg.ntu.mdp.simulator.MapUI;

public class Algothrim {
	private float coverageLimit = 10f;
	private float timeLimit = 500f;//TODO: change this later
	
	public static Direction currentDirection = Direction.East;
	private float sensorTrashold = 1.00f;
	
	public static int[][]exploredData = new int[20][15];
	public static int[][]obstacleData = new int[20][15];
	public static int[][]unreachableData = new int[20][15];
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
	//  EXPLORATION METHODS FOR SIMULATION	 //
	//***************************************//
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
			
			//todo:error here
			//we move forward till there is a wall
			boolean breakloop = false;
			for(int i=19;i>=0;i--){
				for(int j=0;j<=14;j++){
					if(exploredData[i][j]==0&&obstacleData[i][j]==0){
//						System.out.println(i+","+j);
						//TODO: check if this grid is accessable (for now jus check if surrounding can fit a robot)
//						if()
						//base on 1x1 grid of head
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
						breakloop=true;
						if(breakloop==true)
							break;
					}
				}
				if(breakloop==true)
					break;
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
				if(isRightObstacle==true||isRightWall==true){
					callback.changeDirection(Direction.LEFT, 1);
					callback.moveForward(1);
				}else if(isLeftWall==true||isLeftObstacle==true){
					callback.changeDirection(Direction.RIGHT, 1);
					callback.moveForward(1);
				}else{
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
				}else{
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
				}else{
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
				}else{
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
				}else if(isLeftObstacle==false&&isLeftWall==false){
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
				}else if(isBackWall==false&&isBackObstacle==false){
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
	
	//TODO:check if the grib is reachable
	private boolean isTargetCellReachable(int row, int col){
		return false;
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
			System.out.println("returning 1 "+MapUI.instructionQueue.size());
			MapUI.instructionQueue.clear();
//			callback.changeDirection(Direction.LEFT, 1);
			callback.readyForFastestPath();
		}else if(currentLocationFrontRow==18&&currentLocationFrontCol==0){
			System.out.println("returning 2 "+MapUI.instructionQueue.size());
			MapUI.instructionQueue.clear();
//			callback.changeDirection(Direction.LEFT, 2);
			callback.readyForFastestPath();
		}
		else{
			System.out.println("RETURNING ROW="+currentLocationFrontRow);
			System.out.println("RETURNING COL="+currentLocationFrontCol);
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
		}
	}
	
	//***********************************//
	//		SHORTEST PATH METHODS		 //
	//***********************************//
	public void findPath(RobotCallback callback){
		if(currentDirection==Direction.North)
			callback.changeDirection(Direction.RIGHT, 1);
		else if(currentDirection==Direction.South){
			callback.changeDirection(Direction.LEFT, 1);
		}
		else if(currentDirection==Direction.West){
			callback.changeDirection(Direction.LEFT, 2);
		}
		
		
		//TODO: get the shortest path;
		
		
		
		//end of algo
		
		callback.readyForFastestPath();
	}
	
	
}
