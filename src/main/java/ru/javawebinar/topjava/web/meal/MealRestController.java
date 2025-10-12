package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
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

    public Collection<Meal> getAll() {
        log.info("getAll");
        return service.getByUserId(SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        if (SecurityUtil.authUserId() == service.get(id).getUserId()) {
            return service.get(id);
        } else {
            throw new NotFoundException("Еда не найдена");
        }
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkIsNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("get {}", id);
        if (SecurityUtil.authUserId() == service.get(id).getUserId()) {
            log.info("delete {}", id);
            service.delete(id);
        } else {
            throw new NotFoundException("Еда не найдена");
        }

    }

    public void update(Meal meal, int id) {
        log.info("get {}", id);
        if (SecurityUtil.authUserId() == service.get(id).getUserId()) {
            log.info("update {} with id={}", meal, id);
            assureIdConsistent(meal, id);
            service.update(meal);
        } else {
            throw new NotFoundException("Еда не найдена");
        }
    }
}