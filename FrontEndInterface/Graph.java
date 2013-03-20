/*
 * Graph - Used to calculate the shortest route
 */

public class Graph
{
	//global variables
	public Node[][] nodes = new Node[3][3];
	public Node[] unvisitedNodes = new Node[nodes.length*nodes[0].length];
	public String[][][] commands = new String[19][19][19];

	public Graph()
	{
		//as stated in the anaylsis these are hard coded
		//would have taken too much time to write a GUI interface to
		//enable users to create their own warehouse mappings

		//this array states how many neigbours a node has
		//e.g. node 0,0 (x:0, y:0) has 2 neighbours (2 edges connecting to it)
		int[][] links = {{2,3,2}, {2,4,2}, {1,3,1}};

		//this array states which of the 9 cells in a 3x3 grid are nodes
		//in this case its all of them
		int[][] map = {{1,1,1},{1,1,1},{1,1,1}};

		//this is the id for each node
		//e.g. node at x:0,y:0 (0,0) has an id of 6
		int[][] ids = {{6,5,4},{7,2,3},{0,1,9}};

		//this method fills the commands array
		fillCommands();

		//this fills the gloabl nodes array with Nodes
		addNodes(map, ids, links);

		//this gives each node its Node neighbours
		addLinks();
	}

	public void fillCommands()
	{
		//yes this is utterly horrible, however i could't think of a better
		//way of doing it and as stated in the analysis the warehouse floor
		//will be hardcoded

		//this array is a lookup table for converting id routes into robot command
		//e.g. the output of the dijkstras algorithm will be a set of ids like
		//1,2,3,4,5; this means the robot has to go to 1 then 2 then 3 etc.
		//however this does not tell the robot which direction it must turn (L, R)
		//so the route 1,2,3,4,5 is boroken into section 3 long 1,2,3  2,3,4  3,4,5
		//and this array accessed with each 3 long section - commands[1][2][3]
		//which would return "R".
		commands[0][1][2] = "L";
		commands[9][1][2] = "R";
		commands[0][1][9] = "F";
		commands[1][2][5] = "F";
		commands[1][2][3] = "R";
		commands[1][2][7] = "L";
		commands[2][1][0] = "R";
		commands[2][1][9] = "L";
		commands[3][2][1] = "L";
		commands[7][2][1] = "R";
		commands[5][2][1] = "F";
		commands[2][3][4] = "L";
		commands[4][3][2] = "R";
		commands[2][7][6] = "R";
		commands[6][7][2] = "L";
		commands[2][5][4] = "R";
		commands[4][5][2] = "L";
		commands[2][5][6] = "L";
		commands[6][5][2] = "R";
		commands[0][1][0] = "U";
		commands[1][0][1] = "U";
		commands[1][9][1] = "U";
		commands[9][1][9] = "U";
		commands[1][2][1] = "U";
		commands[1][2][1] = "U";
		commands[2][1][2] = "U";
		commands[2][3][2] = "U";
		commands[3][2][3] = "U";
		commands[2][7][2] = "U";
		commands[7][2][7] = "U";
		commands[2][5][2] = "U";
		commands[5][2][5] = "U";
		commands[3][4][3] = "U";
		commands[4][3][4] = "U";
		commands[4][5][4] = "U";
		commands[5][4][5] = "U";
		commands[5][6][5] = "U";
		commands[6][5][6] = "U";
		commands[6][7][6] = "U";
		commands[7][6][7] = "U";
		commands[6][5][4] = "F";
	}

	//this method returns an array of robot commands
	//e.g. {"R","L","F"} from an array of route ids
	public String[] getCommands(int[] _ids)
	{
		//generate a string array based on the ids length
		String[] _commands = new String[_ids.length];
		for(int i = 0; i < _ids.length-2; i ++)
		{
			//fill the new array with the output from the commands lookup table
			_commands[i] = commands[_ids[i]][_ids[i+1]][_ids[i+2]];
		}

		//return the array
		return _commands;
	}

