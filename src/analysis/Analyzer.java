package analysis;
import rules.*

public class Analyzer
{
	private RuleChecker checker;
	
	private class RuleRatio
	{
		//This class will be used to count how many times a rule is satisfied vs
		//how many times its conditions are satisfied
		public int condcount;
		public int rulecount;
		
		public RuleRatio()
		{
			condcount = 0;
			rulecount = 0;
		}
	}
	
	public Analyzer()
	{
		checker = new RuleChecker();
	}
	
	private RuleRatio[] initRatios(int n)
	{
		RuleRatio[] returner = new RuleRatio[n];
		for (int i = 0; i < n; i++)
		{
			returner[i] = new RuleRatio();
		}
		return returner;
	}
	
	public float[] performAnalysis(ArrayList<Rule> rulelist, ArrayList<CollisionEntry> entrylist)
	{
		RuleRatio[] ratiolist = initRatios(rulelist.size()); //There will be a RuleRatio object corresponding to each rule passed in.
		for (int i = 0; i < entrylist.size(); i++)
		{
			checkOneEntry(entrylist.get(i), rulelist, ratiolist);
		}
		float[] returner = new float[ratiolist.size()];
		for (int i = 0; i < rulelist.size(); i++)
		{
			returner[i] = ratiolist[i].rulecount / ratiolist[i].condcount;
		}
		return returner;
	}
	
	public void checkOneEntry(CollisionEntry entry, ArrayList<Rule> rulelist, RuleRatio[] ratiolist)
	{
		for (int i = 0; i < rulelist.size(); i++)
		{
			if (checker.checkRuleConditions(entry, rulelist.get(i))
			{
				ratiolist[i].condcount++;
				if (checker.checkRuleConclusion(entry, rulelist.get(i))
				{
					ratiolist[i].rulecount++;
				}
			}
		}
	}
}
