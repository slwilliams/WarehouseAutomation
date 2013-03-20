/*
 * Item_edit - Edits an items properties
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Item_edit extends JFrame
{	
	//global variables to allow access from within inner class
	protected Database db;
	protected JTextField item_name;
	protected JTextField item_cost;
	protected JComboBox item_depot;
	protected JComboBox select_item;
	protected int id = 0;
	
	public Item_edit(Database _db)
	{
		super("Edit an Item");
		
		//assign passed db object				
		db = _db;
		
		//create a new panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(6,2,20,40));
		
		//create some labels and combo boxes
		JLabel selectItem = new JLabel("Select item");
		select_item = new JComboBox();
		item_depot = new JComboBox();
		
		//fill item combobox with data form database
		DBCursor cur = db.findResults("Items");
		while(cur.hasNext())
		{
			select_item.addItem(cur.next().get("Item_id"));
		}	
			
		//fill item_location combobox with data form database
		DBCursor cur3 = db.findResults("Items");
		while(cur3.hasNext())
		{
			item_depot.addItem(cur3.next().get("Item_location"));
		}
		
		//add a blank item, this will be what is shown first to the user
		item_depot.addItem("");
		item_depot.setSelectedItem("");
		
		//create another label
		JLabel updateItem = new JLabel("Update window");
				
		//add update button, this button finds the current data in the database via the id chosen by the user
		JButton update_item = new JButton("Update");				
		update_item.addActionListener(new ActionListener()
		{
			//this is called when the update_item button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//no validation is needed as data in the database has allready been validated
				//this line gets the current item data for the corresponding id from the database
				DBObject obj = db.findOne("Items", "Item_id", Integer.parseInt(select_item.getSelectedItem().toString()));
				
				//these lines set the text in the text fields to the data we just got
				item_name.setText(obj.get("Item_name").toString());
				item_cost.setText(obj.get("Item_cost").toString());
				item_depot.setSelectedItem(obj.get("Item_location"));
				id = Integer.parseInt(obj.get("Item_id").toString());
			}
		});
	
		//create some more labels
		JLabel itemName = new JLabel("Item name");
		JLabel itemCost = new JLabel("Item Cost");
		JLabel itemDepot = new JLabel("Select Depot");
		
		//create some text fields
		item_name = new JTextField();
		item_cost = new JTextField();
			
		//fill the depots combo box with data from the database
		DBCursor cur2 = db.findResults("Depots");
		while(cur2.hasNext())
		{
			item_depot.addItem((int)(Double.parseDouble(cur2.next().get("Depot_id").toString())));
		}		
		
		//add the elements to the panel
		panel.add(selectItem);
		panel.add(select_item);
		
		panel.add(updateItem);
		panel.add(update_item);
		
		panel.add(itemName);
		panel.add(item_name);
		
		panel.add(itemCost);
		panel.add(item_cost);
		
		panel.add(itemDepot);
		panel.add(item_depot);
		
		//create ok button and add action listener
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			//this is called when the ok button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//validation
				Validator validator = new Validator();
				
				if(item_name.getText().equals(""))
				{
					JOptionPane.showInputDialog(null, "Item must have a name");
					return;
				}
				if(item_cost.getText().equals(""))
				{
					JOptionPane.showInputDialog(null, "Item must have a cost");
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
				//remove current data and replace it with the new one
				BasicDBObject obj = db.makeItemDoc(id, item_name.getText(), Double.parseDouble(item_cost.getText().toString()), Integer.parseInt(item_depot.getSelectedItem().toString()));
				db.removeOne("Items", "Item_id", id);
				db.insertDoc("Items", obj);
				
				//show a message
				JOptionPane.showMessageDialog(null, "Item updated");
				
				//close the window
				dispose();
			}
		});
		
		//add the ok button to the panel
		panel.add(ok);
		
		//create a cancel button
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			//this is called when the cancel button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//close the window
				dispose();	
			}			
		});
		
		//add the cancel button to the panel
		panel.add(cancel);
		
		//add the panel to the frame
		add(panel);
		
		//center the frame
		setSize(300,400);
		setLocationRelativeTo(null);
		
		//show the window
		setVisible(true);
	}
}