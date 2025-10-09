package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealDaoImpl implements MealDaoInt {
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

    public void delete(int id) {
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.remove(meals.get(i));
            }
        }
    }

    public Meal getById(int id) {
        Meal returnedMeal = null;
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                returnedMeal = meals.get(i);
            }
        }
        return returnedMeal;
    }

    public void update(int id, Meal meal) {
        for (int i = 0; i < meals.size(); i++) {
            Meal curMeal = meals.get(i);
            if (curMeal.getId() == id) {
                meals.set(id, meal);
            }
        }
    }

    public int create(Meal meal) {
        meal.setId(counter++);
        meals.add(meal);
        return meals.indexOf(meal);
    }

    public List<Meal> getAll() {
        return meals;
    }
}
