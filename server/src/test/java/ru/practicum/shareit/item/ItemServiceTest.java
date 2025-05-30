package ru.practicum.shareit.item;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.ShareItApp;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.interfaces.ItemService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.interfaces.UserService;

@SpringBootTest(classes = ShareItApp.class)
public class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Test
    void add() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail(genEmail());

        UserDto userSaved = userService.save(user);

        ItemDto item = new ItemDto();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(userSaved.getId());

        ItemDto itemSaved = itemService.add(item, userSaved.getId());
        assertTrue(itemSaved.getId() > 0, "Вещь должна добавиться");
    }

    @Test
    void addNoUser() {
        ItemDto item = new ItemDto();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        assertThrows(Exception.class,
                () -> itemService.add(item, null),
                "Надо указывать пользователя");
    }

    @Test
    void delete() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail(genEmail());

        UserDto userSaved = userService.save(user);

        ItemDto item = new ItemDto();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(userSaved.getId());

        ItemDto itemSaved = itemService.add(item, userSaved.getId());
        assertTrue(itemSaved.getId() > 0, "Вещь должна добавиться");

        itemService.delete(itemSaved.getId());
        assertThrows(Exception.class, () -> itemService.get(item.getId()),
                "Вещь должна удалиться");
    }

    @Test
    void getByUserId() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail(genEmail());

        UserDto userSaved = userService.save(user);

        ItemDto item = new ItemDto();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(userSaved.getId());

        itemService.add(item, userSaved.getId());

        List<ItemDto> itemSaved = itemService.getByUserId(userSaved.getId());
        assertTrue(itemSaved.size() == 1, "Вещь должна добавиться");
    }

    @Test
    void patch() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail(genEmail());

        UserDto userSaved = userService.save(user);

        ItemDto item = new ItemDto();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(userSaved.getId());

        ItemDto itemSaved = itemService.add(item, userSaved.getId());

        ItemDto item2 = new ItemDto();
        item2.setId(itemSaved.getId());
        item2.setName("No name");
        item2.setDescription("New");
        item2.setAvailable(true);
        item2.setOwnerId(userSaved.getId());

        itemService.patch(item2, userSaved.getId());

        ItemDto itemUpdated = itemService.get(itemSaved.getId());
        assertTrue(itemUpdated.getName().equals(item2.getName()), "Имя должно измениться");
        assertTrue(itemUpdated.getDescription().equals(item2.getDescription()), "Описание должно измениться");
    }

    @Test
    void search() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail(genEmail());

        UserDto userSaved = userService.save(user);

        ItemDto item = new ItemDto();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwnerId(userSaved.getId());

        itemService.add(item, userSaved.getId());

        ItemDto item2 = new ItemDto();
        item2.setName("No name");
        item2.setDescription("New");
        item2.setAvailable(true);
        item2.setOwnerId(userSaved.getId());

        itemService.add(item2, userSaved.getId());

        List<ItemDto> items = itemService.findAvailableByNameOrDescription("crIpTi");
        assertTrue(items.size() == 1, "Вещь должна найтись");
    }

    private String genEmail() {
        String emailPrefix = "itemcontrollertest";
        String randomChars = "";
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            randomChars += (char) (random.nextInt(26) + 'a');
        }
        return emailPrefix + randomChars + "@test.test";
    }
}