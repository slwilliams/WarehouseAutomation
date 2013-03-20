/*
 * MainWindow - The main program window
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainWindow implements ActionListener
{
	//time is used to poll the serial port for command requests without disrupting frame reasponsivnes
	public static Timer timer;
	//public array will be set once a new order has been places and the route calculated
	public static String[] commands;
	//will be used to keep track of which command to give the robot on request
	public static int counter = 0;
	//the serial helper object
	public Serial serial;
	
	//global variable to allow them to be changed from within the timer
	public JLabel label1 = null;
	public JLabel label2 = null;
	public Database db = null;
	
	//variables for the timer
	int depotId = 0;
	String inv = "";
	
	public MainWindow(Database _db, Serial _serial)
	{
		//assign the passed db object
		db = _db;
		//asign the serial object
		serial = _serial;
		
		//create the timer but dont start it
		timer = new Timer(600, this);		
		
		//create the main window
		JFrame mainWindow = new JFrame("Automated warehouse System");
		
		//add a menu
		JMenu databaseMenu = new JMenu("Database");
	
		//add items to the menu
		JMenuItem customersItem = new JMenuItem("Customers");
		customersItem.addActionListener(new MainController(db, serial));
		databaseMenu.add(customersItem);
	
		JMenuItem itemsItem = new JMenuItem("Items");
		itemsItem.addActionListener(new MainController(db, serial));
		databaseMenu.add(itemsItem);	
	
		JMenuItem depotsItem = new JMenuItem("Depots");
		depotsItem.addActionListener(new MainController(db, serial));
		databaseMenu.add(depotsItem);
		
		//add a menu	
		JMenu robotMenu = new JMenu("Robot");
		
		//add items to the menu
		JMenuItem newOrderItem = new JMenuItem("New Order");
		newOrderItem.addActionListener(new MainController(db, serial));
		robotMenu.add(newOrderItem);
		
		JMenuItem manualItem = new JMenuItem("Manual Control");
		manualItem.addActionListener(new MainController(db, serial));
		robotMenu.add(manualItem);
		
		//add a menu
		JMenu optionsMenu = new JMenu("Options");
		
		//add items to the menu
		JMenuItem settingsItem = new JMenuItem("Settings");
		settingsItem.addActionListener(new MainController(db, serial));
		optionsMenu.add(settingsItem);
		
		//add menus to the menu bar
		JMenuBar bar = new JMenuBar();
		mainWindow.setJMenuBar(bar);		
		bar.add(databaseMenu);
		bar.add(robotMenu);
		bar.add(optionsMenu);
		
		//set the frame layout
		mainWindow.setLayout(new GridLayout(1,1,10,1));
	
		//add some panel to layout the window
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(2,1));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		
		//add the main text
		JLabel label = new JLabel("<HTML><center>Welcome to the Automated Warehouse System!<br>Use the menu above to access all program functionality.</center></html>");
		label.setFont(new Font("Arial", 1, 20));
		panel.add(label);
		
		//these lables are update automaticly via the actionPerformed method
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,2));
		
		label1 = new JLabel("<html>Database Statistics:<br><br>Total records: " + db.totalRecords + " <br>Total Customers: " + db.customerId + " <br>Total orders: " + db.orderId + " <br></html>", JLabel.LEFT);
		label2 = new JLabel("<html>Robot information:<br><br>Current Depot: " + depotId + " <br>Current Inventory: " + inv + "<br>Robot is not moving</html>", JLabel.RIGHT);
		
		//add the labels to the panel
		panel2.add(label1);
		panel2.add(label2);		
		
		//add the panels to the top panel
		top.add(panel);
		top.add(panel2);
		
		//add the top panel to the frame
		mainWindow.add(top);
		
		//set the program to exit if the window is closed
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//center the frame
		mainWindow.setSize(500,500);
		mainWindow.setLocationRelativeTo(null);
			
		//show the frame
		mainWindow.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		//this is fired every 100 milliseconds once the timer is started from within newOrder
		String data = serial.readMessage();
		
		//update the main text
		label1.setText("<html>Database Statistics:<br><br>Total records: " + db.totalRecords + " <br>Total Customers: " + db.customerId + " <br>Total orders: " + db.orderId + " <br></html>");
		label2.setText("<html><p>Robot information:<br><br>Current Depot: " + depotId + " <br>Current Inventory: " + inv + "</br>Robot is moving</p></html>");
		
				
		//if the robot is not moving
		if(data.equals("Request"))
		{
			//robot is at junction
			//send new command
			if(counter > commands.length)
			{
				//robot is facing the wrong direction at depot 0
				serial.sendMessage("E");
				//that command will make it perform  U turn but stay at depot 0
				
				//inform the user the robot has stopped
				label2.setText("<html><p>Robot information:<br><br>Current Depot: " + depotId + " <br>Current Inventory: " + inv + "</br>Robot has stopped moving</p></html>");
				
				//reset the counter
				counter = 0;
				//stop this timer
				timer.stop();				
			}
			else
			{
				//robot is not moving
				//send its next command
				serial.sendMessage(commands[counter]);
				inv +=
				counter ++;
			}			
		}
	}	
}