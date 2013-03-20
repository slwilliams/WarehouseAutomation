/*
 * Customer_delete - Deletes a customer from the database
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Customer_delete extends JFrame
{	
	//global as to allow access from inner class
	protected Database db;
	protected JComboBox select_customer;
	
	public Customer_delete(Database _db)
	{
		super("Delete a Customer");
		
		//assign passed db object
		db = _db;
		
		//cretae a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(2,2,20,30));
		
		//cretae a label and combo box
		JLabel selectCustomer = new JLabel("Select Customer");
		select_customer = new JComboBox();
		
		//fill combo box with data from database
		DBCursor cur = db.findResults("Customers");	
		while(cur.hasNext())
		{
			select_customer.addItem(cur.next().get("Customer_id"));
		}
		
		//add the elements to the panel
		panel.add(selectCustomer);
		panel.add(select_customer);
		
		//create the ok button
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			//this is called when the ok button is pressed
			public void actionPerformed(ActionEvent e)
			{
				//remove the customer from the database
				db.removeOne("Customers", "Customer_id", Integer.parseInt(select_customer.getSelectedItem().toString()));
				
				//show a message
				JOptionPane.showMessageDialog(null, "Customer deleted");
				
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
		
		//add the cancel button the the panel
		panel.add(cancel);
		
		//add the panel to the frame
		add(panel);
		
		//center the frame
		setSize(300,150);
		setLocationRelativeTo(null);
		
		//show the window
		setVisible(true);
	}
}