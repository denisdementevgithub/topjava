package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealRestControllerClass {

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/filter")
    public String getMealsBetween(Model model,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(required = false) String startTime,
                                  @RequestParam(required = false) String endTime) {
        log.debug("jspMealController method getMealsBetween");
        LocalDate localStartDate = parseLocalDate(startDate);
        LocalDate localEndDate = parseLocalDate(endDate);
        LocalTime localStartTime = parseLocalTime(startTime);
        LocalTime localEndTime = parseLocalTime(endTime);
        model.addAttribute("meals", super.getBetween(localStartDate, localStartTime, localEndDate, localEndTime));
        return "meals";
    }

    @GetMapping
    public String getMeals(Model model) {
        log.debug("jspMealController method getMeals");
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/update/{id}")
    public String getMeal(Model model, @PathVariable("id") int id) {
        log.debug("jspMealController method getMeal");
        final Meal meal = super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/create")
    public String getNewMeal(Model model) {
        log.debug("jspMealController method getNewMeal");
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable("id") int id) {
        log.debug("jspMealController method deleteMeal");
        super.delete(id);
        return "redirect:/meals";
    }

    @PostMapping
    public String save(HttpServletRequest request) {
        log.debug("jspMealController method save");
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

