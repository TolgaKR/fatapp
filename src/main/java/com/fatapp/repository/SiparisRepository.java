package com.fatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fatapp.model.Siparis;

@Repository
public interface SiparisRepository extends JpaRepository<Siparis, Long> {

}