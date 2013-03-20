/*
 * Customer_add - Enables a customer to be added to the database
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Customer_add extends JFrame
{	
	//gloabl so as to allow access from inner class
	protected Database db;
	protected JTextField customer_name;
	protected JTextField customer_address;
	protected JTextField customer_phone;
	
	public Customer_add(Database _db)
	{
		super("Add a Customer");
		
		//assign passed db object
		db = _db;
		
		//create a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(4,2,20,50));
		
		//cretae some lables
		JLabel customerName = new JLabel("Customer name");
		JLabel customerAddress = new JLabel("Customer Address");
		JLabel customerPhone = new JLabel("Customer Phone");
		
		//textfields for user input
		customer_name = new JTextField();
	 	customer_address = new JTextField();
		customer_phone = new JTextField();
		
		//add the elements to the panel
		panel.add(customerName);
		panel.add(customer_name);
		
		panel.add(customerAddress);
		panel.add(customer_address);
		
		panel.add(customerPhone);
		panel.add(customer_phone);
		
		//create the ok button
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener()
		{
			//this is called when the ok button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//validation
				Validator validator = new Validator();
				
				if(validator.containsNumbers(customer_name.getText()) == true)
				{
					JOptionPane.showMessageDialog(null, "Customer name connot contain numbers");
					return;					
				}
				if(customer_name.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Customer must have a name");
					return;
				}
				if(validator.isNotInt(customer_phone.getText()) == true)
				{
					JOptionPane.showMessageDialog(null, "Customer phone cannot contain letters");
					return;
				}			
				//validation compleate
				//add the customer to the database
				
				//at this stage the cusomter has no orders
				//so an empty array is passed
				int[] orders = {};
				db.insertDoc("Customers", db.makeCustomerDoc(customer_name.getText(), customer_address.getText(), customer_phone.getText(), orders));
				
				//show a message
				JOptionPane.showMessageDialog(null, "Customer Saved");
				
				//close the frame
				dispose();
			}
		});
		
		//add the button to the panel
		panel.add(ok);
		
		//create cancel button
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
		
		//add the cancel button the the panel
		panel.add(cancel);
		
		//add the panel to the frame
		add(panel);
		
		//center the screen
		setSize(300,325);
		setLocationRelativeTo(null);
		
		//show the window
		setVisible(true);
	}
}