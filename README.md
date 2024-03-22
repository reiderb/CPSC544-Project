# CPSC544-Project

## Building

Ensure you have apache ant installed.

Run `ant` to compile the program, download dependencies (if required), and build the jar file.

Run `ant clean` to delete the build directory and the built jar file

Run `ant test` to compile the program, download dependencies (if required), build the jar file and run unit tests.

Run `ant resolve` if you only want to download the the dependency jars into the lib directory

## Adding dependencies

If you find a dependency on the maven central repository that you want to add, go to its page, scroll to the snippets section, change the selection from "Apache Maven" to "ivy", and copy-paste that snippet into the `ivy.xml` file between the `dependencies` tags
