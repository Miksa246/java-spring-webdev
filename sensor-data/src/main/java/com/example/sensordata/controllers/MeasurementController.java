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

    // Tarkistaa, että tuleeko sisään puuttuvat attribuutit measurement-oliolle ja palauttaa
    // BAD-REQUESTIN, jos puuttuu jotain
    private ResponseEntity<MeasurementEntity> checkMissingAttributes(@RequestParam Map<String, String> rParams) {
        for (String attribute : new String[]{"unit", "value", "location"}){
            if (rParams.get(attribute) == null){
                HttpHeaders header = new HttpHeaders();
                header.set("status", attribute + " missing");
                return new ResponseEntity<>(header, HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    // REST API
    @GetMapping("/measurement")
    public ResponseEntity<List<MeasurementEntity>> getMeasurements(){
        try {
            return new ResponseEntity<>(measurementRepository.findAll(), HttpStatus.OK); // oletuksena jos kaikki on ok
        } catch (Exception e) { // Mahdollisen virhetilanteen sattuessa
            // TODO: Käsittele jollain lailla virhe, esim. lokittamalla
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/measurement")
    public ResponseEntity<MeasurementEntity> postMeasurement(@RequestParam Map<String, String> rParams){
        // 400 bad request ja sille on hyvä antaa tietoa mahdollisesta häikästä
        try {
            ResponseEntity<MeasurementEntity> header = checkMissingAttributes(rParams);
            if (header != null) return header;
            MeasurementEntity measurementEntity = new MeasurementEntity(rParams.get("unit"), Double.parseDouble(rParams.get("value")), rParams.get("location"));
            measurementRepository.save(measurementEntity);
            return new ResponseEntity<>(measurementEntity, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/measurement/{id}")
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

    @PutMapping("/measurement/{id}")
    public ResponseEntity<MeasurementEntity> putMeasurement(@PathVariable Long id, @RequestParam Map<String,String> rParams){
        try {
            // Tähän on kaksi vaihtoehtoa, mukana tulevat joko kaikki tiedot, tai vain muutettavat
            // Jos oletetaan, että kaikki tiedot, niin käsitellään samalla lailla kuin POST:
            ResponseEntity<MeasurementEntity> header = checkMissingAttributes(rParams);
            if (header != null) return header;
            MeasurementEntity measurementEntity = measurementRepository.findById(id).orElseThrow();
            measurementEntity.setUnit(rParams.get("unit"));
            measurementEntity.setValue(Double.parseDouble(rParams.get("value")));
            measurementEntity.setLocation(rParams.get("location"));
            measurementRepository.save(measurementEntity);
            return new ResponseEntity<>(measurementEntity, HttpStatus.OK);
        } catch (NoSuchElementException e) { // nousee kun yritetään hakea tietoa, mitä ei ole olemassa
            HttpHeaders header = new HttpHeaders();
            header.set("status", "missing measurement with id " + id);
            return new ResponseEntity<>(header, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
