package boo.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;
import java.util.Random;

public class FixedPortRMISocketFactory extends RMISocketFactory {
	
	public static int DEFAULT_BASE_PORT_NUMBER = 30000;
	public static int DEFAULT_RANGE_WIDTH = 10;
	
	private int basePortNumber;
	private int rangeWidth;
	private Random prng;
	
	public FixedPortRMISocketFactory() {
		this(DEFAULT_BASE_PORT_NUMBER, DEFAULT_RANGE_WIDTH);
	}
	
	public FixedPortRMISocketFactory(int basePortNumber) {
		this(basePortNumber, DEFAULT_RANGE_WIDTH);
	}
	
	public FixedPortRMISocketFactory(int basePortNumber, int rangeWidth) {
		this.basePortNumber = basePortNumber;
		this.rangeWidth = rangeWidth;
		this.prng = new Random();
	}
	
	private int getRandomPort() {
		int portNumber = basePortNumber + prng.nextInt(rangeWidth);
		System.err.println("socket on port " + portNumber);
		return portNumber;
	}
	
	public Socket createSocket(String host, int port) 
	        throws IOException {
		if (port == 0)
			port = getRandomPort();
	    return new Socket(host, port); 
	} 
	
	public ServerSocket createServerSocket(int port) 
	        throws IOException {   
		if (port == 0)
			port = getRandomPort();
	    return new ServerSocket(port); 
	}
	  
}
