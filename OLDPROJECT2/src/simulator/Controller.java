package simulator;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import algorithms.AStarPathFinder;
import algorithms.MazeExplorer;
import algorithms.Path;
import dataTypes.Directions;
import arena.Arena;
import arena.FileReaderWriter;
import robot.Robot;

public class Controller {

	public static final String arenaDescriptorPath = System.getProperty("user.dir") + "/map-descriptors/arena.txt";
	private static final int thresholdBufferTime = 3;
	private static Controller instance;
	private UserInterface ui;
	private Timer timer;
	private int[] robotPosition = new int[2];
	private Directions robotOrientation;
	private int speed, targetCoverage = 100, timeLimit = 360;
	private float actualCoverage;
	private boolean reachedStart, reachedTimeThreshold;
	
	private Controller(){
		ui = new UserInterface();
	}
	
	public boolean reachedTimeThreshold(){
		return reachedTimeThreshold;
	}
	
	public static Controller getInstance(){
		if(instance == null){
			instance = new Controller();
		}
		return instance;
	}
	
	public void run(){
		ui.setVisible(true);
		ui.refreshInput();
	}
	
	public void toggleObstacle(JButton[][] mapGrids, int x, int y){
		if(mapGrids[x][y].getBackground() == Color.GREEN){
			mapGrids[x][y].setBackground(Color.RED);
		}
		else{
			mapGrids[x][y].setBackground(Color.GREEN);
		}
	}
	
	public void switchComboBox(JComboBox main, JPanel cardPanel){
		CardLayout cardLayout = (CardLayout) (cardPanel.getLayout());
		cardLayout.show(cardPanel,  (String) main.getSelectedItem());
	}
	
