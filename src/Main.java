import parsing.CollisionEntryParser;
import rules.*;
import parsing.CollisionEntry;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        ArrayList<CollisionEntry> entrylist = CollisionEntryParser.Parse_CSV("collision-databse_1999-2019.csv");
        System.out.println("Making empty rule");
        Rule test = new Rule();
        test.cond.add(new Predicate(Predicate.FEATURE.C_YEAR, 1999, 2003)); //adds a predicate to the condition list that a collision occured between january 1, 1999 and december 31, 2002
        test.cond.add(new Predicate(Predicate.FEATURE.C_MNTH, 1)); //adds a condition that a collision occured in january (i believe)
        test.cond.add(new Predicate(Predicate.FEATURE.C_SEV, CollisionEntry.C_SEV.NON_FATAL_INJURY.ordinal())); //thought i should show how to represent enum values for the predicates..
        test.conc.add(new Predicate(Predicate.FEATURE.V_YEAR, 1990)); //rule conclusion is that the vehicle is a 1990 model
        
        //so, the rule says that if a an accident happened in january between 1999 and the end of 2002, then the vehicle involved must have been made in 1990
        //a silly rule, but this is just for test purposes. it should be true for the 0th entry, but not the first entry.
        
        RuleChecker checker = new RuleChecker();
        if (checker.checkRule(entrylist.get(0), test))
        {
            System.out.println("The rule applies to the 0th entry");
        }
        else
        {
            System.out.println("The rule does NOT apply to the 0th entry");
        }
        
        if (checker.checkRule(entrylist.get(1), test))
        {
            System.out.println("The rule applies to the 1st entry");
        }
        else
        {
            System.out.println("The rule does NOT apply to the 1st entry");
        }
    }
}
