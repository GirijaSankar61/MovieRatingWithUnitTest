package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Rating;

public interface RatingRepository extends JpaRepository<Rating,Long>{

}
