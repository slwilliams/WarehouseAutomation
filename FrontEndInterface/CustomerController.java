/*
 * CustomerController - Handles button presses form the customer menu
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class CustomerController implements ActionListener
{	
	//gloabal as to allow access from inner class
	protected Database db;
	
	public CustomerController(Database _db)
	{
		//assign the passed db object
		db = _db;
	}
	
	//possible routes
	private final int ADD = 0;
	private final int EDIT = 1;
	private final int DELETE = 2;	
		
	public void actionPerformed(ActionEvent e)
	{		
		//get the button pressed as a string
		String button = e.getActionCommand();
		//convert to int
		int type = convertToInt(button);
		//do something dependent on button pressed
		switch(type)
		{
			case ADD:		new Customer_add(db);
							break;
							
			case EDIT:		new Customer_edit(db);
							break;
			
			case DELETE:	new Customer_delete(db);
							break;
		
		}
	}
	
	public int convertToInt(String name)
	{
		//returns an int
		if(name.equals("Add Customer"))
		{
			return ADD;
		}
		if(name.equals("Edit Customer"))
		{
			return EDIT;
		}
		if(name.equals("Delete Customer"))
		{
			return DELETE;
		}
		return -1;
	}
}