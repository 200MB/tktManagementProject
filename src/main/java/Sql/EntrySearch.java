package Sql;

import com.mysql.cj.protocol.Resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntrySearch {
    //checks if user exists
    public static boolean exists(String email, String password) {
        try {
            return Initialization.connection.prepareStatement("SELECT * FROM tkt.users where email = \"%s\" and password = \"%S\";".formatted(email, password)).executeQuery().next();
        } catch (SQLException throwables) {
            return false;
        }
    }

    //returns ID of the user (also can be used to determine if user exists)
    public static boolean exists(String email) {
        try {
            return Initialization.connection.prepareStatement("SELECT * FROM tkt.users where email = \"%s\";".formatted(email)).executeQuery().next();
        } catch (SQLException throwables) {
            return false;
        }
    }

    public static int getID(String email) {
        try {
             ResultSet res = Initialization.connection.prepareStatement("SELECT idusers FROM tkt.users where email = \"%s\";".formatted(email)).executeQuery();
             while (res.next()){
                 return res.getInt(1);
             }
        } catch (SQLException throwables) {
            return -1;
        }
        return -1;
    }
}
