import java.util.*;
import java.io.*;
/**
 * This program analyzes relationship between users who are in some kind of social network. This program
 * takes advantage of breadth first search algorithm to solve methods like distance, path, centrality
 * and reachable. It also analyzes the property of graph formed by the given users in a graph. It also
 * computes most popular user, most follower user, density and reciprocity of graph.
 * @author Suvash Acharya
 * @version 05-05-2017
 *
 */
public class SocNet{
	DiGraph networks;
	Map<String, Integer> stringIndexMap;
	Map<Integer, String> indexStringMap;
	
	/**
	 * Constructor to draw a graph from given file
	 * @param fileName Name of the file from which graph has to be drawn
	 */
	public SocNet(String fileName){
		/**
		 * Initializing instance variables
		 */
		networks = new DiGraph();
		stringIndexMap = new HashMap<>();
		indexStringMap = new HashMap<>();
		
		try{//Catch exception

			Scanner in = new Scanner(new File(fileName));
			int count = 0;
			while (in.hasNext()){
				String vertex1 = in.next();
				String vertex2 = in.next();

				if (!stringIndexMap.containsKey(vertex1)){
					stringIndexMap.put(vertex1, count);
					indexStringMap.put(count, vertex1);
					count++;
				}

				if (!stringIndexMap.containsKey(vertex2)){
					stringIndexMap.put(vertex2, count);
					indexStringMap.put(count, vertex2);
					count++;
				}

				int intVertex1 = stringIndexMap.get(vertex1);
				int intVertex2 = stringIndexMap.get(vertex2);
				networks.addVertex(intVertex1);//adds follower user to the graph
				networks.addVertex(intVertex2);//add followed user to the graph
				networks.addEdge(intVertex1,intVertex2);//add edge between first user and second user
			}  

		}
		catch (FileNotFoundException e){//Handle Exception
			System.out.println("File Not Found!"); 
		}
	} 
	/**
	 * Computes most popular user among all the users
	 * @return Most popular user
	 */
	public String mostPopular(){
		Map<Integer, Integer> countMap = new HashMap<>();
		for(Map.Entry<Integer, String> e: indexStringMap.entrySet()){
			countMap.put(e.getKey(),0);
		}
		for (Map.Entry<Integer, String> e: indexStringMap.entrySet()){
			int vertex = e.getKey();
			for (int adjV: networks.getAdjacent(vertex)){//goes through every children and checks for follwings of each vertex
				if (countMap.containsKey(adjV)){
					int tempoV = countMap.get(adjV);
					countMap.put(adjV, tempoV+1);
				}
			}
		}
		int maximum = 0;
		String popular = "";
		for (Map.Entry<Integer, Integer> e: countMap.entrySet()){
			if (e.getValue()>maximum){//finds maximum followings
				maximum = e.getKey();
				popular = indexStringMap.get(e.getKey());
			}
		}
		return popular;  
	}
	/**
	 * Finds user who follows most number of other users
	 * @return Highest Follower USer
	 */
	public String topFollower(){
		String topFollow = "";
		int highestValue = 0;
		for(Map.Entry<Integer, String> e: indexStringMap.entrySet()){
			int tempP = e.getKey();
			if((networks.getAdjacent(tempP)).size()>highestValue){//iterates over every followings of follower and finds maximum
				topFollow = e.getValue();
				highestValue = networks.getAdjacent(tempP).size();
			}
		}
		return topFollow;
	}
	/**
	 * Computes measure of connectedness in a graph
	 * @return Ratio of edges in the graph to possible number of edges of that graph
	 */
	public double density(){
		int possibleEdge = indexStringMap.size()-1;
		double totalEdges = indexStringMap.size() * possibleEdge;
		int graphEdges = networks.edges();//Computes number of edges in the graph
		return graphEdges/totalEdges;
	}
	/**
	 * Computes the measure of symmetry of the given graph
	 * @return ratio of number of symmetric edges to number of edges in the graph
	 */
	public double reciprocity(){
		double count = 0;
		/**
		 * Computes number of symmetric edges
		 */
		for (Map.Entry<Integer, String> e: indexStringMap.entrySet()){
			int vertex = e.getKey();
			for (int adjV: networks.getAdjacent(vertex)){
				if (networks.getAdjacent(adjV).contains(vertex)){
					count+=1;
				}
			}
		}
		return count/networks.edges();
	}
	
	/**
	 * Provides a collection of leaders
	 * @return Set of user who is followed by at least 30 percent of all users and who has more 
	 * followers than followings
	 */
	public Set<String> leaders(){
		Map<Integer, Integer> countMap = new HashMap<>();
		Set<String> popularPeople = new TreeSet<>();
		for(Map.Entry<Integer, String> e: indexStringMap.entrySet()){
			countMap.put(e.getKey(),0);
		}
		for (Map.Entry<Integer, String> e: indexStringMap.entrySet()){
			int vertex = e.getKey();
			for (int adjV: networks.getAdjacent(vertex)){
				if (countMap.containsKey(adjV)){
					int tempoV = countMap.get(adjV);
					countMap.put(adjV, tempoV+1);
				}
			}
		}
		int maximum = 0;
		String popular = "";
		for (Map.Entry<Integer, Integer> e: countMap.entrySet()){
			if (e.getValue()>=0.3*indexStringMap.size()&& e.getValue()>networks.getAdjacent(e.getKey()).size()){
				popularPeople.add(indexStringMap.get(e.getKey()));
			}
		}
		return popularPeople;
	}
	
