package mealplanner;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import mealplanner.database.IngredientsHandler;
import mealplanner.database.MealsDatabase;
import mealplanner.database.MealsHandler;
import mealplanner.database.PlanHandler;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static Connection connection;

    public static void main(String[] args) {
        try {
            MealsDatabase mealsDatabase = new MealsDatabase();
            connection = mealsDatabase.initialize();
            getOption();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
    }

    public static Meal addMeal() {
        Meal meal = new Meal();
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)? ");
        meal.setCategory(scanner);
        System.out.println("Input the meal's name:");
        meal.setName(scanner);
        System.out.println("Input the ingredients:");
        meal.setIngredients(scanner);
        System.out.println("The meal has been added!");
        return meal;
    }

    public static void showMeals() throws SQLException {
        MealsHandler mealsHandler = new MealsHandler(connection);
        try {
            String input = scanner.nextLine();

            Meal.possibleCategories.valueOf(input);

            Meal[] meals = mealsHandler.getMealByCategory(input);
            if (meals.length == 0) {
                System.out.println("No meals found.");
                getOption();
            }

            System.out.println("Category: " + input + "\n");

            for (Meal meal : meals) {
                meal.print();
                System.out.println();
            }
            getOption();
        } catch (IllegalArgumentException ex) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            showMeals();
        }
    }

    public static void planMeal() throws SQLException {
        PlanHandler planHandler = new PlanHandler(connection);
        String[] days = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] categories = new String[] {"breakfast", "lunch", "dinner"};

        for (String day : days) {
            System.out.println(day);
            for (String category : categories) {
                Meal res = getPlanInput(day, category);
                planHandler.addPlan(day, res.getName(), res.getCategory(), res.getId());
            }
            System.out.println("Yeah! We planned the meals for " + day + ".\n");
        }


        for (String day : days) {
            Plan[] plan = planHandler.getPlanByDay(day);
            plan[0].printDay();
            System.out.println();
        }

        getOption();
    }

    private static Meal getPlanInput(String day, String category) throws SQLException {
        MealsHandler mealsHandler = new MealsHandler(connection);
        Meal[] meals = mealsHandler.getMealByCategory(category, true);
        for (Meal meal : meals) {
            System.out.println(meal.getName());
        }
        System.out.printf("Choose the %s for %s from the list above: \n", category, day);
        var ref = new Object() {
            String input = scanner.nextLine();
        };
        while(Arrays.stream(meals).noneMatch(x -> Objects.equals(x.getName(), ref.input))) {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            ref.input = scanner.nextLine();
        }
       return Arrays.stream(meals).filter(x -> x.getName().equals(ref.input)).findFirst().get();
    }

    public static void saveCart() throws  SQLException{
        PlanHandler planHandler = new PlanHandler(connection);
        IngredientsHandler ingredientsHandler = new IngredientsHandler(connection);
        Plan[] plans = planHandler.getAllPlans();
        if (plans.length == 0) {
            System.out.println("Unable to save. Plan your meals first. ");
            getOption();
        }
        System.out.println("Input a filename:");
        String fileName = scanner.nextLine();

        List<String> stringList = new ArrayList<>();
        List<String> ingredientsList = new ArrayList<>();
        for (Plan plan : plans) {
           ingredientsList.addAll(Arrays.asList(ingredientsHandler.getIngredientsById(plan.getBreakfastId())));
            ingredientsList.addAll(Arrays.asList(ingredientsHandler.getIngredientsById(plan.getLunchId())));
            ingredientsList.addAll(Arrays.asList(ingredientsHandler.getIngredientsById(plan.getDinnerId())));
        }

        for (String s : ingredientsList) {
            addOrReplaceString(stringList, s);
        }

        try {
        FileWriter fileWriter = new FileWriter(fileName);
            for (String str : stringList) {
                fileWriter.write(str + System.lineSeparator());
            }
            fileWriter.close();
            System.out.println("Saved!");

            getOption();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(126);
        }
    }

    private static void addOrReplaceString(List<String> stringList, String value){
        Optional<String> found = stringList.stream().filter(x -> x.contains(value)).findFirst();
        if (found.isPresent()) {
            String foundString = found.get();
            if (foundString.contains("x")) {
                int i = Integer.parseInt(foundString.split("x")[1]);
                String newVal = value + " x" + ++i;

                stringList.replaceAll(x -> x.contains(foundString) ? newVal : x);
            } else
                stringList.replaceAll(x -> x.equals(value) ? value + " x2" : x);
        } else {
            stringList.add(value);
        }
    }

    public static void getOption() throws SQLException {
        MealsHandler mealsHandler = new MealsHandler(connection);
        IngredientsHandler ingredientsHandler = new IngredientsHandler(connection);
        System.out.println("What would you like to do (add, show, plan, save, exit)?");
        String option = scanner.nextLine();
        switch (option.toLowerCase()) {
            case "add":
                Meal meal = addMeal();
                int mealId = mealsHandler.addMeal(meal);
                ingredientsHandler.addIngredientsByMealId(meal.getIngredients(), mealId);
                getOption();
                break;
            case "show":
                System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
                showMeals();
                break;
            case "plan":
                planMeal();
                break;
            case "save":
                saveCart();
                break;
            case "exit":
                System.out.println("Bye!");
                System.exit(0);
            default:
                getOption();
        }
    }
}
