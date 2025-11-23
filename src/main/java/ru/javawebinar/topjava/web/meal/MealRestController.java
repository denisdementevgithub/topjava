package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    static {
        SecurityUtil.setAuthUserId(100000);
    }

    static final String REST_URL = "/rest/admin/users/100000/meals";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @Override
    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }

    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam(required = false) @Nullable String startDateTime,
                                   @RequestParam(required = false) @Nullable String endDateTime) {
        LocalDateTime localStartDateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime localEndDateTime = LocalDateTime.parse(endDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDate startDate = localStartDateTime.toLocalDate();
        LocalTime startTime = localStartDateTime.toLocalTime();
        LocalDate endDate = localEndDateTime.toLocalDate();
        LocalTime endTime = localEndDateTime.toLocalTime();
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
