package arena;

import java.awt.Color;

import javax.swing.JButton;

import dataTypes.Directions;

public class Arena {

	public static final int mapWidth = 20;
	public static final int mapLength = 15;

	public static Arena instance;
	private boolean[][] layout;

	public static Arena getInstance() // return type class Arena
	{
		if (instance == null) {
			instance = new Arena();
		}
		return instance;
	}

	public boolean[][] getLayout() {
		return layout;
	}

	public void setLayout(JButton[][] mapGrids) {
		layout = new boolean[mapLength][mapWidth]; // [15][20]
		for (int i = 0; i < mapWidth; i++) // 20 rows
		{
			for (int j = 0; j < mapLength; j++) { // 15 columns
				if (mapGrids[i][j].getBackground() == Color.RED) {
					layout[j][19 - i] = true; // if there is obstacle
				} else {
					layout[j][19 - i] = false; // if no obstacle or clear grid
				}
			}
		}
	}

	public int getNumOfClearGrids(int[] sensorPos, Directions sensorDirection) {
		int numOfClearGrids = 0;

		switch (sensorDirection) {
		case NORTH:
			for (int y = sensorPos[1] + 1; y < Arena.mapWidth; y++)// 20
			{
				if (layout[sensorPos[0]][y] == false) // if there is no obstacle
				{
					numOfClearGrids++;
				} else
					break;
			}
			break;

		case SOUTH:
			for (int y = sensorPos[1] - 1; y >= 0; y--) {
				if (layout[sensorPos[0]][y] == false) {
					numOfClearGrids++;
				} else
					break;
			}
			break;

		case EAST:
			for (int x = sensorPos[0] + 1; x < Arena.mapLength; x++) { // 15
				if (layout[x][sensorPos[1]] == false) {
					numOfClearGrids++;
				} else
					break;
			}
			break;
		case WEST:
			for (int x = sensorPos[0] - 1; x >= 0; x--) {
				if (layout[x][sensorPos[1]] == false) {
					numOfClearGrids++;
				} else
					break;
			}
			break;

		}
		return numOfClearGrids;
	}
}
