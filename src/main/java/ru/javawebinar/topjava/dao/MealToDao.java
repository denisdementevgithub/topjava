package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class MealToDao {
    private List<MealTo> listOfMealTo;

    public MealToDao() {
        this.listOfMealTo = MealsUtil.getListOfMealTo();
    }

    public void deleteMealTo(int id) {
        MealsUtil.meals.remove(id);
        listOfMealTo = MealsUtil.getListOfMealTo();
    }

    public MealTo getMealToById(int id) {
        return listOfMealTo.get(id);
    }

    public void updateMealTo(int id, Meal meal) {
        if (MealsUtil.meals.size()>id) {
            MealsUtil.meals.set(id, meal);
        } else {
            MealsUtil.meals.add(meal);
        }
        listOfMealTo = MealsUtil.getListOfMealTo();
    }

    public List<MealTo> getAllMealTo() {
        return listOfMealTo;
    }
}
