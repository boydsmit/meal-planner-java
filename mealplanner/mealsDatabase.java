package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class mealsDatabase {

    private static Connection connection;

    public static void initialize() {
        try {
            String DB_URL = "jdbc:postgresql:meals_db";
            String USER = "postgres";
            String PASS = "1111";
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);

            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table if exists meals");
            statement.executeUpdate("drop table if exists ingredients");
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

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
    }

    public static void addMealWithIngredients(Meal meal) {
        try {
            PreparedStatement mealsInsertion =
                    connection.prepareStatement("insert into meals (category, meal) values (?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            mealsInsertion.setString(1, meal.getCategory());
            mealsInsertion.setString(2, meal.getName());
            mealsInsertion.executeUpdate();

            ResultSet resultSet = mealsInsertion.getGeneratedKeys();
            resultSet.next();
            int key = resultSet.getInt(3);

            PreparedStatement ingredientsInsertion =
                    connection.prepareStatement("insert into ingredients (ingredient, meal_id) values (?,?)");

            for (String ingredient : meal.getIngredients()) {
                ingredientsInsertion.setString(1, ingredient);
                ingredientsInsertion.setInt(2, key);
                ingredientsInsertion.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
    }

    public static Meal[] getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        try {
            Statement mealStatement = connection.createStatement();
            ResultSet mealsResult = mealStatement.executeQuery("SELECT * FROM public.meals ORDER BY meal_id ASC ");

            while(mealsResult.next()) {
                Statement ingredientsStatement = connection.createStatement();
                ResultSet ingredientsResult = ingredientsStatement.executeQuery("SELECT * FROM public.ingredients where meal_id = " + mealsResult.getInt(3));

                Meal curMeal = new Meal();
                curMeal.setCategory(mealsResult.getString("category"));
                curMeal.setName(mealsResult.getString("meal"));

                List<String> ingredients = new ArrayList<>();
                while(ingredientsResult.next()) {
                    ingredients.add(ingredientsResult.getString(1));
                }
                curMeal.setIngredients(ingredients.toArray(String[]::new));

                meals.add(curMeal);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
        return meals.toArray(Meal[]::new);
    }
}
