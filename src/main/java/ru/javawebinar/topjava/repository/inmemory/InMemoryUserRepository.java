package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> usersMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        List<User> users = Arrays.asList(
                new User(null,"Иван", "ivan@mail.ru", "123", Role.ADMIN),
                new User(null, "Дмитрий", "dmitrii@mail.ru", "123", Role.USER, Role.ADMIN),
                new User(null, "Антон", "anton@mail.ru", "123", Role.USER)
        );
        for (User user : users) {
            save(user);
        }
    }


    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return usersMap.remove(id) != null;
    }

    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            usersMap.put(user.getId(), user);
            return user;
        }
        // handle case: update, but not present in storage
        return usersMap.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return usersMap.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = new ArrayList<>(usersMap.values());
        System.out.println("users in repo " + users);
        users.sort((u1, u2)->u1.getName().compareTo(u2.getName()));
        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        User returnedUser = null;
        for(User user: usersMap.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                returnedUser = user;
            }
        }
        return returnedUser;
    }
}
