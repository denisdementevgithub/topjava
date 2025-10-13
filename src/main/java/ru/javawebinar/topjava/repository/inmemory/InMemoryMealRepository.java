package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

     {
         List<Meal> meals = MealsUtil.meals;
         meals.get(0).setUserId(1);
         meals.get(1).setUserId(1);
         meals.get(2).setUserId(0);
         meals.get(3).setUserId(0);
        for (Meal meal : meals) {
            save(meal, meal.getUserId());
        }
         System.out.println(meals);
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealsMap.put(meal.getId(), meal);
            System.out.println(meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, Integer userId) {
         Meal meal = new ArrayList<Meal>(mealsMap.values()).get(id);
        if (meal.getUserId() != null) {
            if (meal.getUserId() == userId) {
                return mealsMap.remove(id) != null;
            }
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {Meal meal = mealsMap.get(id);
        if (meal.getUserId() != null) {
            if (meal.getUserId() == userId) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        List<Meal> returnedMeals = new ArrayList<Meal>();
        log.info("getAll");
        for (Meal meal:new ArrayList<Meal>(mealsMap.values())) {
            if (meal.getUserId() != null) {
                if (meal.getUserId() == userId) {
                    returnedMeals.add(meal);
                }
            }
        }
        returnedMeals.sort((m1, m2)->m2.getDate().compareTo(m1.getDate()));
        System.out.println("meals in repo " + returnedMeals);
        return returnedMeals;
    }

    @Override
    public Collection<Meal> getByUserId(int userId) {
        return mealsMap.values().stream().filter(m->m.getUserId() == userId)
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }
}

