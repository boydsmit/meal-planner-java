package mealplanner;

import java.sql.*;

public class mealsDatabase {

    public static void initialize() {
        try {
            String DB_URL = "jdbc:postgresql:meals_db";
            String USER = "postgres";
            String PASS = "1111";

            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);

            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table if exists meals");
            statement.executeUpdate("drop table if exists ingredients");
            statement.executeUpdate("create table meals (" +
                    "category varchar(1024)," +
                    "meal varchar(1024)," +
                    "meal_id int" +
                    ")");

            statement.executeUpdate("create table ingredients(" +
                    "ingredients varchar(1024)," +
                    "ingredient_id int," +
                    "meal_id integer" +
                    ")");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
