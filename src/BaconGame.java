import java.util.*;
import java.util.Queue;
import net.datastructures.*;
import java.io.*;
import java.util.Map;

/**
 * Plays the Bacon Game (degrees of separation) using graphs. Problem Set 5.
 * @author Irene Feng, Orestis Lykouropoulos
 * @param <V>
 * @param <E>
 *
 */
public class BaconGame<V, E> {
	
	private AdjacencyListGraphMap<V, E> graph;
	private DirectedAdjListMap<V, E> BFSTree;
	
	//all the files
	private static final String actorFile = "../data/actors.txt"; 
	private static final String movieFile = "../data/movies.txt"; 
	private static final String movieActorsFile = "../data/movie-actors.txt";
	
	/**
	 * Constructor for the Bacon Game
	 * @param graph
	 * @param graphRoot
	 */
	public BaconGame(AdjacencyListGraphMap<V,E> graph, V graphRoot){
		this.graph = graph;
		this.BFSTree = computeTree(graphRoot);
	}
	
	/**
	 * Creates and returns a Graph of actor vertices and movie edges.
	 * @param actorFile
	 * @param movieFile
	 * @param IDFile
	 * @return
	 * @throws IOException
	 */
	public static AdjacencyListGraphMap<String, String> createBaconGraph(String actorFile, String movieFile, String IDFile) throws IOException{ 
		
		AdjacencyListGraphMap<String, String> baconGraph = new AdjacencyListGraphMap<String, String>();
		
		Map<String,String> actorMap = idToNameMap(actorFile); //map of actor ids to actor names
		Map<String, String> movieMap = idToNameMap(movieFile); //map of movie ids to movie names
		Map<String, ArrayList<String>> moviesAndActors = moviesMap(IDFile); //map of movie ids to (an arraylist of) actor ids
		
		//insert all actor vertices
		Set<String> actorIDs = actorMap.keySet();
		for(String actorID : actorIDs)
			baconGraph.insertVertex(actorMap.get(actorID));
		
		//insert movie edges for every pair of actors
		Set<String> movieIDs = moviesAndActors.keySet();
		
		for(String movieID : movieIDs){
			String movieName = movieMap.get(movieID);
			
			//nested for loop to create every pair of actors in each movie's arrayList
			for(int i=0; i<moviesAndActors.get(movieID).size()-1; i++){
				for(int j=i+1; j<moviesAndActors.get(movieID).size();j++){
					
					String actor1Name = actorMap.get(moviesAndActors.get(movieID).get(i)); 
					String actor2Name = actorMap.get(moviesAndActors.get(movieID).get(j));
					
					baconGraph.insertEdge(actor1Name, actor2Name, movieName);
				}
			}
		}
		
		return baconGraph;
	}
	
