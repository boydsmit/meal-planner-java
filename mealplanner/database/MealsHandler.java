package mealplanner.database;

import mealplanner.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MealsHandler {
    private final Connection connection;

    public MealsHandler(Connection connection) {
        this.connection = connection;
    }

    private Meal[] getMealsByResultSet(ResultSet resultSet) throws SQLException {
        IngredientsHandler ingredientsHandler = new IngredientsHandler(connection);
        List<Meal> meals = new ArrayList<>();
        while (resultSet.next()) {
            Meal curMeal = new Meal();
            curMeal.setCategory(resultSet.getString("category"));
            curMeal.setName(resultSet.getString("meal"));
            curMeal.setIngredients(ingredientsHandler.getIngredientsById(resultSet.getInt(3)));
            curMeal.setId(resultSet.getInt("meal_id"));

            meals.add(curMeal);
        }
        return meals.toArray(Meal[]::new);
    }


    public Meal[] getAllMeals() throws  SQLException {
            Statement mealStatement = connection.createStatement();
            ResultSet mealsResult = mealStatement.executeQuery("SELECT * FROM public.meals ORDER BY meal_id ASC ");
            return getMealsByResultSet(mealsResult);
        }

    public Meal[] getMealByCategory(String category) throws  SQLException {
        return getMealByCategory(category, false);
    }

        public Meal[] getMealByCategory(String category, Boolean sortedByName) throws  SQLException {
            PreparedStatement mealStatement = sortedByName ? connection.prepareStatement("SELECT * FROM meals where category = ? ORDER BY meal") : connection.prepareStatement("SELECT * FROM meals where category = ? ORDER BY meal_id ");

            mealStatement.setString(1, category);
            ResultSet mealsResult = mealStatement.executeQuery();
            return getMealsByResultSet(mealsResult);
    }

    public int addMeal(Meal meal) throws SQLException{
            PreparedStatement mealsInsertion =
                    connection.prepareStatement("insert into meals (category, meal) values (?,?)",
                            Statement.RETURN_GENERATED_KEYS);

            mealsInsertion.setString(1, meal.getCategory());
            mealsInsertion.setString(2, meal.getName());
            mealsInsertion.executeUpdate();

            ResultSet resultSet = mealsInsertion.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(3);
    }
}
