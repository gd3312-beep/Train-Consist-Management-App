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
