import parsing.CollisionEntryParser;
import parsing.CollisionEntry;
import parsing.CollisionEntry.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Apriori {
    int mincount;
    ArrayList<Object[]> itemsets;
    HashMap<Object[], Integer> frequent;
    ArrayList<CollisionEntry> collisions;
    
	public Apriori(double minsup, ArrayList<CollisionEntry> db) {
		System.out.println("Size " + db.size());
		this.mincount = (int) (minsup * (double) db.size());
		System.out.println("Min count is " + this.mincount);
		this.collisions = db;
	}
    
        
    private void constructItemSets() {
    	itemsets = new ArrayList<Object[]>();
    	for(int i = 0; i < 22; i++) {
    	    if(i == 0) {
    	        for(int j = 1999; j <= 2019; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        } 
    	    } else if(i == 1){
    	    	for(int j = 1; j <= 12; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 2){
    	    	for(C_WDAY j: C_WDAY.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 3){
    	    	for(int j = 0; j <= 23; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 4){
    	    	for(C_SEV j : C_SEV.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 5){
    	    	for(int j = -4; j <= 99; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 6){
    	    	for(C_CONF j : C_CONF.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 7){
    	    	for(C_RCFG j : C_RCFG.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 8){
    	    	for(C_WTHR j : C_WTHR.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 9){
    	    	for(C_RSUR j : C_RSUR.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 10){
    	    	for(C_RALN j : C_RALN.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 11){
    	    	for(C_TRAF j : C_TRAF.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 12){
    	    	for(int j = 1; j <= 99; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 13){
    	    	for(V_TYPE j : V_TYPE.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 14){
    	    	for(int j = 1900; j <= 2019; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        }
    	    } else if(i == 15){
    	    	for(int j = 1; j <= 99; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 16){
    	    	for(P_SEX j : P_SEX.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 17){
    	    	for(int j = 0; j <= 99; j++) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 18){
    	    	for(P_PSN j : P_PSN.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 19){
    	    	for(P_ISEV j : P_ISEV.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 20){
    	    	for(P_SAFE j : P_SAFE.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    } else if(i == 21){
    	    	for(P_USER j : P_USER.values()) {
    	        	Object[] temp = new Object[22];
    	        	Arrays.fill(temp, -5);
    	        	temp[i] = j;
    	        	itemsets.add(temp);
    	        }
    	    }
    	}
    	checkSupport();
    }
    
    private void findCandidates() {
    	ArrayList<Object[]> candidates = new ArrayList<Object[]>();
    	for(int i = 0; i < itemsets.size(); i++) {
    		for(int j = i+1; j < itemsets.size(); j++) {
    			Object[] m = itemsets.get(i);
    			Object[] n = itemsets.get(j);
    			int diff = -1;
    			for(int k = 0; k < 22; k++) {
    				//(m[k] instanceof Integer && (int) m[k] == -5)
    				//(n[k] instanceof Integer && (int) n[k] == -5)
    				//both are null
    				if((m[k] instanceof Integer && (int) m[k] == -5) && (n[k] instanceof Integer && (int) n[k] == -5)) continue;
    				//neither are null
    				else if(!(m[k] instanceof Integer && (int) m[k] == -5) && !(n[k] instanceof Integer && (int) n[k] == -5)){
    					if(m[k] != n[k]) {
    						diff = -1;
    						break;
    					}else continue;
    				//only m is null
    				}else if((m[k] instanceof Integer && (int) m[k] == -5)) {
    					if(diff != -1) {
    						diff = -1;
    						break;
    					}else diff = k;
    				}
    			}
    			if(diff >= 0) {
    				Object[] temp = m.clone();
    				temp[diff] = n[diff];
    				candidates.add(temp);
    				//System.out.println("Set 1: " + Arrays.toString(m));
    				//System.out.println("Set 2: " + Arrays.toString(n));
    				//System.out.println("New  : " + Arrays.toString(temp));
    			}
    		}
    	}
    	System.out.println("Found " + candidates.size() + " candidates");
    	itemsets = candidates;
    }
    
    private void checkSupport() {
    	System.out.println(itemsets.size() + " itemsets prior to checking support");
    	ArrayList<Object[]> supported = new ArrayList<Object[]>();
    	int[] count = new int[itemsets.size()];
    	for(CollisionEntry c : collisions) {
    		for(int i = 0; i < itemsets.size(); i++) {
    			if(c.contains(itemsets.get(i))) count[i]++;
    		}
    	}
    	
    	for(int i = 0; i < itemsets.size(); i++) {
    		if(count[i] > mincount) {
    			frequent.put(itemsets.get(i), count[i]);
    			supported.add(itemsets.get(i));
    		}
    	}
    	itemsets = supported;
    	System.out.println(itemsets.size() + " supported itemsets");
    }
    
    public HashMap<Object[], Integer> compute(){
    	frequent = new HashMap<Object[], Integer>();
    	constructItemSets();
    	while(itemsets.size() != 0) {
    		findCandidates();
    		checkSupport();
    	}
    	return frequent;
    }
    
    
    public static void main(String[] args) throws Exception {
    	ArrayList<CollisionEntry> entrylist = CollisionEntryParser.Parse_CSV("collision-databse_1999-2019.csv");
        Apriori ml = new Apriori(0.5, entrylist);
        HashMap<Object[], Integer> FrequentItemSets = ml.compute();
        for (HashMap.Entry<Object[], Integer> entry : FrequentItemSets.entrySet()) {
            Object[] itemset = entry.getKey();
            Integer support = entry.getValue();
            
            System.out.print("Itemset: {");
            for (int i = 0; i < itemset.length; i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(itemset[i]);
            }
            System.out.println("} Support: " + support);
        }
    }
}