	int[] ids = new int[40];
	int counter = 0;

	public int[] calculateRoute(Node _node)
	{
		//check to see if it's the first time
		if(ids[ids.length-1] == 0)
		{
			//it is, fill the array with -1s
			java.util.Arrays.fill(ids,-1);
		}

		//add the passed nodes id into the route array
		ids[counter] = _node.getId();
		//increment counter
		counter ++;

		//this loop finds the next node in the route
		for(int i = 0; i < _node.getLinks().length; i ++)
		{
			if(_node.getTenativeDist() == (_node.getLinks()[i].getTenativeDist() + _node.getDistance()[i]))
			{
				//call the method again with the next node
				return calculateRoute(_node.getLinks()[i]);
			}
		}

		//no nodes are left in the route
		//return the id array reversed
		return reverseArray();
	}

	//the array calculated above is found backwards
	//this method reverses and shotens the array
	public int[] reverseArray()
	{
		//this calulates the actual size of the array
		int size = -1;
		for(int i = 0; i < ids.length; i ++)
		{
			if(ids[i] == -1)
			{
				size = i;
				break;
			}
		}

		//create the array to reutrn with the correct size
		int[] newIds = new int[size];

		int count = 0;
		for(int i = size-1; i >= 0; i --)
		{
			//assign the new array
			newIds[count] = ids[i];
			count ++;
		}

		//return the array
		return newIds;
	}

	//this method fills the global nodes[][] array with Nodes
	public void addNodes(int[][] map, int[][] ids, int[][] links)
	{
		for(int i = 0; i < map.length; i ++)
		{
			for(int j = 0; j < map[0].length; j ++)
			{
				if(map[i][j] == 1)
				{
					//assign nodes[i][j] with a new node
					nodes[i][j] = new Node(i,j,ids[i][j],links[i][j]);
				}
			}
		}
	}

	//this method adds the links to each of nodes
	public void addLinks()
	{
		//these are hard coded as stated in the analysis
		//the nodeLinks array contains the numbers of neighbours for each node,
		//e.g. the number of nodes a node has connected to it
		Node[][][] nodeLinks = {{{nodes[0][1],nodes[1][0]},{nodes[0][0],nodes[0][2],nodes[1][1]},{nodes[0][1],nodes[1][2]}},{{nodes[0][0],nodes[1][1]},{nodes[1][0],nodes[0][1],nodes[1][2],nodes[2][1]},{nodes[1][1],nodes[0][2]}},{{nodes[2][1]},{nodes[2][0],nodes[1][1],nodes[2][2]},{nodes[2][1]}}};

		//the distances array contains the distances for each link
		int[][][] distances = {{{5,10},{5,5,10},{5,10}},{{10,5},{5,10,5,10},{5,10}},{{5},{5,10,5},{5}}};

		//assign the links and distances to each node
		for(int i = 0; i < nodeLinks.length; i ++)
		{
			for(int j = 0; j < nodeLinks[i].length; j ++)
			{
				for(int k = 0; k < nodeLinks[i][j].length; k ++)
				{
					nodes[i][j].addLink(nodeLinks[i][j][k]);
					nodes[i][j].addDistance(distances[i][j][k]);
				}
			}
		}
	}

	//this method sets up the graph and creates the unvisited set
	public int[] dijkstra(int x1, int y1, int x2, int y2)
	{
		//assign the start node based no passed parameters
		Node startNode = nodes[x1][y1];

		//set the start nodes tentative distance
		startNode.setTenativeDist(0);
		//set the start node to visited
		startNode.visited();

		//create the unvisited set, minus the start node
		createUnvisitedSet(startNode);

		//assign the end node based on passed in parameters
		Node endNode = nodes[x2][y2];

		//this method call actually performes the algorithm
		//result is assigned to the end node
		Node result = traverseGraph(unvisitedNodes, endNode.getId());

		//this call returns an int[] by backscanning through the graph
		return calculateRoute(result);
	}

