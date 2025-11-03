package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveORMProfileResolver;
import ru.javawebinar.topjava.service.BaseMealServiceTest;

@ActiveProfiles(resolver = ActiveORMProfileResolver.class)
public class DataJpaMealServiceTest extends BaseMealServiceTest {
}
