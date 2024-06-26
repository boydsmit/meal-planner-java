package mealplanner;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Meal {
    private String category;
    private String name;
    private String[] ingredients;
    private int id;

    public enum possibleCategories {
        breakfast,
        lunch,
        dinner
    }

    public String setCategory(Scanner scanner) {
        try {
            String category = scanner.nextLine();
            possibleCategories.valueOf(category);
            return this.category = category;

        } catch (InputMismatchException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
        }
        return setCategory(scanner);
    }

    public String setName(String name) {
        return this.name = name;
    }

    public String setCategory(String category) {
        return this.category = category;
    }

    public String[] setIngredients(String[] ingredients) {
        return this.ingredients = ingredients;
    }

    public String setName(Scanner scanner) {
        try {
            return this.name = validateString(scanner.nextLine());
        } catch (InputMismatchException ex) {
            System.out.println(ex.getMessage());
            return setName(scanner);
        }
    }

    public String[] setIngredients(Scanner scanner) {
        try {
            String[] ingredientsArray = scanner.nextLine().split(",");
            for (int i = 0; i < ingredientsArray.length; i++) {
                validateString(ingredientsArray[i]);
                ingredientsArray[i] = ingredientsArray[i].trim();
            }
            return this.ingredients = ingredientsArray;
        } catch (InputMismatchException ex) {
            System.out.println(ex.getMessage());
            return setIngredients(scanner);
        }
    }

    public String validateString(String input) throws InputMismatchException {
        if (!input.matches("[a-zA-Z,\\s]+") || input.trim().isEmpty()) {
            throw new InputMismatchException("Wrong format. Use letters only!");
        }
        return input;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void print() {
        System.out.printf("""
                Name: %s
                ingredients:
                %s
                """, name, String.join("\n", ingredients));
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public int getId() {
        return id;
    }
}

