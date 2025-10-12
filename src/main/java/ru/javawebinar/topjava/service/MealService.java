package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id) {
        checkNotFound(repository.delete(id), id);
    }

    public Meal get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public Collection<Meal> getAll() {
        Collection<Meal> collection = repository.getAll();
        System.out.println(collection);
        return collection;
    }

    public Collection<Meal> getByUserId(Integer userId) {
        return checkNotFound(repository.getByUserId(userId), "userId=" + userId);
    }

    public void update(Meal meal) {
        checkNotFound(repository.save(meal), meal.getId());
    }

}