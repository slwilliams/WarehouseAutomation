/*
 * Item_delete - Removes an item from the database
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Item_delete extends JFrame
{	
	//global variables to allow access from within inner class
	protected Database db;
	protected JComboBox select_item;
	
	public Item_delete(Database _db)
	{
		super("Delete an Item");
		
		//assign passed database object
		db = _db;
		
		//create a jpanel with a grid layout to arrange the components
		JPanel panel = new JPanel(new GridLayout(2,2,20,30));
		
		//create a new jlabel and add it to the panel
		JLabel selectItem = new JLabel("Select Item");
		select_item = new JComboBox();
		
		//fill combo box with data from database
		DBCursor cur = db.findResults("Items");
		while(cur.hasNext())
		{
			select_item.addItem(cur.next().get("Item_id").toString());
		}
		
		//add the combo box to the panel
		panel.add(selectItem);
		panel.add(select_item);
		
		//create ok button
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			//this is called when the ok button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//remove one item form the database
				db.removeOne("Items", "Item_id", Integer.parseInt(select_item.getSelectedItem().toString()));
				
				//show a message
				JOptionPane.showMessageDialog(null, "Item deleted");
				
				//close the frame
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
				//close the frame
				dispose();	
			}			
		});
		
		//add the cancel button to the panel
		panel.add(cancel);
		
		//add the panel to the frame
		add(panel);
		
		//center the frame
		setSize(300,150);
		setLocationRelativeTo(null);
		
		//show the frame
		setVisible(true);
	}
}