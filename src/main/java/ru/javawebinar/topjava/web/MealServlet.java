package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealToDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealToDao mealToDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mealToDao = new MealToDao();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //MealToDao mealToDao = new MealToDao();
        String forward = null;
        String action = request.getParameter("action");;
        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("delete")) {
                int mealToId = Integer.parseInt(request.getParameter("mealToId"));
                mealToDao.deleteMealTo(mealToId);
            } else if (action.equalsIgnoreCase("edit")) {
                forward = "/meal.jsp";
                log.debug("forward to meal from edit");
                int mealToId = Integer.parseInt(request.getParameter("mealToId"));
                MealTo mealTo = mealToDao.getMealToById(mealToId);
                request.setAttribute("mealTo", mealTo);
            } else if (action.equalsIgnoreCase("listOfMealTo")) {
                forward = "/meals.jsp";
                log.debug("forward to meals from listOfMealTo");
                request.setAttribute("allMealTo", mealToDao.getAllMealTo());
            } else if (action.equalsIgnoreCase("addMeal")) {
                forward = "/meal.jsp";
                log.debug("forward to meals from addMeal");
                Meal meal = new Meal(LocalDateTime.now().withNano(0).withSecond(0), null, 0);
                int id = mealToDao.createMealTo(meal);
                request.setAttribute("mealTo", mealToDao.getMealToById(id));
            } else {
                forward = "/meals.jsp";
                log.debug("forward to meals from 'else'");
                request.getRequestDispatcher(forward).forward(request, response);
            }
            if (forward == null) {
                log.debug("redirect to meals");
                response.sendRedirect("meals");
            } else {
                RequestDispatcher view = request.getRequestDispatcher(forward);
                view.forward(request, response);
            }
        } else {
            forward = "/meals.jsp";
            log.debug("forward to meals from great else");
            request.setAttribute("allMealTo", mealToDao.getAllMealTo());
            request.getRequestDispatcher(forward).forward(request, response);
            RequestDispatcher view = request.getRequestDispatcher(forward);
            view.forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //MealToDao mealToDao = new MealToDao();
        String action = request.getParameter("id");
        if (action != null && !action.isEmpty()) {
            int id = Integer.parseInt(action);
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            meal.setId(id);
            mealToDao.updateMealTo(id, meal);
        } else {
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            mealToDao.createMealTo(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher("/meals.jsp");
        request.setAttribute("allMealTo", mealToDao.getAllMealTo());
        view.forward(request, response);
    }

}
