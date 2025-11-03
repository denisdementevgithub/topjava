package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.ActiveORMProfileResolver;
import ru.javawebinar.topjava.service.BaseUserServiceTest;


@ActiveProfiles(resolver = ActiveORMProfileResolver.class)
public class JpaUserServiceTest extends BaseUserServiceTest {
}
