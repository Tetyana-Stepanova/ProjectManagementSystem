import cli.CliFSM;
import storage.ConnectionProvider;
import storage.DBInitService;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        new DBInitService().initDb();
        ConnectionProvider connectionProvider = new ConnectionProvider();
        new CliFSM(connectionProvider);
        connectionProvider.close();
    }
}
