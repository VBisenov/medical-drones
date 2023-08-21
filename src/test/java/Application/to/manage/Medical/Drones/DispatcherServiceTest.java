package Application.to.manage.Medical.Drones;

import com.medical_drones.MedicalDronesApplication;
import com.medical_drones.exception.DroneNotFoundException;
import com.medical_drones.exception.DroneUnableToBeLoadedException;
import com.medical_drones.model.Drone;
import com.medical_drones.model.DroneModel;
import com.medical_drones.model.DroneState;
import com.medical_drones.model.Medication;
import com.medical_drones.repository.DroneRepository;
import com.medical_drones.service.DispatcherService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = MedicalDronesApplication.class)
public class DispatcherServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @InjectMocks
    private DispatcherService dispatcherService;

    @Test
    public void testGetDroneBySerialNumber_Success() {
        Drone drone = new Drone("SN1", DroneModel.MIDDLEWEIGHT, 300, 100, DroneState.IDLE);
        when(droneRepository.getBySerialNumber("SN1")).thenReturn(Optional.of(drone));

        Drone result = dispatcherService.getDroneBySerialNumber("SN1");

        assertNotNull(result);
        assertEquals("SN1", result.getSerialNumber());
    }

    @Test
    public void testGetDroneBySerialNumber_NotFound() {
        when(droneRepository.getBySerialNumber("SN1")).thenReturn(Optional.empty());

        assertThrows(DroneNotFoundException.class, () -> dispatcherService.getDroneBySerialNumber("SN1"));
    }

    @Test
    public void testGetTotalWeight() {
        List<Medication> medications = new ArrayList<>();
        medications.add(new Medication(1L, "Paracetamol", 50.0, "PCTM", "data:image/jpeg;base64,/"));
        medications.add(new Medication(2L, "Tramadol", 30.0, "TRMDL", "base64:/pathToImage"));

        double totalWeight = dispatcherService.getTotalWeight(medications);

        assertEquals(80.0, totalWeight);
    }

    @Test
    public void testCheckIfDroneCapableToBeLoaded_Success() {
        Drone drone = new Drone("SN1", DroneModel.MIDDLEWEIGHT, 300, 30, DroneState.IDLE);

        List<Medication> medications = new ArrayList<>();
        medications.add(new Medication(1L, "Paracetamol", 50.0, "PCTM", "data:image/jpeg;base64,/"));
        medications.add(new Medication(2L, "Tramadol", 30.0, "TRMDL", "base64:/pathToImage"));

        assertDoesNotThrow(() -> dispatcherService.checkIfDroneCapableToBeLoaded(drone, medications));
    }

    @Test
    public void testCheckIfDroneCapableToBeLoaded_BatteryLow() {
        Drone drone = new Drone("SN1", DroneModel.LIGHTWEIGHT, 150, 23, DroneState.IDLE);

        List<Medication> medications = new ArrayList<>();
        medications.add(new Medication(1L, "Paracetamol", 50.0, "PCTM", "data:image/jpeg;base64,/"));
        medications.add(new Medication(2L, "Paracetamol", 50.0, "PCTM", "data:image/jpeg;base64,/"));
        medications.add(new Medication(3L, "Tramadol", 50.0, "TRMDL", "base64:/pathToImage"));
        medications.add(new Medication(4L, "Tramadol", 50.0, "TRMDL", "base64:/pathToImage"));

        assertThrows(DroneUnableToBeLoadedException.class, () -> dispatcherService.checkIfDroneCapableToBeLoaded(drone, medications));
    }

    @Test
    public void testCheckIfDroneCapableToBeLoaded_CompletelyFilled() {
        Drone drone = new Drone("SN1", DroneModel.LIGHTWEIGHT, 150, 23, DroneState.IDLE);

        List<Medication> medications = new ArrayList<>();
        medications.add(new Medication(1L, "Paracetamol", 50.0, "PCTM", "data:image/jpeg;base64,/"));

        assertThrows(DroneUnableToBeLoadedException.class, () -> dispatcherService.checkIfDroneCapableToBeLoaded(drone, medications));
    }

    @Test
    public void testGetLoadedMedications() {
        Drone drone = new Drone("SN1", DroneModel.LIGHTWEIGHT, 150, 23, DroneState.IDLE);
        Medication medication1 = new Medication(1L, "Paracetamol", 50.0, "PCTM", "data:image/jpeg;base64,/");
        Medication medication2 = new Medication(2L, "Tramadol", 30.0, "TRMDL", "base64:/pathToImage");
        drone.getLoadedMedications().add(medication1);
        drone.getLoadedMedications().add(medication2);

        when(droneRepository.getBySerialNumber("SN1")).thenReturn(Optional.of(drone));

        List<Medication> loadedMedications = dispatcherService.getLoadedMedications("SN1");

        assertEquals(2, loadedMedications.size());
        assertTrue(loadedMedications.contains(medication1));
        assertTrue(loadedMedications.contains(medication2));
    }

    @Test
    public void testGetBatteryLevel() {
        Drone drone = new Drone("SN1", DroneModel.LIGHTWEIGHT, 150, 50, DroneState.IDLE);
        when(droneRepository.getBySerialNumber("SN1")).thenReturn(Optional.of(drone));

        Integer batteryLevel = dispatcherService.getBatteryLevel("SN1");

        assertEquals(50, batteryLevel);
    }
}