package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {

            //return null;
        }
        int id = user.getId();
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", id);
        SimpleJdbcInsert insertUserRole = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_role")
                .usingColumns("user_id", "role");
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (Role role:user.getRoles()) {
                Map<String, Object> roleParams = new HashMap<>();
                roleParams.put("user_id", user.getId());
                if (role.equals(Role.USER)) {
                    roleParams.put("role", "USER");
                } else if (role.equals(Role.ADMIN)) {
                    roleParams.put("role", "ADMIN");
                }
                insertUserRole.execute(roleParams);
            }
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id WHERE id=?", getResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id WHERE email=?", getResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }


    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id ORDER BY name, email",
                //ROW_MAPPER);
                getResultSetExtractor());
    }
    private ResultSetExtractor<List<User>> getResultSetExtractor() {
        return new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, User> map = new HashMap<>();
                while(rs.next()){
                    int userId = rs.getInt("id");
                    User user = null;
                    if (map.containsKey(userId)) {
                        user = map.get(userId);
                    } else  {
                        user = new User();
                        user.setId(userId);
                        user.setEmail(rs.getString("email"));
                        user.setPassword(rs.getString("password"));
                        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                        user.setName(rs.getString("name"));
                        user.setEnabled(rs.getObject("enabled", Boolean.class));
                        user.setRegistered(rs.getObject("registered", Date.class));
                        user.setRoles(new ArrayList<Role>());
                        map.put(userId, user);
                    }
                    String role = rs.getString("role");
                    if (role != null) {
                        if (role.equalsIgnoreCase("ADMIN")) {
                            user.getRoles().add(Role.ADMIN);
                        } else if (role.equalsIgnoreCase("USER")) {
                            user.getRoles().add(Role.USER);
                        }
                    }
                }
                return map.values().stream().toList().stream()
                        .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                        .collect(Collectors.toList());
            }
        };
    }
}
