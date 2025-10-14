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
        for (Meal meal : meals) {
            save(meal, meal.getUserId());
        }
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
                if (meal.isNew()) {
                    meal.setId(counter.incrementAndGet());
                    meal.setUserId(userId);
                    mealsMap.put(meal.getId(), meal);
                    return meal;
                }
                // handle case: update, but not present in storage
        if (meal.getUserId() == userId) {
                return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
return null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
         Meal meal = mealsMap.get(id);
            if (meal.getUserId() == userId) {
                return mealsMap.remove(Integer.valueOf(id)) != null;
            }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {Meal meal = mealsMap.get(id);
            if (meal.getUserId() == userId) {
                return meal;
            }

        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        return mealsMap.values().stream().filter(m->m.getUserId() == userId)
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }
}

