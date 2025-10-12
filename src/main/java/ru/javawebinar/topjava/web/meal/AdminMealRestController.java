package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AbstractUserController;

import java.util.List;

@Controller
public class AdminMealRestController extends AbstractMealController {

    @Override
    public List<Meal> getAll() {
        return super.getAll();
    }

    @Override
    public Meal get(int id) {
        return super.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        return super.create(meal);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public void update(Meal meal, int id) {
        super.update(meal, id);
    }

}