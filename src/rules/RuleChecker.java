package rules;
import parsing.CollisionEntry;
import java.util.ArrayList;

public class RuleChecker
{
	private CollisionEntry.C_WDAY[] c_wday;
	private CollisionEntry.C_SEV[] c_sev;
	private CollisionEntry.C_CONF[] c_conf;
	private CollisionEntry.C_RCFG[] c_rcfg;
	private CollisionEntry.C_WTHR[] c_wthr;
	private CollisionEntry.C_RSUR[] c_rsur;
	private CollisionEntry.C_RALN[] c_raln;
	private CollisionEntry.C_TRAF[] c_traf;
	private CollisionEntry.V_TYPE[] v_type;
	private CollisionEntry.P_SEX[] p_sex;
	private CollisionEntry.P_PSN[] p_psn;
	private CollisionEntry.P_ISEV[] p_isev;
	private CollisionEntry.P_SAFE[] p_safe;
	private CollisionEntry.P_USER[] p_user;
	private CollisionEntry.L_COND[] l_cond;
	
	public RuleChecker()
	{
		c_wday = CollisionEntry.C_WDAY.values();
		c_sev = CollisionEntry.C_SEV.values();
		c_conf = CollisionEntry.C_CONF.values();
		c_rcfg = CollisionEntry.C_RCFG.values();
		c_wthr = CollisionEntry.C_WTHR.values();
		c_rsur = CollisionEntry.C_RSUR.values();
		c_raln = CollisionEntry.C_RALN.values();
		c_traf = CollisionEntry.C_TRAF.values();
		v_type = CollisionEntry.V_TYPE.values();
		p_sex = CollisionEntry.P_SEX.values();
		p_psn = CollisionEntry.P_PSN.values();
		p_isev = CollisionEntry.P_ISEV.values();
		p_safe = CollisionEntry.P_SAFE.values();
		p_user = CollisionEntry.P_USER.values();
		l_cond = CollisionEntry.L_COND.values();
		
	}
	
	public boolean checkPredicateList(CollisionEntry entry, ArrayList<Predicate> predlist)
	{
		int i = 0;
		boolean flag = true;
		while (i < predlist.size() && flag)
		{
			flag = checkPredicate(entry, predlist.get(i));
			i++;
		}
		return flag;
	}
	
	public boolean checkRuleConditions(CollisionEntry entry, Rule rule)
	{
		return checkPredicateList(entry, rule.cond);
	}
	
	public boolean checkRuleConclusion(CollisionEntry entry, Rule rule)
	{
		return checkPredicateList(entry, rule.conc);
	}
	
	public boolean checkRule(CollisionEntry entry, Rule rule)
	{
		return (checkRuleConditions(entry, rule) && checkRuleConclusion(entry, rule));
	}
	
	public boolean checkPredicate(CollisionEntry entry, Predicate pred)
	{
		boolean flag = false;
		switch(pred.predtype)
		{
			case FEATURE_VALUE:
				flag = checkFeatureValue(entry, pred);
				break;
			case VALUE_RANGE:
				flag = checkFeatureRange(entry, pred);
				break;
			case MULTIPLE_VALUES:
				flag = true; //change this once we implement the external knowledge
				break;
		}
		return flag;
	}
	
