package rules;

public class SunTime
{
	public Predicate month;
	public Predicate timerange;
	
	public SunTime(Predicate newmonth, Predicate newrange)
	{
		month = newmonth;
		timerange = newrange;
	}
}
