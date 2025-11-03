package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveORMProfileResolver;
import ru.javawebinar.topjava.service.BaseMealServiceTest;

@ActiveProfiles(resolver = ActiveORMProfileResolver.class)
public class JpaMealServiceTest extends BaseMealServiceTest {
}
