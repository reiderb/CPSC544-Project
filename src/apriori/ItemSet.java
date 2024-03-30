package apriori;

import java.util.ArrayList; //ol' reliable..
import rules.*;

public class ItemSet
{
	public ArrayList<Predicate> items;
	public ArrayList<Integer> indices; //indices of CollisionEntry objects containing the given items
	
	public ItemSet()
	{
		items = new ArrayList<Predicate>();
		indices = new ArrayList<Integer>();
	}
	
	public ItemSet(Predicate pred)
	{
		items = new ArrayList<Predicate>();
		items.add(pred);
		indices = new ArrayList<Integer>();
	}
}
