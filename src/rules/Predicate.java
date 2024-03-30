package rules;

public class Predicate
{
    public enum PRED_TYPE
    {
        FEATURE_VALUE,
        VALUE_RANGE,
        OTHER //this will be used for knowledge not represented in the collision entries
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
        L_COND;
    }
    
    public PRED_TYPE predtype;
    public FEATURE feature;
    public int value; //this could be either an index, or just the actual value (eg for C_YEAR)
    public int min;
    public int max; //for range predicates, we'll use min and max
    
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
