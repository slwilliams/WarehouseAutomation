/*
 * Customer_edit - Enables a customers details to be editied
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Customer_edit extends JFrame
{	
	//global as to allow access from inner class
	protected Database db;
	protected JComboBox select_customer;
	protected JTextField customer_name;
	protected JTextField customer_address;
	protected JTextField customer_phone;
	int id = 0;
	int[] orders = {1,2};
	
	public Customer_edit(Database _db)
	{
		super("Edit a Customer");
		//assign the passed db object
		db = _db;
		
		//create a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(6,2,20,40));
		
		//cretae a label and combo box
		JLabel selectCustomer = new JLabel("Select Customer");
		select_customer = new JComboBox();
		
		//fill combo box with data from database
		DBCursor cur = db.findResults("Customers");
		while(cur.hasNext())
		{
			select_customer.addItem(cur.next().get("Customer_id"));
		}
		
		//cretae another label
		JLabel updateCustomer = new JLabel("Update window");		
		
		//create update button
		JButton update_customer = new JButton("Update");		
		update_customer.addActionListener(new ActionListener()
		{
			//this actionlistner fills the textboxes with data from the database
			public void actionPerformed(ActionEvent e)
			{
				//find the correct document from the database
				DBObject doc = db.findOne("Customers", "Customer_id", Integer.parseInt(select_customer.getSelectedItem().toString()));
				//set the textfields, no validation needed here
				customer_name.setText(doc.get("Customer_name").toString());
				customer_address.setText(doc.get("Customer_address").toString());
				customer_phone.setText(doc.get("Customer_phone").toString());
				id = Integer.parseInt(doc.get("Customer_id").toString());				
			}
		});
		
		//create some more labels
		JLabel customerName = new JLabel("Customer name");
		JLabel customerAddress = new JLabel("Customer Address");
		JLabel customerPhone = new JLabel("Customer Phone");
		
		//cretae some text fields
		customer_name = new JTextField();
		customer_address = new JTextField();
		customer_phone = new JTextField();
		
		//add the elements to the panel
		panel.add(selectCustomer);
		panel.add(select_customer);
		
		panel.add(updateCustomer);
		panel.add(update_customer);
			
		panel.add(customerName);
		panel.add(customer_name);
		
		panel.add(customerAddress);
		panel.add(customer_address);
		
		panel.add(customerPhone);
		panel.add(customer_phone);
		
		//create ok button
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			//this is called when ok button is clicked
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
				//remove old customer document and insert new one
				BasicDBObject obj = db.makeCustomerDoc(id, customer_name.getText(), customer_address.getText(), customer_phone.getText(), orders);
				db.removeOne("Customers", "Customer_id", id);
				db.insertDoc("Customers", obj);
				
				//show a message
				JOptionPane.showMessageDialog(null, "Customer updated");
				
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
		
		//show the frame
		setVisible(true);
	}
}