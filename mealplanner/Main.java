package mealplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Meal> meals = new ArrayList<>();

    public static void main(String[] args) {
        mealsDatabase.initialize();
        getOption();
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

    public static void showMeals() {
        if (meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
            getOption();
        }
        System.out.println();
        for (Meal meal : meals) {
            meal.print();
            System.out.println();
        }
        getOption();
    }

    public static void getOption() {
        System.out.println("What would you like to do (add, show, exit)?");
        String option = scanner.nextLine();
        switch (option.toLowerCase()) {
            case "add":
                meals.add(addMeal());
                getOption();
                break;
            case "show":
                showMeals();
                break;
            case "exit":
                System.out.println("Bye!");
                System.exit(0);
            default:
                getOption();
        }
    }
}
