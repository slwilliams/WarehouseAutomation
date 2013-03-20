/*
 * Manual - Allows manual control of the robot
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Manual extends JFrame implements KeyListener
{
	//global variable to allow access from within inner methods
	Serial serial = null;
	public Manual(Serial _serial)
	{
		super("Manual Control");
		
		//assign the serial object
		serial = _serial;
		
		//send M to the robot
		//this will put it into manual control mode
		serial.sendMessage("M");
		
		//add the keylistener to the frame
		addKeyListener(this);
		
		//cretae a label
		JLabel label = new JLabel("<html><center>Use the arrow keys to move the robot<br>Press e to exit this window</center></html>", JLabel.CENTER);
		add(label);

		//centre the frame
		setSize(300,300);
		setLocationRelativeTo(null);

		//show frame
		setVisible(true);
	}	
	
	//necessary methods to override
	public void keyReleased(KeyEvent k){}
	public void keyTyped(KeyEvent k){}
	
	public void keyPressed(KeyEvent k)
	{
		final int UP = 38;
		final int DOWN = 40;
		final int LEFT = 37;
		final int RIGHT = 39;
		final int EXIT = 69;
		
		switch(k.getKeyCode())
		{
			case UP: 	serial.sendMessage("1");
						break;
						
			case DOWN:	serial.sendMessage("2");
						break;
						
			case LEFT: 	serial.sendMessage("3");
						break;
						
			case RIGHT:	serial.sendMessage("4");
						break;
						
			case EXIT:  serial.sendMessage("S");
						dispose();
						break;
		}
	}	
}
