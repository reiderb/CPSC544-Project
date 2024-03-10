import parsing.CollisionEntryParser;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        CollisionEntryParser.Parse_CSV("collision-databse_1999-2019.csv");
    }
}
