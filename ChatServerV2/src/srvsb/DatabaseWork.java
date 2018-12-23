package srvsb;

import java.sql.*;
import java.util.UUID;

public class DatabaseWork {

    private Connection connection;

    public DatabaseWork(Connection connection) {
        this.connection = connection;
    }

    public String selectMessage() throws SQLException {
        String mes = "";
        String query = "SELECT * FROM messages";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            String timeMessage = resultSet.getString("timemes");
            String textMessage = resultSet.getString("textmes");
            //System.out.println(String.format("%s: %s", timeMessage, textMessage));
            mes = timeMessage + ": " + textMessage;
        }
        return mes;
    }

    public String selectHistory() throws SQLException {
        String history = "";
        String query = "SELECT * FROM messages";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        while (resultSet.next()) {
            String timeMessage = resultSet.getString("timemes");
            String textMessage = resultSet.getString("textmes");
            //System.out.println(String.format("%s: %s", timeMessage, textMessage));
            history += timeMessage + ": " + textMessage + "\n";
        }
        return history;
    }

    public void insertMessage(String time, String text) throws SQLException {
        String query = "INSERT INTO messages (timemes, textmes) VALUES (?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, time);
        preparedStatement.setString(2, text);

        preparedStatement.execute();
    }
}
