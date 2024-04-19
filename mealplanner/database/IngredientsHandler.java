package mealplanner.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientsHandler {
    private final Connection connection;

    public IngredientsHandler(Connection connection) {
        this.connection = connection;
    }

    public String[] getIngredientsById(int id) throws SQLException {

        List<String> ingredients = new ArrayList<>();
        Statement ingredientsStatement = connection.createStatement();
        ResultSet ingredientsResult = ingredientsStatement.executeQuery("SELECT * FROM public.ingredients where meal_id = " + id);

        while (ingredientsResult.next()) {
            ingredients.add(ingredientsResult.getString(1));
        }
        return ingredients.toArray(String[]::new);
    }

    public void addIngredientsByMealId(String[] ingredients, int mealId) throws SQLException {
        PreparedStatement ingredientsInsertion =
                connection.prepareStatement("insert into ingredients (ingredient, meal_id) values (?,?)");

        for (String ingredient : ingredients) {
            ingredientsInsertion.setString(1, ingredient);
            ingredientsInsertion.setInt(2, mealId);
            ingredientsInsertion.executeUpdate();
        }
    }

}
