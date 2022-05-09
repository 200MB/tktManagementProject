package Sql;

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
}
