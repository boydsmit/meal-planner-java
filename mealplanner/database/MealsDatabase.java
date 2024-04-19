package mealplanner.database;

import java.sql.*;

public class MealsDatabase {

    public Connection initialize() throws SQLException {

        String DB_URL = "jdbc:postgresql:meals_db";
        String USER = "postgres";
        String PASS = "1111";
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);

        Statement statement = connection.createStatement();
//            statement.executeUpdate("drop table if exists meals");
//            statement.executeUpdate("drop table if exists ingredients");
//            statement.executeUpdate("drop table if exists plan");
        statement.executeUpdate("create table if not exists meals (" +
                "category varchar(1024)," +
                "meal varchar(1024)," +
                "meal_id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY" +
                ")");

        statement.executeUpdate("create table if not exists ingredients(" +
                "ingredient varchar(1024)," +
                "ingredient_id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                "meal_id int" +
                ")");

        statement.executeUpdate("create table if not exists plan(" +
                "day varchar(1024)," +
                "meal_option varchar(1024)," +
                "meal_category varchar(1024)," +
                "meal_id int" +
                ")");

        return connection;
    }
}
