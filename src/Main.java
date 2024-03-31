import parsing.CollisionEntryParser;
import apriori.*;
import rules.*;
import apriori.*;
import parsing.CollisionEntry;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        
        ArrayList<CollisionEntry> entrylist = CollisionEntryParser.Parse_CSV("collision-databse_1999-2019.csv");
        int mincov = entrylist.size() / 4; //so, if an item set is satisfied in a quarter the entries, it should be included in the itemsets found by Apriori
        IndexedApriori apriorisets = new IndexedApriori(entrylist, mincov);
        ArrayList<ArrayList<ItemSet>> itemsets = apriorisets.itemlists;
        for (int i = 0; i < itemsets.size(); i++)
        {
            System.out.println(Integer.toString(i + 1) + " ITEM SETS");
            for (int j = 0; j < itemsets.get(i).size(); j++)
            {
                itemsets.get(i).get(j).display();
            }
        }
        /*
        IndexedApriori crap = new IndexedApriori();
        
        ArrayList<Integer> test1 = new ArrayList<Integer>();
        test1.add(1);
        test1.add(2);
        test1.add(3);
        test1.add(4);
        test1.add(5);
        ArrayList<Integer> test2 = new ArrayList<Integer>();
        test2.add(2);
        test2.add(4);
        ArrayList<Integer> test3 = crap.indexIntersection(test1, test2);
        for (int i = 0; i < test3.size(); i++)
        {
            System.out.println(test3.get(i));
        }
        */
        /*
        System.out.println("Making empty rule");
        Rule test = new Rule();
        test.cond.add(new Predicate(Predicate.FEATURE.C_YEAR, 1999, 2003)); //adds a predicate to the condition list that a collision occured between january 1, 1999 and december 31, 2002
        test.cond.add(new Predicate(Predicate.FEATURE.C_MNTH, 1)); //adds a condition that a collision occured in january (i believe)
        test.cond.add(new Predicate(Predicate.FEATURE.C_SEV, CollisionEntry.C_SEV.NON_FATAL_INJURY.ordinal())); //thought i should show how to represent enum values for the predicates..
        test.conc.add(new Predicate(Predicate.FEATURE.V_YEAR, 1990)); //rule conclusion is that the vehicle is a 1990 model
        Predicate daycheck = new Predicate(Predicate.FEATURE.L_COND, CollisionEntry.L_COND.DAY.ordinal());
        
        //so, the rule says that if a an accident happened in january between 1999 and the end of 2002, then the vehicle involved must have been made in 1990
        //a silly rule, but this is just for test purposes. it should be true for the 0th entry, but not the first entry.
        
        RuleChecker checker = new RuleChecker();
        
        if (checker.checkPredicate(entrylist.get(0), daycheck))
        {
            System.out.println("0th entry incorrectly detected as at daytime");
        }
        else
        {
            System.out.println("0th entry correctly detected as NOT at daytime");
        }
        if (checker.checkPredicate(entrylist.get(3), daycheck))
        {
            System.out.println("3rd entry correctly detected as daytime");
        }
        else
        {
            System.out.println("3rd entry incorrectly detected as NOT at daytime");
        }
        */
    }
}
