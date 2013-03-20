/*
 * Main - Entry point of the program
 */

import java.util.Scanner;
import java.io.File;

public class Main
{
	public Main()
	{		
		//this section reads the current ip of the database from the ip file
		String databaseIp = null;
		try
		{
			Scanner scanner = new Scanner(new File("database"));
			databaseIp = scanner.nextLine();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Could not locate databse file, please reinstall\n " + e);
			System.exit(0);
		}
		
		//cretae a new database object
		Database Db = null;
		try
		{
			//attempt to connect the database
			Db = new Database(databaseIp, 27017);
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null, "Cannot connect to databse, is it running?");
			System.exit(0);
		}
	
		//setup serial object
		Serial Serial = new Serial();
		
		//create login window
		new Login(Db, Serial);
	}
	
	public static void main(String[] args)
	{
		//entry point
		new Main();
	}
}