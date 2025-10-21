package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.getUpdated;
import static ru.javawebinar.topjava.UserTestData.user;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"

})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void create() {
        Meal created = mealService.create(MealTestData.getNew(), MealTestData.USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(mealService.get(newId, MealTestData.USER_ID), newMeal);
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_ID, MealTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID, MealTestData.USER_ID));
    }

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID, MealTestData.USER_ID);
        MealTestData.assertMatch(meal, meals.get(0));
    }


    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2022, Month.OCTOBER, 28);
        LocalDate endDate = LocalDate.of(2022, Month.OCTOBER, 29);
        MealTestData.assertMatch(new ArrayList<>(Arrays.asList(meals.get(1), meals.get(2))), mealService.getBetweenInclusive(startDate, endDate, MealTestData.USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(MealTestData.USER_ID);
        MealTestData.assertMatch(all, meals.get(0), meals.get(1), meals.get(2));
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        mealService.update(updated, MealTestData.USER_ID);
        MealTestData.assertMatch(mealService.get(MealTestData.MEAL_ID, MealTestData.USER_ID), MealTestData.getUpdated());
    }
}