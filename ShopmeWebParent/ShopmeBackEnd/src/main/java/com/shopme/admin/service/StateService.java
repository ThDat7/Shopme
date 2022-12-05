package com.shopme.admin.service;

import com.shopme.admin.repository.StateRepository;
import com.shopme.common.entity.State;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {
    @Autowired
    private StateRepository stateRepository;

    public List<State> getAll() {
        return stateRepository.findAll();
    }

    public int save(State state) {
        return stateRepository.save(state).getId();
    }

    public void delete(int id) {
        if (!stateRepository.existsById(id))
            throw new ResourceNotFoundException();
        stateRepository.deleteById(id);
    }

    public List<State> findByCountryId(int country_id) {
        return stateRepository.findByCountryId(country_id);
    }
}
