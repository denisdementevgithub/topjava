package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.MealTestData;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.MealTestData.mealsTos;
import static ru.javawebinar.topjava.TestUtil.userAuth;
import static ru.javawebinar.topjava.UserTestData.admin;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users")
                .with(userAuth(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void unAuth() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getMeals() throws Exception {
        perform(get("/profile/meals")
                .with(userAuth(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MealTestData.TO_MATCHER.contentJson(mealsTos));
    }
}