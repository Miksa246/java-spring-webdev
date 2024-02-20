package com.example.sensordata.controllers;

import com.example.sensordata.entities.MeasurementEntity;
import com.example.sensordata.repositories.MeasurementRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class MeasurementController {
    // otetaan käyttöön measurementRepository tekemällä privaatti muuttuja
    private final MeasurementRepository measurementRepository;
    // ja lisätään se konstruktoriin
    public MeasurementController(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;

        if (this.measurementRepository.count() == 0){
            MeasurementEntity m1 = new MeasurementEntity("m/s", 1.4, "1");
            measurementRepository.save(m1);
            MeasurementEntity m2 = new MeasurementEntity("humidity", 0.67, "1");
            measurementRepository.save(m2);
            MeasurementEntity m3 = new MeasurementEntity("m/s", 0.5, "2");
            measurementRepository.save(m3);
            MeasurementEntity m4 = new MeasurementEntity("humidity", 0.30, "2");
            measurementRepository.save(m4);
        }
    }


    // REST API
    @GetMapping("/measurements")
    public ResponseEntity<List<MeasurementEntity>> getMeasurements(){
        try {
            return new ResponseEntity<>(measurementRepository.findAll(), HttpStatus.OK); // oletuksena jos kaikki on ok
        } catch (Exception e) { // Mahdollisen virhetilanteen sattuessa
            // TODO: Käsittele jollain lailla virhe, esim. lokittamalla
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/measurements")
    public ResponseEntity<MeasurementEntity> postMeasurement(@RequestBody MeasurementEntity newMeasurement){
        // Tarkista tässä kohtaa, että kaikki tarvittavat kentät ovat määritelty
        // Esimerkiksi, jos olet lisännyt validointiannotaatioita MeasurementEntity-luokkaan,
        // voit käyttää Springin @Valid tai @Validated annotaatiota validaation aktivoimiseksi:
        // @RequestBody @Valid MeasurementEntity newMeasurement

        // Tallenna mittaus tietokantaan
        MeasurementEntity savedMeasurement = measurementRepository.save(newMeasurement);

        // Palauta luotu mittaus ja HTTP 201 Created status-koodi
        return new ResponseEntity<>(savedMeasurement, HttpStatus.CREATED);
    }


    @DeleteMapping("/measurements/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id){
        try {
            if (measurementRepository.existsById(id)){
                measurementRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/measurements/{id}")
    public ResponseEntity<MeasurementEntity> putMeasurement(@PathVariable Long id, @RequestBody MeasurementEntity updatedMeasurement) {
        return measurementRepository.findById(id)
                .map(measurement -> {
                    // Päivitä mittauksen tiedot. Voit asettaa tässä vaiheessa uudet arvot suoraan `updatedMeasurement`-oliosta.
                    measurement.setUnit(updatedMeasurement.getUnit());
                    measurement.setUnit(updatedMeasurement.getUnit());
                    measurement.setValue(updatedMeasurement.getValue());
                    measurement.setLocation(updatedMeasurement.getLocation());
                    measurement.setMeasurementTime(updatedMeasurement.getMeasurementTime()); // Olettaen että haluat myös päivittää ajan

                    // Tallenna päivitetty mittaus tietokantaan
                    MeasurementEntity savedMeasurement = measurementRepository.save(measurement);

                    // Palauta päivitetty mittaus ja HTTP 200 OK status-koodi
                    return new ResponseEntity<>(savedMeasurement, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Jos mittaus tunnisteella ei löydy, palauta 404 Not Found
    }



}
