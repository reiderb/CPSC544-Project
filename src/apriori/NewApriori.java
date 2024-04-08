import rules.*;
import parsing.CollisionEntry;
import java.util.*;
import apriori.*;

public class NewApriori {
    ArrayList<Predicate> predicates;
    ArrayList<ArrayList<Integer>> predicateSupport;
    ArrayList<HashMap<ArrayList<Integer>, Integer>> itemsets;

    public NewApriori(ArrayList<Predicate> predicates) {
        this.predicates = predicates;
    }

    public void readDatabase(ArrayList<CollisionEntry> entries) {
        predicateSupport = new ArrayList<>();
        for (int predId = 0; predId < predicates.size(); predId++)
            predicateSupport.add(new ArrayList<>());
        RuleChecker checker = new RuleChecker();
        for (int entryId = 0; entryId < entries.size(); entryId++)
            for (int predId = 0; predId < predicates.size(); predId++)
                if (checker.checkPredicate(entries.get(entryId), predicates.get(predId)))
                    predicateSupport.get(predId).add(entryId);
    }

    public void run(int minSupport) {
        ArrayList<HashMap<ArrayList<Integer>, Integer>> itemsets = new ArrayList<>();
        filterPredicates(minSupport);
        itemsets.add(getOneItemsets());
        while (!itemsets.get(itemsets.size()-1).isEmpty()) {
            System.out.println("Generated " + itemsets.get(itemsets.size()-1).size() + " " + itemsets.size() + "-itemsets ");
            itemsets.add(createNextItemsets(itemsets.get(itemsets.size()-1), minSupport));
        }
        itemsets.remove(itemsets.size() - 1);
        this.itemsets = itemsets;
    }

    public ArrayList<ArrayList<ItemSet>> getItemSets() {
        ArrayList<ArrayList<ItemSet>> result = new ArrayList<>();
        for (var kItemset : this.itemsets) {
            ArrayList<ItemSet> kResult = new ArrayList<>();
            for (var set : kItemset.keySet()) {
                ItemSet itemset = new ItemSet();
                for (int predId : set)
                    itemset.items.add(predicates.get(predId));
                Collections.sort(itemset.items);
                itemset.support = kItemset.get(set);
                kResult.add(itemset);
            }
            result.add(kResult);
        }
        return result;
    }

    public void filterPredicates(int minSupport) {
        ArrayList<Predicate> newPredicates = new ArrayList<>();
        ArrayList<ArrayList<Integer>> newPredicateSupport = new ArrayList<>();
        for (int predId = 0; predId < predicates.size(); predId++) {
            if (predicateSupport.get(predId).size() >= minSupport) {
                newPredicates.add(predicates.get(predId));
                newPredicateSupport.add(predicateSupport.get(predId));
            }
        }
        predicates = newPredicates;
        predicateSupport = newPredicateSupport;
    }

    private HashMap<ArrayList<Integer>, Integer>
        createNextItemsets(HashMap<ArrayList<Integer>, Integer> prevItemsets,
                           int minSupport) {
        HashMap<ArrayList<Integer>, Integer> nextItemsets = new HashMap<>();
        for (var itemset1 : prevItemsets.keySet()) {
            for (var itemset2 : prevItemsets.keySet()) {
                if (!canCombineItemsets(itemset1, itemset2))
                    continue;
                var newItemset = combineItemsets(itemset1, itemset2);
                int support = getSupport(newItemset, minSupport);
                if (support >= minSupport)
                    nextItemsets.put(newItemset, support);
            }
        }
        return nextItemsets;
    }

    private HashMap<ArrayList<Integer>, Integer> getOneItemsets() {
        HashMap<ArrayList<Integer>, Integer> oneItemSets = new HashMap<>();
        for (int pred_id = 0; pred_id < predicates.size(); pred_id++)
            oneItemSets.put(new ArrayList<>(Arrays.asList(pred_id)),
                            predicateSupport.get(pred_id).size());
        return oneItemSets;
    }

    private boolean canCombineItemsets(ArrayList<Integer> itemset1,
                                       ArrayList<Integer> itemset2) {
        if (itemset1.size() != itemset2.size())
            throw new RuntimeException("Itemsets must have equal sizes");
        int size = itemset1.size();
        for (int i = 0; i < size-1; i++)
            if (itemset1.get(i) != itemset2.get(i))
                return false;
        if (predicates.get(itemset1.get(size-1)).feature ==
            predicates.get(itemset2.get(size-1)).feature)
            return false;
        return itemset1.get(size-1) < itemset2.get(size-1);
    }

    private ArrayList<Integer> combineItemsets(ArrayList<Integer> itemset1,
                                               ArrayList<Integer> itemset2) {
        ArrayList<Integer> newItemset = new ArrayList<>(itemset1);
        newItemset.add(itemset2.get(itemset2.size() - 1));
        return newItemset;
    }

    private int getSupport(ArrayList<Integer> itemset, int minSupport) {
        ArrayList<Integer> support = predicateSupport.get(itemset.get(0));
        for (int i = 1; i < itemset.size(); i++) {
            if (support.size() < minSupport)
                return 0;
            support = linearIntersection(support, predicateSupport.get(itemset.get(i)));
        }
        return support.size();
    }

    private ArrayList<Integer> linearIntersection(ArrayList<Integer> support1,
                                                  ArrayList<Integer> support2) {
        int i = 0;
        int j = 0;
        ArrayList<Integer> newSupport = new ArrayList<>();
        while (i < support1.size() && j < support2.size()) {
            if (support1.get(i).equals(support2.get(j))) {
                newSupport.add(support1.get(i));
                i++;
                j++;
            } else if (support1.get(i) < support2.get(j))
                i++;
            else
                j++;
        }
        return newSupport;
    }
}
        
