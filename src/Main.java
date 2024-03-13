import parsing.CollisionEntryParser;
import rules.Rule;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        CollisionEntryParser.Parse_CSV("collision-databse_1999-2019.csv");
        System.out.println("Making empty rule");
        Rule test = new Rule();
    }
}
