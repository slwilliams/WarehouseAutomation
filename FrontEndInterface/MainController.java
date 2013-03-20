/*
 * MainController - Handles button presses for mainwindow
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainController implements ActionListener
{
	//global variables to allow access from within inner class
	public Database db;
	public Serial serial;
	
	public MainController(Database _db, Serial _serial)
	{
		db = _db;
		serial = _serial;
	}
	
	//possible routes
	private final int CUSTOMERS = 0;
	private final int ITEMS = 1;
	private final int DEPOTS = 2;	
	private final int NEWORDER = 3;
	private final int MANUAL = 4;
	private final int SECURITY = 5;
	private final int SETTINGS = 6;
	
	public void actionPerformed(ActionEvent e)
	{
		String button = e.getActionCommand();
		int type = convertToInt(button);
		switch(type)
		{
			//do somthing depending on which button was pressed
			case CUSTOMERS:	new Customer_menu(db);
							break;
							
			case ITEMS:		new Item_menu(db);
							break;
			
			case DEPOTS:	new Depot_select(db);
							break;
							
			case NEWORDER:	new newOrder(db, serial);
							break;
							
			case MANUAL:	new Manual(serial);
							break;
							
			case SETTINGS:	new Settings();
							break;
		}
	}
	
	public int convertToInt(String name)
	{
		//return int dependent on button pressed
		if(name.equals("Customers"))
		{
			return CUSTOMERS;
		}
		if(name.equals("Items"))
		{
			return ITEMS;
		}
		if(name.equals("Depots"))
		{
			return DEPOTS;
		}
		if(name.equals("New Order"))
		{
			return NEWORDER;
		}
		if(name.equals("Manual Control"))
		{
			return MANUAL;
		}
		if(name.equals("Settings"))
		{
			return SETTINGS;
		}
		return -1;
	}
}