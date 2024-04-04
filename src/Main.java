import parsing.CollisionEntryParser;
import parsing.ConfigFile;
import apriori.*;
import rules.*;
import apriori.*;
import parsing.CollisionEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) throws Exception {

        Gson jsonParser = new Gson();
        ConfigFile config = jsonParser.fromJson(Files.readString(Path.of(args[0])), ConfigFile.class);
        System.out.printf("Running with configuration:\n\tDatabase path: %s\n\tSuntimes path: %s\n\tMinimum coverage: %f\n\tMinimum rule accuracy: %f\n\tWriting rules to: %s\n",
        config.database_path, config.suntimes_path, config.min_coverage, config.rule_accuracy, config.rules_out_path);

        ArrayList<CollisionEntry> entrylist = CollisionEntryParser.Parse_CSV(config.database_path, config.suntimes_path);
        int n_examples = entrylist.size();
        int mincov = (int)(entrylist.size() * config.min_coverage);
        float minacc = config.rule_accuracy;

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
        System.out.println("runtime of apriori algorithm in milliseconds:");
        System.out.println(finish - start);
        start = System.currentTimeMillis();
        RuleGenerator rulelist = new RuleGenerator(itemsets, minacc, n_examples);
        finish = System.currentTimeMillis();
        Collections.sort(rulelist.rulelist, Comparator.comparing(Rule::getLift));
        for (int i = 0; i < rulelist.rulelist.size(); i++)
        {
            System.out.println(parsing.RulesIO.encodeRule(rulelist.rulelist.get(i)));
        }
        System.out.println("runtime of rule generation in milliseconds:");
        System.out.println(finish - start);
        parsing.RulesIO.writeRules(rulelist.rulelist, config.rules_out_path);
    }
}
