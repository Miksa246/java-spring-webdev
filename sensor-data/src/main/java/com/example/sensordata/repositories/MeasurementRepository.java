package com.example.sensordata.repositories;

import com.example.sensordata.entities.MeasurementEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeasurementRepository extends CrudRepository<MeasurementEntity, Long> {

    List<MeasurementEntity> findAll();
}
