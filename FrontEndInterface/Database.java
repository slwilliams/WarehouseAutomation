/*
 * Database - Functionality connected to the database
 */

import com.mongodb.*;

public class Database
{
	//gloabl variables
	Mongo mongo = null;
	DB db = null;
	public int customerId = 0;
	public int itemId = 0;
	public int orderId = 0;
	public int orderItemsId = 0;
	public int totalRecords = 0;
	
	public static void main(String[] args)
	{
		new Database("127.0.0.1", 28017);
	}
	
	public Database(String ip, int port)
	{
		try
		{
			//try and connect to the database
			mongo = new Mongo(ip, port);
		}catch(Exception e)
		{
			//somthing went wrong
			javax.swing.JOptionPane.showMessageDialog(null, "Could not connect to database" + e);
		}
		
		//select the Warehouse database
		db = mongo.getDB("Warehouse");
		//login to the database
	//	db.authenticate("admin", "1234".toCharArray());
		
		//create a database object
		BasicDBObject dbo = new BasicDBObject();
		
		//these next if statments create the collections if they do not already exist
		//if they do, we calculate the current id and +1 to it
		if(!db.collectionExists("Customers"))
		{
			//it doesnt exist so create it
			db.createCollection("Customers", dbo);
			//set id to 0
			customerId = 0;
		}
		else 
		{
			//it does exist
			DBCursor cur = findResults("Customers");
			while(cur.hasNext())
			{
				//find current id
				customerId = Integer.parseInt(cur.next().get("Customer_id").toString());
			}
			//add 1 to current id
			customerId ++;
			totalRecords += customerId;
		}
		
		if(!db.collectionExists("Orders"))
		{
			db.createCollection("Orders", dbo);
			orderId = 0;
		}
		else
		{
			DBCursor cur = findResults("Orders");
			while(cur.hasNext())
			{
				orderId = Integer.parseInt(cur.next().get("Order_id").toString());
			}
			orderId ++;
			totalRecords += orderId;
		}
		
		if(!db.collectionExists("Items"))
		{
			db.createCollection("Items", dbo);
			itemId = 0;
		}
		else
		{
			DBCursor cur = findResults("Items");
			while(cur.hasNext())
			{
				itemId = Integer.parseInt(cur.next().get("Item_id").toString());
			}
			itemId ++;
			totalRecords += itemId;
		}		
		
		//depots are hard coded for the purpose of this project	
		if(!db.collectionExists("Depots"))
		{
			db.createCollection("Depots", dbo);
		}			
		
		if(!db.collectionExists("OrderItems"))
		{
			db.createCollection("OrderItems", dbo);
			orderItemsId = 0;
		}	
		else
		{
			DBCursor cur = findResults("OrderItems");
			while(cur.hasNext())
			{
				orderItemsId = Integer.parseInt(cur.next().get("OrderItems_id").toString());
			}
			orderItemsId ++;
			totalRecords += orderItemsId;
		}
	}
	
	//the rest of this class is pretty self explanetory
	//most add new "thing" methods have a corresponding
	//make new "thing" doc method which returns a basic database object	
	
	public void addNewOrder(int Customer_id, int[] itemIds, double cost)
	{
		DBCollection coll = db.getCollection("Orders");
		coll.insert(makeOrderDoc(Customer_id, itemIds, cost));
	}
	
	public BasicDBObject makeOrderDoc(int cusid, int[] itemids, double cost)
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("Order_id", orderId);
		orderId ++;
		doc.put("Order_cost", cost);
		doc.put("Order_orderItemsId", orderItemsId);
		newOrderItems(itemids);
		return doc;
	}

	public void newOrderItems(int[] items)
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("OrderItems_id", orderItemsId);
		doc.put("OrderItems_items", items);
		insertDoc("OrderItems", doc);
		orderItemsId ++;
	}
	
	public DBCursor findResults(String collection)
	{
		DBCollection coll = db.getCollection(collection);
		DBCursor cur = coll.find();
		return cur;
	}
	
	public void removeOne(String collection, String field, String search)
	{
		DBCollection coll = db.getCollection(collection);
		coll.remove(new BasicDBObject(field, search));
	}
	
	public void removeOne(String collection, String field, int search)
	{		
		DBCollection coll = db.getCollection(collection);
		coll.remove(new BasicDBObject(field, search));
	}
	
	public DBObject findOne(String collection, String search, int search2)
	{
		DBCollection coll = db.getCollection(collection);
		DBObject doc = coll.findOne(new BasicDBObject(search, search2));
		return doc;
	}
	
	public void insertDoc(String collection, BasicDBObject obj)
	{
		DBCollection coll = db.getCollection(collection);
		coll.insert(obj);
		totalRecords ++;
	}
	
	public BasicDBObject makeCustomerDoc(String Customer_name, String Customer_address, String Customer_phone, int[] Customer_orders)
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("Customer_id", customerId);
		customerId ++;
		doc.put("Customer_name", Customer_name);
		doc.put("Customer_address", Customer_address);
		doc.put("Customer_phone", Customer_phone);
		doc.put("Customer_orders", Customer_orders);
		return doc;	
	}
	
	public BasicDBObject makeCustomerDoc(int Customer_id, String Customer_name, String Customer_address, String Customer_phone, int[] Customer_orders)
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("Customer_id", Customer_id);
		doc.put("Customer_name", Customer_name);
		doc.put("Customer_address", Customer_address);
		doc.put("Customer_phone", Customer_phone);
		doc.put("Customer_orders", Customer_orders);
		return doc;	
	}
	
	public BasicDBObject makeItemDoc(String Item_name, double Item_cost, int Item_location)
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("Item_id", itemId);
		itemId ++;
		doc.put("Item_name", Item_name);
		doc.put("Item_cost", Item_cost);
		doc.put("Item_location", Item_location);
		return doc;
	}
	
	public BasicDBObject makeItemDoc(int Item_id, String Item_name, double Item_cost, int Item_location)
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("Item_id", Item_id);
		doc.put("Item_name", Item_name);
		doc.put("Item_cost", Item_cost);
		doc.put("Item_location", Item_location);
		return doc;
	}		
}