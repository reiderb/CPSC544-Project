package apriori;

import java.util.ArrayList; //ol' reliable..
import rules.*;

public class ItemSet implements Comparable<ItemSet>
{
	public ArrayList<Predicate> items;
	public ArrayList<Integer> indices; //indices of CollisionEntry objects containing the given items
	public int support; //the number of times "items" occur in the database
	
	public ItemSet()
	{
		items = new ArrayList<Predicate>();
		indices = new ArrayList<Integer>();
		support = 0;
	}
	
	public ItemSet(Predicate pred)
	{
		items = new ArrayList<Predicate>();
		items.add(pred);
		indices = new ArrayList<Integer>();
		support = 0;
	}
	
	public void display()
	{
		String message = "";
		for (int i = 0; i < items.size(); i++)
		{
			message = message + items.get(i).display() + ", ";
		}
		message = message + " SUPPORT = " + Integer.toString(support);
		System.out.println(message);
	}
	
	@Override
	public int compareTo(ItemSet obj)
	{
		int i = 0;
		int comp;
		Predicate pred1, pred2;
		while (i < this.items.size() && i < obj.items.size())
		{
			pred1 = this.items.get(i);
			pred2 = obj.items.get(i);
			comp = pred1.compareTo(pred2);
			if (comp < 0) {return -1;}
			if (comp > 0) {return 1;}
			i++;
		}
		if (this.items.size() < obj.items.size()) {return -1;}
		if (this.items.size() > obj.items.size()) {return 1;}
		return 0;
	}
}
