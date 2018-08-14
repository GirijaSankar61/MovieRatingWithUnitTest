package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement(name = "Movie")
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	
//	@OneToMany(mappedBy = "movie")
//	private Set<Rating> ratings;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public Set<Rating> getRatings() {
//		return ratings;
//	}
//
//	public void setRatings(Set<Rating> ratings) {
//		this.ratings = ratings;
//	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", name=" + name + "]";
	}

	public Movie() {
		super();
	}

	public Movie(String name) {
		super();
		if(name==null||"".equals(name.trim())) {
			throw new IllegalArgumentException("Movie name should not be empty.");
		}else {
			this.name = name;
		}
	}

	

}
