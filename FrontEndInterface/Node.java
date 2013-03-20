/*
 * Node - A node used in the graph of the warehouse
 */

public class Node
{
	//This class is a node
	//A graph consists of multiple nodes connected via edges
	//ech node has a certain number of properties
	
	//a node knows where is it and what its id is
	private int x, y, id;
	//this is its distance from the robot
	private int tentativeDist = Integer.MAX_VALUE;	
	private int visited = 0;
	//a node knows its neighbours and how far they are away
	private int[] distances = null;
	//this array contains each of the nodes neighbours as Nodes
	private Node[] links = null;
	private int counter = 0;
	
	public Node(int _x, int _y, int _id, int _links)
	{
		//constructor assigns node properties
		x = _x;
		y = _y;
		id = _id;
		links = new Node[_links];
		distances = new int[_links];
	}
	
	//these methods are pretty self explaniitory	
	public void setTenativeDist(int dist)
	{
		tentativeDist = dist;
	}
	
	public int getTenativeDist()
	{
		return tentativeDist;
	}
	
	public int hasBeenVisited()
	{
		return visited;
	}
	
	public void visited()
	{
		visited = 1;
	}
	
	public void reset()
	{
		visited = 0;
	}
	
	public void addLink(Node link)
	{
		links[counter] = link;		
	}
	
	public void addDistance(int dis)
	{
		distances[counter] = dis;
		counter ++;
	}
	
	public int[] getDistance()
	{
		return distances;
	}
	
	public Node[] getLinks()
	{
		return links;
	}
	
	public int getId()
	{
		return id;
	}
}