package com.example.lab2.database;

import static com.example.lab2.database.Constants.SCHEMAS.PRODUCTION;
import static com.example.lab2.database.Constants.SCHEMAS.TEST;

public class DatabaseConnectionFactory {

    public static DbConnection getConnectionWrapper(SupportedDatabase db, boolean test) {
        final String schema = test ? TEST : PRODUCTION;
        switch (db) {
            case MYSQL -> {return new JDBCConnectionWrapper(schema);}
            default -> throw new IllegalArgumentException("Usupported database: " + db);
        }
    }
}
