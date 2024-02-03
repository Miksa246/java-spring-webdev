package com.example.sensordata.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.Date;

public class MeasurementEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private @NonNull String unit;
    @Column
    private @NonNull double value;

    @Column
    private @NonNull String location;

    @Column
    @Temporal(TemporalType.TIMESTAMP) // sisältää päivän ja kellonajan
    private Timestamp measurementTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    public void setLocation(@NonNull String location) {
        this.location = location;
    }

    public Timestamp getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(Timestamp measurementTime) {
        this.measurementTime = measurementTime;
    }

    public MeasurementEntity(@NonNull String unit, double value, @NonNull String location) {
        this.unit = unit;
        this.value = value;
        this.location = location;
        Date date = new Date();
        this.measurementTime = new Timestamp(date.getTime());
    }

    public MeasurementEntity() {
        Date date = new Date();
        this.measurementTime = new Timestamp(date.getTime());
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                ", location='" + location + '\'' +
                ", measurementTime=" + measurementTime +
                '}';
    }
}
