package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    public void createOrUpdate(@Valid UserTo userTo, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            result.getFieldErrors().forEach(fe->sb.append(fe.getField()).append(" ").append(fe.getDefaultMessage()).append("; "));
            throw new IllegalRequestDataException(sb.toString());
        }
        if (userTo.isNew()) {
            super.create(userTo);
        } else {
            super.update(userTo, userTo.id());
        }
    }

    /*
@PostMapping(consumes = "application/json")
public ResponseEntity<User> createOrUpdate(@Valid @RequestBody UserTo userTo) {

    if (userTo.isNew()) {
        User user = super.create(userTo);
        String REST_URL = "/admin/users";
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(user);
    } else {
        super.update(userTo, userTo.id());

    }
    return null;
}

     */


    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}
