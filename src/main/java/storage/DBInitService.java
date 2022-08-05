package storage;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitService {

    public void initDb() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(StorageConstants.CONNECTION_URL);
        dataSource.setUser(StorageConstants.USERNAME);
        dataSource.setPassword(StorageConstants.PASSWORD);

        Flyway flyway = Flyway
                .configure()
                .dataSource(dataSource)
                .load();

        flyway.migrate();
    }
}
