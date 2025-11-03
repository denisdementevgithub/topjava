package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.DESC, "dateTime");
    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        meal.setUser(userFromDb.get());
        if (meal.isNew()) {
            return crudRepository.save(meal);
        } else {
            Optional<Meal> mealFromDb = crudRepository.findById(meal.getId());
            meal.setUser(userFromDb.get());
            return mealFromDb.isPresent() && mealFromDb.get().getUser().getId() == userId ?
                    crudRepository.save(meal) : null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Optional<Meal> mealOptional = crudRepository.findById(id);
        if (mealOptional.isPresent() && mealOptional.get().getUser().getId() == userId) {
            return crudRepository.delete(id) != 0;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> mealOptional = crudRepository.findById(id);
        return mealOptional.isPresent() && mealOptional.get().getUser().getId() == userId ?
                mealOptional.get() : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserId(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }
}
