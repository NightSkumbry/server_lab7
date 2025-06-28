package controls;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import consoles.Printer;
import models.Flat;

/**
 * The CollectionControl class manages a collection of {@link Flat} objects.
 * It provides methods for adding, removing, retrieving, and performing operations
 * on the collection, while ensuring data integrity.
 */
public class CollectionControl {

    /**
     * The collection of {@link Flat} objects.
     */
    private Set<Flat> flatsCollection = new HashSet<>();

    /**
     * The initialization time of the collection.
     */
    private LocalDateTime initTime;

    /**
     * The last time the collection was saved.
     */
    private LocalDateTime lastSaveTime;

    /**
     * The set of occupied IDs to ensure unique IDs for flats.
     */
    private Set<Long> occupiedIDs = new HashSet<>();

    private DataBaseControl dataBaseControl;


    private final Object collectionLock = new Object();

    /**
     * Constructs a new CollectionControl instance and initializes the collection's metadata.
     */
    public CollectionControl(Printer printer ) {
        super();
        dataBaseControl = new DataBaseControl(printer);

        try {
            flatsCollection = dataBaseControl.load();
        }
        catch (SQLException e) {
            printer.printError("Ошибка загрузки коллекции из базы данных: " + e.getMessage());
            flatsCollection = new HashSet<>();
        }

        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                occupiedIDs.add(flat.getId());
            }
        }

        initTime = LocalDateTime.now();
        lastSaveTime = null;
    }


    /**
     * Retrieves the initialization time of the collection.
     *
     * @return The {@link LocalDateTime} of the collection's initialization.
     */
    public LocalDateTime getInitTime() {
        return initTime;
    }

    public DataBaseControl getDataBaseControl() {
        return dataBaseControl;
    }

    /**
     * Retrieves the last save time of the collection.
     *
     * @return The {@link LocalDateTime} of the last save operation, or null if not saved yet.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Updates the last save time of the collection to the current time.
     */
    public void updateLastSaveTime() {
        lastSaveTime = LocalDateTime.now();
    }

    /**
     * Retrieves the current collection of flats.
     *
     * @return A set of {@link Flat} objects in the collection.
     */
    public Set<Flat> getCollection() {
        synchronized (collectionLock) {
            return new HashSet<>(flatsCollection);
        }
    }

    /**
     * Retrieves a {@link Flat} object by its ID.
     *
     * @param id The ID of the flat to retrieve.
     * @return The {@link Flat} object with the specified ID, or null if not found.
     */
    public Flat getById(Long id) {
        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                if (flat.getId() == id) return flat;
            }
        }
        return null;
    }

    /**
     * Removes a {@link Flat} object by its ID.
     *
     * @param id The ID of the flat to remove.
     * @return The removed {@link Flat} object, or null if not found.
     */
    public Flat removeById(Long id, String userName) {
        int userId;
        try {
            userId = dataBaseControl.getUserIdFromUsername(userName);
        }
        catch (Exception e) {
            System.out.println("Ошибка получения ID пользователя: " + e.getMessage());
            return null;
        }
        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                if (flat.getId() == id) {
                    try {
                        if (dataBaseControl.removeFlat(id, userId)) {
                            flatsCollection.remove(flat);
                            occupiedIDs.remove(id);
                            return flat;
                        }
                    }
                    catch (SQLException e) {
                        System.out.println("Ошибка удаления квартиры из базы данных: " + e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Clears the entire collection, resetting all associated metadata.
     */
    public void clear(String userName) {
        Long[] idsCopy;
        synchronized (collectionLock) {
            idsCopy = occupiedIDs.toArray(new Long[occupiedIDs.size()]);
        }
        for (long id : idsCopy) {
            removeById(id, userName);
        }
    }

    /**
     * Adds a {@link Flat} to the collection, replacing any existing flat with the same ID.
     *
     * @param flat The {@link Flat} object to add.
     */
    public boolean add(Flat flat, String userName) {
        int userId;
        try {
            userId = dataBaseControl.getUserIdFromUsername(userName);
        }
        catch (Exception e) {
            System.out.println("Ошибка получения ID пользователя: " + e.getMessage());
            return false;
        }
        try {
            Long flat_id = dataBaseControl.insertFlat(flat, userId);
            if (flat_id != null) {
                flat.setId(flat_id);
                synchronized (collectionLock) {
                    flatsCollection.add(flat);
                    occupiedIDs.add(flat.getId());
                }
                return true;
            }
        }
        catch (SQLException e) {
            System.out.println("Ошибка добавления квартиры в базу данных: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Flat flat, String userName) {
        int userId;
        try {
            userId = dataBaseControl.getUserIdFromUsername(userName);
        }
        catch (Exception e) {
            System.out.println("Ошибка получения ID пользователя: " + e.getMessage());
            return false;
        }
        try {
            if (dataBaseControl.modifyFlat(flat.getId(), flat, userId)) {
                synchronized (collectionLock) {
                    flatsCollection.removeIf(f -> f.getId().equals(flat.getId()));
                    flatsCollection.add(flat);
                }
                return true;
            }
        }
        catch (SQLException e) {
            System.out.println("Ошибка добавления квартиры в базу данных: " + e.getMessage());
        }
        return false;
    }

    /**
     * Determines whether a given flat would be the maximum in the collection.
     *
     * @param base The {@link Flat} to compare.
     * @return True if the flat would be the maximum, false otherwise.
     */
    public boolean willBeMax(Flat base) {
        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                if (flat.compareTo(base) >= 0) return false;
            }
        }
        return true;
    }

    /**
     * Removes all flats in the collection that are greater than the specified flat.
     *
     * @param base The {@link Flat} to compare.
     * @return The number of flats removed.
     */
    public long removeGreater(Flat base, String userName) {
        List<Long> toRemove = new ArrayList<>();
        long count = 0;
        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                if (flat.compareTo(base) > 0) {
                    toRemove.add(flat.getId());
                }
            }
        }
        for (Long id : toRemove) {
            if (removeById(id, userName) != null) {
                count += 1;
            }
        }
        return count;
    }
    
    /**
     * Removes all flats in the collection that are smaller than the specified flat.
     *
     * @param base The {@link Flat} to compare.
     * @return The number of flats removed.
     */
    public long removeLower(Flat base, String userName) {
        List<Long> toRemove = new ArrayList<>();
        long count = 0;
        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                if (flat.compareTo(base) < 0) {
                    toRemove.add(flat.getId());
                }
            }
        }
        for (Long id : toRemove) {
            if (removeById(id, userName) != null) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Retrieves the {@link Flat} with the latest creation date in the collection.
     *
     * @return The {@link Flat} with the latest creation date, or null if the collection is empty.
     */
    public Flat maxByCreationDate() {
        synchronized (collectionLock) {
            if (flatsCollection.isEmpty()) return null;
            return flatsCollection.stream().max((f1, f2) -> f1.getCreationDate().compareTo(f2.getCreationDate())).get();
        }
    }

    /**
     * Returns a string representation of the collection sorted in descending order.
     *
     * @return A string representation of the collection in descending order, or a message if empty.
     */
    public String toDescendingString() {
        synchronized (collectionLock) {
            if (flatsCollection.isEmpty()) return "\nКоллекция пуста!";

            String result = "";
            for (Flat flat : flatsCollection.stream().sorted((f1, f2) -> f2.compareTo(f1)).collect(Collectors.toList())) {
                result += "\n" + flat + "\n";
            }
            return result;
        }
    }

    /**
     * Retrieves a set of unique values for the time to metro by transport across all flats.
     *
     * @return A set of unique time values as {@link Float}.
     */
    public Set<Float> getUniqueTimeToMetroByTransport() {
        Set<Float> res = new HashSet<>();
        synchronized (collectionLock) {
            for (Flat flat : flatsCollection) {
                res.add(flat.getTimeToMetroByTransport());
            }
        }
        return res;
    }

    /**
     * Returns a string representation of the collection sorted by flat IDs.
     *
     * @return A string representation of the collection, or a message if empty.
     */
    @Override
    public String toString() {
        synchronized (collectionLock) {
            if (flatsCollection.isEmpty()) return "\nКоллекция пуста!";

            String result = "";
            for (Flat flat : flatsCollection.stream().sorted((f1, f2) -> f1.getId().compareTo(f2.getId())).toList()) {
                result += "\n" + flat + "\n";
            }
            return result;
        }
    }


    
}
