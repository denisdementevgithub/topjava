package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;
    private InMemoryMealRepository inMemoryMealRepository;

    public MealRestController(InMemoryMealRepository inMemoryMealRepository, MealService service) {
        this.inMemoryMealRepository = inMemoryMealRepository;
        this.service = service;
    }

    public Collection<Meal> getAll(Integer userId) {
        log.info("getAll");
        return service.getAll(userId);
    }

    public Meal get(int id, Integer userId) {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal, Integer userId) {
        log.info("create {}", meal);
        checkIsNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        //return inMemoryMealRepository.save(meal, userId);
        return service.create(meal, userId);
    }

    public void delete(int id, Integer userId) {
        log.info("get {}", id);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id, Integer userId) {
        log.info("get {}", id);
        service.update(meal, userId);
    }
}