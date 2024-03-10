package parsing;

import java.util.ArrayList;

import parsing.CollisionEntry.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CollisionEntryParser {

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
                C_WDAY weekday = C_WDAY.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[2])) {
                        case 1:
                            weekday = C_WDAY.MONDAY;
                            break;
                        case 2:
                            weekday = C_WDAY.TUESDAY;
                            break;
                        case 3:
                            weekday = C_WDAY.WEDNESDAY;
                            break;
                        case 4:
                            weekday = C_WDAY.THURSDAY;
                            break;
                        case 5:
                            weekday = C_WDAY.FRIDAY;
                            break;
                        case 6:
                            weekday = C_WDAY.SATURDAY;
                            break;
                        case 7:
                            weekday = C_WDAY.SUNDAY;
                            break;
                    }
                } catch (NumberFormatException e) {
                    if (splitEntry[2].charAt(0) == 'U') {
                        weekday = C_WDAY.UNKNOWN;
                    } else {
                        weekday = C_WDAY.NOT_PROVIDED;
                    }
                }

                // C_HOUR
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

                // C_SEV
                C_SEV severity = C_SEV.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[4])) {
                        case 1:
                            severity = C_SEV.FATAL_INJURY;
                            break;
                        case 2:
                            severity = C_SEV.NON_FATAL_INJURY;
                            break;
                    }
                } catch (NumberFormatException e) {
                    if (splitEntry[4].charAt(0) == 'U') {
                        severity = C_SEV.UNKNOWN;
                    } else {
                        severity = C_SEV.NOT_PROVIDED;
                    }
                }

                // C_VEHS
                byte v_count = 0;
                try {
                    v_count = Byte.parseByte(splitEntry[5]);
                } catch (NumberFormatException e) {
                    if (splitEntry[5].charAt(0) == 'U') {
                        v_count = -1;
                    } else {
                        v_count = -2;
                    }
                }

                // C_CONF
                C_CONF v_config = C_CONF.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[6])) {
                        case 1:
                            v_config = C_CONF.SVM_HIT_MOVING_OBJECT;
                            break;
                        case 2:
                            v_config = C_CONF.SVM_HIT_STATIONARY_OBJECT;
                            break;
                        case 3:
                            v_config = C_CONF.SVM_OFF_LEFT_SHOULDER;
                            break;
                        case 4:
                            v_config = C_CONF.SVM_OFF_RIGHT_SHOULDER;
                            break;
                        case 5:
                            v_config = C_CONF.SVM_ROLLOVER;
                            break;
                        case 6:
                            v_config = C_CONF.SVM_OTHER;
                            break;
                        case 21:
                            v_config = C_CONF.TVM_SDT_REAR_END;
                            break;
                        case 22:
                            v_config = C_CONF.TVM_SDT_SIDE_SWIPE;
                            break;
                        case 23:
                            v_config = C_CONF.TVM_SDT_LEFT_TURN_CONFLICT;
                            break;
                        case 24:
                            v_config = C_CONF.TVM_SDT_RIGHT_TURN_CONFLICT;
                            break;
                        case 25:
                            v_config = C_CONF.TVM_SDT_OTHER;
                            break;
                        case 31:
                            v_config = C_CONF.TVM_DDT_HEAD_ON;
                            break;
                        case 32:
                            v_config = C_CONF.TVM_DDT_SIDE_SWIPE;
                            break;
                        case 33:
                            v_config = C_CONF.TVM_DDT_LEFT_TURN;
                            break;
                        case 34:
                            v_config = C_CONF.TVM_DDT_RIGHT_TURN;
                            break;
                        case 35:
                            v_config = C_CONF.TVM_DDT_RIGHT_ANGLE_COLLISION;
                            break;
                        case 36:
                            v_config = C_CONF.TVM_DDT_OTHER;
                            break;
                        case 41:
                            v_config = C_CONF.TV_HIT_PARKED_VEHICLE;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[6].charAt(0)) {
                        case 'Q':
                            v_config = C_CONF.OTHER;
                            break;
                        case 'U':
                            v_config = C_CONF.UNKNOWN;
                            break;
                        case 'X':
                            v_config = C_CONF.NOT_PROVIDED;
                            break;
                    }
                }

                // C_RGFG
                C_RCFG road_config = C_RCFG.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[7])) {
                        case 1:
                            road_config = C_RCFG.NON_INTERSECTION;
                            break;
                        case 2:
                            road_config = C_RCFG.TWO_PUBLIC_ROADWAY_INTERSECTION;
                            break;
                        case 3:
                            road_config = C_RCFG.OTHER_INTERSECTION;
                            break;
                        case 4:
                            road_config = C_RCFG.RAILROAD_CROSSING;
                            break;
                        case 5:
                            road_config = C_RCFG.BRIDGE_OVERPASS_VIADUCT;
                            break;
                        case 6:
                            road_config = C_RCFG.TUNNEL_UNDERPASS;
                            break;
                        case 7:
                            road_config = C_RCFG.PASSING_OR_CLIMBING_LANE;
                            break;
                        case 8:
                            road_config = C_RCFG.RAMP;
                            break;
                        case 9:
                            road_config = C_RCFG.TRAFFIC_CIRCLE;
                            break;
                        case 10:
                            road_config = C_RCFG.FREEWAY_EXPRESS_LANE;
                            break;
                        case 11:
                            road_config = C_RCFG.FREEWAY_COLLECTOR_LANE;
                            break;
                        case 12:
                            road_config = C_RCFG.FREEWAY_TRANSFER_LANE;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[7].charAt(0)) {
                        case 'Q':
                            road_config = C_RCFG.OTHER;
                            break;
                        case 'U':
                            road_config = C_RCFG.UNKNOWN;
                            break;
                        case 'X':
                            road_config = C_RCFG.NOT_PROVIDED;
                            break;
                    }
                }

                // C_WTHR
                C_WTHR weather = C_WTHR.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[8])) {
                        case 1:
                            weather = C_WTHR.CLEAR_SUNNY;
                            break;
                        case 2:
                            weather = C_WTHR.OVERCAST_CLOUDY;
                            break;
                        case 3:
                            weather = C_WTHR.RAINING;
                            break;
                        case 4:
                            weather = C_WTHR.SNOWING;
                            break;
                        case 5:
                            weather = C_WTHR.FREEZING_RAIN_SLEET_HAIL;
                            break;
                        case 6:
                            weather = C_WTHR.VISIBILITY_LIMITATION;
                            break;
                        case 7:
                            weather = C_WTHR.STRONG_WIND;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[8].charAt(0)) {
                        case 'Q':
                            weather = C_WTHR.OTHER;
                            break;
                        case 'U':
                            weather = C_WTHR.UNKNOWN;
                            break;
                        case 'X':
                            weather = C_WTHR.NOT_PROVIDED;
                            break;
                    }
                }

                // C_RSUR
                C_RSUR road_surface = C_RSUR.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[9])) {
                        case 1:
                            road_surface = C_RSUR.NORMAL;
                            break;
                        case 2:
                            road_surface = C_RSUR.WET;
                            break;
                        case 3:
                            road_surface = C_RSUR.FRESH_SNOW;
                            break;
                        case 4:
                            road_surface = C_RSUR.WET_SNOW_SLUSH;
                            break;
                        case 5:
                            road_surface = C_RSUR.ICY;
                            break;
                        case 6:
                            road_surface = C_RSUR.SAND_GRAVEL_DIRT;
                            break;
                        case 7:
                            road_surface = C_RSUR.MUDDY;
                            break;
                        case 8:
                            road_surface = C_RSUR.OIL;
                            break;
                        case 9:
                            road_surface = C_RSUR.FLOODED;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[9].charAt(0)) {
                        case 'Q':
                            road_surface = C_RSUR.OTHER;
                            break;
                        case 'U':
                            road_surface = C_RSUR.UNKNOWN;
                            break;
                        case 'X':
                            road_surface = C_RSUR.NOT_PROVIDED;
                            break;
                    }
                }

                // C_RALN
                C_RALN raln = C_RALN.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[10])) {
                        case 1:
                            raln = C_RALN.STRAIGHT_LEVEL;
                            break;
                        case 2:
                            raln = C_RALN.STRAIGHT_GRADIENT;
                            break;
                        case 3:
                            raln = C_RALN.CURVED_LEVEL;
                            break;
                        case 4:
                            raln = C_RALN.CURVED_GRADIENT;
                            break;
                        case 5:
                            raln = C_RALN.TOP_OF_HILL_OR_GRADIENT;
                            break;
                        case 6:
                            raln = C_RALN.BOTTOM_OF_HILL_OR_GRADIENT;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[10].charAt(0)) {
                        case 'Q':
                            raln = C_RALN.OTHER;
                            break;
                        case 'U':
                            raln = C_RALN.UNKNOWN;
                            break;
                        case 'X':
                            raln = C_RALN.NOT_PROVIDED;
                            break;
                    }
                }

                // C_TRAF
                C_TRAF traffic_control = C_TRAF.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[11])) {
                        case 1:
                            traffic_control = C_TRAF.SIGNALS_OPERATIONAL;
                            break;
                        case 2:
                            traffic_control = C_TRAF.SIGNALS_FLASHING;
                            break;
                        case 3:
                            traffic_control = C_TRAF.STOP_SIGN;
                            break;
                        case 4:
                            traffic_control = C_TRAF.YIELD_SIGN;
                            break;
                        case 5:
                            traffic_control = C_TRAF.WARNING_SIGN;
                            break;
                        case 6:
                            traffic_control = C_TRAF.PEDESTRIAN_CROSSWALK;
                            break;
                        case 7:
                            traffic_control = C_TRAF.POLICE_OFFICER;
                            break;
                        case 8:
                            traffic_control = C_TRAF.SCHOOL_GUARD;
                            break;
                        case 9:
                            traffic_control = C_TRAF.SCHOOL_CROSSING;
                            break;
                        case 10:
                            traffic_control = C_TRAF.REDUCED_SPEED_ZONE;
                            break;
                        case 11:
                            traffic_control = C_TRAF.NO_PASSING_ZONE_SIGN;
                            break;
                        case 12:
                            traffic_control = C_TRAF.ROAD_MARKINGS;
                            break;
                        case 13:
                            traffic_control = C_TRAF.STOPPED_SCHOOL_BUS_LIGHT_FLASHING;
                            break;
                        case 14:
                            traffic_control = C_TRAF.STOPPED_SCHOOL_BUS_NO_FLASHING;
                            break;
                        case 15:
                            traffic_control = C_TRAF.RAILWAY_CROSSING_SIGNALS;
                            break;
                        case 16:
                            traffic_control = C_TRAF.RAILWAY_CROSSING_ONLY_SIGNS;
                            break;
                        case 17:
                            traffic_control = C_TRAF.NOT_SPECIFIED;
                            break;
                        case 18:
                            traffic_control = C_TRAF.NO_CONTROL;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[11].charAt(0)) {
                        case 'Q':
                            traffic_control = C_TRAF.OTHER;
                            break;
                        case 'U':
                            traffic_control = C_TRAF.UNKNOWN;
                            break;
                        case 'X':
                            traffic_control = C_TRAF.NOT_PROVIDED;
                            break;
                    }
                }

                // V_ID
                byte v_seq_num;
                try {
                    v_seq_num = Byte.parseByte(splitEntry[12]);
                } catch (NumberFormatException e) {
                    v_seq_num = -1;
                }

                // V_TYPE
                V_TYPE v_type = V_TYPE.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[13])) {
                        case 1:
                            v_type = V_TYPE.LIGHT_DUTY_VEHICLE;
                            break;
                        case 5:
                            v_type = V_TYPE.PANEL_CARGO_VAN;
                            break;
                        case 6:
                            v_type = V_TYPE.OTHER_TRUCK_VAN;
                            break;
                        case 7:
                            v_type = V_TYPE.HEAVY_UNIT_TRUCK;
                            break;
                        case 8:
                            v_type = V_TYPE.ROAD_TRACTOR;
                            break;
                        case 9:
                            v_type = V_TYPE.SCHOOL_BUS;
                            break;
                        case 10:
                            v_type = V_TYPE.SMALL_SCHOOL_BUS;
                            break;
                        case 11:
                            v_type = V_TYPE.CITY_BUS;
                            break;
                        case 14:
                            v_type = V_TYPE.MOTORCYCLE_MOPED;
                            break;
                        case 16:
                            v_type = V_TYPE.OFF_ROAD_VEHICLES;
                            break;
                        case 17:
                            v_type = V_TYPE.BICYCLE;
                            break;
                        case 18:
                            v_type = V_TYPE.MOTOR_HOME;
                            break;
                        case 19:
                            v_type = V_TYPE.FARM_EQUIPMENT;
                            break;
                        case 20:
                            v_type = V_TYPE.CONSTRUCTION_EQUIPMENT;
                            break;
                        case 21:
                            v_type = V_TYPE.FIRE_ENGINE;
                            break;
                        case 22:
                            v_type = V_TYPE.SNOWMOBILE;
                            break;
                        case 23:
                            v_type = V_TYPE.STREETCAR;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[13].charAt(0)) {
                        case 'N':
                            v_type = V_TYPE.NOT_APPLICABLE;
                            break;
                        case 'Q':
                            v_type = V_TYPE.OTHER;
                            break;
                        case 'U':
                            v_type = V_TYPE.UNKNOWN;
                            break;
                        case 'X':
                            v_type = V_TYPE.NOT_PROVIDED;
                            break;
                    }
                }

                // V_YEAR
                short model_year = -1;
                try {
                    model_year = Short.parseShort(splitEntry[14]);
                } catch (NumberFormatException e) {
                    switch (splitEntry[14].charAt(0)) {
                        case 'N':
                            model_year = -4;
                            break;
                        case 'U':
                            model_year = -1;
                            break;
                        case 'X':
                            model_year = -2;
                            break;
                    }
                }

                // P_ID
                byte person_id = -1;
                try {
                    person_id = Byte.parseByte(splitEntry[15]);
                } catch (NumberFormatException e) {
                    switch (splitEntry[15].charAt(0)) {
                        case 'N':
                            person_id = -4;
                            break;
                        case 'U':
                            person_id = -1;
                            break;
                    }
                }

                // P_SEX
                P_SEX sex = P_SEX.UNKNOWN;
                switch (splitEntry[16].charAt(0)) {
                    case 'F':
                        sex = P_SEX.FEMALE;
                        break;
                    case 'M':
                        sex = P_SEX.MALE;
                        break;
                    case 'N':
                        sex = P_SEX.NOT_APPLICABLE;
                        break;
                    case 'U':
                        sex = P_SEX.UNKNOWN;
                        break;
                    case 'X':
                        sex = P_SEX.NOT_PROVIDED;
                        break;
                }

                // P_AGE
                byte age = -1;
                try {
                    age = Byte.parseByte(splitEntry[17]);
                } catch (NumberFormatException e) {
                    switch (splitEntry[17].charAt(0)) {
                        case 'N':
                            age = -4;
                            break;
                        case 'U':
                            age = -1;
                            break;
                        case 'X':
                            age = -2;
                            break;
                    }
                }

                // P_PSN
                P_PSN p_position = P_PSN.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[18])) {
                        case 11:
                            p_position = P_PSN.DRIVER;
                            break;
                        case 12:
                            p_position = P_PSN.FRONT_CENTER;
                            break;
                        case 13:
                            p_position = P_PSN.FRONT_RIGHT;
                            break;
                        case 21:
                            p_position = P_PSN.SECOND_LEFT;
                            break;
                        case 22:
                            p_position = P_PSN.SECOND_CENTER;
                            break;
                        case 23:
                            p_position = P_PSN.SECOND_RIGHT;
                            break;
                        case 31:
                            p_position = P_PSN.THIRD_LEFT;
                            break;
                        case 32:
                            p_position = P_PSN.THIRD_CENTER;
                            break;
                        case 33:
                            p_position = P_PSN.THIRD_RIGHT;
                            break;
                        case 41:
                            p_position = P_PSN.FOURTH_LEFT;
                            break;
                        case 42:
                            p_position = P_PSN.FOURTH_CENTER;
                            break;
                        case 43:
                            p_position = P_PSN.FOURTH_RIGHT;
                            break;
                        case 51:
                            p_position = P_PSN.FIFTH_LEFT;
                            break;
                        case 52:
                            p_position = P_PSN.FIFTH_CENTER;
                            break;
                        case 53:
                            p_position = P_PSN.FIFTH_RIGHT;
                            break;
                        case 61:
                            p_position = P_PSN.SIXTH_LEFT;
                            break;
                        case 62:
                            p_position = P_PSN.SIXTH_CENTER;
                            break;
                        case 63:
                            p_position = P_PSN.SIXTH_RIGHT;
                            break;
                        case 71:
                            p_position = P_PSN.SEVENTH_LEFT;
                            break;
                        case 72:
                            p_position = P_PSN.SEVENTH_CENTER;
                            break;
                        case 73:
                            p_position = P_PSN.SEVENTH_RIGHT;
                            break;
                        case 81:
                            p_position = P_PSN.EIGHTH_LEFT;
                            break;
                        case 82:
                            p_position = P_PSN.EIGHTH_CENTER;
                            break;
                        case 83:
                            p_position = P_PSN.EIGHTH_RIGHT;
                            break;
                        case 91:
                            p_position = P_PSN.NINTH_LEFT;
                            break;
                        case 92:
                            p_position = P_PSN.NINTH_CENTER;
                            break;
                        case 93:
                            p_position = P_PSN.NINTH_RIGHT;
                            break;
                        case 96:
                            p_position = P_PSN.UNKNOWN_BUT_DEFINITELY_OCCUPANT;
                            break;
                        case 97:
                            p_position = P_PSN.ON_LAP;
                            break;
                        case 98:
                            p_position = P_PSN.OUTSIDE_PASSENGER_COMPARTMENT;
                            break;
                        case 99:
                            p_position = P_PSN.PEDESTRIAN;
                            break;

                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[18].charAt(0)) {
                        case 'N':
                            p_position = P_PSN.NOT_APPLICABLE;
                            break;
                        case 'Q':
                            p_position = P_PSN.OTHER;
                            break;
                        case 'U':
                            p_position = P_PSN.UNKNOWN;
                            break;
                        case 'X':
                            p_position = P_PSN.NOT_PROVIDED;
                            break;
                    }
                }

                // P_ISEV
                P_ISEV p_injury_sev = P_ISEV.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[19])) {
                        case 1:
                            p_injury_sev = P_ISEV.NO_INJURY;
                            break;
                        case 2:
                            p_injury_sev = P_ISEV.INJURY;
                            break;
                        case 3:
                            p_injury_sev = P_ISEV.FATALITY;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[19].charAt(0)) {
                        case 'N':
                            p_injury_sev = P_ISEV.NOT_APPLICABLE;
                            break;
                        case 'U':
                            p_injury_sev = P_ISEV.UNKNOWN;
                            break;
                        case 'X':
                            p_injury_sev = P_ISEV.NOT_PROVIDED;
                            break;
                    }
                }

                // P_SAFE
                P_SAFE p_safety_device = P_SAFE.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[20])) {
                        case 1:
                            p_safety_device = P_SAFE.NO_SAFETY_DEVICE_OR_CHILD_RESTRAINT;
                            break;
                        case 2:
                            p_safety_device = P_SAFE.SAFETY_DEVICE_OR_CHILD_RESTRAINT;
                            break;
                        case 9:
                            p_safety_device = P_SAFE.HELMET;
                            break;
                        case 10:
                            p_safety_device = P_SAFE.REFLECTIVE_CLOTHING;
                            break;
                        case 11:
                            p_safety_device = P_SAFE.HELMET_AND_REFLECTIVE_CLOTHING;
                            break;
                        case 12:
                            p_safety_device = P_SAFE.OTHER_SAFETY_DEVICE;
                            break;
                        case 13:
                            p_safety_device = P_SAFE.NO_SAFETY_DEVICE_EQUIPPED;
                            break;
                    }
                } catch (NumberFormatException e) {
                    switch (splitEntry[20].charAt(0)) {
                        case 'N':
                            p_safety_device = P_SAFE.NOT_APPLICABLE;
                            break;
                        case 'Q':
                            p_safety_device = P_SAFE.OTHER;
                            break;
                        case 'U':
                            p_safety_device = P_SAFE.UNKNOWN;
                            break;
                        case 'X':
                            p_safety_device = P_SAFE.NOT_PROVIDED;
                            break;
                    }
                }

                // P_USER
                P_USER p_user = P_USER.UNKNOWN;
                try {
                    switch (Byte.parseByte(splitEntry[21])) {
                        case 1:
                            p_user = P_USER.MOTOR_VEHICLE_DRIVER;
                            break;
                        case 2:
                            p_user = P_USER.MOTOR_VEHICLE_PASSENGER;
                            break;
                        case 3:
                            p_user = P_USER.PEDESTRIAN;
                            break;
                        case 4:
                            p_user = P_USER.BICYCLIST;
                            break;
                        case 5:
                            p_user = P_USER.MOTORCYCLIST;
                            break;
                    }
                } catch (NumberFormatException e) {
                    p_user = P_USER.UNKNOWN;
                }

                //C_CASE
                int case_number = Integer.parseInt(splitEntry[22]);

                // System.out.println(year);
                // System.out.println(month);
                // System.out.println(weekday);
                // System.out.println(hour);
                // System.out.println(severity);
                // System.out.println(v_count);
                // System.out.println(v_config);
                // System.out.println(road_config);
                // System.out.println(weather);
                // System.out.println(road_surface);
                // System.out.println(raln);
                // System.out.println(traffic_control);
                // System.out.println(v_seq_num);
                // System.out.println(v_type);
                // System.out.println(model_year);
                // System.out.println(person_id);
                // System.out.println(sex);
                // System.out.println(age);
                // System.out.println(p_position);
                // System.out.println(p_injury_sev);
                // System.out.println(p_safety_device);
                // System.out.println(p_user);
                // System.out.println(case_number);
                
                CollisionEntry tempEntry = new CollisionEntry(year, month, weekday, hour, severity, v_count, v_config, road_config, weather, road_surface, raln, traffic_control, v_seq_num, v_type, model_year, person_id, sex, age, p_position, p_injury_sev, p_safety_device, p_user, case_number);
                returnList.add(tempEntry);
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
        System.out.printf("Parsed %d entries!\n", returnList.size());
        return returnList;
    }
}
