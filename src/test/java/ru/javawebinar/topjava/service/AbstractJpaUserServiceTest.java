package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.JpaUtil;

public abstract class AbstractJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    protected JpaUtil jpaUtil;

    @Override
    public void setup() {
        super.setup();
        jpaUtil.clear2ndLevelHibernateCache();
    }
}
