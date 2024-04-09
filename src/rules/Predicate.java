package rules;
import java.util.*;

public class Predicate implements Comparable<Predicate>
{
    public enum PRED_TYPE
    {
        FEATURE_VALUE,
        VALUE_RANGE,
        MULTIPLE_VALUES,
    }
    
    public enum FEATURE
    {
        C_WDAY,
        C_SEV,
        C_CONF,
        C_RCFG,
        C_WTHR,
        C_RSUR,
        C_RALN,
        C_TRAF,
        V_TYPE,
        P_SEX,
        P_PSN,
        P_ISEV,
        P_SAFE,
        P_USER,
        C_YEAR,
        C_MNTH,
        C_HOUR,
        C_VEHS,
        V_ID,
        V_YEAR,
        P_ID,
        P_AGE,
        C_CASE, //not sure if the case number is relevant to our purposes, but might as well include it.
        V_DRAGE, // age of the driver of the vehicle, is NULL for 
        L_COND,
        V_AGE;
    }
    
    public PRED_TYPE predtype;
    public FEATURE feature;
    public int value; //this could be either an index, or just the actual value (eg for C_YEAR)
    public int min;
    public int max; //for range predicates, we'll use min and max
    public ArrayList<Integer> values;
    
    public Predicate(FEATURE newfeat, int newval)
    {
        //if the constructor is passed a single integer, we'll assume only value has to be defined.
        predtype = PRED_TYPE.FEATURE_VALUE;
        feature = newfeat;
        value = newval;
    }
    
    public Predicate(FEATURE newfeat, int newmin, int newmax)
    {
        //if the constructor is passed two integers, we'll assume they represent a range
        //probably wouldn't hurt to put some common sense checks here, like, is min <= max
        //or does it make sense to define a range for the given feature.. maybe later..
        predtype = PRED_TYPE.VALUE_RANGE;
        feature = newfeat;
        min = newmin;
        max = newmax;
    }

    public Predicate(FEATURE newfeat, ArrayList<Integer> values) {
        predtype = PRED_TYPE.MULTIPLE_VALUES;
        feature = newfeat;
        this.values = values;
        Collections.sort(this.values);
    }
    
    @Override
    public int compareTo(Predicate pred)
    {
		if (this.feature.ordinal() < pred.feature.ordinal())
		{
			return -1;
		}
		if (this.feature.ordinal() > pred.feature.ordinal())
		{
			return 1;
		}
		//if we get to this point, then both predicates concern the same feature
		if (this.predtype.ordinal() < pred.predtype.ordinal())
		{
			return -1;
		}
		if (this.predtype.ordinal() > pred.predtype.ordinal())
		{
			return 1;
		}
		//if we get to this point, then both predicates have the same type
		if (this.predtype == PRED_TYPE.FEATURE_VALUE)
		{
			if (this.value < pred.value) {return -1;}
			if (this.value > pred.value) {return 1;}
			return 0;
		}
		if (this.predtype == PRED_TYPE.VALUE_RANGE)
		{
			if (this.min < pred.min) {return -1;}
			if (this.min > pred.min) {return 1;}
			if (this.max < pred.max) {return -1;}
			if (this.max > pred.max) {return 1;}
			return 0;
		}
                if (this.predtype == PRED_TYPE.MULTIPLE_VALUES)
                    return Arrays.compare(this.values.toArray(new Integer[0]),
                                          pred.values.toArray(new Integer[0]));
		return 0;
	}
	
	public String display()
	{
		String message = feature.toString();
		if (predtype == PRED_TYPE.FEATURE_VALUE)
		{
			message = message + " = " + Integer.toString(value);
		}
		if (predtype == PRED_TYPE.VALUE_RANGE)
		{
			message = message + " in [" + Integer.toString(min) + ", " + Integer.toString(max) + ")";
		}
                if (predtype == PRED_TYPE.MULTIPLE_VALUES) {
                    message = message + " in {";
                    for (Integer v : values)
                        message = message + v + ",";
                    message = message.substring(0, message.length()-1);
                    message = message + "}";
                }
		return message;
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Predicate p = (Predicate) obj;
        return this.max == p.max && this.min == p.min && this.feature == p.feature && this.predtype == p.predtype;
    }
}
