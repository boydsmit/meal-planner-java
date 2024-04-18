package mealplanner;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

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
        try {
            String input = scanner.nextLine();

            Meal.possibleCategories.valueOf(input);

            Meal[] meals = mealsDatabase.getMealByCategory(input);
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
        } catch (Exception ex) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            showMeals();
        }
    }

    public static void getOption() {
        System.out.println("What would you like to do (add, show, exit)?");
        String option = scanner.nextLine();
        switch (option.toLowerCase()) {
            case "add":
                mealsDatabase.addMealWithIngredients(addMeal());
                getOption();
                break;
            case "show":
                System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
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
