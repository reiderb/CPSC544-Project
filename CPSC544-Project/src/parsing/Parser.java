package parsing;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }

    public static ArrayList<CollisionEntry> Parse_CSV(String path) {
        ArrayList<CollisionEntry> returnList = new ArrayList<CollisionEntry>();
        BufferedReader bReader = null;
        String line = "";
        String delimiter = ",";

        try {
            bReader = new BufferedReader(new FileReader(path));
            bReader.readLine(); // skip first line

            while ((line = bReader.readLine()) != null) {
                String[] splitEntry = line.split(delimiter);

                // the hell section (parsing)

                // C_YEAR
                short year = Short.parseShort(splitEntry[0]);

                // C_MNTH
                byte month = 0;
                try {
                    month = Byte.parseByte(splitEntry[1]);
                } catch (NumberFormatException e) {
                    if (splitEntry[1].charAt(0) == 'U') {
                        month = -1;
                    } else {
                        month = -2;
                    }
                }

                // C_WDAY
                CollisionEntry.C_WDAY weekday;
                try {
                    switch (Byte.parseByte(splitEntry[2])) {
                        case 1:
                            weekday = CollisionEntry.C_WDAY.MONDAY;
                            break;
                        case 2:
                            weekday = CollisionEntry.C_WDAY.TUESDAY;
                            break;
                        case 3:
                            weekday = CollisionEntry.C_WDAY.WEDNESDAY;
                            break;
                        case 4:
                            weekday = CollisionEntry.C_WDAY.THURSDAY;
                            break;
                        case 5:
                            weekday = CollisionEntry.C_WDAY.FRIDAY;
                            break;
                        case 6:
                            weekday = CollisionEntry.C_WDAY.SATURDAY;
                            break;
                        case 7:
                            weekday = CollisionEntry.C_WDAY.SUNDAY;
                            break;
                    }
                } catch (NumberFormatException e) {
                    if (splitEntry[2].charAt(0) == 'U') {
                        weekday = CollisionEntry.C_WDAY.UNKNOWN;
                    } else {
                        weekday = CollisionEntry.C_WDAY.NOT_PROVIDED;
                    }
                }

                //C_HOUR
                byte hour = 0;
                try {
                    hour = Byte.parseByte(splitEntry[3]);
                } catch (NumberFormatException e) {
                    if (splitEntry[3].charAt(0) == 'U') {
                        hour = -1;
                    } else {
                        hour = -2;
                    }
                }

                //C_SEV
                CollisionEntry.C_SEV severity;
                try {
                    switch (Byte.parseByte(splitEntry[4])) {
                        case 1:
                            severity = CollisionEntry.C_SEV.FATAL_INJURY;
                            break;
                        case 2:
                            severity = CollisionEntry.C_SEV.NON_FATAL_INJURY;
                            break;
                    }
                } catch (NumberFormatException e) {
                    if (splitEntry[4].charAt(0) == 'U') {
                        severity = CollisionEntry.C_SEV.UNKNOWN;
                    } else {
                        severity = CollisionEntry.C_SEV.NOT_PROVIDED;
                    }
                }

                //C_VEHS

                //TODO Repeat for the rest of the fields, create a CollisionEntry object out of what is parsed and add it to returnList.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return returnList;
    }
}
