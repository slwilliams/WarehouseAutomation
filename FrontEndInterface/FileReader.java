/*
 * FileReader - Reads the login & database text file
 */

import java.util.*;
import java.io.*;

public class FileReader
{
	//the main login file
	File fileName = new File("login");

	public FileReader()
	{
		//Constructor
	}
	
	//writes a new login the the login text file
	public void newLogin(String _name, String _pass)
	{
		//hash the name and password
		int name = _name.hashCode();
		int pass = _pass.hashCode();
		
		//concatinate these together
		String details = name + ";" + pass;
		
		//and hash this again
		details = Integer.toString(details.hashCode());
		
		//the file writer object
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(fileName, true);
			PrintWriter pw = new PrintWriter(fw);
			
			//write the details to the file
			pw.println(details);
			
			//close the file
			pw.close();
			fw.close();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "file not found; reinstall" + e);
		}	
	}
	
	//checks to see if a correct login
	public boolean correctLogin(String _name, String _pass)
	{
		//hash the username and password
		int name = _name.hashCode();
		int pass = _pass.hashCode();
		
		//concatinate these together
		String details = name + ";" + pass;
		//and hash this again
		details = Integer.toString(details.hashCode());
		
		//the scanner object
		Scanner file = null;
		try
		{
			//open the file
			file = new Scanner(fileName);
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Couldn't open login file; reinstall" + e);
		}
		
		//iterate through the file contents
		while(file.hasNext())
		{
			//check to see if a line in the file
			//is equal to what was passed
			if(file.nextLine().equals(details))
			{
				return true;
			}
		}
		//a mathching login wasn't found
		return false;
	}
	
	//changes a users password
	public boolean changePassword(String currentName, String currentPass, String newPass)
	{
		//hash the details to check if correct old password
		String details = Integer.toString((Integer.toString(currentName.hashCode()) + ";" + Integer.toString(currentPass.hashCode())).hashCode());
		
		//open the file
		Scanner file = null;
		try
		{
			file = new Scanner(fileName);
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Couldn't open login file; reinstall" + e);
		}
		
		//a string to hold the contents of the login file
		String fullFile = "";
		boolean found = false;
		
		//iterate through the file
		while(file.hasNext())
		{
			//attempt to find the user
			String temp = file.nextLine();
			if(temp.equals(details))
			{
				//user was found
				//we don't want to include this use in the file
				//so we continue one iteration, this will mean it's not appended to the string
				//and therefore not writen to the new file
				found = true;
				continue;
			}
			//append the login to the string
			fullFile += temp;
		}
		
		if(found == false)
		{
			//user wasn't found
			return false;
		}
		
		//the filewriter object
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(fileName);
			fw.write("");
			PrintWriter pw = new PrintWriter(fw);
			
			//write the new file without the current user
			pw.println(fullFile);
			fw.close();
			pw.close();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Couldn't write new login file; reinstall" + e);
		}
		
		//add the new user
		newLogin(currentName, newPass);
		
		return true;
	}
	
	//deletes a users login from the file
	public boolean deleteUser(String name, String pass)
	{
		//hash the user & pass to find the user
		String details = Integer.toString((Integer.toString(name.hashCode()) + ";" + Integer.toString(pass.hashCode())).hashCode());
		
		//open the file
		Scanner file = null;
		try
		{
			file = new Scanner(fileName);
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Coudn't open login file; reinstall" + e);
		}
			
		//a string to hold the contents of the login file
		String fullFile = "";
		boolean found = false;
		
		while(file.hasNext())
		{
			//attempt to find the user
			String temp = file.nextLine();
			if(temp.equals(details))
			{
				//found it
				//we don't want to include this use in the file
				//so we continue one iteration, this will mean it's not appended to the string
				//and therefore not writen to the new file
				found = true;
				continue;
			}
			fullFile += temp;
		}
		
		if(found == false)
		{
			//user wasn't found
			return false;
		}
		
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(fileName);
			fw.write("");
			PrintWriter pw = new PrintWriter(fw);
			//re-write the file exculding the user in question
			pw.println(fullFile);
			fw.close();
			pw.close();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Coudn't write login fule; reinstall" + e);
		}
		
		return true;
	}
	
	//changes the ip of the database to connect to
	public boolean changeDatabaseIp(String ip)
	{
		//open the file
		FileWriter fw = null;
		try
		{
			fw = new FileWriter("database");
			
			//clear the file
			fw.write("");
			
			PrintWriter pw = new PrintWriter(fw);
			//write the new ip
			pw.println(ip);
			
			//close the file
			fw.close();
			pw.close();
			return true;
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Couldn't write database ip file; reinstall" + e);
			return false;
		}		
	}
}