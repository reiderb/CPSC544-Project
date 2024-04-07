package rules;
import java.util.ArrayList;

public class Rule
{
	public ArrayList<Predicate> cond;
	public ArrayList<Predicate> conc;
	public float freq;
	
	public Rule()
	{
		cond = new ArrayList<Predicate>();
		conc = new ArrayList<Predicate>();
		freq = 0;
	}
	
	public Rule(ArrayList<Predicate> newcond, ArrayList<Predicate> newconc, float newfreq)
	{
		cond = newcond;
		conc = newconc;
		freq = newfreq;
	}

	@Override
	public boolean equals(Object obj) {
			if (this == obj) {
					return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
					return false;
			}
			Rule r = (Rule) obj;

			if(cond.size() != r.cond.size()) {
				return false;
			}

			for(int i = 0; i < cond.size(); i++) {
				if(!cond.get(i).equals(r.cond.get(i))) return false;
			}
			
			if(conc.size() != r.conc.size()) {
				return false;
			}

			for(int i = 0; i < conc.size(); i++) {
				if(!conc.get(i).equals(r.conc.get(i))) return false;
			}

			
			return this.freq == r.freq;
	}
    public boolean sameRule(Rule other) {
        return other.cond.containsAll(cond) && other.conc.containsAll(conc);
    }
    public static ArrayList<Rule> filter(ArrayList<Rule> rules,
                                         ArrayList<Rule> excluded) {
        // This is O(nm) now, could sort to make it faster
        ArrayList<Rule> goodRules = new ArrayList<>();
        for (Rule rule : rules) {
            boolean isGood = true;
            for (Rule badRule : excluded) {
                if (rule.sameRule(badRule)) {
                    isGood = false;
                    break;
                }
            }
            if (isGood)
                goodRules.add(rule);
        }
        return goodRules;
    }
}
