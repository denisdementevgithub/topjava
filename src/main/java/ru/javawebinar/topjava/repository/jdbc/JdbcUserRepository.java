package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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

import javax.validation.Validator;
import java.sql.PreparedStatement;
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
    private final ResultSetExtractor<List<User>> resultSetExtractor;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
        this.resultSetExtractor = getResultSetExtractor();
    }

    @Override
    @Transactional
    public User save(User user) {
        UtilsForJdbc.makeValidation(validator, user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            int id = user.getId();
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", id);
            jdbcTemplate.batchUpdate("UPDATE users SET name=?, email=?, password=?, calories_per_day=?, enabled=? WHERE id=?",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, user.getName());
                            ps.setString(2, user.getEmail());
                            ps.setString(3, user.getPassword());
                            ps.setInt(4, user.getCaloriesPerDay());
                            ps.setBoolean(5, user.isEnabled());
                            ps.setInt(6, user.getId());
                        }

                        @Override
                        public int getBatchSize() {
                            return 1;
                        }
                    });
        }
        List<Role> roles = user.getRoles().stream().toList();
        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, roles.get(i).toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
        return user;

            /*
            if (
                    namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {

                //return null;
            }
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
         */
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id WHERE id=?", resultSetExtractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id WHERE email=?", resultSetExtractor, email);
        return DataAccessUtils.singleResult(users);
    }


    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id ORDER BY name, email",
                //ROW_MAPPER);
                resultSetExtractor);
    }

    private ResultSetExtractor<List<User>> getResultSetExtractor() {
        return new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, User> map = new LinkedHashMap<>();
                while (rs.next()) {
                    int userId = rs.getInt("id");
                    User user;
                    if (map.containsKey(userId)) {
                        user = map.get(userId);
                    } else {
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
                        user.getRoles().add(Role.valueOf(role));
                    }
                }
                return map.values().stream().toList();
            }
        };
    }
}
