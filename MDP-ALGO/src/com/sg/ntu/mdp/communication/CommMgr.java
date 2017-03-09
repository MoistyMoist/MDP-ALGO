package com.sg.ntu.mdp.communication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class CommMgr {
	
	public static CommMgr _commMgr = null;
	
	// For communication with the Raspberry-Pi
	private static final String HOST = "192.168.13.13";
//	private static final String HOST = "localhost";
	private static final int PORT = 5182;
	
	public static final String MSG_TYPE_ANDROID = "1,";
	public static final String MSG_TYPE_ARDUINO = "3,";

	private static Socket _conn = null;

	private static BufferedOutputStream _bos = null;
	private static OutputStreamWriter _osw = null;
	private static BufferedReader _br = null;
	
	/**
	 * Private constructor used to support the Singleton design pattern
	 * <p>
	 */
	public CommMgr()
	{

	}
	
	/**
	 * Public static function used to get hold of the CommMgr
	 * 
	 * @return The static instance of the CommMgr
	 */
	public static CommMgr getCommMgr()
	{
		if(_commMgr == null)
		{
			_commMgr = new CommMgr();
		}
		
		return _commMgr;
	}
	
	public boolean setConnection(int timeoutInMs) {
		
		try {

			_conn = new Socket();
			_conn.connect(new InetSocketAddress(HOST, PORT), timeoutInMs);
			_conn.setSoTimeout(timeoutInMs);

			_bos = new BufferedOutputStream(_conn.getOutputStream());
			_osw = new OutputStreamWriter(_bos, "US-ASCII");
			_br = new BufferedReader(new InputStreamReader(
					_conn.getInputStream()));

			// Successful connection, return true
			System.out.println("setConnection() ->" +
					" Connection established successfully!");
			
			return true;
			
		} catch(UnknownHostException e) {
			System.out.println("setConnection() -> Unknown Host Exception");
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("setConnection() -> IO Exception");
		} catch(Exception e) {
			System.out.println("setConnection() -> Exception");
		}
		
		System.out.println("Failed to establish connection!");
		return false;
	}
	
	public void closeConnection() {
		try {
			if(_bos != null)
				_bos.close();
			if(_osw != null)
				_osw.close();
			if(_br != null)
				_br.close();
			
			if(_conn != null) {
				_conn.close();
				_conn = null;
			}
			
		} catch (IOException e) {
			System.out.println("closeConnection() -> IO Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("closeConnection() -> Null Pointer Exception");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("closeConnection() -> Exception");
			e.printStackTrace();
		}
	}
	
	public boolean sendMsg(String msg, String msgType, boolean ack) {
		try {
			String outputMsg = msgType + msg;

			outputMsg = String.format("%-128s", outputMsg);
//			System.out.println("Sending out msg: " + outputMsg);
			
			_osw.write(outputMsg);
			_osw.flush();
//			_osw.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("sendMsg() -> IOException");
		} catch (Exception e) {
			System.out.println("sendMsg() -> Exception");
		}
		
		return false;
	}

	
	public String recvMsg() {
		try {
			
			
		    InputStream is = _conn.getInputStream();
		    byte[] buffer = new byte[1024];
		    int read;
		    String output="";
		    while((read = is.read(buffer)) != -1) {
		        output += new String(buffer, 0, read);
		        if(output.contains("}"))
		        	break;
		        System.out.println(output+"buffering..");
		        System.out.flush();
		    };
//		    _conn.close();
		   
		    return output;
		   
			
			
		} catch(IOException e) {
//			e.printStackTrace();
//			System.out.println("recvMsg() -> IO exception");
		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("recvMsg() -> Exception");
		}
		
		return null;
	}
	
	public boolean isConnected() {
		if(_conn==null)
			return false;
		else 
			return _conn.isConnected();
	}

}