	//this is the actual algorithm, it calculates the tentative distances
	//it is passed a Nodes[] array which is an array containing the neighbours
	//of the pervious node
	public Node traverseGraph(Node[] _nodes, int _id)
	{
		//this loops traverses the passed nodes array
		for(int j = 0; j < _nodes[0].getLinks().length; j ++)
		{
			//if it has allready been visited skip it
			if(_nodes[0].getLinks()[j].hasBeenVisited() == 1)
			{
				continue;
			}
			//create a temp tentative distance based on the current tentative distance
			//plus the link length connecting the nodes
			int tenDist = _nodes[0].getTenativeDist() + _nodes[0].getDistance()[j];

			//if this temp distance is less than the current distance replace it
			if(tenDist < _nodes[0].getLinks()[j].getTenativeDist())
			{
				_nodes[0].getLinks()[j].setTenativeDist(tenDist);
			}
		}

		//set the node we have just calulated to visited
		_nodes[0].visited();

		//if all of the nodes are visited
		if(allVisited(_nodes) == true)
		{
			//we have finished
			//return the end node
			return endNode(_id);
		}

		//recursivly call this function with a new array of nodes
		//keeping the same end _id
		return traverseGraph(sort(_nodes), _id);
	}

	//this method returns a node based on an id number
	public Node endNode(int _id)
	{
		for(int i = 0; i < nodes.length; i ++)
		{
			for(int j = 0; j < nodes[i].length; j ++)
			{
				//check to see if this node has id of _id
				if(nodes[i][j].getId() == _id)
				{
					//it has the same id, return the node
					return nodes[i][j];
				}
			}
		}

		//this method should never get here
		return null;
	}

	//this method returns either true/false if all nodes have been visited or not
	public boolean allVisited(Node[] _nodes)
	{
		boolean visited = true;
		for(int i = 0; i < _nodes.length; i ++)
		{
			if(_nodes[i] != null && _nodes[i].hasBeenVisited() == 0)
			{
				//one hasnt been visited
				visited = false;
			}
		}

		//return the result
		return visited;
	}

	//returns a sorted passed array
	public Node[] sort(Node[] _nodes)
	{
		//this section finds the node with least tentative distance
		int min = 0;
		Node least = null;
		for(int i = 1; i < _nodes.length; i ++)
		{
			if(_nodes[i] != null)
			{
				//assign min with the first elements distance
				min = _nodes[i].getTenativeDist();
				least = _nodes[i];
				break;
			}
		}

		//iterate through the array searching for the smallest dist
		for(int i = 1; i < _nodes.length; i ++)
		{
			if(_nodes[i] != null && _nodes[i].getTenativeDist() < min)
			{
				min = _nodes[i].getTenativeDist();
				least = _nodes[i];
			}
		}

		//return a new array based on this least value
		return newNodeArray(_nodes, least);
	}

	//creates a new array based on the least found dist above
	public Node[] newNodeArray(Node[] _nodes, Node least)
	{
		//create the new array
		Node[] newArray = new Node[_nodes.length];

		//assign the least node to the 0th element
		newArray[0] = least;

		//assign the new array with the old arrays elements
		for(int i = 1; i < newArray.length; i ++)
		{
			if(_nodes[i] == least)
			{
				continue;
			}
			newArray[i] = _nodes[i];
		}

		//return the new array
		return newArray;
	}

	//create the set of unvisited nodes
	public void createUnvisitedSet(Node start)
	{
		//assign the passed start node
		unvisitedNodes[0] = start;

		//iterate through the nodes array
		int counter = 1;
		for(int i = 0; i < nodes.length; i ++)
		{
			for(int j = 0; j < nodes[i].length; j ++)
			{
				if(nodes[i][j] == start)
				{
					continue;
				}
				//assign the unvisitednodes array
				unvisitedNodes[counter] = nodes[i][j];
				counter ++;
			}
		}
	}
}