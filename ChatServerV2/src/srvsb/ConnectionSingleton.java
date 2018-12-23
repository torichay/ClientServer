package srvsb;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {
    private static Connection instance = null;
    private ConnectionSingleton(){}
    public static Connection getInstance(){
        try {
            Driver driver = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            DriverManager.registerDriver(driver);

            instance = DriverManager.getConnection("jdbc:oracle:thin:@sql.edu-netcracker.com:1251:XE",
                    "TLT_16", "TLT_16");
            return instance;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
