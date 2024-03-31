package apriori;

import java.util.ArrayList;
import rules.*;
import parsing.CollisionEntry;

public class IndexedApriori
{
	public ArrayList<ArrayList<ItemSet>> itemlists; //this is an arraylist of arraylists, so it should work like,
	//the 0th entry is the list of 1 item sets, the 1st entry is the list of 2 item sets, etc.
	
	public IndexedApriori(ArrayList<CollisionEntry> entrylist, int mincov)
	{
		itemlists = new ArrayList<ArrayList<ItemSet>>();
		itemlists.add(oneItemList(entrylist, mincov));
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
				onelist.add(candidates.get(i));
			}
		}
		return onelist;
	}
	
	private boolean isPredicateInList(Predicate subject, ArrayList<Predicate> object)
	{
		//i think i'll want to make predicates sortable before i try this (ie make them comparable)
		return true; //placeholder to make everything compile.
	}
	
	private ArrayList<Integer> indexIntersection(ArrayList<Integer> subject, ArrayList<Integer> object)
	{
		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for (int i = 0; i < subject.size(); i++)
		{
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
		while (left != right && indices.get(mid) != subject)
		{
			if (subject < indices.get(mid))
			{
				right = mid - 1;
			}
			else
			{
				left = mid + 1;
			}
			mid = (left + right) / 2;
		}
		return (indices.get(mid) == subject); //if we found subject, this should return true. otherwise, false.
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
		
		for (int i = 1; i <= 12; i++) //predicates for months
		{
			pred = new Predicate(Predicate.FEATURE.C_MNTH, i);
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
		for (CollisionEntry.C_WDAY i: CollisionEntry.C_WDAY.values()) //predicates for days of week
		{
			pred = new Predicate(Predicate.FEATURE.C_WDAY, i.ordinal());
			item = new ItemSet(pred);
			candidates.add(item);
		}
		
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
