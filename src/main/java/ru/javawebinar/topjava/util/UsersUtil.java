package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User(null,"Иван", "ivan@mail.ru", "123", Role.ADMIN),
            new User(null, "Дмитрий", "dmitrii@mail.ru", "123", Role.USER, Role.ADMIN)
    );


}