	public boolean checkFeatureValue(CollisionEntry entry, Predicate pred)
	{
		boolean flag = false;
		switch (pred.feature)
		{
			case C_WDAY:
				flag = (c_wday[pred.value] == entry.WEEK_DAY);
				break;
			case C_SEV:
				flag = (c_sev[pred.value] == entry.SEVERITY);
				break;
			case C_CONF:
				flag = (c_conf[pred.value] == entry.VEHICLE_CONFIGURATION);
				break;
			case C_RCFG:
				flag = (c_rcfg[pred.value] == entry.ROAD_CONFIGURATION);
				break;
			case C_WTHR:
				flag = (c_wthr[pred.value] == entry.WEATHER);
				break;
			case C_RSUR:
				flag = (c_rsur[pred.value] == entry.ROAD_SURFACE);
				break;
			case C_RALN:
				flag = (c_raln[pred.value] == entry.RALN);
				break;
			case C_TRAF:
				flag = (c_traf[pred.value] == entry.TRAFFIC_CONTROL);
				break;
			case  V_TYPE:
				flag = (v_type[pred.value] == entry.VEHICLE_TYPE);
				break;
			case P_SEX:
				flag = (p_sex[pred.value] == entry.PERSON_SEX);
				break;
			case P_PSN:
				flag = (p_psn[pred.value] == entry.PERSON_POSITION);
				break;
			case P_ISEV:
				flag = (p_isev[pred.value] == entry.PERSON_INJURY_SEVERITY);
				break;
			case P_SAFE:
				flag = (p_safe[pred.value] == entry.SAFETY_DEVICES);
				break;
			case P_USER:
				flag = (p_user[pred.value] == entry.USER);
				break;
			case C_YEAR:
				flag = (pred.value == entry.YEAR);
				break;
			case C_MNTH:
				flag = (pred.value == entry.MONTH);
				break;
			case C_HOUR:
				flag = (pred.value == entry.HOUR);
				break;
			case C_VEHS:
				flag = (pred.value == entry.VEHICLE_COUNT);
				break;
			case V_ID:
				flag = (pred.value == entry.VEHICLE_SEQUENCE_NUM);
				break;
			case V_YEAR:
				flag = (pred.value == entry.VEHICLE_MODEL_YEAR);
				break;
			case P_ID:
				flag = (pred.value == entry.PERSON_ID);
				break;
			case P_AGE:
				flag = (pred.value == entry.PERSON_AGE);
				break;
			case C_CASE:
				flag = (pred.value == entry.CASE_NUMBER);
				break;
			case L_COND:
				flag = (l_cond[pred.value] == entry.LIGHT);
				break;
			case V_DRAGE:
				flag = (pred.value == entry.DRIVER_AGE);
				break;
			case V_AGE:
				flag = (pred.value == entry.VEHICLE_AGE);
				break;
		}
		return flag;
	}
	
	public boolean checkFeatureRange(CollisionEntry entry, Predicate pred)
	{
		//for the range we're going to say the values has to be greater than
		//or equal to the minimum, but strictly less than the max (allowing us
		//to use the max as the min of another range)
		boolean flag = false;
		int temp;
		switch(pred.feature)
		{
			case C_YEAR:
				temp = entry.YEAR;
				break;
			case C_MNTH: //months may have to be a special case, because
			//you could conceivably want to set a range as december to february..
				temp = entry.MONTH;
				break;
			case C_HOUR: //again, you may want to see between 10:00 pm to 2:00 am
				temp = entry.HOUR;
				break;
			case C_VEHS:
				temp = entry.VEHICLE_COUNT;
				break;
			case V_ID: //we may have to tie v_id and case numbers together
			//if two vehicles from two separate incidents have the same V_ID
			//it doesn't seem like that would be particularly significant..
				temp = entry.VEHICLE_SEQUENCE_NUM;
				break;
			case V_YEAR:
				temp = entry.VEHICLE_MODEL_YEAR;
				break;
			case P_ID:
				temp = entry.PERSON_ID;
				break;
			case P_AGE:
				temp = entry.PERSON_AGE;
				break;
			case C_CASE:
				temp = entry.CASE_NUMBER;
				break;
			case V_DRAGE:
				temp = entry.DRIVER_AGE;
				break;
			case V_AGE:
				temp = entry.VEHICLE_AGE;
				break;
			default: //in any other case, the feature value is some non-numeric value
				temp = pred.min - 1; //we want to return false in that case, so we set temp lower than the minimum
				break;
		}
		return (pred.min <= temp && temp < pred.max);
	}
}
