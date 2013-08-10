# Warehouse Automation
## Scott Williams
My final year A-Level project investigating automating a warehouse stock pickup and input system.
The system involved 2 parts; a GUI front end to enable workers to enter customers and order items, and a prototype robot powered by an Arduino Mega2560.

The frontend was written in Java (swing) and connected to a MonogoDB backend database for stock levels and customer data etc.

The Java frontend communicated to the robot via Bluetooth, the Arduino has a Bluetooth modem connected to its serial ports.

The frontend contains an implementation of Dijkstra's algorithm to find the shortest, most efferent path around the warehouse which it sends to the robot.

The robot contains an array of IR sensors and a PID algorithm implementation enables the robot to follow black lines painted onto the warehouse floor.

The project also included a 20,000 word write up available here: [Write up](http://files.scottw.co.uk/warehousewriteup.pdf)
