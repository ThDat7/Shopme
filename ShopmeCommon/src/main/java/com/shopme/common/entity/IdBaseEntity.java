package com.shopme.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass

@Getter @Setter

@AllArgsConstructor
@NoArgsConstructor
public class IdBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
}
