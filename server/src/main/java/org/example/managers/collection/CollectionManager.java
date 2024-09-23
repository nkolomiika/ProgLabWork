package org.example.managers.collection;

import lombok.Getter;
import org.example.exceptions.collection.EmptyCollectionException;
import org.example.exceptions.collection.InvalidLabIdException;
import org.example.exceptions.process.CannotAddLabWorkRuntimeException;
import org.example.managers.db.DatabaseManager;
import org.example.managers.file.DumpManager;
import org.example.model.data.Difficulty;
import org.example.model.data.LabWork;
import org.example.network.dto.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
public class CollectionManager {
    private ArrayDeque<LabWork> collection = new ArrayDeque<>();
    private final LocalDateTime initializationDate;

    public CollectionManager() {
        this.initializationDate = LocalDateTime.now();
    }

    public void saveCollection() throws IOException {
        DumpManager.writeIntoFile(this.collection);
    }

    public void add(LabWork labWork, User user) throws RuntimeException {
        DatabaseManager.addLabWork(labWork, user);
        collection.add(labWork);
    }

    public void addIfMin(LabWork labWork, User user) throws RuntimeException {
        LabWork minLab = collection.stream()
                .min(LabWork::compareTo)
                .orElse(null);

        if (minLab == null
                || minLab.compareTo(labWork) <= 0) {
            DatabaseManager.addLabWork(labWork, user);
            collection.add(labWork);
        }
        throw new CannotAddLabWorkRuntimeException();
    }

    public void update(long id, LabWork labWork, User user) throws RuntimeException {
        Optional<LabWork> optionalLabWork = collection.stream()
                .filter(lw -> lw.getId() == id)
                .findFirst();

        if (optionalLabWork.isEmpty()) throw new InvalidLabIdException();
        labWork.setId(optionalLabWork.get().getId());

        DatabaseManager.updateLabWorkByIdAndUserId(id, labWork, user);
        collection.remove(optionalLabWork.get());
        collection.add(labWork);
    }

    public void removeById(int id, User user) throws RuntimeException {
        DatabaseManager.deleteLabWorkById(id, user);
        collection.remove(getLabById(id));
    }

    private LabWork getLabById(long id) throws RuntimeException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        return collection.stream()
                .filter(lw -> lw.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    throw new InvalidLabIdException();
                });
    }

    public void removeFirst(User user) throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        LabWork labWork = collection.peekFirst();

        DatabaseManager.deleteLabWorkById(labWork.getId(), user);
        collection.removeFirst();
    }


    public void removeGreater(LabWork lab, User user) throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();

        collection.removeAll(collection.stream()
                .filter(lw -> lw.compareTo(lab) < 0)
                .map(LabWork::getId)
                .map(LabWork::new)
                .toList()
        );

        collection.stream()
                .filter(lw -> lw.compareTo(lab) < 0)
                .map(LabWork::getId)
                .forEach(id -> DatabaseManager.deleteLabWorkById(id, user));
    }

    public int sumOfMinimumPoint() {
        return collection.stream()
                .mapToInt(LabWork::getMinimalPoint)
                .sum();
    }

    public String filterByDifficulty(Difficulty difficulty) throws EmptyCollectionException {
        var tmp = new ArrayDeque<>(collection).stream()
                .filter(lw -> lw.getDifficulty().getLevel() == difficulty.getLevel())
                .collect(Collectors.toCollection(ArrayDeque::new));
        return DumpManager.collectionToJson(tmp);
    }

    public int collectionSize() {
        return collection.size();
    }

    public void loadCollection(User user) throws SQLException {
        collection = DatabaseManager.loadCollection(user);
    }

    public void clearCollectionByExit() {
        collection.clear();
    }

    public void clearCollection(User user) throws RuntimeException {
        DatabaseManager.deleteAllLabWorksByUserId(user);
        collection.clear();
    }

    public String convertCollectionToJson() {
        return DumpManager.collectionToJson(collection);
    }

    public String info() {
        return "Collection type: " + collection.getClass().getSimpleName() +
                "\nDate of init: " + initializationDate +
                "\nCollection size: " + collectionSize();
    }
}