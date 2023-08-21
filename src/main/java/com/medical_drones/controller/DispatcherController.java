package com.medical_drones.controller;

import com.medical_drones.model.Drone;
import com.medical_drones.model.Medication;
import com.medical_drones.service.DispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("drones")
public class DispatcherController {

    @Autowired
    private DispatcherService dispatcherService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Drone> registerDrone(@RequestBody Drone newDrone) {
        return ResponseEntity.ok(dispatcherService.save(newDrone));
    }

    @PostMapping(value = "/{serialNumber}/load",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Medication>> loadingDrone(@RequestBody List<Medication> medications, @PathVariable String serialNumber) {
        return ResponseEntity.ok(dispatcherService.loadDroneWithMedications(serialNumber, medications));
    }

    @GetMapping(value = "{serialNumber}/loaded-medications",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Medication>> getLoadedMedications(@PathVariable String serialNumber) {
        return ResponseEntity.ok(dispatcherService.getLoadedMedications(serialNumber));
    }

    @GetMapping(value = "available-for-loading",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        return ResponseEntity.ok(dispatcherService.getAvailableForLoading());
    }

    @GetMapping(value = "{serialNumber}/battery-level",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getDroneBatteryLevel(@PathVariable String serialNumber) {
        return ResponseEntity.ok(dispatcherService.getBatteryLevel(serialNumber));
    }
}