	/**
	 * Creates the actors and movie maps, from id to name.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> idToNameMap(String file) throws IOException{
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Map<String, String> mapped = new TreeMap<String,String>();
		
		String line; 
		while ((line = reader.readLine()) != null) {
			
			String[] tokens = line.split("\\|");
			String id = tokens[0]; //id of element - a unique number
			String name = tokens[1]; //name of element
			
			mapped.put(id, name);
			
		}
		reader.close();
		return mapped;
	}
	
	/**
	 * Creates the map that has key movieIDs and values arrayList of actorIDs
	 * @param iDFile
	 * @return
	 * @throws IOException
	 */
	public static Map<String, ArrayList<String>> moviesMap(String IDFile) throws IOException{
		
		BufferedReader IDs = new BufferedReader(new FileReader(IDFile));
		Map<String, ArrayList<String>> mapped = new HashMap<String,ArrayList<String>>();
		
		String line; 
		while ((line = IDs.readLine()) != null) {
			
			String[] tokens = line.split("\\|");
			String movieID = tokens[0];
			String actorID = tokens[1];
			
			//if the movieID already in map, just add the actorID to the movieID's existing arrayList
			if (mapped.containsKey(movieID)) 
				mapped.get(movieID).add(actorID);
			else { //else create a new ArrayList holding the actors and add the first actor ID value for the movieID
				ArrayList<String> actors = new ArrayList<String>();
				actors.add(actorID);
				mapped.put(movieID, actors);
			}
		}
		IDs.close();
		return mapped;
	}
	
	
	/**
	 * Creates the Tree starting at the root actor
	 * @param root
	 * @return
	 */
	public DirectedAdjListMap<V,E> computeTree(V root){
		
		DirectedAdjListMap<V,E> pathTree = new DirectedAdjListMap<V,E>();
		
		//insert the root into the tree
		pathTree.insertVertex(root);
		
		//create empty queue
		Queue<Vertex<V>> myQ = new LinkedList<Vertex<V>>();
		
		//insert the root vertex object to the queue
		Vertex<V> rootVertex = graph.getVertex(root);
		myQ.add(rootVertex);
		
		//while the queue is not empty
		while (!myQ.isEmpty()){
			
			//dequeue one element from myQ
			Vertex<V> vert = myQ.remove();
			
			//get its incident edges
			Iterable<Edge<E>> edges = graph.incidentEdges(vert);
			
			//for all the adjacent vertices (connected to the incident edges), add them and directed edges to the pathTree
			for (Edge<E> edge : edges){
				Vertex<V> oppVert = graph.opposite(vert, edge);
				
				if (!pathTree.vertexInGraph(oppVert.element())){ //to avoid re-adding
					pathTree.insertVertex(oppVert.element()); 
					//add a directed edge with same elements to tree (from (source) oppVert to (destination) vert)
					pathTree.insertDirectedEdge(oppVert.element(), vert.element(), edge.element());					
					//enqueue opposite vertex
					myQ.add(oppVert);
				}
			}
		}

		return pathTree;
		
	}
	
	/**
	 * Returns a List of Edges that connects the name of an actor to the root actor of the BFSTree
	 * @param name
	 * @return
	 */
	public List<Edge<E>> getPath(V name) {
		if (BFSTree.vertexInGraph(name)){
			
			//create a new linked list
			List<Edge<E>> result = new LinkedList<Edge<E>>();
			
			getPathHelper(name, result);

	 		return result;
	 		
		}
 		else return null;
	}
			
	/**
	 * Helper method of getPath - uses recursion to add to the List
	 * @param name
	 * @param result
	 */
	private void getPathHelper(V name, List<Edge<E>> result) {
			
			Iterator<Edge<E>> iter = BFSTree.incidentEdgesOut(name).iterator();
			
			while (iter.hasNext()){ //should have only one edge out (only done once)
				Edge<E> edge = iter.next();
				result.add(edge);
				
				//recursion by calling the opposite vertex ("parent" vertex)
				V recurName = BFSTree.opposite(name, edge).element();
			
				getPathHelper(recurName,result);
			}
	 		
		}
	
	/**
	 * Method that finds the paths from all vertices to the root and returns them in a List, ordered from least to greatest
	 * @return
	 * @throws IOException 
	 */
	private List<ListHoldingClass<E>> getAllPaths() throws IOException {
		
		//get map
		Map<String,String> nodeMap = idToNameMap(actorFile); //map of actor ids to actor names
		
		//create empty ArrayList
		ArrayList<ListHoldingClass<E>> result = new ArrayList<ListHoldingClass<E>>();
		
		//go through the map
		Set<String> keys = nodeMap.keySet();
		
		for (String key : keys){
			
			List<Edge<E>> path = getPath((V)nodeMap.get(key)); //safe for our purposes
			
			//package the path up into a holding class and add to the list
			if (path != null){

				ListHoldingClass<E> pack = new ListHoldingClass<E>(nodeMap.get(key), path);
				result.add(pack);
				
			}
		}
		
		//sort the result before returning - based on the size of each list in the list (which is the Bacon Number)
		ListComparator<E> comp = new ListComparator<E>();
		Collections.sort(result, comp);
		
		return result;
	}
	
	
	public List<ListHoldingClass<E>> getMaxDist() throws IOException{
		
		//create empty list
		List<ListHoldingClass<E>> result = new ArrayList<ListHoldingClass<E>>();
		
		//all paths
		List<ListHoldingClass<E>> list = getAllPaths();
		
		int max = list.get(list.size()-1).getList().size();
		
		for(int i = list.size()-1; list.get(i).getList().size() == max; i--){
			result.add(list.get(i));
		}
		
		return result;
	}
	
