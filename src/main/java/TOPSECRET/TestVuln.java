package TOPSECRET;

import java.sql.Connection;
import java.sql.Statement;

public class TestVuln {
    public void vulnerable(Connection conn, String userInput) throws Exception {
        Statement stmt = conn.createStatement();
        stmt.executeQuery("SELECT * FROM users WHERE name = '" + userInput + "'");
    }
}