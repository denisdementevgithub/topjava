package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class MealToDao {
    public void deleteMealTo(int id) {
        MealsUtil.remove(id);
    }

    public MealTo getMealToById(int id) {
        return MealsUtil.getMealTo(MealsUtil.getMealById(id));
    }

    public void updateMealTo(int id, Meal meal) {
        MealsUtil.set(id, meal);
    }

    public int createMealTo(Meal meal) {
        return MealsUtil.createMeal(meal);
    }

    public List<MealTo> getAllMealTo() {
        return MealsUtil.getListOfMealTo();
    }
}
