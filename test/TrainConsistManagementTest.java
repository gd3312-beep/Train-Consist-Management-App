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

}
