package com.sg.ntu.mdp.communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Descriptor {
	
	private int exloredData[][];
	private int obstacleData[][];
	
	private String fileName = "MDFStrings_week4.txt";
	
	public void writeDescriptorFromFile(String exploredData, String obstacleData){
		try{
		    PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		    
//		    writer.println("MDF String 1:");
//		    writer.println(Arrays.toString(splitToNChar(exploredData, 15)));
//		    writer.println("MDF String 2:");
//		    writer.println(Arrays.toString(splitToNChar(obstacleData, 15)));
		    writer.println(exploredData);
		    writer.println(obstacleData);
		    
		    //writer.println("MDF String 1:\n"+exploredData);
		    //writer.println("MDF String 2:\n"+obstacleData);
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	public String readDescriptorFromFile(int i){
		String content="";
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	if(i==1){
		    		content = line;
		    	}else{
		    		content = line;
		    		break;
		    	}
		    }
		}
		 catch (IOException e) {
		        e.printStackTrace();
		    }
		 return content;
	}

	
	public String hexToBinary(String hex){
		String binary = "";
		for(int i=0;i<hex.length();i++){
			String value = new BigInteger(hex.charAt(i)+"", 16).toString(2);
			String addzeroString="";
			while((value.length()+addzeroString.length())<4){
				addzeroString+="0";
			}
			binary += addzeroString+value;
			addzeroString="";
		}
//		System.out.println(binary);
		return binary;
	}
	//[20][15]
	public int[][] getExploredDataToSimulator() throws IOException{
		int[][] arena = new int[20][15];
		String hexvalue = readDescriptorFromFile(0);
		String binaryValue = hexToBinary(hexvalue);
		int charAt=0;
		for(int i=19;i>=0;i--){
			for(int j=14;j>=0;j--){
				arena[i][j]=Integer.parseInt(binaryValue.charAt(charAt)+"");
				charAt++;
			}
		}
		
        return arena;
    }
	//[20][15]
	public int[][] getObstacleDataToSimulator() throws IOException{ 
		int[][] arena = new int[20][15];
		String obstacleHexvalue = readDescriptorFromFile(1);
		String obstacleBinaryValue = hexToBinary(obstacleHexvalue);
		String exploredHexvalue = readDescriptorFromFile(0);
		String exploredBinaryValue = hexToBinary(exploredHexvalue);

		int index=0;
		String buildObstacleBinary="";
		for(int i=0;i<exploredBinaryValue.length();i++){
			if((exploredBinaryValue.charAt(i)+"").equals("1")){
				if(index<obstacleBinaryValue.length()){
					if((obstacleBinaryValue.charAt(index)+"").equals("1")){
						buildObstacleBinary+="1";
					}else{
						buildObstacleBinary+="0";
					}
					index++;
				}else{
					buildObstacleBinary+="0";
				}
			}else{
				buildObstacleBinary+="0";
			}
			
		}
		
		System.out.println(buildObstacleBinary);
		
		int charAt=0;
		for(int i=19;i>=0;i--){
			for(int j=14;j>=0;j--){
				arena[i][j]=Integer.parseInt(buildObstacleBinary.charAt(charAt)+"");
				charAt++;
			}
		}
        
        return arena;
    }
	
	
	
	
	public void saveDataFromSimulatorToFile(int[][] exploredData, int[][]obstacleData){
		String exploredHex = saveExploredDataToFile(exploredData);
//		System.out.println("EXPLORED HEX VALUE : "+exploredHex);
		
		String obstacleHex = saveObstacleDataToFile(obstacleData,exploredData);
//		System.out.println("OBSTACL HEX VALUE : "+obstacleHex);
		

		writeDescriptorFromFile(exploredHex, obstacleHex);
	}
	
	public String binaryToHex(String binary){
		String binaryZeroHead="";
		
		while((binaryZeroHead.length()+binary.length())%4!=0){
//			System.out.println((binaryZeroHead.length()+binary.length())%4);
			binaryZeroHead+="0";
		}
		
		binary = binary+binaryZeroHead;
		
		String hexString="";
		for(int i=0;i<binary.length();i+=4){
			int decimal = Integer.parseInt(binary.substring(i, i+4),2);
			String hexStr = Integer.toString(decimal,16);
			hexString=hexString+hexStr;
		}
		return hexString;
	}
	public String saveExploredDataToFile(int[][] data){
		String stringData = "11";
		String temp="";
		for(int i=data.length-1;i>=0;i--){
			for(int j =0;j<data[i].length;j++){
				stringData+=data[i][j];
				temp+=data[i][j];
			}
//			System.out.println(temp);
			temp="";
		}
		this.exloredData = data;
		return binaryToHex(stringData+="11");
	}
	public String saveObstacleDataToFile(int[][] data, int[][]exploredData){
		String stringExploredData = "";
		String temp="";
		for(int i=data.length-1;i>=0;i--){
			for(int j =0;j<exploredData[i].length;j++){
				stringExploredData+=exploredData[i][j];
				temp+=exploredData[i][j];
			}
			temp="";
		}
		
		String stringObstacleData = "";
		temp="";
		for(int i=data.length-1;i>=0;i--){
			for(int j =0;j<data[i].length;j++){
				stringObstacleData+=data[i][j];
				temp+=data[i][j];
			}
//			System.out.println(temp);
			temp="";
		}
		this.obstacleData = data;
		
		
		String taskData="";
		int exploredCOunt=0;
//		System.out.println(stringExploredData);
//		System.out.println(stringObstacleData);
		
		for(int i=0;i<stringExploredData.length();i++){
			if((stringExploredData.charAt(i)+"").equals("1")){
				
				if((stringObstacleData.charAt(i)+"").equals("1")){
					taskData+="1";
				}else{
					taskData+="0";
				}
			}
		}
		
		return binaryToHex(taskData);
	}
}
