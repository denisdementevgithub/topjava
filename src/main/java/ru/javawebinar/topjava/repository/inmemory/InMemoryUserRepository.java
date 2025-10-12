package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.ArrayList;
import java.util.Collection;
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
        for (User user : UsersUtil.users) {
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
        return usersMap.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return usersMap.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        System.out.println("users in repo " + usersMap.values());
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        User returnedUser = null;
        for(Integer usersMapKey:usersMap.keySet()) {
            if (usersMap.get(usersMapKey).getEmail().equalsIgnoreCase(email)) {
                returnedUser = usersMap.get(usersMapKey);
            }
        }
        return returnedUser;
    }
}
