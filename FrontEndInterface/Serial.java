/*
 * Serial - Communicates with the C serial port server
 */

import java.io.*;
import java.net.*;

public class Serial
{
	//Creates a socket to 127.0.0.1:8000

	//the socket
	Socket requestSocket;
	
	PrintWriter out;
 	BufferedReader in;

	public Serial()
	{
		try
		{
			//try and open the socket
			requestSocket = new Socket("localhost", 8000);
		}
		catch(Exception e)
		{
			//something went wrong
			javax.swing.JOptionPane.showMessageDialog(null, "Failed to connect to socket:\n" + e);
		}

		try
		{
			//try and setup the buffered input and output
			out = new PrintWriter(requestSocket.getOutputStream(), true);
			out.flush();
			in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
		}
		catch(Exception e)
		{
			//somthing went wrong
			javax.swing.JOptionPane.showMessageDialog(null, "Failed to create in/out serial object:\n" + e);
		}
	}

	public void sendMessage(String msg)
	{
		//sends a message out of the socket
		//the C program recives this and tranmites it to the robot
		try
		{
			out.print(msg);
			out.flush();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Failed to send serial message: " + e);
		}
	}

	public String readMessage()
	{
		//reads a message from the socket
		String line = null;
		try
		{
			line = in.readLine();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Failed to read line: " + e);
		}

		//returns the read in message
		return line;
	}
}