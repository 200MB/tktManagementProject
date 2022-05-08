package Sql;

import java.sql.*;

public class Initialization {
    protected static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(InfoCreds.HOST, InfoCreds.USER, InfoCreds.PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //returns false if table exists
    private static boolean tableCheck(String tableName) throws SQLException {
        PreparedStatement p = connection.prepareStatement("SHOW TABLES LIKE '" + tableName + "';");
        ResultSet res = p.executeQuery();
        return !res.next();
    }


    //Method to automate table creation.
    public static void startup() throws SQLException {
        if (tableCheck("users")) {
            connection.prepareStatement("""
                    CREATE TABLE `tkt`.`users` (
                      `idusers` INT NOT NULL AUTO_INCREMENT,
                      `email` VARCHAR(45) NOT NULL,
                      `password` VARCHAR(45) NOT NULL,
                      PRIMARY KEY (`idusers`, `email`, `password`));
                    """).execute();
            System.out.println("DEBUG: CREATED TABLE USERS");
            //admin user inserted as first with id = 1
            connection.prepareStatement("INSERT INTO `tkt`.`users` (`idusers`, `email`, `password`) VALUES ('0', 'admin', 'admin');\n").execute();
        }
        if (tableCheck("userinfo")) {
            connection.prepareStatement("""
                    CREATE TABLE `tkt`.`userinfo` (
                      `iduserinfo` INT NOT NULL,
                      `name` VARCHAR(45) NOT NULL,
                      `balance` VARCHAR(45) NOT NULL,
                      PRIMARY KEY (`iduserinfo`));
                    """).execute();
            System.out.println("DEBUG: CREATED TABLE USERINFO");
        }
        if (tableCheck("tickethistory")) {
            connection.prepareStatement("""
                    CREATE TABLE `tkt`.`tickethistory` (
                      `date` VARCHAR(45) NOT NULL,
                      `price` INT NOT NULL,
                      `moviename` VARCHAR(45) NULL);
                    """).execute();
            System.out.println("DEBUG: CREATED TABLE TICKETHISTORY");
        }
        if (tableCheck("moviehalls")){
            connection.prepareStatement("""
                    CREATE TABLE `tkt`.`moviehalls` (
                      `idmoviehalls` INT NOT NULL AUTO_INCREMENT,
                      `name` VARCHAR(45) NULL,
                      `seats` VARCHAR(45) NULL,
                      PRIMARY KEY (`idmoviehalls`));""").execute();
            System.out.println("DEBUG: CREATED TABLE MOVIEHALLS");
        }
        if (tableCheck("prices")){
            connection.prepareStatement("""
                    CREATE TABLE `tkt`.`prices` (
                      `idofmovie` VARCHAR(45) NOT NULL,
                      `nameofhall` VARCHAR(45) NOT NULL,
                      `price` VARCHAR(45) NOT NULL,
                      `time` VARCHAR(45) NOT NULL);
                    """).execute();
            System.out.println("DEBUG: CREATED TABLE PRICES");
        }

    }
}
