import parsing.Parser;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Parser parser = new Parser();
        CommandLineParser cmd_parser = new DefaultParser();
    }
}
