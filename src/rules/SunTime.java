package rules;

public class SunTime
{
	public Predicate month;
	public Predicate dayrange;
	public int hour;
	
	public SunTime(Predicate newmonth, Predicate newdays, int newhour)
	{
		month = newmonth;
		dayrange = newdays;
		hour = newhour;
	}
}
