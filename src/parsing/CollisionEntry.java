package parsing;

public class CollisionEntry {
    
    public enum C_WDAY {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_SEV {
        FATAL_INJURY,
        NON_FATAL_INJURY,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_CONF {
        SVM_HIT_MOVING_OBJECT,
        SVM_HIT_STATIONARY_OBJECT,
        SVM_OFF_LEFT_SHOULDER,
        SVM_OFF_RIGHT_SHOULDER,
        SVM_ROLLOVER,
        SVM_OTHER,

        TVM_SDT_REAR_END,
        TVM_SDT_SIDE_SWIPE,
        TVM_SDT_LEFT_TURN_CONFLICT,
        TVM_SDT_RIGHT_TURN_CONFLICT,
        TVM_SDT_OTHER,

        TVM_DDT_HEAD_ON, //ouch
        TVM_DDT_SIDE_SWIPE,
        TVM_DDT_LEFT_TURN,
        TVM_DDT_RIGHT_TURN,
        TVM_DDT_RIGHT_ANGLE_COLLISION,
        TVM_DDT_OTHER,

        TV_HIT_PARKED_VEHICLE,

        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_RCFG {
        NON_INTERSECTION,
        TWO_PUBLIC_ROADWAY_INTERSECTION,
        OTHER_INTERSECTION,
        RAILROAD_CROSSING,
        BRIDGE_OVERPASS_VIADUCT,
        TUNNEL_UNDERPASS,
        PASSING_OR_CLIMBING_LANE,
        RAMP,
        TRAFFIC_CIRCLE,
        FREEWAY_EXPRESS_LANE,
        FREEWAY_COLLECTOR_LANE,
        FREEWAY_TRANSFER_LANE,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_WTHR {
        CLEAR_SUNNY,
        OVERCAST_CLOUDY,
        RAINING,
        SNOWING,
        FREEZING_RAIN_SLEET_HAIL,
        VISIBILITY_LIMITATION,
        STRONG_WIND,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_RSUR {
        NORMAL,
        WET,
        FRESH_SNOW,
        WET_SNOW_SLUSH,
        ICY,
        SAND_GRAVEL_DIRT,
        MUDDY,
        OIL,
        FLOODED,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_RALN {
        STRAIGHT_LEVEL,
        STRAIGHT_GRADIENT,
        CURVED_LEVEL,
        CURVED_GRADIENT,
        TOP_OF_HILL_OR_GRADIENT,
        BOTTOM_OF_HILL_OR_GRADIENT,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum C_TRAF {
        SIGNALS_OPERATIONAL,
        SIGNALS_FLASHING,
        STOP_SIGN,
        YIELD_SIGN,
        WARNING_SIGN,
        PEDESTRIAN_CROSSWALK,
        POLICE_OFFICER,
        SCHOOL_GUARD,
        SCHOOL_CROSSING,
        REDUCED_SPEED_ZONE,
        NO_PASSING_ZONE_SIGN,
        ROAD_MARKINGS,
        STOPPED_SCHOOL_BUS_LIGHT_FLASHING,
        STOPPED_SCHOOL_BUS_NO_FLASHING,
        RAILWAY_CROSSING_SIGNALS,
        RAILWAY_CROSSING_ONLY_SIGNS,
        NOT_SPECIFIED,
        NO_CONTROL,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum V_TYPE {
        LIGHT_DUTY_VEHICLE,
        PANEL_CARGO_VAN,
        OTHER_TRUCK_VAN,
        HEAVY_UNIT_TRUCK,
        ROAD_TRACTOR,
        SCHOOL_BUS,
        SMALL_SCHOOL_BUS,
        CITY_BUS,
        MOTORCYCLE_MOPED,
        OFF_ROAD_VEHICLES,
        BICYCLE,
        MOTOR_HOME,
        FARM_EQUIPMENT,
        CONSTRUCTION_EQUIPMENT,
        FIRE_ENGINE,
        SNOWMOBILE,
        STREETCAR,
        NOT_APPLICABLE,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum P_SEX {
        FEMALE,
        MALE,
        NOT_APPLICABLE,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum P_PSN {
        DRIVER,
        FRONT_CENTER,
        FRONT_RIGHT,
        SECOND_LEFT,
        SECOND_CENTER,
        SECOND_RIGHT,
        THIRD_LEFT,
        THIRD_CENTER,
        THIRD_RIGHT,
        FOURTH_LEFT,
        FOURTH_CENTER,
        FOURTH_RIGHT,
        FIFTH_LEFT,
        FIFTH_CENTER,
        FIFTH_RIGHT,
        SIXTH_LEFT,
        SIXTH_CENTER,
        SIXTH_RIGHT,
        SEVENTH_LEFT,
        SEVENTH_CENTER,
        SEVENTH_RIGHT,
        EIGHTH_LEFT,
        EIGHTH_CENTER,
        EIGHTH_RIGHT,
        NINTH_LEFT,
        NINTH_CENTER,
        NINTH_RIGHT,
        UNKNOWN_BUT_DEFINITELY_OCCUPANT,
        ON_LAP,
        OUTSIDE_PASSENGER_COMPARTMENT,
        PEDESTRIAN,
        NOT_APPLICABLE,
        OTHER,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum P_ISEV {
        NO_INJURY,
        INJURY,
        FATALITY,
        NOT_APPLICABLE,
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum P_SAFE {
        NO_SAFETY_DEVICE_OR_CHILD_RESTRAINT,
        SAFETY_DEVICE_OR_CHILD_RESTRAINT,
        HELMET,
        REFLECTIVE_CLOTHING,
        HELMET_AND_REFLECTIVE_CLOTHING,
        OTHER_SAFETY_DEVICE,
        NO_SAFETY_DEVICE_EQUIPPED,
        NOT_APPLICABLE,
        OTHER, //fucking WHY [for when the P_SAFE is other]
        UNKNOWN,
        NOT_PROVIDED;
    }

    public enum P_USER {
        MOTOR_VEHICLE_DRIVER,
        MOTOR_VEHICLE_PASSENGER,
        PEDESTRIAN,
        BICYCLIST,
        MOTORCYCLIST,
        UNKNOWN;
    }

    //For numeric values: -1 = Unknown; -2 = Not Provided; -3 = Other; -4 = Not applicable
    //instead of using ints, i used the smallest numeric type that would fit valid ranges to save memory
    public short YEAR; //C_YEAR
    public byte MONTH; //C_MNTH
    public C_WDAY WEEK_DAY;
    public byte HOUR; //C_HOUR
    public C_SEV SEVERITY;
    public byte VEHICLE_COUNT;//C_VEHS; 99 means 99+ vehicles
    public C_CONF VEHICLE_CONFIGURATION;
    public C_RCFG ROAD_CONFIGURATION;
    public C_WTHR WEATHER;
    public C_RSUR ROAD_SURFACE;
    public C_RALN RALN; //idk what this stands for
    public C_TRAF TRAFFIC_CONTROL;
    public byte VEHICLE_SEQUENCE_NUM; //V_ID; 99 means pedestrians
    public V_TYPE VEHICLE_TYPE;
    public short VEHICLE_MODEL_YEAR; //V_YEAR
    public byte PERSON_ID; //P_ID
    public P_SEX PERSON_SEX;
    public byte PERSON_AGE; //P_AGE; 0 is less than one year old; 99 is 99 or older
    public P_PSN PERSON_POSITION;
    public P_ISEV PERSON_INJURY_SEVERITY; //why does this and C_SEV exist at the same time? who knows!!!!
    public P_SAFE SAFETY_DEVICES;
    public P_USER USER;
    public int CASE_NUMBER; //C_CASE, a column not documented on the data sheet.
    public byte DRIVER_AGE; // Age of the Driver
    

    //auto generated constructor, thats why the argument names look so bad.
    public CollisionEntry(short yEAR, byte mONTH, CollisionEntry.C_WDAY wEEK_DAY, byte hOUR,
            CollisionEntry.C_SEV sEVERITY, byte vEHICLE_COUNT, CollisionEntry.C_CONF vEHICLE_CONFIGURATION,
            CollisionEntry.C_RCFG rOAD_CONFIGURATION, CollisionEntry.C_WTHR wEATHER, CollisionEntry.C_RSUR rOAD_SURFACE,
            CollisionEntry.C_RALN rALN, CollisionEntry.C_TRAF tRAFFIC_CONTROL, byte vEHICLE_SEQUENCE_NUM,
            CollisionEntry.V_TYPE vEHICLE_TYPE, short vEHICLE_MODEL_YEAR, byte pERSON_ID,
            CollisionEntry.P_SEX pERSON_SEX, byte pERSON_AGE, CollisionEntry.P_PSN pERSON_POSITION,
            CollisionEntry.P_ISEV pERSON_INJURY_SEVERITY, CollisionEntry.P_SAFE sAFETY_DEVICES,
            CollisionEntry.P_USER uSER, int cASE_NUMBER) {
        YEAR = yEAR;
        MONTH = mONTH;
        WEEK_DAY = wEEK_DAY;
        HOUR = hOUR;
        SEVERITY = sEVERITY;
        VEHICLE_COUNT = vEHICLE_COUNT;
        VEHICLE_CONFIGURATION = vEHICLE_CONFIGURATION;
        ROAD_CONFIGURATION = rOAD_CONFIGURATION;
        WEATHER = wEATHER;
        ROAD_SURFACE = rOAD_SURFACE;
        RALN = rALN;
        TRAFFIC_CONTROL = tRAFFIC_CONTROL;
        VEHICLE_SEQUENCE_NUM = vEHICLE_SEQUENCE_NUM;
        VEHICLE_TYPE = vEHICLE_TYPE;
        VEHICLE_MODEL_YEAR = vEHICLE_MODEL_YEAR;
        PERSON_ID = pERSON_ID;
        PERSON_SEX = pERSON_SEX;
        PERSON_AGE = pERSON_AGE;
        PERSON_POSITION = pERSON_POSITION;
        PERSON_INJURY_SEVERITY = pERSON_INJURY_SEVERITY;
        SAFETY_DEVICES = sAFETY_DEVICES;
        USER = uSER;
        CASE_NUMBER = cASE_NUMBER;
    }

}
