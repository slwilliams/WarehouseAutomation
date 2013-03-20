/*
 * Item_add - Adds an item to the database
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Item_add extends JFrame
{	
	//global variables to allow access from within inner class
	protected Database db;
	protected JTextField item_name;
	protected JTextField item_cost;
	protected JComboBox item_depot;

	public Item_add(Database _db)
	{
		super("Add an Item");
		//assign passed databse object
		db = _db;
		
		//create a new panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(4,2,20,50));
		
		//create the text labels
		JLabel itemName = new JLabel("Item name");
		JLabel itemCost = new JLabel("Item Cost");
		JLabel itemDepot = new JLabel("Select Depot");
		
		//create the user input fields
		item_name = new JTextField();
		item_cost = new JTextField();
		item_depot = new JComboBox();
		
		//fill combo box with data from database
		DBCursor cur = db.findResults("Depots");
		while(cur.hasNext())
		{
			item_depot.addItem((int)(Double.parseDouble(cur.next().get("Depot_id").toString())));
		}		
		
		//add elements to the panel
		panel.add(itemName);
		panel.add(item_name);
		
		panel.add(itemCost);
		panel.add(item_cost);
		
		panel.add(itemDepot);
		panel.add(item_depot);
		
		//create ok button
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener()
		{
			//this is called when ok button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//validation
				Validator validator = new Validator();
				
				if(item_name.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Item must have a name");
					return;
				}
				if(item_cost.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Item must have a cost");
					return;
				}
				if(validator.isNotDouble(item_cost.getText()) == true)
				{
					JOptionPane.showMessageDialog(null, "Item cost cannot contain letters");
					return;
				}
				if(validator.isNotInt(item_depot.getSelectedItem().toString()) == true)
				{
					JOptionPane.showMessageDialog(null, "Item depot must be an integer");
					return;
				}
				
				//validation compleate
				//add data into database				
				db.insertDoc("Items", db.makeItemDoc(item_name.getText(), Double.parseDouble(item_cost.getText()),Integer.parseInt(item_depot.getSelectedItem().toString())));
				
				//show a message
				JOptionPane.showMessageDialog(null, "Item added");
				
				//close the window
				dispose();
			}
		});
		
		//add ok button
		panel.add(ok);
		
		//create cancel button
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			//this is called when cancel button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//close the window
				dispose();	
			}			
		});
		
		//add close button
		panel.add(cancel);
		
		//add the panel to the frame
		add(panel);
		
		//center window
		setSize(300,325);
		setLocationRelativeTo(null);
		
		//show frame
		setVisible(true);
	}
}