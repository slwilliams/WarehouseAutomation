/*
 * Depot_select - A window to select a depot to view
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.*;

public class Depot_select extends JFrame
{
	//global variable to allow access from within inner class
	public Database db;
	public JComboBox select_depot;

	public Depot_select(Database _db)
	{
		super("Select Depot");

		//assign the passed db object
		db = _db;

		//create a new panel with a grid layout
		JPanel panel = new JPanel(new GridLayout(2,2,20,30));

		//create a label and combo box
		JLabel selectDepot = new JLabel("Select Depot");
		select_depot = new JComboBox();

		//get depots ids from database
		DBCursor cur = db.findResults("Depots");
		while(cur.hasNext())
		{
			select_depot.addItem((int)(Double.parseDouble(cur.next().get("Depot_id").toString())));
		}

		//create an ok button
		JButton ok = new JButton("Go");
		ok.addActionListener(new ActionListener()
		{
			//this is called once the ok button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//retreive the items for that depot
				new Depot_list(Integer.parseInt(select_depot.getSelectedItem().toString()), db);
			}
		});

		//create a cancel button
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			//this is called once the cancel button is clicked
			public void actionPerformed(ActionEvent e)
			{
				//close the window
				dispose();
			}
		});

		//add the elements to the panel
		panel.add(selectDepot);
		panel.add(select_depot);
		panel.add(ok);
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