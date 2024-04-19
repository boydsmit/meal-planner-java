package mealplanner;

public class Plan {
    private String day;
    private String breakfast;
    private String lunch;
    private String dinner;


    public void printDay() {
        System.out.printf("""
                %s
                Breakfast: %s
                Lunch: %s
                Dinner: %s
                """,  day, breakfast, lunch, dinner);
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDay() {
        return day;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public String getDinner() {
        return dinner;
    }
}