	public void loadMap(JButton[][] mapGrids){
		Arena arena = Arena.getInstance();
		arena.setLayout(mapGrids);
		
		boolean[][] layout = arena.getLayout();
		String arenaDescriptor = "";
		
		for(int i = 0; i < Arena.mapWidth; i++){
			for(int j = 0; j < Arena.mapLength; j++){
				if(layout[i][j] == true){
					arenaDescriptor += "1";
				}
				else{
					arenaDescriptor += "0";
				}
			}
			arenaDescriptor = arenaDescriptor + System.getProperty("line.separator");
		}
		
		try{
			FileReaderWriter fileWriter = new FileReaderWriter(FileSystems.getDefault().getPath(arenaDescriptorPath));
			fileWriter.fileWriter(arenaDescriptor);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		ui.setStatus("Finished Map Loading");
	}
	
	public void clearMap(JButton[][] mapGrids){
		for(int x = 0; x < Arena.mapWidth; x++){
			for(int y = 0; y < Arena.mapLength; y++){
				if(mapGrids[x][y].getBackground() == Color.RED){
					mapGrids[x][y].setBackground(Color.GREEN);
				}
			}
		}
		
		Arena arena = Arena.getInstance();
		arena.setLayout(mapGrids);
		ui.setStatus("Finished Map Clearing");
	}
	
	public void resetRobotInMaze(JButton[][] mazeGrids, int x, int y){
		if(x < 2 || x > 14 || y < 2 || y > 9){
			ui.setStatus("Warning: Robot Position Out of Range");
			resetMaze(mazeGrids);
		}
		else{
			for(int i = x - 1; i <= x + 1; i++){
				for(int j = y - 1; j <= y + 1; j++){
					if(i == x & j == y + 1){
						mazeGrids[20 - j][i - 1].setBackground(Color.PINK);
					}
					else{
						mazeGrids[20 - j][i - 1].setBackground(Color.CYAN);
					}
				}
			}
		}
		robotPosition[0] = x - 1;
		robotPosition[1] = y - 1;
		robotOrientation = Directions.NORTH;
		ui.setStatus("Robot Initial Position Set");
	}
	
	public void resetMaze(JButton[][] mazeGrids){
		for(int x = 0; x < Arena.mapWidth; x++){
			for(int y = 0; y < Arena.mapLength; y++){
				mazeGrids[x][y].setBackground(Color.BLACK);
				if((x >= 0 & x <= 2 & y >= 12 & y <= 14) || (y >= 0 & y <= 2 & x >= 17 & x <= 19)){
					mazeGrids[x][y].setBackground(Color.ORANGE);
				}
			}
		}
	}
	
	public void setExploreSpeed(int speed){
		this.speed = speed;
		ui.setStatus("Robot Speed Set");
	}
	
	class TimeClass implements ActionListener{
		int counter;
		
		public TimeClass(int timeLimit){
			counter = timeLimit;
		}
		
		public void actionPerformed(ActionEvent e){
			
			counter--;
			ui.setTimerCounter(counter);
			
			if(counter >= 0){
				SwingWorker<Void, Float> getThreshold = new SwingWorker<Void, Float>(){
					Path backPath;
					protected Void doInBackground() throws Exception{
						float threshold;
						MazeExplorer explorer = MazeExplorer.getInstance();
						AStarPathFinder pathFinder = AStarPathFinder.getInstance();
						backPath = pathFinder.findFastestPath(robotPosition[0], robotPosition[1], 
								MazeExplorer.START[0], MazeExplorer.START[1], explorer.getMazeRef());
						threshold = backPath.getNumOfSteps() * (1 / (float)speed) + thresholdBufferTime;
						publish(threshold);
						return null;
					}
					
					protected void process(List<Float> chunks){
						Float curThreshold = chunks.get(chunks.size() - 1);
						if(counter <= curThreshold){
							reachedTimeThreshold = true;
						}
					}
				};
				
				if(counter == 0){
					timer.stop();
					Toolkit.getDefaultToolkit().beep();
				}
				getThreshold.execute();
			}
		}
	}
	
	public void exploreMaze(){
		ui.refreshInput();
		Arena arena = Arena.getInstance();
		MazeExplorer explorer = MazeExplorer.getInstance();
		Robot robot = Robot.getInstance();
		if(arena.getLayout() == null){
			ui.setStatus("Warning: No Layout Loaded Yet");
		}
		else{
			TimeClass timeActionListener = new TimeClass(timeLimit);
			SwingWorker<Void, Void> exploreMaze = new SwingWorker<Void, Void>(){
				protected Void doInBackground() throws Exception{
					robot.setSpeed(speed);
					reachedStart = false;
					explorer.explore(robotPosition);
					return null;
				}
				
				public void done(){
					reachedStart = true;
					if(actualCoverage < targetCoverage){
						ui.setStatus("Exploration Completed: Not Reach 100%");
					}
					else{
						ui.setStatus("Exploration Completed: Reach 100%");
					}
				}
			};
			SwingWorker<Void, Void> updateCoverage = new SwingWorker<Void, Void>(){
				protected Void doInBackground() throws Exception{
					int numExplored;
					JButton[][] mazeGrids = ui.getMazeGrids();
					while(!reachedStart){
						numExplored = 0;
						for(int x = 0; x < Arena.mapWidth; x++){
							for(int y = 0; y < Arena.mapLength; y++){
								if(mazeGrids[x][y].getBackground() != Color.BLACK){
									numExplored++;
								}
							}
						}
						actualCoverage = (float)(100 * numExplored) / (float)(Arena.mapLength * Arena.mapWidth);
						ui.setCoverage(String.format("%.1f",  actualCoverage));
					}
					if(timer.isRunning()){
						timer.stop();
					}
					return null;
				}
			};
			
			timer = new Timer(1000, timeActionListener);
			timer.start();
			ui.setStatus("Robot Exploring");
			reachedTimeThreshold = false;
			exploreMaze.execute();
			updateCoverage.execute();
		}
	}
	
	public void turnRobotRight(){
		JButton[][] mazeGrids = ui.getMazeGrids();
		
		switch(robotOrientation){
		case NORTH:
			mazeGrids[18 - robotPosition[1]][robotPosition[0]].setBackground(Color.CYAN);
			mazeGrids[19 - robotPosition[1]][robotPosition[0] + 1].setBackground(Color.PINK);
			robotOrientation = Directions.EAST;
			break;
		case SOUTH:
			mazeGrids[20 - robotPosition[1]][robotPosition[0]].setBackground(Color.CYAN);
			mazeGrids[19 - robotPosition[1]][robotPosition[0] - 1].setBackground(Color.PINK);
			robotOrientation = Directions.WEST;
			break;
		case EAST:
			mazeGrids[19 - robotPosition[1]][robotPosition[0] + 1].setBackground(Color.CYAN);
			mazeGrids[20 - robotPosition[1]][robotPosition[0]].setBackground(Color.PINK);
			robotOrientation = Directions.SOUTH;
			break;
		case WEST:
			mazeGrids[19 - robotPosition[1]][robotPosition[0] - 1].setBackground(Color.CYAN);
			mazeGrids[18 - robotPosition[1]][robotPosition[0]].setBackground(Color.PINK);
			robotOrientation = Directions.NORTH;
		}
		updateMazeColor();
	}
	
	public void moveRobotForward(){
		JButton[][] mazeGrids = ui.getMazeGrids();
		
		switch(robotOrientation){
		case NORTH:
			for(int i = 17 - robotPosition[1]; i <= 19 - robotPosition[1]; i++){
				for(int j = robotPosition[0] - 1; j <= robotPosition[0] + 1; j++){
					if(i == 17 - robotPosition[1] & j == robotPosition[0]){
						mazeGrids[i][j].setBackground(Color.PINK);
					}
					else{
						mazeGrids[i][j].setBackground(Color.CYAN);
					}
				}
			}
			robotPosition[1] = robotPosition[1] + 1;
			break;
		case SOUTH:
			for(int i = 19 - robotPosition[1]; i <= 21 - robotPosition[1]; i++){
				for(int j = robotPosition[0] - 1; j <- robotPosition[0] + 1; j++){
					if(i == 21 - robotPosition[1] & j == robotPosition[0]){
						mazeGrids[i][j].setBackground(Color.PINK);
					}
					else{
						mazeGrids[i][j].setBackground(Color.CYAN);
					}
				}
			}
			robotPosition[1] = robotPosition[1] - 1;
			break;
		case EAST:
			for(int i = 18 - robotPosition[1]; i <= 20 - robotPosition[1]; i++){
				for (int j = robotPosition[0]; j <= robotPosition[0] + 2; j++){
					if(i == 19 - robotPosition[1] & j == robotPosition[0] + 2){
						mazeGrids[i][j].setBackground(Color.PINK);
					}
					else{
						mazeGrids[i][j].setBackground(Color.CYAN);
					}
				}
			}
			robotPosition[0] = robotPosition[0] + 1;
			break;
		case WEST:
			for(int i = 18 - robotPosition[1]; i <= 20 - robotPosition[1]; i++){
				for(int j = robotPosition[0] - 2; j <= robotPosition[0]; j++){
					if(i == 19 - robotPosition[1] && j == robotPosition[0] - 2){
						mazeGrids[i][j].setBackground(Color.PINK);
					}
					else{
						mazeGrids[i][j].setBackground(Color.CYAN);
					}
				}
			}
			robotPosition[0] = robotPosition[0] - 1;
		}
		updateMazeColor();
	}
	
	public void turnRobotLeft(){
		JButton[][] mazeGrids = ui.getMazeGrids();
		
		switch(robotOrientation){
		case NORTH:
			mazeGrids[18 - robotPosition[1]][robotPosition[0]].setBackground(Color.PINK);
			mazeGrids[19 - robotPosition[1]][robotPosition[0] - 1].setBackground(Color.CYAN);
			robotOrientation = Directions.WEST;
			break;
		case SOUTH:
			mazeGrids[20 - robotPosition[1]][robotPosition[0]].setBackground(Color.PINK);
			mazeGrids[19 - robotPosition[1]][robotPosition[0] + 1].setBackground(Color.CYAN);
			robotOrientation = Directions.EAST;
			break;
		case EAST:
			mazeGrids[19 - robotPosition[1]][robotPosition[0] + 1].setBackground(Color.PINK);
			mazeGrids[18 - robotPosition[1]][robotPosition[0] + 1].setBackground(Color.CYAN);
			robotOrientation = Directions.NORTH;
			break;
		case WEST:
			mazeGrids[19 - robotPosition[1]][robotPosition[0] - 1].setBackground(Color.PINK);
			mazeGrids[20 - robotPosition[1]][robotPosition[0]].setBackground(Color.CYAN);
			robotOrientation = Directions.SOUTH;
		}
		updateMazeColor();
	}
	
	private void updateMazeColor(){
		JButton[][] mazeGrids = ui.getMazeGrids();
		MazeExplorer explorer = MazeExplorer.getInstance();
		int[][] mazeRef = explorer.getMazeRef();
		
		for(int i = 0; i < Arena.mapLength; i++){
			for(int j = 0; j < Arena.mapWidth; j++){
				if(mazeRef[i][j] == MazeExplorer.isEmpty){
					if(i < robotPosition[0] - 1 || i > robotPosition[0] + 1 || 
					j < robotPosition[1] - 1 || j > robotPosition[1] + 1){
						if((i >= MazeExplorer.start[0] - 1 & i <= MazeExplorer.start[0] + 1 & 
						j >= MazeExplorer.start[1] - 1 & j <= MazeExplorer.start[1] + 1) || 
						(i >= MazeExplorer.goal[0] - 1 & i <= MazeExplorer.goal[0] + 1 & 
						j >= MazeExplorer.goal[1] - 1 & j <= MazeExplorer.goal[1] + 1)){
							mazeGrids[19 - j][i].setBackground(Color.ORANGE);
						}
						else{
							mazeGrids[19 - j][i].setBackground(Color.GREEN);
						}
					}
					else if(mazeRef[i][j] == MazeExplorer.isObstacle){
						mazeGrids[19 - j][i].setBackground(Color.RED);
					}
				}
			}
		}
	}
	
	public void findFastestPath(){
		AStarPathFinder pathFinder = AStarPathFinder.getInstance();
		
		SwingWorker<Void, Void> findFastestPath = new SwingWorker<Void, Void>(){
			Path fastestPath;

			protected Void doInBackground() throws Exception{
				MazeExplorer explorer = MazeExplorer.getInstance();
				fastestPath = pathFinder.findFastestPath(MazeExplorer.start[0], MazeExplorer.start[1], 
								MazeExplorer.goal[0], MazeExplorer.goal[1], explorer.getMazeRef());
				pathFinder.moveRobotAlongFastestPath(fastestPath, Directions.NORTH);
				ArrayList<Path.Step> steps = fastestPath.getSteps();
				JButton[][] mazeGrids = ui.getMazeGrids();
				for(Path.Step step : steps){
					int x = step.getX();
					int y = step.getY();
					mazeGrids[19 - y][x].setBackground(Color.YELLOW);
				}
				return null;
			}
			
			public void done(){
				ui.setStatus("Fastest Path Found");
			}
		};
		findFastestPath.execute();
	}
}
