package com.medical_drones.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(hidden = true)
    private Long id;

    @Size(max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "'name' field should only contain letters, numbers, '-', and '_'")
    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private double weight;

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "'code' field should only contain upper case letters, numbers, and '_'")
    @Column(name = "code")
    private String code;

    /**
     * Base64 encoded image
     */
    @Column(name = "image")
    @Lob
    private String image;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    @JsonBackReference
    @Schema(hidden = true)
    private Drone drone;

    public Medication() {
    }

    public Medication(Long id, String name, double weight, String code, String image) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }
}