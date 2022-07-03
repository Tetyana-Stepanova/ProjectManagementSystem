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

//    private static Connection conn;
//
//    public static Connection getConn() {
//        try {
//            MysqlDataSource dataSource = new MysqlDataSource();
//            dataSource.setURL(StorageConstants.CONNECTION_URL);
//            dataSource.setUser(StorageConstants.USERNAME);
//            dataSource.setPassword(StorageConstants.PASSWORD);
//            conn = dataSource.getConnection();
//
//        } catch (SQLException e) {
//            System.out.println("Cannot get connection: " + e);
//        }
//        return conn;
//    }

    public void initDb() {
//       createFromFile("C:\\Users\\Yura\\Desktop\\GoIT\\ProjectManagementSystem\\src\\main\\resources\\db\\migration\\V1__initDB.sql");
//       createFromFile("C:\\Users\\Yura\\Desktop\\GoIT\\ProjectManagementSystem\\src\\main\\resources\\db\\migration\\V2__populateDB.sql");
//
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(StorageConstants.CONNECTION_URL);
        dataSource.setUser(StorageConstants.USERNAME);
        dataSource.setPassword(StorageConstants.PASSWORD);

        Flyway flyway = Flyway
                .configure()
                .dataSource(dataSource)
//                .locations("filesystem:resources/db/migration/V1__initDB.sql")
                .load();

        flyway.migrate();
    }

//    private void createFromFile(String fileUrl){
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    Statement stmt = getConn().createStatement() ;
//                    int i = 0 ;
//                    for(i=1;i<=57;i++) {
//                        FileReader fr = new FileReader(fileUrl) ;
//                        BufferedReader br = new BufferedReader(fr) ;
//                        stmt.execute(br.readLine()) ;
//                    }
//                    stmt.close();
//                    conn.close();
//                    JOptionPane.showMessageDialog(null, " Records Successfully Inserted into database !", "Success !", 1) ;
//                } catch(Exception e) {
//                    JOptionPane.showMessageDialog(null, e, "ERROR", JOptionPane.ERROR_MESSAGE) ;
//                }
//            }
//        });
//    }
}
