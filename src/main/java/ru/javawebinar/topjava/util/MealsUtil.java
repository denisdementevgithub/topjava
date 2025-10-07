package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealsUtil {
    private static int CALORIES_PER_DAY = 2000;
    public static ArrayList<Meal> meals;
    public static Integer counter;

    static {
        counter = 0;
        meals = new ArrayList<>(Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        for (int i = 0; i < meals.size(); i++) {
            meals.get(i).setId(counter++);
        }
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        MealTo mealTo = new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
        mealTo.setId(meal.getId());
        return mealTo;
    }

    public static List<MealTo> getListOfMealTo() {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return meals.stream()
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }

    public static MealTo getMealTo(Meal providedMeal) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return meals.stream()
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .filter(meal -> meal.getDateTime().equals(providedMeal.getDateTime()))
                .findFirst()
                .get();
    }

    public static void remove(int id) {
        List<Meal> meals = MealsUtil.meals;
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.remove(meals.get(i));
            }
        }
    }


    public static Meal getMealById(int id) {
        Meal returnedMeal = null;
        for (int i = 0; i < MealsUtil.meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                returnedMeal = MealsUtil.meals.get(i);
            }
        }
        return returnedMeal;
    }

    public static void set(int id, Meal meal) {
        MealsUtil.meals.set(id, meal);
    }

    public static int createMeal(Meal meal) {
        meal.setId(counter++);
        MealsUtil.meals.add(meal);
        return MealsUtil.meals.indexOf(meal);
    }
}