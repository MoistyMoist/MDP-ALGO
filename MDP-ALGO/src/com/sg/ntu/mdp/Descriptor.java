package com.sg.ntu.mdp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Descriptor {
	
	private int exloredData[][];
	private int obstacleData[][];
	
	private String fileName = "MDFStrings_week4.txt";
	
	//TODO:
	public void writeDescriptorFromFile(String exploredData, String obstacleData){
		try{
		    PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		    writer.println("MDF String 1:\n"+exploredData);
		    writer.println("MDF String 1:\n"+obstacleData);
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//TODO:
	public String readDescriptorFromFile(){
		String content="";
		File file = new File(fileName);
		FileReader reader = null;
		 try {
			 	
			  reader = new FileReader(file);
		      char[] chars = new char[(int) file.length()];
		      reader.read(chars);
		      content = new String(chars);
		      reader.close();
		    
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		 return content;
	}
	
	
	
	//TODO:
	public String hexToBinary(String hex){
		return "";
	}
	//TODO:[20][15]
	public int[][] getExploredDataToSimulator(){
		return null;
	}
	//TODO:[20][15]
	public int[][] getObstacleDataToSimulator(){
		return null;
	}
	
	
	
	
	public void saveDataFromSimulatorToFile(int[][] exploredData, int[][]obstacleData){
		String exploredHex = saveExploredDataToFile(exploredData);
		String obstacleHex = saveObstacleDataToFile(obstacleData);
		
		writeDescriptorFromFile(exploredHex, obstacleHex);
	}
	//TODO:
	public String binaryToHex(String binary){
		return binary;
	}
	public String saveExploredDataToFile(int[][] data){
		String stringData = "11";
		for(int i=0;i<data.length;i++){
			for(int j =0;j<data[i].length;j++){
				stringData+=data[i][j];
			}
		}
		return binaryToHex(stringData+="11");
	}
	public String saveObstacleDataToFile(int[][] data){
		String stringData = "00";
		for(int i=0;i<data.length;i++){
			for(int j =0;j<data[i].length;j++){
				stringData+=data[i][j];
			}
		}
		return binaryToHex(stringData+="00");
	}
}
