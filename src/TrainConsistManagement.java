import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TrainConsistManagement {

    private static final Pattern TRAIN_ID_PATTERN = Pattern.compile("^TRN-\\d{4}$");
    private static final Pattern CARGO_CODE_PATTERN = Pattern.compile("^[A-Z]{3}-\\d{3}$");

    public static void main(String[] args) {
        try {
            Train train = uc1InitializeTrain();
            uc2AddPassengerBogies(train);
            uc3TrackUniqueBogieIds(train);
            uc4MaintainOrderedBogieIds(train);
            uc5PreserveInsertionOrder(train);
        } catch (Exception e) {
            System.out.println("Program stopped: " + e.getMessage());
        }
    }

    private static Train uc1InitializeTrain() {
        printTitle("UC1 - Initialize Train and Display Consist Summary");
        Train train = new Train("TRN-1234");
        System.out.println("Train created with ID: " + train.getTrainId());
        System.out.println("Initial total bogies: " + train.getBogies().size());
        train.displaySummary();
        return train;
    }

    private static void uc2AddPassengerBogies(Train train) throws InvalidCapacityException {
        printTitle("UC2 - Add Passenger Bogies to Train");
        PassengerBogie sleeper = new PassengerBogie("BG101", "Sleeper", 72);
        PassengerBogie acChair = new PassengerBogie("BG102", "AC Chair", 56);
        PassengerBogie firstClass = new PassengerBogie("BG103", "First Class", 24);
        PassengerBogie extraCoach = new PassengerBogie("BG104", "Sleeper", 72);

        train.addBogie(sleeper);
        train.addBogie(acChair);
        train.addBogie(firstClass);
        train.addBogie(extraCoach);

        System.out.println("After adding passenger bogies:");
        train.displaySummary();

        System.out.println("Does BG102 exist? " + train.containsBogie("BG102"));
        System.out.println("Removing BG104: " + train.removeBogie("BG104"));
        System.out.println("After removal:");
        train.displaySummary();
    }

    private static void uc3TrackUniqueBogieIds(Train train) throws InvalidCapacityException {
        printTitle("UC3 - Track Unique Bogie IDs");
        Set<String> uniqueIds = new HashSet<>();

        for (Bogie bogie : train.getBogies()) {
            uniqueIds.add(bogie.getId());
        }

        GoodsBogie goodsOne = new GoodsBogie("BG201", "Rectangular", 80, "COAL", "COL-210");
        GoodsBogie goodsTwo = new GoodsBogie("BG202", "Cylindrical", 65, "PETROLEUM", "PET-450");

        System.out.println("Trying to add duplicate ID BG101: " + uniqueIds.add("BG101"));
        if (uniqueIds.add(goodsOne.getId())) {
            train.addBogie(goodsOne);
            System.out.println(goodsOne.getId() + " added to train");
        }
        if (uniqueIds.add(goodsTwo.getId())) {
            train.addBogie(goodsTwo);
            System.out.println(goodsTwo.getId() + " added to train");
        }

        System.out.println("Unique bogie IDs: " + uniqueIds);
        train.displaySummary();
    }

    private static void uc4MaintainOrderedBogieIds(Train train) {
        printTitle("UC4 - Maintain Ordered Bogie IDs");
        SortedSet<String> orderedIds = new TreeSet<>();

        for (Bogie bogie : train.getBogies()) {
            orderedIds.add(bogie.getId());
        }

        System.out.println("Ordered bogie IDs using TreeSet:");
        for (String id : orderedIds) {
            System.out.println(id);
        }
    }

    private static void uc5PreserveInsertionOrder(Train train) {
        printTitle("UC5 - Preserve Insertion Order of Bogies");
        LinkedHashSet<String> orderedByInsertion = new LinkedHashSet<>();

        for (Bogie bogie : train.getBogies()) {
            orderedByInsertion.add(bogie.getId());
        }

        orderedByInsertion.add("BG102");

        System.out.println("Bogie IDs with insertion order and no duplicates:");
        for (String id : orderedByInsertion) {
            System.out.println(id);
        }
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("==========================================");
        System.out.println(title);
        System.out.println("==========================================");
    }
}

class Train {
    private final String trainId;
    private final List<Bogie> bogies;

    public Train(String trainId) {
        this.trainId = trainId;
        this.bogies = new ArrayList<>();
    }

    public String getTrainId() {
        return trainId;
    }

    public List<Bogie> getBogies() {
        return bogies;
    }

    public void addBogie(Bogie bogie) {
        bogies.add(bogie);
    }

    public boolean containsBogie(String bogieId) {
        for (Bogie bogie : bogies) {
            if (bogie.getId().equals(bogieId)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeBogie(String bogieId) {
        for (int i = 0; i < bogies.size(); i++) {
            if (bogies.get(i).getId().equals(bogieId)) {
                bogies.remove(i);
                return true;
            }
        }
        return false;
    }

    public void displaySummary() {
        System.out.println("Train ID: " + trainId);
        System.out.println("Total bogies: " + bogies.size());

        int passengerCount = 0;
        int goodsCount = 0;

        for (Bogie bogie : bogies) {
            if (bogie instanceof PassengerBogie) {
                passengerCount++;
            } else if (bogie instanceof GoodsBogie) {
                goodsCount++;
            }
        }

        System.out.println("Passenger bogies: " + passengerCount);
        System.out.println("Goods bogies: " + goodsCount);

        if (bogies.isEmpty()) {
            System.out.println("No bogies attached yet");
        } else {
            for (Bogie bogie : bogies) {
                System.out.println(bogie);
            }
        }
    }
}

abstract class Bogie {
    private final String id;
    private final int capacity;

    public Bogie(String id, int capacity) throws InvalidCapacityException {
        if (capacity <= 0) {
            throw new InvalidCapacityException("Capacity must be greater than zero for bogie " + id);
        }
        this.id = id;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public abstract String getCategory();

    public abstract String getType();
}

class PassengerBogie extends Bogie {
    private final String coachType;

    public PassengerBogie(String id, String coachType, int seatCapacity) throws InvalidCapacityException {
        super(id, seatCapacity);
        this.coachType = coachType;
    }

    public String getCategory() {
        return "Passenger";
    }

    public String getType() {
        return coachType;
    }

    public String toString() {
        return getId() + " | Passenger | " + coachType + " | Seats: " + getCapacity();
    }
}

class GoodsBogie extends Bogie {
    private final String shape;
    private String cargoType;
    private String cargoCode;

    public GoodsBogie(String id, String shape, int loadCapacity, String cargoType, String cargoCode) throws InvalidCapacityException {
        super(id, loadCapacity);
        this.shape = shape;
        this.cargoType = cargoType;
        this.cargoCode = cargoCode;
    }

    public String getShape() {
        return shape;
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }

    public void setCargoCode(String cargoCode) {
        this.cargoCode = cargoCode;
    }

    public String getCategory() {
        return "Goods";
    }

    public String getType() {
        return shape;
    }

    public String toString() {
        return getId() + " | Goods | " + shape + " | Load: " + getCapacity() + " | Cargo: " + cargoType + " | Code: " + cargoCode;
    }
}

class InvalidCapacityException extends Exception {
    public InvalidCapacityException(String message) {
        super(message);
    }
}

class UnsafeCargoAssignmentException extends Exception {
    public UnsafeCargoAssignmentException(String message) {
        super(message);
    }
}
