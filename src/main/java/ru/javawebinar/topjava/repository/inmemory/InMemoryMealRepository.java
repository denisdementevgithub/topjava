package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

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
        for (Meal meal : MealsUtil.meals) {
            save(meal);
        }
    }


    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {

        return mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id) {

        return mealsMap.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        List<Meal> meals = new ArrayList(mealsMap.values());
        log.info("getAll");
        System.out.println("meals in repo " + meals.get(0));
        meals.sort((m1, m2)->m2.getDate().compareTo(m1.getDate()));
        return meals;
    }

    @Override
    public Collection<Meal> getByUserId(int userId) {
        return mealsMap.values().stream().filter(m->m.getUserId() == userId)
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());
    }
}

