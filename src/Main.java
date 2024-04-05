import parsing.CollisionEntryParser;
import parsing.ConfigFile;
import apriori.*;
import rules.*;
import apriori.*;
import parsing.CollisionEntry;
import analysis.*;

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
        
        int mincov = (int)(entrylist.size() * config.min_coverage);
        //int mincov = 2;
        float minacc = config.rule_accuracy;

        /*
        //uncomment if you'd like to check the support value of some set of predicates
        ArrayList<Predicate> predlist = new ArrayList<Predicate>();
        Predicate temp = new Predicate(rules.Predicate.FEATURE.C_SEV, 1);
        predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.C_WTHR, 0);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.C_RSUR, 0);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.C_RALN, 0);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.C_TRAF, 17);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.V_TYPE, 0);
        predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.P_SEX, 1);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.P_PSN, 0);
        predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.P_ISEV, 1);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.P_SAFE, 1);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.P_USER, 0);
        predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.C_VEHS, 2);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.V_YEAR, 2000, 2020);
        //predlist.add(temp);
        temp = new Predicate(rules.Predicate.FEATURE.L_COND, 0);
        //predlist.add(temp);
        
        Collections.sort(predlist);
        Analyzer checker = new Analyzer();
        int checkval = checker.checkSupport(predlist, entrylist);
        System.out.println("support == " + checkval);
        */

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
        RuleGenerator rulelist = new RuleGenerator(itemsets, minacc);
        finish = System.currentTimeMillis();
        for (int i = 0; i < rulelist.rulelist.size(); i++)
        {
            System.out.println(parsing.RulesIO.encodeRule(rulelist.rulelist.get(i)));
        }
        System.out.println("runtime of rule generation in milliseconds:");
        System.out.println(finish - start);
        parsing.RulesIO.writeRules(rulelist.rulelist, config.rules_out_path);
    }
}
