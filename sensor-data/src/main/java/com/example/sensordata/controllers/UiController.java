package com.example.sensordata.controllers;

import com.example.sensordata.entities.MeasurementEntity;
import com.example.sensordata.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui")
public class UiController {

    @Autowired
    private MeasurementRepository measurementRepository;

    @GetMapping
    public String getAllMeasurements(Model model) { // käyttöliittymämalli
        // mallille saa attribuuttien avulla määritettyä sisältöä
        model.addAttribute("measurements", measurementRepository.findAll());
        return "ui/list"; // palautetaan polku ui-kansion list.html-tiedostoon
    }

    @GetMapping("/{id}")
    public String getMeasurementById(@PathVariable Long id, Model model) {
        model.addAttribute("measurement", measurementRepository.findById(id).orElse(null));
        return "ui/view";
    }

    // Tähän tulee kutsu kun painetaan käyttöliittymässä nappia "Add a new measurement"
    @GetMapping("/new")
    public String newMeasurementForm(Model model) {
        // Tässä alla siis luodaan uusi measurement -objekti. Ei vielä ole Id:tä eikä varsinaisia arvoja.
        model.addAttribute("measurement", new MeasurementEntity());
        return "ui/form";
    }

    @GetMapping("/edit/{id}")
    public String editMeasurementForm(@PathVariable Long id, Model model) {
        model.addAttribute("measurement", measurementRepository.findById(id).orElse(null));
        return "ui/form";
    }

    @PostMapping("/new")
    public String newMeasurementSubmit(@ModelAttribute MeasurementEntity measurementEntity) {
        measurementRepository.save(measurementEntity);
        return "redirect:/ui"; // Palautetaan lista käyttöliittymään
    }

    @PutMapping("/edit/{id}")
    public String editMeasurementSubmit(@ModelAttribute MeasurementEntity measurementEntity, @PathVariable Long id){
        measurementRepository.save(measurementEntity);
        return "redirect:/ui";
    }

    @DeleteMapping("{id}")
    public String deleteMeasurement(@PathVariable Long id){
        measurementRepository.deleteById(id);
        return "redirect:/ui";
    }


}



