import java.util.Comparator;
import java.util.List;
import net.datastructures.Edge;

/**
 * Comparator implementation for Paths in Bacon Game (Extra Credit)
 * @author Irene Feng, Orestis Lykouropoulos
 *
 */
public class ListComparator<E> implements Comparator<ListHoldingClass<E>> {
	
	public int compare(ListHoldingClass<E> LHC1, ListHoldingClass<E> LHC2) {
		//Returns 1 if list1.size() > list2.size(), 0 if equal and -1 if list1.size() < list2.size()
		return (int)Math.signum(LHC1.getList().size() - LHC2.getList().size());
	}
	
}
