package com.medical_drones.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "medical_drone")
@Table(name = "medical_drone", indexes = {
        @Index(name = "idx_serial_number", columnList = "serial_number")
})
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(hidden = true)
    private Long id;

    @Size(min = 0, message = "'serialNumber' field size should me more than 0 symbols")
    @Size(max = 100, message = "'serialNumber' field size should me less than 100 symbols")
    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    /**
     * Lightweight, Middleweight, Cruiserweight, Heavyweight
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private DroneModel model;

    @Min(value = 0, message = "'weightLimit' field should be greater than 0")
    @Max(value = 500, message = "'weightLimit' field should be less than 500")
    @NotNull
    @Column(name = "weight_limit")
    private double weightLimit;

    @Min(value = 0, message = "'batteryLevel' field should be greater than 0")
    @Max(value = 100, message = "'batteryLevel' field should be less than 100")
    @NotNull
    @Column(name = "battery_level")
    private int batteryLevel;

    /**
     * IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private DroneState state;

    @OneToMany(mappedBy = "drone",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    @Schema(hidden = true)
    private List<Medication> loadedMedications = new ArrayList<>();

    public Drone() {
    }

    public Drone(String serialNumber,
                 DroneModel model,
                 double weightLimit,
                 int batteryLevel,
                 DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryLevel = batteryLevel;
        this.state = state;
    }



    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public void setModel(DroneModel model) {
        this.model = model;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(double weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }

    public List<Medication> getLoadedMedications() {
        return loadedMedications;
    }

    public void setLoadedMedications(List<Medication> loadedMedications) {
        this.loadedMedications = loadedMedications;
    }

}