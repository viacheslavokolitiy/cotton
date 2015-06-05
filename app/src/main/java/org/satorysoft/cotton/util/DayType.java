package org.satorysoft.cotton.util;

/**
 * Created by viacheslavokolitiy on 05.06.2015.
 */
public enum DayType {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    int type;

    DayType(int type){
        this.type = type;
    }

    public static DayType getDayType(int day){
        switch (day){
            case 1:
                return SUNDAY;
            case 2:
                return MONDAY;
            case 3:
                return TUESDAY;
            case 4:
                return WEDNESDAY;
            case 5:
                return THURSDAY;
            case 6:
                return FRIDAY;
            case 7:
                return SATURDAY;

        }

        return null;
    }
}
