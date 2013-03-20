/*
 * newOrder - Allows a new order to be entered
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class newOrder extends JFrame
{
	//global variables to allow access from within inner class
	protected Database db;
	protected Serial serial;
	protected JComboBox select_customer;
	protected JTextField input_items;

	public newOrder(Database _db, Serial _serial)
	{
		super("Input new Order");
		
		//assign passed global variables
		db = _db;
		serial = _serial;

		//cretae a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(3,2,20,30));

		//cretae a label and combo box
		JLabel selectCustomer = new JLabel("Select Customer");
		select_customer = new JComboBox();

		//fill combo box with data from database
		DBCursor cur = db.findResults("Customers");
		while(cur.hasNext())
		{
			select_customer.addItem(cur.next().get("Customer_id").toString());
		}

		//add elements to the panel
		panel.add(selectCustomer);
		panel.add(select_customer);

		//textfield to input item ids to order
		JLabel inputItems = new JLabel("Input item ids");
		input_items = new JTextField();

		//add the items to the panel
		panel.add(inputItems);
		panel.add(input_items);

		//create ok button and add action listener
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			//this is called when the ok button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//validation
				Validator validator = new Validator();
				if(validator.isNotInt(select_customer.getSelectedItem().toString()) == true)
				{
					JOptionPane.showMessageDialog(null, "Customer id must be an integer");
					return;
				}
				if(input_items.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Please input item ids");
					return;
				}
				if(validator.notCorrectOrderItems(input_items.getText()) == true)
				{
					JOptionPane.showMessageDialog(null, "Order items not formated correctly,\nmust be in the format 1,2,3,4");
					return;
				}

				//validation compleate
				//get customer id from text field
				int cusId = Integer.parseInt(select_customer.getSelectedItem().toString());
				
				//create an array of item ids by seperating the textfield e.g. 1,2,3,4
				String[] items = input_items.getText().split(",");
				
				//convert that array into an array of ints
				int[] itemids = new int[items.length];
				
				for(int i = 0; i < items.length; i ++)
				{
					itemids[i] = Integer.parseInt(items[i]);
				}
				
				//generate total cost of the order
				double cost = 0;
				DBCursor cur = db.findResults("Items");
				
				while(cur.hasNext())
				{
					cost += Double.parseDouble(cur.next().get("Item_cost").toString());
				}

				//add the new order details into the database
				db.addNewOrder(cusId, itemids, cost);

				//get the COMMANDS (L,R,U,F) from the graph helper by converting the result
				//of getRoute
				String[] result = new Graph().getCommands(getRoute(itemids, 2, 0));
				
				//this finds the actual length of the results array
				int arrayLength = -1;
				for(int i = 0 ; i < result.length; i ++)
				{
					if(result[i] == null)
					{
						arrayLength = i;
						break;
					}
				}

				//copy results array into the MainWindow commands array
				MainWindow.commands = new String[arrayLength];

				//remove the selected items from the items database
				for(int i = 0; i < itemids.length; i ++)
				{
					db.removeOne("Items", "Item_id", itemids[i]);
				}
				
				//send the null command to start the robot driving
				serial.sendMessage("F");
				
				//show a message to user
				JOptionPane.showMessageDialog(null, "Commands are being sent");
				
				//start the timer
				MainWindow.timer.start();
				
				//generate address label
				generateAddressLabel(cusId);

				//close the frame
				dispose();
			}
		});

		//add the ok button
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
		setSize(300,180);
		setLocationRelativeTo(null);

		//show the frame
		setVisible(true);
	}
	
	public void generateAddressLabel(int _cusId)
	{
		JFrame labelFrame = new JFrame("Adress Label");
		JTextArea text = new JTextArea(db.findOne("Customers", "Customer_id", _cusId).get("Customer_name").toString() + "\n" + db.findOne("Customers", "Customer_id", _cusId).get("Customer_address").toString());
		labelFrame.add(text);
		labelFrame.setSize(200,200);
		labelFrame.setLocationRelativeTo(null);
		labelFrame.setVisible(true);
	}

	//global variables so as to keep state during recursive calls
	int counter = 0;
	int mainCounter = 0;

	//the array which will be returned
	int[] fullRoute = new int[100];

	//method to generate the array of commands
	public int[] getRoute(int[] items, int _x1, int _y1)
	{
		//this check will tell if this is the first time this method has been called
		if(fullRoute[fullRoute.length-1] == 0)
		{
			//it is so fill the array with -1s
			java.util.Arrays.fill(fullRoute,-1);
		}

		//create graph object
		Graph graph = new Graph();
		
		//this variable will hold the location of the current item needed
		int location = -1;

		//get items data from database
		DBCursor cur2 = db.findResults("Items");
		while(cur2.hasNext())
		{
			DBObject dbo = cur2.next();
			//if this item has the same id as the one we need asign the location
			//variable to the location of this item
			if(Integer.parseInt(dbo.get("Item_id").toString()) == items[counter])
			{
				location = Integer.parseInt(dbo.get("Item_location").toString());
				break;
			}
		}

		//get depot data from database
		DBCursor cur3 = db.findResults("Depots");

		int _x = -1;
		int _y = -1;

		//the item location is a depot id
		//this code finds the correct depot based on the item location
		//and assigns x and y to the position of that depot
		while(cur3.hasNext())
		{
			DBObject dbo = cur3.next();
			int _temp = (int)Double.parseDouble(dbo.get("Depot_id").toString());
			//if this depot id == the items location
			if(_temp == location)
			{
				//it is; assign x and y
				_x = (int)Double.parseDouble(dbo.get("x").toString());
				_y = (int)Double.parseDouble(dbo.get("y").toString());
				break;
			}
		}

		//now we have the location of the item we wish to collect
		//at this point in time we are at our pervious item location
		//so we use the pervious iterations x and y which are passed in
		//and the x and y just calculated
		//if it is the first iteration 2,0 is passed (starting position)
		int[] result = graph.dijkstra(_x1,_y1,_x,_y);

		//result is now an array contating the shortest route in terms of nodes e.g. 1,2,3,4
		//however we need this in terms of commands e.g. L,R,U,L
		//so the result of this method will be passed into Graph.getCommands()

		//however the result array is only part of the journy we need
		//so it will be APPENDED to fullRoute

		for(int i = 0; i < result.length; i ++)
		{
			//append the results array to fullRoute
			fullRoute[mainCounter] = result[i];
			mainCounter ++;
		}

		//if there are more items left to collect
		if(counter < items.length-1)
		{
			//there are; increment counter and re-call this funtion with our current
			//x and y values which will be used as the robots current position
			counter ++;
			return getRoute(items, _x, _y);
		}
		else
		{
			//no more items to collect, deposit current items at packaging & dispatch
			int[] finalResult = new Graph().dijkstra(_x, _y, 2, 2);
			for(int i = 0; i < finalResult.length; i ++)
			{
				//append the final results array to fullRoute
				fullRoute[mainCounter] = finalResult[i];
				mainCounter ++;
			}
		
			//now return to starting point
			fullRoute[mainCounter] = 2;
			fullRoute[mainCounter+1] = 1;

			//return the sorted array
			return sortArray();
		}
	}

	//method to shorten the array
	public int[] sortArray()
	{
		//calculate the actual size for the new array
		int size = -1;
		for(int i = 0; i < fullRoute.length; i ++)
		{
			if(fullRoute[i] == -1)
			{
				size = i;
				break;
			}
		}

		//work out duplicates in the array
		int minus = 0;
		for(int i = 0; i < size; i ++)
		{
			if(fullRoute[i] == fullRoute[i+1])
			{
				minus ++;
			}
		}

		//minus the no. of dulicates from the length of the new array
		int[] newRoute = new int[size-minus];

		//copy the old array into the new array, skiping duplicates
		int j = 0;
		for(int i = 0; i < (newRoute.length+minus); i ++)
		{
			if(fullRoute[i] == fullRoute[i+1])
			{
				continue;
			}
			newRoute[j] = fullRoute[i];
			j ++;
		}

		//return the new array
		return newRoute;
	}
}