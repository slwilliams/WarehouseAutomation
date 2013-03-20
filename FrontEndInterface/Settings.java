/*
 * Settings - A settings menu i.e. change password
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Settings extends JFrame
{
	public Settings()
	{
		super("Settings");
		
		//create a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(4,1,5,15));
		
		//create buttons
		JButton login_add = new JButton("Add New Login");
		login_add.addActionListener(new ActionListener()
		{
			//this is called when login_add is clicked
			public void actionPerformed(ActionEvent e)
			{
				//get new username
				String user = JOptionPane.showInputDialog(null, "Input new username:");
				if(user.equals(""))
				{
					JOptionPane.showMessageDialog(null, "No username entered");
					return;
				}
				
				//get password
				String pass = JOptionPane.showInputDialog(null, "Input new password:");
				if(pass.length() <= 4)
				{
					JOptionPane.showMessageDialog(null, "Password must be longer then 4 digits");
					return;
				}
				
				//create filereader object
				FileReader reader = new FileReader();
				
				//pass it the new details
				reader.newLogin(user, pass);
				
				//show a message
				JOptionPane.showMessageDialog(null, "User sucesfully added");
			}
		});
		
		JButton login_edit = new JButton("Edit Login");
		login_edit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//get current username
				String currentName = JOptionPane.showInputDialog(null, "Input current username:");
				
				//get current password
				String currentPass = JOptionPane.showInputDialog(null, "input currernt password:");
				
				//get new password
				String newPass = JOptionPane.showInputDialog(null, "input new password:");
				
				if(newPass.length() <= 4)
				{
					JOptionPane.showMessageDialog(null, "New password must be longer than 4 digits");
					return;
				}
				
				//create filreader object
				FileReader reader = new FileReader();
				
				//change the password
				if(reader.changePassword(currentName, currentPass, newPass) == true)
				{
					JOptionPane.showMessageDialog(null, "Password sucessfully chaned");
				}
				else
				{
					//user wasn't found or pass incorrect
					JOptionPane.showMessageDialog(null, "Username or password incorrect");
				}
			}
		});
		
		JButton login_delete = new JButton("Delete Login");
		login_delete.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//get username
				String username = JOptionPane.showInputDialog(null, "Input username:");
				//get password
				String password = JOptionPane.showInputDialog(null, "Input password:");
				
				//create the filereader object
				FileReader reader = new FileReader();
				
				if(reader.deleteUser(username, password) == true)
				{
					JOptionPane.showMessageDialog(null, "User deleted");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "User not found");
				}
			}
		});
		
		JButton database_ip = new JButton("Change database ip");
		database_ip.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//get new ip
				String newIp = JOptionPane.showInputDialog(null, "Input new IP:");
				
				//create file reader object
				FileReader reader = new FileReader();
				if(reader.changeDatabaseIp(newIp) == true)
				{
					JOptionPane.showMessageDialog(null, "Database ip changed");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Change ip failed, reinstall");
				}
				
			}
		});
		
		//add buttons to the panel
		panel.add(login_add);
		panel.add(login_edit);
		panel.add(login_delete);
		panel.add(database_ip);
		
		//add the panel to the frame
		add(panel);
		
		//centre the frame
		setSize(300,400);
		setLocationRelativeTo(null);
		
		//show frame
		setVisible(true);
	}		
}