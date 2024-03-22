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

    public static ArrayList<Rule> readRules(String path) {
        ArrayList<Rule> rules = new ArrayList<>();
        try {
            BufferedReader r  = new BufferedReader(new FileReader(path));
            boolean hasNext = true;
            String line;
            int i = 1;

            try {
                line = r.readLine();
                final HashMap<Integer, Predicate.FEATURE> featureList = getFeatureList(line);

                while (hasNext) {
                    i++;
                    line = r.readLine();
                    if(line.length() < 1) {
                        hasNext = false;
                    } else {

                    }
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

    public static String encodeRule(Rule rule) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int n = rule.cond.size();
        
        sb.append(encodePredicate(rule.cond.get(0)));
        for(int i = 1; i < n; i++) {
            sb.append(',').append(encodePredicate(rule.cond.get(i)));
        }
        sb.append("}").append(',').append(encodePredicate(rule.conc)).append(',').append(rule.freq);

        return sb.toString();
    }

    public static String encodePredicate(Predicate p) {
        String retval = "("+ p.feature.ordinal() + "," + p.predtype.ordinal();
        switch (p.predtype) {
            case FEATURE_VALUE:
                retval += "," + p.value;
                break;
            case VALUE_RANGE:
                retval += "," + p.min + "," + p.max;
                break;
            case OTHER:
                break;
            default:
                break;
        }
        retval += ")";

        return retval;
    }

    public static Predicate decodePredicate(String str, HashMap<Integer, Predicate.FEATURE> featureList) {
        int n = str.length();
        String arr[] = str.substring((str.charAt(0) == '(') ? 1 : 0, (str.charAt(n - 1) == ')') ? n - 1 : n).split(",");
        FEATURE f = featureList.get(Integer.parseInt(arr[0]));
        if(arr.length == 4) return new Predicate(f, Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
        if(arr.length == 3) return new Predicate(f, Integer.parseInt(arr[2]));

        return null;
    }

    public static Rule decodeRule(String line, HashMap<Integer, Predicate.FEATURE> featureList) {
        Pattern pattern = Pattern.compile("\\{([\\(\\),\\d]+)\\},([\\(\\)\\,\\d]+),([\\d\\.]+)");
        Matcher matcher = pattern.matcher(line);

        if(matcher.find()) {
            String[] condStr = matcher.group(1).split("\\),");
            ArrayList<Predicate> cond = new ArrayList<>();
            Predicate concl = decodePredicate(matcher.group(2), featureList);

            for(int i = 0; i < condStr.length; i++) {
                cond.add(decodePredicate(condStr[i], featureList));
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