	/**
	 * Calculates distance of shortest path from given user to all other user. Uses breadth first search to accomplish this
	 * @param user The main user from which distance is to be calculated from
	 * @return Array of distances from given user to all other users
	 */
	private int[] distances(String user){
		int u1 = stringIndexMap.get(user);
		int[] dist = new int[indexStringMap.size()];
		dist[u1]=0;
		Set<Integer> visited = new HashSet<>();
		LinkedList<Integer> queue = new LinkedList<>();
		queue.add(u1);
		visited.add(u1);
		/**
		 * Execution of breadth first search
		 */
		while(!queue.isEmpty()){//Checks if queue is empty
			int node = queue.poll();
			for (int neighbour: networks.getAdjacent(node)){
				if (!visited.contains(neighbour)){
					dist[neighbour] = dist[node]+1;
					queue.add(neighbour);
					visited.add(neighbour);
				}
			}
		}
		for (int i = 0; i<dist.length; i++){
			if (i==u1){
				continue;
			}
			else{
				if (dist[i]==0){
					dist[i] = Integer.MAX_VALUE;
				}
			}
		}
		return dist;
	}
	/**
	 * Calculates shortest distance between any two given user. Uses distances method
	 * @param user1 The user from which the distance is to be found
	 * @param user2 The user up to which the distance is to be found
	 * @return Length of shortest path between any two user1 and user2 or infinite value if path does not exist
	 * between them
	 */
	public int distance(String user1, String user2){
		/**
		 * Locates the index of user2 in distance array returned by distances method and returns the value of it
		 */
		return distances(user1)[stringIndexMap.get(user2)];
	}
	
	/**
	 * Computes vertices that involves in shortest path between user1 and user2
	 * @param user1 The user from which the path has to be started
	 * @param user2 The user to which the path has to be ended
	 * @return String representation of shortest path between user1 and user2 or [NONE] if path is not found
	 */
	public String path(String user1, String user2){
		List<String> pathList = new ArrayList<>();
		StringBuilder resultString = new StringBuilder("");
		int u1 = stringIndexMap.get(user1);
		int u2 = stringIndexMap.get(user2);
		Map<Integer, Integer> parents = new HashMap<>();
		int node = u1;
		Set<Integer> visited = new HashSet<>();
		LinkedList<Integer> queue = new LinkedList<>();
		queue.add(u1);
		visited.add(u1);
		while(!queue.isEmpty()){
			node = queue.poll();
			if (node==u2){
				break;
			}
			else{
				for (int neighbour: networks.getAdjacent(node)){
					if (!visited.contains(neighbour)){
						parents.put(neighbour,node);
						queue.add(neighbour);
						visited.add(neighbour);
					}
				}
			}
		}
		if (node!=u2){//Checks if path is found to the destination user
			return "[NONE]";
		}
		else{
			for (int i = u2; i!=u1; i = parents.get(i)){
				pathList.add(indexStringMap.get(i));
			}
			for (int j = pathList.size()-1; j>=0;j--){
				resultString.append("|"+pathList.get(j));
			}
			return "["+user1 + resultString.toString()+ "]";
		}

	}
	
	/**
	 * Computes mean length of shortest path from one user to all other users.Uses distances method to accomplish
	 * this task
	 * @param user The user from which the mean length is being calculated from
	 * @return Average length of shortest path from one user to all other users
	 */
	public double centrality(String user){
		double totalDistance = 0;
		int[] myArray = distances(user);
		for (int i = 0; i<myArray.length; i++){
			totalDistance+=myArray[i];
		}
		return totalDistance/((myArray.length)-1);
	}
	
	/**
	 * Finds all the users who are reachable from a given user
	 * @param user The user from which the reachability has to be calculated from
	 * @return Set of all users reachable from user
	 */
	public Set<String> reachable(String user){
		int a = stringIndexMap.get(user);
		Queue<Integer> que = new LinkedList<Integer>();
		Set<String> secQ = new HashSet<String>();
		Set<Integer> visited = new HashSet<Integer>();
		que.add(a);
		visited.add(a);
		secQ.add(indexStringMap.get(a));
		while(que.size()>0){
			a = que.poll();
			Iterator iter = networks.getAdjacent(a).iterator();
			while(iter.hasNext()){
				int b = (int)iter.next();
				if(!visited.contains(b)){
					visited.add(b);
					secQ.add(indexStringMap.get(b));//adds reachable user to the set

					que.add(b);
				}
			}
		}
		secQ.remove(user);
		return secQ;
	}
}
