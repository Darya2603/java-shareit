package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.ShareItApp;

@SpringBootTest(classes = { ShareItApp.class })
@SpringJUnitConfig
class ShareItTests {

    @Test
    void contextLoads() {
    }

}
