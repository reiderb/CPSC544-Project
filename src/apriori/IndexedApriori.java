package apriori;

import java.util.*;
import rules.*;
import parsing.CollisionEntry;

public class IndexedApriori
{
	public ArrayList<ArrayList<ItemSet>> itemlists; //this is an arraylist of arraylists, so it should work like,
	//the 0th entry is the list of 1 item sets, the 1st entry is the list of 2 item sets, etc.
	
	public IndexedApriori()
	{
		itemlists = new ArrayList<ArrayList<ItemSet>>();
	}
	
	public IndexedApriori(ArrayList<CollisionEntry> entrylist, int mincov)
	{
		itemlists = new ArrayList<ArrayList<ItemSet>>();
		itemlists.add(oneItemList(entrylist, mincov));
		if (itemlists.get(0).size() > 0)
		{generateItemSets(mincov);}
	}
	
	public ArrayList<ItemSet> oneItemList(ArrayList<CollisionEntry> entrylist, int mincov)
	{
		//mincov is the minimum number of entries a predicate has to be in
		RuleChecker checker = new RuleChecker();
		ArrayList<ItemSet> candidates = oneItemCandidates();
		for (int i = 0; i < entrylist.size(); i++)
		{
			for (int j = 0; j < candidates.size(); j++)
			{
				if (checker.checkPredicateList(entrylist.get(i), candidates.get(j).items))
				{
					candidates.get(j).indices.add(i); //i believe these arraylists will always be ordered, which allows for binary searches!!
				}
			}
		}
		
		ArrayList<ItemSet> onelist = new ArrayList<ItemSet>();
		for (int i = 0; i < candidates.size(); i++)
		{
			if (candidates.get(i).indices.size() >= mincov)
			{
				candidates.get(i).support = candidates.get(i).indices.size();
				onelist.add(candidates.get(i));
			}
		}
		System.out.println("One item lists generated");
		Collections.sort(onelist);
		return onelist;
	}
	
	public void generateItemSets(int mincov)
	{
		//we assume the one item set has already been generated at this point
		int k = 1;
		ArrayList<ItemSet> newlist = kItemList(k, mincov);
		while (newlist.size() > 0)
		{
			System.out.println(Integer.toString(k + 1) + " Item lists generated");
			itemlists.add(newlist);
			k = k + 1;
			newlist = kItemList(k, mincov);
			clearIndices(k - 1);
		}
	}
	
	public void clearIndices(int i)
	{
		for (int j = 0; j < itemlists.get(i).size(); j++)
		{
			itemlists.get(i).get(j).indices.clear();
		}
	}
	
	public ArrayList<ItemSet> kItemList(int k, int mincov)
	{
		//here, k is the index of the item set we want to generate in "itemlist".
		//we assume that all sets less than k have already been generated.
		//so, for example, if we want to generate the 2 item list, then we'd set k to 1 and assume the 1 item list is at index 0
		//System.out.println("Entering kItemList()");
		ArrayList<ItemSet> oneitem = itemlists.get(0);
		ArrayList<ItemSet> prevlist = itemlists.get(k - 1);
		ArrayList<ItemSet> newlist = new ArrayList<ItemSet>();
		ItemSet item;
		ArrayList<Integer> indices;
		ArrayList<Predicate> predlist;
		for (int i = 0; i < prevlist.size(); i++) //we want to compare the previous item sets to the one item sets
		{
			//System.out.println("Entering outer loop");
			for (int j = 0; j < oneitem.size(); j++)
			{
				//System.out.println("Entering inner loop");
				predlist = cloneItemList(prevlist.get(i).items);
				//System.out.println("cloned predicate list");
				//predlist = prevlist.get(i).items; //referencing the list rather than cloning it makes it so when you change the reference you change original. whoops!
				if (!isPredicateInList(oneitem.get(j).items.get(0), predlist) && noSimilarFeature(oneitem.get(j).items.get(0), predlist) && notBlacklisted(oneitem.get(j), prevlist.get(i))) //if the predicate in a one item list ISN'T in the itemset
				{
					predlist.add(oneitem.get(j).items.get(0));
					Collections.sort(predlist);
					//System.out.println("Sorted predicate list");
					item = new ItemSet();
					item.items = predlist;
					if (!doesItemSetExist(item, newlist)) //check that we haven't already made an item set with these predicates
					{
						//System.out.println("new item set found, checking for intersection of indices");
						indices = reconstructIndices(item, mincov);
						//System.out.println("Found intersection of indices");
						if (indices.size() >= mincov)
						{
							item.support = indices.size();
							item.blacklist = joinBlacklists(oneitem.get(j), prevlist.get(i));
							item.blacklist = (prevlist.get(i).blacklist);
							newlist.add(item);
							Collections.sort(newlist); //perhaps it would be better to do a search and insert, but let's see how it runs first
							//System.out.println("sorted newlist");
							
						}
						else
						{
							prevlist.get(i).blacklist.add(oneitem.get(j));
							if (prevlist.get(i).items.size() == 1)
							{
								oneitem.get(j).blacklist.add(prevlist.get(i));
							}
						}
						indices.clear();
					}
				}
			}
		}
		return newlist;
	}
	
