package com.sg.ntu.mdp;

public interface RobotCallback {
	//TODO:add int times so  turn south 
	public void changeDirection(Direction direction, int times);
	public void moveForward(int distance);
	public void readyForFastestPath();
}
