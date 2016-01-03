import java.util.List;
import net.datastructures.Edge;

/**
 * Holding class for Paths for statistics of the Bacon Game (Extra credit)
 * @author Irene Feng and Orestis Lykouropoulos
 *
 */

public class ListHoldingClass<E> {

	private String name;
	private int baconNum;
	private List<Edge<E>> list;
	

	public ListHoldingClass(String name, List<Edge<E>> list){
		this.name = name;
		this.baconNum =  list.size();
		this.list = list;
		
		
	}
	
	//Override toString 
	public String toString(){
		if (list.size()>0 )
			return name + ": " + baconNum;
		else 
			return null;
	}
	
	//getter for list
	public List<Edge<E>> getList() {
		return list;
	}


}
