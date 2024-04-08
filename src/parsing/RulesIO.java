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
                while((line = r.readLine()) != null) {
                    rules.add(decodeRule(line));
                }
            } catch (IOException e) {
                System.err.println("Error encountered when parsing line " + i);
                e.printStackTrace();
            } finally {
                r.close();
            }
        } catch (FileNotFoundException e) {
			System.out.println("No rules found at " + path + ". Continuing with empty list of rules.");
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
                for(int i = 0; i < rules.size(); i++) {
                    w.write(encodeRule(rules.get(i)));
                    w.newLine();
                }
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
            case MULTIPLE_VALUES:
                break; // TODO
            default:
                break;
        }
        retval += ")";

        return retval;
    }

    // from a string and a hashmap of features, constructs a Predicate object (could turn hashmap into an ArrayList?)
    // A valid predicate is of the form (0,1,5,6) or (0,1,5) where the first integer is the key to a Faature in the hashmap. The brackets are escaped, so 0,1,5,6 is also valid
    public static Predicate decodePredicate(String str) {
        Pattern pattern = Pattern.compile("([A-Z_]+) = (\\d+)|([A-Z_]+) in \\[(\\d+), (\\d+)\\)");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            if(matcher.group(1) != null) {
                return new Predicate(FEATURE.valueOf(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } else {
                return new Predicate(FEATURE.valueOf(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));
            }
        }
        return null;
    }

    // from a string and a hashmap of features, constructs a Rules object (could turn hashmap into an ArrayList?)
    // a valid rule is of the form {(0,1,5,6)},{(4,0,4)},0.9 for example
    public static Rule decodeRule(String line) {
        Pattern pattern = Pattern.compile("\\{(.+)\\} -> \\{(.+)\\},([\\.\\d+]+)");
        Matcher matcher = pattern.matcher(line);

        if(matcher.find()) {
            String[] condStr = matcher.group(1).split("\\),\\(");
            String[] concStr = matcher.group(2).split("\\),\\(");
            ArrayList<Predicate> cond = new ArrayList<>();
            ArrayList<Predicate> concl = new ArrayList<>();

            for(int i = 0; i < condStr.length; i++) {
                cond.add(decodePredicate(condStr[i]));
            }
            for (int i = 0; i < concStr.length; i++)
            {
				concl.add(decodePredicate(concStr[i]));
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
}
