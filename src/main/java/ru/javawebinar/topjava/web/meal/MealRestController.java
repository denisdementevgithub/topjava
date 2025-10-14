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
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.text.CollationElementIterator;
import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkIsNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public MealRestController(InMemoryMealRepository inMemoryMealRepository, MealService service) {
        this.service = service;
    }

    public Collection<Meal> getAll() {
        log.info("getAll");
        return service.getAll(SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkIsNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("get {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("get {}", id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }


    public List<MealTo> getAllBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        Predicate<Meal> filter = meal -> meal.getDateTime().isBefore(endDateTime)
                && meal.getDateTime().isAfter(startDateTime);
        return MealsUtil.filterByPredicate(getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY, filter);
    }

}