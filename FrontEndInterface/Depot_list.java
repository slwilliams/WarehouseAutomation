/*
 * Depot_list - Views items in a selected depot
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Depot_list extends JFrame
{
	//global variable as to allow access form within inner class
	public Database db = null;

	public Depot_list(int depot_id, Database _db)
	{
		super("View Depot");
		//asign the passed database object
		db = _db;

		//creates a panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(1,1));

		//creates the textarea to hold the data
		JTextArea text = new JTextArea();
		text.setWrapStyleWord(true);

		//set text area table headings
		text.append("Name:\tID:\tCost:\n");

		//populate the textarea with data from the batabase
		DBCursor cur = db.findResults("Items");
		while(cur.hasNext())
		{
			DBObject dbo = cur.next();
			if(Integer.parseInt(dbo.get("Item_location").toString()) == depot_id)
			{
				text.append(dbo.get("Item_name") + "\t" + dbo.get("Item_id") + "\t £" + dbo.get("Item_cost") + "\n");
			}
		}

		//add a scroll bar to the textarea
		//this will enable lots of data to be viewed
		JScrollPane pane = new JScrollPane(text);

		//add the text area to the panel
		panel.add(pane, BorderLayout.CENTER);

		//add the panel to the frame
		add(panel);

		//center the frame
		setSize(300,150);
		setLocationRelativeTo(null);

		//show the frame
		setVisible(true);
	}
}