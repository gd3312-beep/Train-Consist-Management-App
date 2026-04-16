import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainConsistManagementTest {

    @Test
    void testFilterPassengerBogies() throws InvalidCapacityException {
        List<Bogie> bogies = Arrays.asList(
                new PassengerBogie("BG101", "Sleeper", 72),
                new PassengerBogie("BG102", "AC Chair", 56),
                new PassengerBogie("BG103", "First Class", 24),
                new GoodsBogie("BG201", "Rectangular", 80, "COAL", "COL-210")
        );

        List<PassengerBogie> result = TrainConsistManagement.filterPassengerBogies(bogies, 50);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(bogie -> bogie.getCapacity() >= 50));
    }

    @Test
    void testGroupBogiesByType() throws InvalidCapacityException {
        List<Bogie> bogies = Arrays.asList(
                new PassengerBogie("BG101", "Sleeper", 72),
                new PassengerBogie("BG102", "Sleeper", 68),
                new PassengerBogie("BG103", "AC Chair", 56)
        );

        Map<String, List<Bogie>> grouped = TrainConsistManagement.groupBogiesByType(bogies);

        assertEquals(2, grouped.get("Sleeper").size());
        assertEquals(1, grouped.get("AC Chair").size());
    }

    @Test
    void testCountTotalSeats() throws InvalidCapacityException {
        List<Bogie> bogies = Arrays.asList(
                new PassengerBogie("BG101", "Sleeper", 72),
                new PassengerBogie("BG102", "AC Chair", 56),
                new GoodsBogie("BG201", "Rectangular", 80, "COAL", "COL-210")
        );

        int totalSeats = TrainConsistManagement.countTotalSeats(bogies);

        assertEquals(128, totalSeats);
    }

    @Test
    void testValidateTrainAndCargoCodes() {
        assertTrue(TrainConsistManagement.isValidTrainId("TRN-1234"));
        assertFalse(TrainConsistManagement.isValidTrainId("TRN1234"));
        assertTrue(TrainConsistManagement.isValidCargoCode("PET-450"));
        assertFalse(TrainConsistManagement.isValidCargoCode("pet-450"));
    }

    @Test
    void testFindUnsafeGoodsBogies() throws InvalidCapacityException {
        List<GoodsBogie> goodsBogies = Arrays.asList(
                new GoodsBogie("BG201", "Rectangular", 80, "COAL", "COL-210"),
                new GoodsBogie("BG202", "Cylindrical", 65, "PETROLEUM", "PET-450"),
                new GoodsBogie("BG203", "Cylindrical", 70, "COAL", "COL-333")
        );

        List<GoodsBogie> unsafeBogies = TrainConsistManagement.findUnsafeGoodsBogies(goodsBogies);

        assertEquals(1, unsafeBogies.size());
        assertEquals("BG203", unsafeBogies.get(0).getId());
    }

}