	/**
	 * Method to get average distance from root
	 * @return
	 * @throws IOException 
	 */
	public double getAverageDist() throws IOException{
		
		List<ListHoldingClass<E>> list = getAllPaths();
		
		double sum = 0.0;
		
		for(int i=0; i<list.size(); i++){
			sum+=list.get(i).getList().size();
		}
		
		return (sum/list.size());
	}
	
	/**
	 * Method to get the median distance from root
	 * @throws IOException 
	 * 
	 */
	public double getMedianDist() throws IOException{
				
		List<ListHoldingClass<E>> list = getAllPaths();
		
		//if even size
		if(list.size()%2 == 0) return (list.get(list.size()/2).getList().size() + list.get(list.size()/2 + 1).getList().size())/2;
		//if odd size
		return list.get(list.size()/2 + 1).getList().size();
	}
	
	/**
	 * Method to get the standard deviation of distance from the root
	 * @return
	 * @throws IOException
	 */
	public double getStandardDev() throws IOException{
		
		List<ListHoldingClass<E>> list = getAllPaths();

		double mean = getAverageDist();
		
		double sum = 0;
		
		for(int i=0; i<list.size(); i++){
			double sq = Math.pow(list.get(i).getList().size() - mean, 2);
			sum += sq;
		}
		
		double temp = sum/list.size();
		
		return Math.sqrt(temp);
	}
	
	
	
	public static void main(String[] args) {
		
		Scanner userInput = new Scanner(System.in);
		
		try {
			ActorSpelling corrector = new ActorSpelling(actorFile); 
			AdjacencyListGraphMap<String, String> bg = createBaconGraph(actorFile, movieFile, movieActorsFile);
			System.out.println("Pick an actor as the root of the BFS tree:");
			String root = userInput.nextLine();
			
				while(!bg.vertexInGraph(root)){
					//gives a suggestion if what the user typed in was close to an actor in the graph
					String suggestion = corrector.correct(root);

					if(suggestion!=null)
						System.out.println("Possible mispelling. Try " + suggestion + ", or type another name.");
					else{
						System.out.println("No actor with that name or a similar name was found. Try again");
					}
				 	root = userInput.nextLine();

					}
				
				BaconGame<String,String> playGame = new BaconGame<String, String>(bg, root);
		
				System.out.println("Enter an actor to show their path to " + root);
				System.out.println("Or, to quit the program and see some amazing, surprising, astonishing stunning and mind-blowing statistics about the graph with root actor " + root + ", press the return key.");
				String actor = userInput.nextLine();
				
				while(!actor.equals("")){
					List<Edge<String>> test = playGame.getPath(actor);
				
					if(test==null){
						System.err.println("Your actor doesn't seem to be connected with " + root + ". Try again.");
						//gives a suggestion if what the user typed in was close to an actor in the graph
						String suggestion = corrector.correct(root);
						if(suggestion!=null)
							System.out.println("Possible mispelling. Try " + suggestion + ", or type another name.");
					 		actor = userInput.nextLine();

					}	
					else{
						System.out.println(actor + "'s number is " + test.size());
						for(Edge<String> e : test){
							System.out.println(bg.endVertices(e)[0] + " appeared in " + e.element() + " with " + bg.endVertices(e)[1]);
						}
					//repeat
					System.out.println("Enter an actor to show their path to " + root);
					actor = userInput.nextLine();
				}
				}
				
				
				//print some statistics before quitting
				List<ListHoldingClass<String>> max = playGame.getMaxDist();
				
				System.out.println("The actors with the largest distance from "+ root + " are " + max.toString());
				System.out.println("The average distance from " + root + " is " + playGame.getAverageDist());
				System.out.println("The median distance from " + root + " is " + playGame.getMedianDist());
				System.out.println("The standard deviation from " + root + " is " + playGame.getStandardDev());
				System.out.println("");
				
				
				System.out.println("Thanks for playing!");
			}
		catch (IOException e) {
			System.err.println("Cannot find specified files");
		}
		
		userInput.close();
		
	}
	
}
