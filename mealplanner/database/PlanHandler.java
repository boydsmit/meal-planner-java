package mealplanner.database;

import mealplanner.Plan;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanHandler {
    private final Connection connection;

    public PlanHandler(Connection connection) {
        this.connection = connection;
    }

    public void addPlan(String day, String option, String category, int id) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("insert into plan (day, meal_option, meal_category, meal_id) values (?,?,?,?)");
        preparedStatement.setString(1, day);
        preparedStatement.setString(2, option);
        preparedStatement.setString(3, category);
        preparedStatement.setInt(4, id);
        preparedStatement.executeUpdate();
    }

    public Plan[] getAllPlans() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from plan");
        return convertSetsToPlans(resultSet);
    }

    public Plan[] getPlanByDay(String day) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from plan where day = ?");
        statement.setString(1, day);
        ResultSet resultSet = statement.executeQuery();

        return convertSetsToPlans(resultSet);
    }

    private Plan[] convertSetsToPlans(ResultSet resultSet) throws SQLException {
        List<Plan> plans = new ArrayList<>();
        while(resultSet.next()) {
            Plan plan = new Plan();
            plan.setDay(resultSet.getString("day"));
            plan.setBreakfast(resultSet.getString("meal_option"));
            resultSet.next();
            plan.setLunch(resultSet.getString("meal_option"));
            resultSet.next();
            plan.setDinner(resultSet.getString("meal_option"));
            plans.add(plan);
        }
        return plans.toArray(Plan[]::new);
    }
}
