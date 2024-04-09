# CPSC544-Project

## Configuration

Configuration for the program are set in a json config file who's path is passed as an argument to the program when running. It takes the format

```
{
  "min_coverage": 0.4,
  "rule_accuracy": 0.95,
  "exclude_external": false,
  "database_path": "data/collision-databse_1999-2019.csv",
  "suntimes_path": "config/suntimes.txt",
  "excluded_rules_path": "config/excluded_rules.txt",
  "rules_out_path": "out/rules.txt"
}
```

A sample one is included in `config/config.json` that can be modified to achieve the settings you want without recompiling the program.

## Building

Apache Ant is the Java build system installed on the university linux servers, so it is what this project was set up to use.

Run `ant build` to compile the program, download dependencies (if required), and build the jar file.

Run `ant clean` to delete the build directory and the built jar file

Run `ant test` to compile the program, download dependencies (if required), build the jar file and run unit tests.

Run `ant resolve` if you only want to download the the dependency jars into the lib directory


## Running

After building, there should be an executable jar file in the root directory of the repository called `CPSC544Project.jar`. You can run it with `java` program the -jar argument.

Since the system uses so much memory, it may be necessary to allocate more memory on the heap. We found that running with 10GB allocated achieved the best result.

So, to run the system with the above arguments, with the config file passed in, execute this command from the root directory:

`java -Xmx10G -jar CPSC544Project.jar config/config.json`