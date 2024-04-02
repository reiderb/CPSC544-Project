package parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.io.FileNotFoundException;

import rules.Predicate;
import rules.Rule;
import rules.Predicate.FEATURE;

public class RulesIO {

    // reads rules from a file from the given path
    public static ArrayList<Rule> readRules(String path) {
        ArrayList<Rule> rules = new ArrayList<>();
        try {
            BufferedReader r  = new BufferedReader(new FileReader(path));
            String line;
            int i = 1;

            try {
                line = r.readLine();
                final HashMap<Integer, Predicate.FEATURE> featureList = getFeatureList(line);

                while((line = r.readLine()) != null) {
                    rules.add(decodeRule(line, featureList));
                }
            } catch (IOException e) {
                System.err.println("Error encountered when parsing line " + i);
                e.printStackTrace();
            } finally {
                r.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rules;
    }

    // Writes an array of rules to a file at the given path
    public static boolean writeRules(ArrayList<Rule> rules, String path) {
        BufferedWriter w;
        try {
            w = new BufferedWriter(new FileWriter(path));

            try {
                w.write(getFeatureListString(Predicate.FEATURE.values()));
                for(int i = 0; i < rules.size(); i++) {
                    w.newLine();
                    w.write(encodeRule(rules.get(i)));
                }
                w.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                w.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Encodes a rule object to a string
    public static String encodeRule(Rule rule) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int n = rule.cond.size();
        
        sb.append(encodePredicate(rule.cond.get(0)));
        for(int i = 1; i < n; i++) {
            sb.append(',').append(encodePredicate(rule.cond.get(i)));
        }
        sb.append("}").append(' ').append('-').append('>').append(' ').append("{").append(encodePredicate(rule.conc.get(0)));
        n = rule.conc.size();
        for(int i = 1; i < n; i++) {
            sb.append(',').append(encodePredicate(rule.conc.get(i)));
        }
        sb.append("}").append(",").append(rule.freq);

        return sb.toString();
    }

    // Encodes a predicate object to a string, it used the ordinal position of the Feature enum to encode the feature to an integer.
    public static String encodePredicate(Predicate p) {
        String retval = "("+ p.feature.toString();
        switch (p.predtype) {
            case FEATURE_VALUE:
                retval += " = " + p.value;
                break;
            case VALUE_RANGE:
                retval += " in [" + p.min + ", " + p.max + ")";
                break;
            case OTHER:
                break;
            default:
                break;
        }
        retval += ")";

        return retval;
    }

    // from a string and a hashmap of features, constructs a Predicate object (could turn hashmap into an ArrayList?)
    // A valid predicate is of the form (0,1,5,6) or (0,1,5) where the first integer is the key to a Faature in the hashmap. The brackets are escaped, so 0,1,5,6 is also valid
    public static Predicate decodePredicate(String str, HashMap<Integer, Predicate.FEATURE> featureList) {
        int n = str.length();
        String arr[] = str.substring((str.charAt(0) == '(') ? 1 : 0, (str.charAt(n - 1) == ')') ? n - 1 : n).split(",");
        FEATURE f = featureList.get(Integer.parseInt(arr[0]));
        if(arr.length == 4) return new Predicate(f, Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
        if(arr.length == 3) return new Predicate(f, Integer.parseInt(arr[2]));

        return null;
    }

    // from a string and a hashmap of features, constructs a Rules object (could turn hashmap into an ArrayList?)
    // a valid rule is of the form {(0,1,5,6)},{(4,0,4)},0.9 for example
    public static Rule decodeRule(String line, HashMap<Integer, Predicate.FEATURE> featureList) {
        Pattern pattern = Pattern.compile("\\{([\\(\\),\\d]+)\\},\\{([\\(\\)\\,\\d]+)\\},([\\d\\.]+)");
        Matcher matcher = pattern.matcher(line);

        if(matcher.find()) {
            String[] condStr = matcher.group(1).split("\\),");
            String[] concStr = matcher.group(2).split("\\),");
            ArrayList<Predicate> cond = new ArrayList<>();
            ArrayList<Predicate> concl = new ArrayList<Predicate>();
            //Predicate concl = decodePredicate(matcher.group(2), featureList);

            for(int i = 0; i < condStr.length; i++) {
                cond.add(decodePredicate(condStr[i], featureList));
            }
            for (int i = 0; i < concStr.length; i++)
            {
				concl.add(decodePredicate(concStr[i], featureList));
			}

            Rule r = new Rule(cond, concl, Float.parseFloat(matcher.group(3)));
            return r;
        }

        return null;
    }

    public static String getFeatureListString(Predicate.FEATURE[] featureList) {
        String featureListString[] = new String[featureList.length];

        for(int i = 0; i < featureList.length; i++) featureListString[i] = featureList[i].name();
        return String.join(",", featureListString);
    }

    public static HashMap<Integer, Predicate.FEATURE> getFeatureList(String line) {
        String featureListString[] = line.split(",");
        HashMap<Integer, Predicate.FEATURE> featureList = new HashMap<>();

        for(int i = 0; i < featureListString.length; i++) {
            featureList.put(i,Predicate.FEATURE.valueOf(featureListString[i]));
        }
        return featureList;
    }

}
