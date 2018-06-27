package com.example.demo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Rating {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int value;
	private Long customerId;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "movie_id")
	private Movie movie;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if(value<0 || value>5) {
			throw new IllegalArgumentException("Review no should be in between 0 to 5");
		}else {
			this.value = value;
		}
		
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Rating() {
		super();
	}

	@Override
	public String toString() {
		return "Rating [id=" + id + ", value=" + value + ", customerId=" + customerId + ", movie=" + movie + "]";
	}

	public Rating(int value, Long customerId, Movie movie) {
		super();
		if(value<0 || value>5) {
			throw new IllegalArgumentException("Review no should be in between 0 to 5");
		}else {
			this.value = value;
		}
		this.customerId = customerId;
		this.movie = movie;
	}

	
}
