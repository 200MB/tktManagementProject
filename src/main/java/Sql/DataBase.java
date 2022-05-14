package Sql;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
    public static void addUser(String name, String email, String password) throws SQLException {
        Initialization.connection.prepareStatement("INSERT INTO `tkt`.`users` (`email`, `password`) VALUES ('%s', '%s');".formatted(email, password)).execute();
        Initialization.connection.prepareStatement("INSERT INTO `tkt`.`userinfo` (`iduserinfo`, `name`, `balance`) VALUES ('%d', '%s', '%d');".formatted(EntrySearch.getID(email), name, 0)).execute();
        System.out.println("DEBUG: ADDED USER");
    }

    public static void removeUser(String email) throws SQLException {
        Initialization.connection.prepareStatement("DELETE FROM `tkt`.`userinfo` WHERE (`iduserinfo` = '%d');".formatted(EntrySearch.getID(email))).execute();
        Initialization.connection.prepareStatement("DELETE FROM `tkt`.`users` WHERE (`email` = '%s');".formatted(email)).execute();
        System.out.println("DEBUG: REMOVED USER");
    }

    public static boolean hallExists(String name) throws SQLException {
        return Initialization.connection.prepareStatement("SELECT * FROM `tkt`.`moviehalls` WHERE NAME = \"%s\"".formatted(name)).executeQuery().next();
    }

    public static ResultSet getPrices(String id) throws SQLException {
        return Initialization.connection.prepareStatement("SELECT * FROM `tkt`.`prices` WHERE idofmovie = \"%s\"".formatted(id)).executeQuery();
    }

    public static ResultSet getHallInfo(String hallName) {
        try {
            return Initialization.connection.prepareStatement("SELECT * FROM tkt.moviehalls WHERE NAME =  \"%s\";".formatted(hallName)).executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String getHallSeats(String hallName) {
        ResultSet set = getHallInfo(hallName);
        try {
            assert set != null;
            if (set.next()) {
                return set.getString(3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonSeat(String hallName) {
        ResultSet set = getHallInfo(hallName);
        try {
            JSONParser parser = new JSONParser();
            if (set.next()) {
                return (JSONObject) parser.parse(set.getString(4));
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void updateJsonSeat(JSONObject json, String hallName) {
        try {
            PreparedStatement statement = Initialization.connection.prepareStatement("UPDATE tkt.moviehalls SET json = ? WHERE name = ?;");
            statement.setString(1, json.toJSONString());
            statement.setString(2, hallName);
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getUserBalance(String email) {
        int id = EntrySearch.getID(email);
        try {
            ResultSet set = Initialization.connection.prepareStatement("SELECT * FROM `tkt`.`userinfo` where iduserinfo = \"%s\"".formatted(id)).executeQuery();
            if (set.next()) {
                return set.getString(3);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String getUserName(String email) {
        int id = EntrySearch.getID(email);
        try {
            ResultSet set = Initialization.connection.prepareStatement("SELECT * FROM `tkt`.`userinfo` where iduserinfo = \"%s\"".formatted(id)).executeQuery();
            if (set.next()) {
                return set.getString(2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void recordTransaction(String id, String name, String price) {
        try {
            Initialization.connection.prepareStatement("INSERT INTO tkt.tickethistory VALUES(\"%s\",\"%s\",\"%s\")".formatted(id,price,name)).execute();
            System.out.println("DEBUG: UPDATED RECORD");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
