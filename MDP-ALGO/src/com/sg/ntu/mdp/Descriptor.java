package com.sg.ntu.mdp;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Descriptor {
	
	private int exloredData[][];
	private int obstacleData[][];
	
	private String fileName = "MDFStrings_week4.txt";
	
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
	
	public String binaryToHex(String binary){
		return "";
	}
	//[20][15]
	public int[][] getExploredDataToSimulator(){
		return null;
	}
	//[20][15]
	public int[][] getObstacleDataToSimulator(){
		return null;
	}
}
