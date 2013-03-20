/*
 * Item_menu - A menu to select further item functionality
 */

import javax.swing.*;
import java.awt.*;

public class Item_menu extends JFrame
{	
	public Item_menu(Database db)
	{
		super("Item menu");
		
		//create a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(3,1,5,15));
		
		//create buttons
		JButton item_add = new JButton("Add Item");
		item_add.addActionListener(new ItemsController(db));
		
		JButton item_edit = new JButton("Edit Item");
		item_edit.addActionListener(new ItemsController(db));
		
		JButton item_delete = new JButton("Delete Item");
		item_delete.addActionListener(new ItemsController(db));
		
		//add the buttons to the panel
		panel.add(item_add);
		panel.add(item_edit);
		panel.add(item_delete);
		
		//add the panel to the frame
		add(panel);
		
		//centre the frame
		setSize(300,300);
		setLocationRelativeTo(null);
		
		//show frame
		setVisible(true);
	}
}