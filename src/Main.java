import parsing.CollisionEntryParser;
import rules.Rule;
import parsing.CollisionEntry;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        //CollisionEntryParser.Parse_CSV("collision-databse_1999-2019.csv");
        System.out.println("Making empty rule");
        Rule test = new Rule();
        
        CollisionEntry.C_WDAY daynum = CollisionEntry.C_WDAY.UNKNOWN;
        CollisionEntry.C_WDAY[] dayarray = CollisionEntry.C_WDAY.values();
        
        int unknowndex = CollisionEntry.C_WDAY.UNKNOWN.ordinal();
        
        
        for (int i = 0; i < dayarray.length; i++)
        {
                if (dayarray[i] == daynum)
                {
                    System.out.println("that's unknown, alright..");
                }
                else
                {
                    System.out.println(dayarray[i].toString());
                }
                if (i == unknowndex)
                {
                    System.out.println("test test");
                    if (daynum.ordinal() == unknowndex)
                    {
                        System.out.println("all as expected..");
                    }
                }
        }
    }
}
