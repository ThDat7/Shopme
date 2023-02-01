package com.shopme.admin.controller;

import com.shopme.admin.service.StateService;
import com.shopme.common.dto.StateDTO;
import com.shopme.common.entity.State;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateController {
    private StateService service;

    private ModelMapper modelMapper;

    public StateController(StateService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<State> states = service.getAll();

        return ResponseEntity.ok(
                modelMapper.map(states, StateDTO.class)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(State state) {
        return ResponseEntity.ok(service.save(state));
    }

    @GetMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        service.delete(id);
    }


    @GetMapping("/list_by_country/{country_id}")
    public ResponseEntity<?> findByCountry(@PathVariable("country_id")
                                           int country_id) {
        return ResponseEntity.ok(service.findByCountryId(country_id));
    }
}
