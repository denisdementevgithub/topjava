package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ+3;
    public static final int USER_ID = START_SEQ+1;
    public static final int NOT_FOUND = 10;

    public static final List<Meal> meals = new ArrayList<>(Arrays.asList(
        new Meal(100003, LocalDateTime.of(2022, Month.OCTOBER, 30, 21, 00), "ужин", 1000),
            new Meal(100005, LocalDateTime.of(2022, Month.OCTOBER, 29, 13, 00), "обед", 500),
            new Meal(100004, LocalDateTime.of(2022, Month.OCTOBER, 28, 10,00), "обед", 1300)));

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2016, Month.JUNE, 2, 18, 0), "Ужин", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meals.get(0));
        updated.setDateTime(LocalDateTime.of(1999, Month.JANUARY, 1, 01, 01));
        updated.setDescription("Updated");
        updated.setCalories(1);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("user_id").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }
}