	private ArrayList<ItemSet> joinBlacklists(ItemSet sub, ItemSet obj)
	{
		Collections.sort(sub.blacklist);
		Collections.sort(obj.blacklist);
		ArrayList<ItemSet> newblacklist = new ArrayList<ItemSet>();
		int i = 0;
		int j = 0;
		int comp;
		while (i < sub.blacklist.size() && j < obj.blacklist.size())
		{
			comp = sub.blacklist.get(i).compareTo(obj.blacklist.get(j));
			if (comp == 0) //if the same itemset is in both blacklists, we only want to add it once
			{
				newblacklist.add(sub.blacklist.get(i));
				i++;
				j++;
			}
			else if (comp < 0)
			{
				newblacklist.add(sub.blacklist.get(i));
				i++;
			}
			else
			{
				newblacklist.add(obj.blacklist.get(j));
				j++;
			}
		}
		while (i < sub.blacklist.size())
		{
			newblacklist.add(sub.blacklist.get(i));
			i++;
		}
		while (j < obj.blacklist.size())
		{
			newblacklist.add(obj.blacklist.get(j));
			j++;
		}
		return newblacklist;
	}
	
	private boolean notBlacklisted(ItemSet sub, ItemSet obj)
	{
		int comp;
		for (int i = 0; i < obj.blacklist.size(); i++)
		{
			comp = sub.compareTo(obj.blacklist.get(i));
			if (comp == 0) {return false;}
		}
		return true;
	}
	
