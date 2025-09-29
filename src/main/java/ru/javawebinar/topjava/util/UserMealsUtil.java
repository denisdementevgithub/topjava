package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 29, 13, 0), "Обед", 5000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 28, 13, 0), "Обед", 500)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumOfCalories = getDateExcessMapByCycles(meals, caloriesPerDay);
        List<UserMealWithExcess> returnedList = new ArrayList<>();
        for (UserMeal currentUserMeal : meals) {
            if (TimeUtil.isBetweenHalfOpen(currentUserMeal.getDateTime().toLocalTime(), startTime, endTime) == true) {
                returnedList.add(new UserMealWithExcess(currentUserMeal.getDateTime(),
                        currentUserMeal.getDescription(), currentUserMeal.getCalories(),
                        sumOfCalories.get(currentUserMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return returnedList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumOfCalories = getDateExcessMapByStream(meals, caloriesPerDay);
        return meals.stream().
                filter(x -> TimeUtil.isBetweenHalfOpen(x.getDateTime().toLocalTime(), startTime, endTime)).
                map(x -> new UserMealWithExcess(x.getDateTime(),
                        x.getDescription(), x.getCalories(),
                        sumOfCalories.get(x.getDateTime().toLocalDate()) > caloriesPerDay)).
                collect(Collectors.toList());
    }

    private static Map<LocalDate, Integer> getDateExcessMapByCycles(List<UserMeal> meals, int caloriesPerDay) {
        Map<LocalDate, Integer> tempMap = new HashMap<>();
        for (UserMeal userMeal : meals) {
            tempMap.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), (prev, current) -> prev + current);
        }
        return tempMap;
    }

    private static Map<LocalDate, Integer> getDateExcessMapByStream(List<UserMeal> meals, int caloriesPerDay) {
        Map<LocalDate, Integer> tempMap = new HashMap<>();
        meals.stream().
                forEach(um -> tempMap.merge(um.getDateTime().toLocalDate(), um.getCalories(),
                        (prev, current) -> prev + current));
        return tempMap;
    }
}
