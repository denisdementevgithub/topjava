package ru.javawebinar.topjava.controller;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealToDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.UserServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealController extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealToDao mealToDao = new MealToDao();
        String forward = "";
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("delete")) {
            int mealToId = Integer.parseInt(request.getParameter("mealToId"));
            mealToDao.deleteMealTo(mealToId);
            forward = "/meals.jsp";
            request.setAttribute("allMealTo", mealToDao.getAllMealTo());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = "/meal.jsp";
            int mealToId = Integer.parseInt(request.getParameter("mealToId"));
            MealTo mealTo = mealToDao.getMealToById(mealToId);
            request.setAttribute("mealTo", mealTo);
        } else if (action.equalsIgnoreCase("listOfMealTo")) {
            forward = "/meals.jsp";
            request.setAttribute("allMealTo", mealToDao.getAllMealTo());
        } else if (action.equalsIgnoreCase("addMeal")) {
            forward = "/meal.jsp";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Meal meal = new Meal(MealsUtil.counter++, LocalDateTime.now().withNano(0).withSecond(0), null, 0);
            request.setAttribute("mealTo", MealsUtil.createTo(meal, false));
        } else {
            forward = "/meal.jsp";
            request.getRequestDispatcher(forward).forward(request, response);
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealToDao mealToDao = new MealToDao();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        int id = Integer.parseInt(request.getParameter("mealToId"));
        Meal meal = new Meal(id, LocalDateTime.parse(request.getParameter("dateTime"), formatter),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        mealToDao.updateMealTo(id, meal);
        RequestDispatcher view = request.getRequestDispatcher("/meals.jsp");
        request.setAttribute("allMealTo", mealToDao.getAllMealTo());
        view.forward(request, response);
    }
}
