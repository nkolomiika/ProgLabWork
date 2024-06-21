package org.example.managers.collection;

import lombok.Getter;
import org.example.exceptions.collection.EmptyCollectionException;
import org.example.exceptions.collection.InvalidLabIdException;
import org.example.managers.file.DumpManager;
import org.example.model.Difficulty;
import org.example.model.LabWork;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.stream.Collectors;


@Getter
public class CollectionManager {
    private ArrayDeque<LabWork> collection = new ArrayDeque<>();

    public boolean saveCollection() throws IOException {
        return DumpManager.writeIntoFile(this.collection);
    }

    public boolean add(LabWork labWork) {
        collection.addLast(labWork);
        return true;
    }

    public boolean addIfMin(LabWork labWork) {
        LabWork minLab = collection.stream()
                .min(LabWork::compareTo)
                .orElse(null);

        if (minLab == null
                || minLab.compareTo(labWork) <= 0) {
            collection.addFirst(labWork);
            return true;
        }
        return false;
    }

    public long getMaxId() {
        return collection.stream()
                .mapToLong(LabWork::getId)
                .max()
                .orElse(0);
    }


    public boolean addById(long id, LabWork labWork) {
        /*if (!this.isEmpty()) {
            if (collection.contains(getCollectionById(id))) {
                return false;
            }
            if (this.getCollectionById(id) != null) {
                return false;
            }
        }
        labWork.setId(id);
        collection.add(labWork);
        return true;*/
        return true;
    }

    public boolean removeById(long id) throws InvalidLabIdException, EmptyCollectionException {
        return collection.remove(this.getLabById(id));
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

    public boolean removeFirst() throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        collection.removeFirst();
        return true;
    }


    public boolean removeGreater(LabWork lab) throws EmptyCollectionException {
        if (collection.isEmpty()) throw new EmptyCollectionException();
        collection.removeIf(lw -> lw.compareTo(lab) < 0);
        return true;
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


    public boolean addToCollection(LabWork lab) {
        return collection.add(lab);
    }

    public boolean loadCollection() throws IOException {
        collection = (ArrayDeque<LabWork>) DumpManager.readCollection();
        return true;
    }

    public boolean clearCollection() {
        collection.clear();
        return true;
    }

    public String convertCollectionToJson() {
        return DumpManager.collectionToJson(collection);
    }
}