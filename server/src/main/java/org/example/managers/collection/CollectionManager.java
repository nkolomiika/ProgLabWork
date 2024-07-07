package org.example.managers.collection;

import lombok.Getter;
import org.example.exceptions.collection.EmptyCollectionException;
import org.example.exceptions.collection.InvalidLabIdException;
import org.example.exceptions.process.CannotAddLabWorkRuntimeException;
import org.example.managers.file.DumpManager;
import org.example.model.data.Difficulty;
import org.example.model.data.IdCounter;
import org.example.model.data.LabWork;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.StringJoiner;
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

    public void add(LabWork labWork) {
        collection.addLast(labWork);
    }

    public void addIfMin(LabWork labWork) throws CannotAddLabWorkRuntimeException {
        LabWork minLab = collection.stream()
                .min(LabWork::compareTo)
                .orElse(null);

        if (minLab == null
                || minLab.compareTo(labWork) <= 0) {
            collection.addFirst(labWork);
        }
        throw new CannotAddLabWorkRuntimeException();
    }

    public long getMaxId() {
        return collection.stream()
                .mapToLong(LabWork::getId)
                .max()
                .orElse(0);
    }


    public void update(long id, LabWork labWork) throws InvalidLabIdException {
        Optional<LabWork> optionalLabWork = collection.stream()
                .filter(lw -> lw.getId() == id)
                .findFirst();

        if (optionalLabWork.isEmpty()) throw new InvalidLabIdException();

        LabWork lab = optionalLabWork.get();
        collection.remove(lab);
        collection.add(labWork);
        labWork.setId(id);
    }

    public void removeById(long id) throws InvalidLabIdException, EmptyCollectionException {
        collection.remove(this.getLabById(id));
    }

    public LabWork getLabById(long id) throws InvalidLabIdException, EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        return collection.stream()
                .filter(lw -> lw.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    throw new InvalidLabIdException();
                });
    }

    public void removeFirst() throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        collection.removeFirst();
    }


    public void removeGreater(LabWork lab) throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        collection.removeIf(lw -> lw.compareTo(lab) < 0);
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

    public void loadCollection() throws IOException {
        collection = (ArrayDeque<LabWork>) DumpManager.readCollection();
        collection.forEach(labWork -> labWork.setId(IdCounter.getNextIdAndIncrement()));
    }

    public void clearCollection() {
        collection.clear();
    }

    public String convertCollectionToJson() {
        return DumpManager.collectionToJson(collection);
    }

    public String info() {
        return "[" +
                "\nCollection type: " + collection.getClass().getSimpleName() +
                "\nDate of init: " + initializationDate +
                "\nCollection size: " + collectionSize() +
                ']';
    }
}