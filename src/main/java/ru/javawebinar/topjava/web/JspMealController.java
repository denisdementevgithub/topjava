package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.AbstractMealRestControllerClass;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealRestControllerClass {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String getMealsBetween(Model model, HttpServletRequest request,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(required = false) String startTime,
                                  @RequestParam(required = false) String endTime
                                  ) {
        log.debug("jspMealController method getUsersBetween");
        if (startDate==null || endDate==null || startTime==null || endTime==null) {
            model.addAttribute("meals", super.getAll());
            return "meals";
        } else {
            LocalDate localStartDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate localEndDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime localStartTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime localEndTime = parseLocalTime(request.getParameter("endTime"));
            //request.setAttribute("meals", mealRestController.getBetween(startDate, startTime, endDate, endTime));
            model.addAttribute("meals", super.getBetween(localStartDate, localStartTime, localEndDate, localEndTime));
            return "meals";
        }
    }

    @GetMapping("/meals/action=update&id={id}")
    public String getMeal(Model model, @PathVariable("id") int id, @RequestParam(required = false) String action) {
            log.debug("jspMealController method getUser");
            final Meal meal = super.get(id);
            model.addAttribute("meal", meal);
            return "mealForm";
    }

    @GetMapping("/meals/action=create")
    public String getNewMeal(Model model) {
        log.debug("jspMealController method getNewUser");
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/meals/action=delete&id={id}")
    public String deleteMeal(@PathVariable("id") int id) {
        log.debug("jspMealController method deleteUser");
        super.delete(id);
        return "redirect:/meals";
    }


    @PostMapping("/meals")
    public String setMeal(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            super.update(meal, getId(request));
        } else {
            super.create(meal);
        }
        return "redirect:/meals";
    }






    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

}

