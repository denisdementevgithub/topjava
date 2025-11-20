package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudRepository;
    private final JdbcTemplate jdbcTemplate;


    public DataJpaUserRepository(CrudUserRepository crudRepository, JdbcTemplate jdbcTemplate) {
        this.crudRepository = crudRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public User getWithMeals(int id) {
        Optional<User> userOptional = Optional.of(crudRepository.getWithMeals(id));
        if (!userOptional.isPresent()) {
            return null;
        } else {
            User user = crudRepository.getWithMeals(id);
            List<Role> roles = jdbcTemplate.query("SELECT * FROM user_role WHERE user_id = ?", getResultSetExtractor(),id);
            user.setRoles(roles);
            return user;
        }
        //return crudRepository.getWithMeals(id);

        /*User user = userOptional.get();
        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_role WHERE user_id = ?", getResultSetExtractor(),id);
        user.setRoles(roles);
        return user;

         */
    }

    private ResultSetExtractor<List<Role>> getResultSetExtractor() {
        return new ResultSetExtractor<List<Role>>() {
            @Override
            public List<Role> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Role> list = new ArrayList<>();
                while (rs.next()) {
                    String role = rs.getString("role");
                    list.add(Role.valueOf(role));
                }
                return list;
            }
        };
    }
}
