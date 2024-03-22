import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import parsing.RulesIO;
import rules.Predicate;
import rules.Rule;
import rules.Predicate.FEATURE;
import rules.Predicate.PRED_TYPE;

public class RulesIOTest {
  HashMap<Integer, Predicate.FEATURE> fMap = RulesIO.getFeatureList(RulesIO.getFeatureListString(Predicate.FEATURE.values()));
  
  @Test
  public void testEncodePredicateValue() {
    Predicate p = new Predicate(FEATURE.C_TRAF, 2);

    assertEquals("(7,0,2)", RulesIO.encodePredicate(p)); ;
  }

  @Test
  public void testEncodePredicateRange() {
    Predicate p = new Predicate(FEATURE.C_MNTH, 2, 3);

    assertEquals("(15,1,2,3)", RulesIO.encodePredicate(p)); ;
  }

  @Test
  public void testEncodeRule() {
    Predicate p1 = new Predicate(FEATURE.C_TRAF, 2);
    Predicate p2 = new Predicate(FEATURE.C_MNTH, 2, 3);
    Predicate c = new Predicate(FEATURE.C_HOUR, 16, 18);
    Rule r = new Rule(new ArrayList<Predicate>(){ {add(p2); add(p1);} }, c, 0.8f);

    assertEquals("{(15,1,2,3),(7,0,2)},(16,1,16,18),0.8", RulesIO.encodeRule(r)); ;
  }

  @Test
  public void testDecodePredicateValue() {
    Predicate p = new Predicate(FEATURE.C_TRAF, 2);
    assertEquals(RulesIO.decodePredicate("(7,0,2)", fMap), p); ;
  }

  @Test
  public void testDecodePredicateRange() {
    Predicate p = new Predicate(FEATURE.C_MNTH, 2, 3);
    assertEquals(RulesIO.decodePredicate("(15,1,2,3)", fMap), p);
  }

  @Test
  public void testDencodeRule() {
    Predicate p1 = new Predicate(FEATURE.C_TRAF, 2);
    Predicate p2 = new Predicate(FEATURE.C_MNTH, 2, 3);
    Predicate c = new Predicate(FEATURE.C_HOUR, 16, 18);
    Rule r = new Rule(new ArrayList<Predicate>(){ {add(p2); add(p1);} }, c, 0.8f);

    Rule r_r = RulesIO.decodeRule("{(15,1,2,3),(7,0,2)},(16,1,16,18),0.8", fMap);

    assertEquals(r_r, r);
  }
}
