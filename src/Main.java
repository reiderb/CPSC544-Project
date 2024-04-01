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
        int mincov = (entrylist.size() * 13) / 20;
        IndexedApriori apriorisets = new IndexedApriori();
        long start = System.currentTimeMillis();
        apriorisets.itemlists.add(apriorisets.oneItemList(entrylist, mincov));
        entrylist.clear(); //after the one item sets are generated, we no longer need the entrylist, so we clear it to save memory.
        apriorisets.generateItemSets(mincov);
        ArrayList<ArrayList<ItemSet>> itemsets = apriorisets.itemlists;
        for (int i = 0; i < itemsets.size(); i++)
        {
            System.out.println(Integer.toString(i + 1) + " ITEM SETS");
            for (int j = 0; j < itemsets.get(i).size(); j++)
            {
                itemsets.get(i).get(j).display();
            }
        }
        long finish = System.currentTimeMillis();
        System.out.println("runtime in milliseconds:");
        System.out.println(finish - start);
    }
}
