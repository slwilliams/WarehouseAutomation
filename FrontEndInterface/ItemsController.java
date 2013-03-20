/*
 * ItemsController - Handles button presses for the Items menu 
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ItemsController implements ActionListener
{
	//global variable to allow access from within inner class
	protected Database db;
	
	public ItemsController(Database _db)
	{
		//assign db object
		db = _db;
	}
	
	//possible routes
	private final int ADD = 0;
	private final int EDIT = 1;
	private final int DELETE = 2;	
	
	public void actionPerformed(ActionEvent e)
	{
		//get the button clicked
		String button = e.getActionCommand();
		int type = convertToInt(button);
		
		switch(type)
		{
			case ADD:		new Item_add(db);
							break;
							
			case EDIT:		new Item_edit(db);
							break;
			
			case DELETE:	new Item_delete(db);
							break;
		
		}
	}
	
	public int convertToInt(String name)
	{
		//convert to int
		if(name.equals("Add Item"))
		{
			return ADD;
		}
		if(name.equals("Edit Item"))
		{
			return EDIT;
		}
		if(name.equals("Delete Item"))
		{
			return DELETE;
		}
		return -1;
	}
}