package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public interface MealDaoInt {

    public void delete(int id);

    public Meal getById(int id);

    public void update(int id, Meal meal);

    public int create(Meal meal);

    public List<Meal> getAll();
}
