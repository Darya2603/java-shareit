package ru.practicum.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
        @Query("SELECT i.available FROM Item i WHERE i.id = :itemId")
        Boolean findAvailableByItemId(@Param("itemId") Long itemId);

        List<Item> getByIdIn(List<Long> itemIds);

        List<Item> findByOwnerId(Long ownerId);

        List<Item> findByRequestIdIn(List<Long> requestIds);

        List<Item> findByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(
                        String name,
                        String description);
}