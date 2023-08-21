package com.medical_drones.service;

import com.medical_drones.exception.DroneAlreadyExistsException;
import com.medical_drones.exception.DroneNotFoundException;
import com.medical_drones.exception.DroneUnableToBeLoadedException;
import com.medical_drones.model.Drone;
import com.medical_drones.model.DroneState;
import com.medical_drones.model.Medication;
import com.medical_drones.repository.DroneRepository;
import com.medical_drones.repository.MedicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DispatcherService {

    private static final int BATTERY_LEVEL_THRESHOLD = 25;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Transactional
    public List<Medication> loadDroneWithMedications(String serialNumber, List<Medication> medications) {
        Drone drone = getDroneBySerialNumber(serialNumber);

        checkIfDroneCapableToBeLoaded(drone, medications);

        medications.forEach(medication ->  medication.setDrone(drone));

        drone.setState(DroneState.LOADING);
        this.droneRepository.save(drone);

        return medicationRepository.saveAll(medications);
    }

    public Drone save(Drone newDrone) throws DroneAlreadyExistsException {
        try {
            return droneRepository.save(newDrone);
        } catch (DataIntegrityViolationException ex) {
            throw new DroneAlreadyExistsException("Drone with this serial number already exists: " + newDrone.getSerialNumber());
        }
    }

    public Integer getBatteryLevel(String serialNumber) {
        Drone drone = getDroneBySerialNumber(serialNumber);
        return drone.getBatteryLevel();
    }

    public List<Medication> getLoadedMedications(String serialNumber) {
        Drone drone = getDroneBySerialNumber(serialNumber);
        return drone.getLoadedMedications();
    }

    /**
     * Available for loading - loaded less than weight_limit, and battery_level >= 25% (25 and more)
     * @return list of available to loading drones
     */
    public List<Drone> getAvailableForLoading() {
        return droneRepository.findAvailableForLoading();
    }

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    /**
     * Check if this particular drone can be loaded with these medications (if medications summary weight less than drone can take)
     * @param drone - drone to check
     * @param medications - medications to check
     */
    public void checkIfDroneCapableToBeLoaded(Drone drone, List<Medication> medications) {
        if (drone.getBatteryLevel() < BATTERY_LEVEL_THRESHOLD) {
            throw new DroneUnableToBeLoadedException("Drone '" + drone.getSerialNumber() +
                    "' could not be loaded because it's battery level is " + drone.getBatteryLevel() + "%");
        }

        double totalWeight = getTotalWeight(medications);

        double loadedWeight = getTotalWeight(drone.getLoadedMedications());

        double upToLimit = drone.getWeightLimit() - loadedWeight;

        if (upToLimit < totalWeight) {
            throw new DroneUnableToBeLoadedException("Drone '" + drone.getSerialNumber() +
                    "' could not be loaded because up to it's limit lefts " + upToLimit +
                    " and medications total weight is " + totalWeight);
        }
    }

    /**
     * Returns total weight of medications in list
     */
    public double getTotalWeight(List<Medication> medications) {
        double result = 0.0;
        for (Medication medication : medications) {
            result += medication.getWeight();
        }
        return result;
    }

    /**
     * Returns drone by serialNumber, otherwise throws DroneNotFoundException to handle in DispatchControllerExceptionHandler
     * @return - drone by serialNumber
     */
    public Drone getDroneBySerialNumber(String serialNumber) throws DroneNotFoundException {
        Optional<Drone> droneOptional = droneRepository.getBySerialNumber(serialNumber);

        return droneOptional.orElseThrow(() ->
                new DroneNotFoundException("Drone with serial number '" + serialNumber + "' was not found!"));
    }
}