package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveORMProfileResolver;
import ru.javawebinar.topjava.service.BaseUserServiceTest;

@ActiveProfiles(resolver = ActiveORMProfileResolver.class)
public class DataJpaUserServiceTest extends BaseUserServiceTest {
}
