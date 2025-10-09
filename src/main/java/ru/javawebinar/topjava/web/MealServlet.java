package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.dao.MealDaoInt;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealDaoInt mealDaoImpl;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealDaoImpl = new MealDaoImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = null;
        String action = request.getParameter("action");
        if (action != null && !action.isEmpty() && action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealToId"));
            mealDaoImpl.delete(mealId);
            log.debug("forward to meals from delete");
            response.sendRedirect("meals");
            return;
        } else if (action != null && !action.isEmpty() && action.equalsIgnoreCase("edit")) {
            forward = "/meal.jsp";
            int mealId = Integer.parseInt(request.getParameter("mealToId"));
            Meal meal = mealDaoImpl.getById(mealId);
            request.setAttribute("mealTo", meal);
            log.debug("forward to meal from edit");
        } else if (action != null && !action.isEmpty() && action.equalsIgnoreCase("addMeal")) {
            forward = "/meal.jsp";
            Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), null, 0);
            request.setAttribute("mealTo", meal);
            log.debug("forward to meals from addMeal");
        } else {
            forward = "/meals.jsp";
            request.setAttribute("allMealTo", MealsUtil.getListOfMealTo(mealDaoImpl.getAll()));
            request.getRequestDispatcher(forward).forward(request, response);
            log.debug("forward to meals from else");
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strId = request.getParameter("id");
        if (strId != null && !strId.isEmpty()) {
            int id = Integer.parseInt(strId);
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            meal.setId(id);
            mealDaoImpl.update(id, meal);
        } else {
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            mealDaoImpl.create(meal);
        }
        response.sendRedirect("meals");
    }

}
