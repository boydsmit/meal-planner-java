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

    private static String[] getIngredientsById(int id) throws SQLException {
        List<String> ingredients = new ArrayList<>();
        Statement ingredientsStatement = connection.createStatement();
        ResultSet ingredientsResult = ingredientsStatement.executeQuery("SELECT * FROM public.ingredients where meal_id = " + id);

        while(ingredientsResult.next()) {
            ingredients.add(ingredientsResult.getString(1));
        }
        return ingredients.toArray(String[]::new);
    }

    private static Meal[] getMealsByResultSet(ResultSet resultSet)  throws SQLException {
        List<Meal> meals = new ArrayList<>();
        while(resultSet.next()) {
            Meal curMeal = new Meal();
            curMeal.setCategory(resultSet.getString("category"));
            curMeal.setName(resultSet.getString("meal"));
            curMeal.setIngredients(getIngredientsById(resultSet.getInt(3)));

            meals.add(curMeal);
        }
        return meals.toArray(Meal[]::new);
    }


    public static Meal[] getAllMeals() {
        try {
            Statement mealStatement = connection.createStatement();
            ResultSet mealsResult = mealStatement.executeQuery("SELECT * FROM public.meals ORDER BY meal_id ASC ");
            return getMealsByResultSet(mealsResult);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
        return null;
    }

    public static Meal[] getMealByCategory(String category) {
        try {
            PreparedStatement mealStatement = connection.prepareStatement("SELECT * FROM public.meals where meals.category = ? ORDER BY meal_id ASC ");
            mealStatement.setString(1, category);
            ResultSet mealsResult = mealStatement.executeQuery();
            return getMealsByResultSet(mealsResult);


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
        return null;
    }
}
