/*
 * Login - Login dialog window
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login
{
	//Filereader class to read the login file
	FileReader reader = new FileReader();
	
	//these are global to allow access from within the action listener inner class
	public JFrame loginFrame;
	public JTextField user;
	public JPasswordField pass;
	public JButton login;	
	protected Database db;
	protected Serial serial;
	
	public Login(Database _db, Serial _serial)
	{
		//assign public variables to passed in ones
		db = _db;
		serial = _serial;
		
		//set up the window
		loginFrame = new JFrame("Login to access system");
		//cset the layout of the frame
		loginFrame.setLayout(new GridLayout(3,2,10,10));
		
		//create some labels
		JLabel username = new JLabel("Username:");
		JLabel password = new JLabel("Password:");
		
		//create a login button
		login = new JButton("Login");
		login.addActionListener(new ActionListener()
		{
			//this is called when the login button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//check to see if user & pass match given ones
				if(reader.correctLogin(user.getText(), pass.getText()))
				{
					//it was open main window
					new MainWindow(db, serial);
					loginFrame.dispose();
				}
				else
				{
					//it wasn't show failed message
					JOptionPane.showMessageDialog(null, "Incorrect username or password");
				}				
			}
		});
		
		//create the username input field
		user = new JTextField();
		user.addActionListener(new ActionListener()
		{   
			//this enables a user to press the enter button to login
		    public void actionPerformed(ActionEvent e)
		    {
		        login.doClick();
		    }
		});
		
		//create the password input field
		pass = new JPasswordField();
		pass.addActionListener(new ActionListener()
		{   
			//this enables a user to press the enter button to login
		    public void actionPerformed(ActionEvent e)
		    {
		        login.doClick();
		    }
		});
		
		//create an exit button
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener()
		{
			//this is called when exit is clicked
			public void actionPerformed(ActionEvent e)
			{
				//close the window
				loginFrame.dispose();
			}
		});
		
		//add elements to the frame
		loginFrame.add(username);
		loginFrame.add(user);
		loginFrame.add(password);
		loginFrame.add(pass);
		loginFrame.add(login);
		loginFrame.add(exit);
		login.requestFocus();
		
		//center the frame 
		loginFrame.setSize(200,150);
		loginFrame.setLocationRelativeTo(null);
		
		//show the frame
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
	}
}