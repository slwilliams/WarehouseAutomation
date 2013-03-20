/*
 * Customer_menu - A menu to access customer functionality
 */

import javax.swing.*;
import java.awt.*;

public class Customer_menu extends JFrame
{	
	public Customer_menu(Database db)
	{
		super("Customer menu");
		
		//create a panel with a gid layout
		JPanel panel = new JPanel(new GridLayout(3,1,5,15));
		
		//create buttons
		JButton customer_add = new JButton("Add Customer");
		customer_add.addActionListener(new CustomerController(db));
		
		JButton customer_edit = new JButton("Edit Customer");
		customer_edit.addActionListener(new CustomerController(db));
		
		JButton customer_delete = new JButton("Delete Customer");
		customer_delete.addActionListener(new CustomerController(db));
		
		//add buttons to the panel
		panel.add(customer_add);
		panel.add(customer_edit);
		panel.add(customer_delete);
		
		//add the panel to the frame
		add(panel);
		
		//centre the frame
		setSize(300,300);
		setLocationRelativeTo(null);
		
		//show frame
		setVisible(true);
	}
}