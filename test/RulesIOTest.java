import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import parsing.RulesIO;
import rules.Rule;
import rules.Predicate;
import rules.Predicate.FEATURE;
import rules.Predicate.PRED_TYPE;

public class RulesIOTest {  
  Predicate p1, p2, p3, c1, c2;
  Rule r1, r2;
  ArrayList<Rule> r;
  File f1, f2;
  
  @Before
  public void setUp() {
      p1 = new Predicate(FEATURE.C_TRAF, 2); // (C_TRAF = 2)
      p2 = new Predicate(FEATURE.C_MNTH, 2, 3); // (C_MNTH in [2,3))
      p3 = new Predicate(FEATURE.C_WDAY, 5, 6); // (C_WDAY in [5,6))
      c1 = new Predicate(FEATURE.C_HOUR, 16, 18); // (C_HOUR in [16,18))
      c2 = new Predicate(FEATURE.C_WTHR, 4); // (C_WTHR = 4)
      r1 = new Rule(new ArrayList<Predicate>(){ {add(p2); add(p1);} }, new ArrayList<Predicate>(){{add(c1);}}, 0.8f); // {(C_MNTH in [2,3)),(C_TRAF = 2)} -> {(C_HOUR in [16,18))},0.8
      r2 = new Rule(new ArrayList<Predicate>(){ {add(p3);} }, new ArrayList<Predicate>(){{add(c2);}}, 0.9f); // {(C_WDAY in [5,6))} -> {(C_WTHR = 4)},0.9

      r = new ArrayList<>(){{add(r1); add(r2);}};
  
      try {
        BufferedWriter w = new BufferedWriter(new FileWriter("./test/testfile1.txt"));
        w.write("{(C_MNTH in [2, 3)),(C_TRAF = 2)} -> {(C_HOUR in [16, 18))},0.8");
        w.newLine();
        w.write("{(C_WDAY in [5, 6))} -> {(C_WTHR = 4)},0.9");
        w.newLine();
        w.close();
      }
      catch(IOException e) {
          System.err.println("error creating temporary test file in " + this.getClass().getSimpleName());
      }
  }


  @Test
  public void testEncodePredicateValue() {
    assertEquals("(C_TRAF = 2)", RulesIO.encodePredicate(p1));
    assertEquals("(C_WTHR = 4)", RulesIO.encodePredicate(c2));
  }

  @Test
  public void testEncodePredicateRange() {
    assertEquals("(C_MNTH in [2, 3))", RulesIO.encodePredicate(p2));
    assertEquals("(C_WDAY in [5, 6))", RulesIO.encodePredicate(p3));
    assertEquals("(C_HOUR in [16, 18))", RulesIO.encodePredicate(c1));
  }

  @Test
  public void testEncodeRule() {
    assertEquals("{(C_MNTH in [2, 3)),(C_TRAF = 2)} -> {(C_HOUR in [16, 18))},0.8", RulesIO.encodeRule(r1));
    assertEquals("{(C_WDAY in [5, 6))} -> {(C_WTHR = 4)},0.9", RulesIO.encodeRule(r2));
  }

  @Test
  public void testDecodePredicateValue() {
    assertEquals(RulesIO.decodePredicate("(C_TRAF = 2)"), p1);
    assertEquals(RulesIO.decodePredicate("C_WTHR = 4)"), c2);
  }

  @Test
  public void testDecodePredicateRange() {
    assertEquals(RulesIO.decodePredicate("(C_MNTH in [2, 3))"), p2);
    assertEquals(RulesIO.decodePredicate("(C_WDAY in [5, 6))"), p3);
    assertEquals(RulesIO.decodePredicate("(C_HOUR in [16, 18))"), c1);
  }

  @Test
  public void testDecodeRule() {
    assertEquals(RulesIO.decodeRule("{(C_MNTH in [2, 3)),(C_TRAF = 2)} -> {(C_HOUR in [16, 18))},0.8"), r1);
    assertEquals(RulesIO.decodeRule("{(C_WDAY in [5, 6))} -> {(C_WTHR = 4)},0.9"), r2);
  }

  @Test
  public void testWriteRules() {
    RulesIO.writeRules(r, "./test/testfile2.txt");

    assertTrue(equalFiles("./test/testfile1.txt", "./test/testfile2.txt"));
  }

  @Test
  public void testReadRules() {
    ArrayList<Rule> rr = RulesIO.readRules("./test/testfile1.txt");

    assertEquals(r.size(), rr.size());
    for(int i = 0; i < rr.size(); i++) {
      assertEquals(r.get(i), rr.get(i));
    }
  }



  private static boolean equalFiles(String expectedFileName, String resultFileName) {
    boolean equal;
    BufferedReader bExp;
    BufferedReader bRes;
    String expLine;
    String resLine;

    equal = false;
    bExp = null ;
    bRes = null ;

    try {
      bExp = new BufferedReader(new FileReader(expectedFileName));
      bRes = new BufferedReader(new FileReader(resultFileName));

      if ((bExp != null) && (bRes != null)) {
        expLine = bExp.readLine();
        resLine = bRes.readLine();

        equal = ((expLine == null) && (resLine == null)) || ((expLine != null) && expLine.equals(resLine));

        while(equal && expLine != null) {
          expLine = bExp.readLine();
          resLine = bRes.readLine(); 
          equal = ((expLine == null) && (resLine == null)) || ((expLine != null) && expLine.equals(resLine));
        }
      }
    } catch (IOException e) {

    } finally {
        try {
            if (bExp != null) {
                bExp.close();
            }
            if (bRes != null) {
                bRes.close();
            }
        } catch (IOException e) {
        }
    }
    return equal;
  }
}
