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
        int n_examples = entrylist.size();
        int mincov = (int)(entrylist.size() * config.min_coverage);
        //int mincov = 2;
        float minacc = config.rule_accuracy;
        boolean verifyflag = false; //set this to true if you want to verify the support values, false if you don't.

        ArrayList<Predicate> predicates = new ArrayList<>();
        for (ItemSet i : new IndexedApriori().oneItemCandidates())
            predicates.add(i.items.get(0));
        System.out.println("Generated " + predicates.size() + " predicates");
        NewApriori apriori = new NewApriori(predicates);
        apriori.readDatabase(entrylist);
        entrylist.clear();
        long start = System.currentTimeMillis();
        apriori.run(mincov);
        
        ArrayList<ArrayList<ItemSet>> itemsets = apriori.getItemSets();
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
        RuleGenerator rulelist = new RuleGenerator(itemsets, minacc, n_examples);
        ArrayList<Rule> goodRules = Rule.filter(rulelist.rulelist, excludedRules);
        Co
        finish = System.currentTimeMillis();
        for (Rule rule : goodRules)
            System.out.println(parsing.RulesIO.encodeRule(rule));
        System.out.println("Excluded " + (rulelist.rulelist.size() - goodRules.size()) + " rules");
        System.out.println("runtime of rule generation in milliseconds: " + (finish - start));
        parsing.RulesIO.writeRules(goodRules, config.rules_out_path);
    }
}