	private boolean noSimilarFeature(Predicate sub, ArrayList<Predicate> obj)
	{
		for (int i = 0; i < obj.size(); i++)
		{
			if (sub.feature == obj.get(i).feature)
			{
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<Integer> reconstructIndices(ItemSet item, int mincov)
	{
		ArrayList<Integer> indices = new ArrayList<Integer>();
		if (item.items.size() == 0) {return indices;}
		ItemSet nextone = findOneItem(item.items.get(0));
		if (nextone == null) return indices;
		indices = new ArrayList<Integer>(nextone.indices); //this should copy the list without referring to it.
		for (int i = 1; i < item.items.size(); i++)
		{
			nextone = findOneItem(item.items.get(i));
			//indices = indexIntersection(indices, nextone.indices);
			indices = linearIntersection(indices, nextone.indices); //the system was failing to make any k-item lists with this, and i genuinely have no idea why ;_;
			if (indices.size() < mincov) {return indices;}
		}
		return indices;
	}
	
	public ArrayList<Integer> linearIntersection(ArrayList<Integer> sub, ArrayList<Integer> obj)
	{
		//okay, so, indexIntersection() was way too slow, so we're going to try this instead
		//we assume the arraylists passed in are sorted
		int i = 0;
		int j = 0;
		int comp;
		ArrayList<Integer> indices = new ArrayList<Integer>();
		while (i < sub.size() && j < obj.size())
		{
			comp = sub.get(i).compareTo(obj.get(j));
			if (comp == 0)
			{
				indices.add(sub.get(i));
				i++;
				j++;
			}
			else if (comp < 0)
			{
				i++;
			}
			else //if sub.get(i) > obj.get(j)
			{
				j++;
			}
		}
		return indices;
	}
	
	private ItemSet findOneItem(Predicate subject)
	{
		//find the one item set corresponding to the predicate "subect"
		ArrayList<ItemSet> oneItems = itemlists.get(0); //we assume this has been initialized
		ItemSet temp = new ItemSet(subject);
		int left = 0;
		int right = oneItems.size() - 1;
		int mid = (left + right) / 2;
		int comp;
		while (left <= right)
		{
			comp = temp.compareTo(oneItems.get(mid));
			if (comp == 0) {return oneItems.get(mid);}
			if (comp < 0) {right = mid - 1;}
			if (comp > 0) {left = mid + 1;}
			mid = (left + right) / 2;
		}
		return null;
	}
	
	public ArrayList<Predicate> cloneItemList(ArrayList<Predicate> sub)
	{
		ArrayList<Predicate> newlist = new ArrayList<Predicate>();
		for (int i = 0; i < sub.size(); i++)
		{
			newlist.add(sub.get(i));
		}
		return newlist;
	}
	
	public boolean doesItemSetExist(ItemSet sub, ArrayList<ItemSet> obj)
	{
		if (obj.size() == 0) {return false;}
		int left = 0;
		int right = obj.size() - 1;
		int mid = right / 2;
		int comp;
		while (left <= right)
		{
			comp = sub.compareTo(obj.get(mid));
			if (comp == 0) {return true;}
			if (comp < 0) {right = mid - 1;}
			if (comp > 0) {left = mid + 1;}
			mid = (left + right) / 2;
		}
		return false;
	}
	
	private boolean isPredicateInList(Predicate subject, ArrayList<Predicate> object)
	{
		//we'll assume that the "object" list is sorted before the function is called
		int left = 0;
		int right = object.size() - 1;
		int mid = right / 2;
		int comp;
		while (left <= right)
		{
			comp = subject.compareTo(object.get(mid));
			if (comp == 0) {return true;}
			if (comp < 0) {right = mid - 1;}
			if (comp > 0) {left = mid + 1;}
			mid = (left + right) / 2;
		}
		return false;
	}
	
	public ArrayList<Integer> indexIntersection(ArrayList<Integer> subject, ArrayList<Integer> object)
	{
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for (int i = 0; i < subject.size(); i++)
		{
			//System.out.println("about to enter checkCommonIndex()");
			if (checkCommonIndex(subject.get(i), object))
			{
				intersection.add(subject.get(i));
			}
		}
		return intersection;
	}
	
	private boolean checkCommonIndex(int subject, ArrayList<Integer> indices)
	{
		int left = 0;
		int right = indices.size() - 1;
		int mid = (left + right) / 2;
		while (left <= right && indices.get(mid) != subject)
		{
			if (subject < indices.get(mid))
			{
				right = mid - 1;
			}
			if (subject > indices.get(mid))
			{
				left = mid + 1;
			}
			mid = (left + right) / 2;
		}
		//System.out.println("exited while loop in checkCommonIndex");
		return (left <= right); //if we found subject, this should return true. otherwise, false.
	}
	
	public ArrayList<ItemSet> oneItemCandidates()
	{ //This will make a list of all possible candidates for one item sets, regardless of if they achieve minimum coverage or not
		ArrayList<ItemSet> candidates = new ArrayList<ItemSet>();
		Predicate pred;
		ItemSet item;
		
		int min;
		
		for (int i = 1999; i <= 2019; i++) //predicates for rules
		{
			pred = new Predicate(Predicate.FEATURE.C_YEAR, i);
			item = new ItemSet(pred);
			candidates.add(item);
		}
		//System.out.println("made year candidates");
		for (int i = 1; i <= 12; i++) //predicates for months
		{
			pred = new Predicate(Predicate.FEATURE.C_MNTH, i);
			item = new ItemSet(pred);
			candidates.add(item);
		}
		//System.out.println("made month candidates");
		for (CollisionEntry.C_WDAY i: CollisionEntry.C_WDAY.values()) //predicates for days of week
		{
			pred = new Predicate(Predicate.FEATURE.C_WDAY, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		//System.out.println("made weekday candidates");
		
		int hourrange = 4; //change this to modify range of hours (eg, if it's set to 4, it'll split the day into 4 hour intervals)
		min = 0;
		while (min < 24) //predicates for hour of day
		{
			pred = new Predicate(Predicate.FEATURE.C_HOUR, min, (min + hourrange));
			item = new ItemSet(pred);
			candidates.add(item);
			min = min + hourrange;
		}
		
		for (CollisionEntry.C_SEV i: CollisionEntry.C_SEV.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_SEV, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//next up, C_VEHS, the number of vehicles in a collision
		//this seems to me like a case where for lower numbers we'd want specific values
		//and for higher numbers we'd want a range. but this can be changed, of course.
		
		int vehiclesrange = 10;
		for (int i = -4; i < 5; i++) //initializing i to -4 will include error values
		{
			pred = new Predicate(Predicate.FEATURE.C_VEHS, i);
			item = new ItemSet(pred);
			candidates.add(item);
		}
		pred = new Predicate(Predicate.FEATURE.C_VEHS, 5, 10);
		item = new ItemSet(pred);
		candidates.add(item);
		min = 10;
		while (min < 100)
		{
			pred = new Predicate(Predicate.FEATURE.C_VEHS, min, (min + vehiclesrange));
			item = new ItemSet(pred);
			candidates.add(item);
			min += vehiclesrange;
		}
		
		//C_CONF next (gives information about accident)
		for (CollisionEntry.C_CONF i: CollisionEntry.C_CONF.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_CONF, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//C_RCFG (where the accident happened)
		for (CollisionEntry.C_RCFG i: CollisionEntry.C_RCFG.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_RCFG, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//C_WTHR (weather conditions)
		for (CollisionEntry.C_WTHR i: CollisionEntry.C_WTHR.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_WTHR, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//C_RSUR (road surface conditions)
		for (CollisionEntry.C_RSUR i: CollisionEntry.C_RSUR.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_RSUR, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//C_RALN (whether the road is straight or curved, etc)
		for (CollisionEntry.C_RALN i: CollisionEntry.C_RALN.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_RALN, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//C_TRAF (traffic signals)
		for (CollisionEntry.C_TRAF i: CollisionEntry.C_TRAF.values())
		{
			pred = new Predicate(Predicate.FEATURE.C_TRAF, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//V_ID (vehicle ID)
		//i have a feeling this will be completely meaningless except for 99 which indicates a pedestrian
		pred = new Predicate (Predicate.FEATURE.V_ID, 99);
		item = new ItemSet(pred);
		candidates.add(item);
		//uncomment this if you're a FREAK!!!
		/*
		for (int i = 1; i <= 98; i++)
		{
			pred = new Predicate(Predicate.FEATURE.V_ID, i);
			item = new ItemSet(pred);
			candidates.add(item);
		} 
		*/
		
		//V_TYPE (trucks, buses, cars, etc)
		for (CollisionEntry.V_TYPE i: CollisionEntry.V_TYPE.values())
		{
			pred = new Predicate(Predicate.FEATURE.V_TYPE, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//V_YEAR (model year of vehicle)
		int vyearrange = 20;
		min = 1900; //who knows, you do see people driving model a's every now and then..
		while(min < 2020)
		{
			pred = new Predicate(Predicate.FEATURE.V_YEAR, min, (min + vyearrange));
			item = new ItemSet(pred);
			candidates.add(item);
			min = min + vyearrange;
		}
		
		//P_ID (person ID)
		//this is another one that i have a feeling we can safely ignore, but uncomment if you want
		/*
		for (int i = 1; i <= 99; i++)
		{
			pred = new Predicate(Predicate.FEATURE.P_ID, i);
			item = new ItemSet(pred);
			candidates.add(item);
		}
		*/
		
		//P_SEX (the gender of a given person)
		for (CollisionEntry.P_SEX i: CollisionEntry.P_SEX.values())
		{
			pred = new Predicate(Predicate.FEATURE.P_SEX, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//P_AGE (age of a person)
		int agerange = 10;
		min = 0;
		while (min < 100)
		{
			pred = new Predicate(Predicate.FEATURE.P_AGE, min, (min + agerange));
			item = new ItemSet(pred);
			candidates.add(item);
			min = min + agerange;
		}
		
		//if the age and gender of a person are relevant to an accident, it seems that 
		//it would mainly only be when that person is driving, so perhaps we could filter
		//the above feature values based on whether they're driving or not
		
		//P_PSN (person position. could use this to see if somebody's driving or not)
		//question: for imported right hand drives, does 11 still represent the driver? probably not important..
		for (CollisionEntry.P_PSN i: CollisionEntry.P_PSN.values())
		{
			pred = new Predicate(Predicate.FEATURE.P_PSN, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//P_ISEV (injury severity)
		for (CollisionEntry.P_ISEV i: CollisionEntry.P_ISEV.values())
		{
			pred = new Predicate(Predicate.FEATURE.P_ISEV, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//P_SAFE (safety devices used)
		for (CollisionEntry.P_SAFE i: CollisionEntry.P_SAFE.values())
		{
			pred = new Predicate(Predicate.FEATURE.P_SAFE, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//P_USER (whether the person's a driver, passenger, cyclist, whatever)
		for (CollisionEntry.P_USER i: CollisionEntry.P_USER.values())
		{
			pred = new Predicate(Predicate.FEATURE.P_USER, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		for (CollisionEntry.L_COND i: CollisionEntry.L_COND.values())
		{
			pred = new Predicate(Predicate.FEATURE.L_COND, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		//finally done..
		return candidates;
	}
}
