package com.example.lab2.database;

public class DatabaseConnectionFactory {
    private static final String SCHEMA = "/sd-basics";
    private static final String TEST_SCHEMA = "/sd-basics-test";

    public static DbConnection getConnectionWrapper(SupportedDatabase db, boolean test) {
        final String schema = test ? TEST_SCHEMA : SCHEMA;
        switch (db) {
            case MYSQL -> {return new JDBCConnectionWrapper(schema);}
            default -> throw new IllegalArgumentException("Usupported database: " + db);
        }
    }
}
