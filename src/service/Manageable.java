package service;

import java.util.List;

/**
 * Generic interface defining CRUD operations for manageable entities.
 * Demonstrates abstraction through interfaces and generics.
 *
 * @param <T> The type of entity managed
 * @author Smart Student Management System
 * @version 1.0
 */
public interface Manageable<T> {

    /**
     * Adds a new entity.
     *
     * @param item The entity to add
     * @throws Exception if the operation fails (e.g., duplicate)
     */
    void add(T item) throws Exception;

    /**
     * Updates an existing entity identified by its ID.
     *
     * @param id   The unique identifier
     * @param item The updated entity
     * @throws Exception if the entity is not found
     */
    void update(String id, T item) throws Exception;

    /**
     * Deletes an entity by its ID.
     *
     * @param id The unique identifier of the entity to delete
     * @throws Exception if the entity is not found
     */
    void delete(String id) throws Exception;

    /**
     * Searches for an entity by its ID.
     *
     * @param id The unique identifier
     * @return The matching entity
     * @throws Exception if the entity is not found
     */
    T search(String id) throws Exception;

    /**
     * Returns all entities.
     *
     * @return A list of all entities
     */
    List<T> getAll();

    /**
     * Displays all entities in a formatted manner.
     */
    void displayAll();
}
