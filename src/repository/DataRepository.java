package repository;

import java.util.List;

/**
 * Generic interface for data repositories.
 * Demonstrates abstraction through generics and interfaces.
 * Any entity that needs persistence must implement this contract.
 *
 * @param <T> The type of entity managed by this repository
 * @author Smart Student Management System
 * @version 1.0
 */
public interface DataRepository<T> {

    /**
     * Persists the entire list of items to storage (full overwrite).
     *
     * @param items The items to save
     */
    void saveAll(List<T> items);

    /**
     * Loads all items from storage.
     *
     * @return A list of items (empty if none exist)
     */
    List<T> loadAll();

    /**
     * Checks whether an item with the given identifier exists.
     *
     * @param id The unique identifier to check
     * @return true if the item exists
     */
    boolean exists(String id);
}
