package com.shopme.admin.repository;

import com.shopme.common.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    @Query("SELECT s FROM State s WHERE s.country.id = :country_id")
    List<State> findByCountryId(int country_id);
}
