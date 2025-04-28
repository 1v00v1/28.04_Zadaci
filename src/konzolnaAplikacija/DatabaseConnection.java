package konzolnaAplikacija;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DatabaseConnection {

    static javax.sql.DataSource createDataSource() {

        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);

        return ds;

    }
}
