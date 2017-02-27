package robot;

import arena.Arena;
import dataTypes.Directions;

public class Robot {

	private static Robot instance;
	private Sensor front, frontLeft, frontRight;
	private Sensor right, left;
	private int speed;

	private Robot() {

		front = new Sensor(Sensor.LONG_RANGE);
		frontLeft = new Sensor(Sensor.LONG_RANGE);
		frontRight = new Sensor(Sensor.SHORT_RANGE);

		right = new Sensor(Sensor.SHORT_RANGE);
		left = new Sensor(Sensor.SHORT_RANGE);
	}

	public static Robot getInstance() {

		if (instance == null) {
			instance = new Robot();
		}
		return instance;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int senseFront(int[] sensorPos, Directions robotDirection) {

		int numOfClearGrids;
		Arena arena = Arena.getInstance();
		Directions sensorDirection = robotDirection;
		numOfClearGrids = arena.getNumOfClearGrids(sensorPos, sensorDirection);
		if (numOfClearGrids > Sensor.LONG_RANGE) {
			numOfClearGrids = Sensor.LONG_RANGE;
		}

		return numOfClearGrids;
	}

	public int senseFrontLeft(int[] sensorPos, Directions robotDirection) {

		int numOfClearGrids;
		Arena arena = Arena.getInstance();
		Directions sensorDirection = robotDirection;
		numOfClearGrids = arena.getNumOfClearGrids(sensorPos, sensorDirection);
		if (numOfClearGrids > Sensor.LONG_RANGE) {
			numOfClearGrids = Sensor.LONG_RANGE;
		}

		return numOfClearGrids;
	}

	public int senseFrontRight(int[] sensorPos, Directions robotDirection) {

		int numOfClearGrids;
		Arena arena = Arena.getInstance();
		Directions sensorDirection = robotDirection;
		numOfClearGrids = arena.getNumOfClearGrids(sensorPos, sensorDirection);
		if (numOfClearGrids > Sensor.SHORT_RANGE) {
			numOfClearGrids = Sensor.SHORT_RANGE;
		}

		return numOfClearGrids;
	}

	public int senseLeft(int[] sensorPos, Directions robotDirection) {

		int numOfClearGrids;
		Arena arena = Arena.getInstance();
		Directions sensorDirection;
		switch (robotDirection) {

		case NORTH:
			sensorDirection = Directions.WEST;
			break;
		case SOUTH:
			sensorDirection = Directions.EAST;
			break;
		case EAST:
			sensorDirection = Directions.NORTH;
			break;
		case WEST:
			sensorDirection = Directions.SOUTH;
			break;
		default:
			sensorDirection = null; // break;
		}

		numOfClearGrids = arena.getNumOfClearGrids(sensorPos, sensorDirection);
		if (numOfClearGrids > Sensor.SHORT_RANGE) {
			numOfClearGrids = Sensor.SHORT_RANGE;
		}

		return numOfClearGrids;
	}

	public int senseRight(int[] sensorPos, Directions robotDirection) {

		int numOfClearGrids;
		Arena arena = Arena.getInstance();
		Directions sensorDirection;
		switch (robotDirection) {

		case NORTH:
			sensorDirection = Directions.EAST;
			break;
		case SOUTH:
			sensorDirection = Directions.WEST;
			break;
		case EAST:
			sensorDirection = Directions.SOUTH;
			break;
		case WEST:
			sensorDirection = Directions.NORTH;
			break;
		default:
			sensorDirection = null; // break;
		}

		numOfClearGrids = arena.getNumOfClearGrids(sensorPos, sensorDirection);
		if (numOfClearGrids > Sensor.SHORT_RANGE) {
			numOfClearGrids = Sensor.SHORT_RANGE;
		}

		return numOfClearGrids;
	}

	public void turnRight() {

	}

	public void turnLeft() {

	}

	public void moveForward() {

	}

}
