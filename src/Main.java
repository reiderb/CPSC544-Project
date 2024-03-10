import parsing.Parser;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import java.lang.ClassNotFoundException;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Parser parser = new Parser();
        try
        {
            CommandLineParser cmd_parser = new DefaultParser();
        }
        catch(Exception e)
        {
            System.out.println("Some exception thrown");
            e.printStackTrace();
        }
    }
}
