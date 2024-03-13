package rules;
import java.util.ArrayList;

public class Rule
{
	public ArrayList<Predicate> cond;
	public Predicate conc;
	public float freq;
	
	public Rule()
	{
		cond = new ArrayList<Predicate>();
		conc = null;
		freq = 0;
	}
	
	public Rule(ArrayList<Predicate> newcond, Predicate newconc, float newfreq)
	{
		cond = newcond;
		conc = newconc;
		freq = newfreq;
	}
}
