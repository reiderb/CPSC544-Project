import parsing.CollisionEntryParser;
import parsing.ConfigFile;
import apriori.*;
import rules.*;
import apriori.*;
import parsing.*;
import analysis.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) throws Exception {

        Gson jsonParser = new Gson();
        ConfigFile config = jsonParser.fromJson(Files.readString(Path.of(args[0])), ConfigFile.class);
        System.out.printf("Running with configuration:\n\tDatabase path: %s\n\tSuntimes path: %s\n\tMinimum coverage: %f\n\tMinimum rule accuracy: %f\n\tReading excluded rules from: %s\n\tWriting rules to: %s\n",
                          config.database_path, config.suntimes_path, config.min_coverage, config.rule_accuracy, config.excluded_rules_path, config.rules_out_path);
        ArrayList<Rule> excludedRules = RulesIO.readRules(config.excluded_rules_path);
        ArrayList<CollisionEntry> entrylist = CollisionEntryParser.Parse_CSV(config.database_path, config.suntimes_path);
        
        int mincov = (int)(entrylist.size() * config.min_coverage);
        //int mincov = 2;
        float minacc = config.rule_accuracy;
        boolean verifyflag = false; //set this to true if you want to verify the support values, false if you don't.
        
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
        
        if (verifyflag)
        {
            boolean allgood = true;
            Analyzer checker = new Analyzer();
            int checkval;
            entrylist = CollisionEntryParser.Parse_CSV(config.database_path, config.suntimes_path);
            System.out.println("Verifying support values of generated item sets...");
            for (int i = 0; i < itemsets.size(); i++)
            {
                for (int j = 0; j < itemsets.get(i).size(); j++)
                {
                    checkval = checker.checkSupport(itemsets.get(i).get(j).items, entrylist);
                    if (checkval != itemsets.get(i).get(j).support)
                    {
                        System.out.println("Incorrect support for item set");
                        itemsets.get(i).get(j).display();
                        System.out.println("Calculated support: " + itemsets.get(i).get(j).support + ", Actual support: " + checkval);
                        allgood = false;
                    }
                }
            }
            if (allgood)
            {
                System.out.println("All calculated supports are correct!");
            }
        }
        
        start = System.currentTimeMillis();
        RuleGenerator rulelist = new RuleGenerator(itemsets, minacc);
        ArrayList<Rule> goodRules = Rule.filter(rulelist.rulelist, excludedRules);
        finish = System.currentTimeMillis();

        goodRules.sort(new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                return Float.valueOf(o1.freq).compareTo(Float.valueOf(o2.freq));
            }
        });

        goodRules.sort(new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                int i = o1.cond.size();
                int j = o2.cond.size();
                int c = 0;
                if(i != j) return i > j ? +1 : i < j ? -1 : 0;

                for(i = 0; i < j; i++) {
                    c = o1.cond.get(i).compareTo(o2.cond.get(i));
                    if(c != 0) return c;
                }

                return Float.valueOf(o2.freq).compareTo(Float.valueOf(o2.freq));
            }
        });

        for (Rule rule : goodRules)
            System.out.println(parsing.RulesIO.encodeRule(rule));
        System.out.println("Excluded " + (rulelist.rulelist.size() - goodRules.size()) + " rules");
        System.out.println("runtime of rule generation in milliseconds: " + (finish - start));
        parsing.RulesIO.writeRules(goodRules, config.rules_out_path);
    }
}
