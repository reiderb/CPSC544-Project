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
    }
}
