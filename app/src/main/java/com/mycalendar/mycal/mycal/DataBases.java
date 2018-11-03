package com.mycalendar.mycal.mycal;

import android.provider.BaseColumns;

public class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String YEAR = "year";
        public static final String MONTH = "month";
        public static final String DAY = "day";
        public static final String SCHEDULE = "schedule";
        public static final String _TABLENAME = "scheduletable";
        public static final String _CREATE =
                "create table if not exists " + _TABLENAME + "("
                + _ID + " integer primary key autoincrement, "
                + YEAR + " integer not null, "
                + MONTH + " integer not null, "
                + DAY + " integer not null, "
                + SCHEDULE + " text not null );";
    }
}
