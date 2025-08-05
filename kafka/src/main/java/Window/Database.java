package Window;

import java.sql.SQLException;
import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static final HikariDataSource datasource;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/kafka_dashboard";
    private static final String DB_USER = "kafka_user";
    private static final String DB_PWD = "kafka_password";

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PWD);

        datasource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
}
