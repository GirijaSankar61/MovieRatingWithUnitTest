package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Customer;
import com.example.demo.model.Movie;
import com.example.demo.model.Rating;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;

@RestController
@RequestMapping("/rest")
public class MovieController {
	@Inject
	private CustomerRepository customerRepository;

	@Inject
	private MovieRepository movieRepository;

	@Inject
	RatingRepository ratingRepository;

	@GetMapping("/hello")
	public String getHello() {
		Customer c1 = new Customer(1l, "Hrithik", "Roshan");
		Customer c2 = new Customer(2l, "Amir", "Khan");
		Customer c3 = new Customer(3l, "Shah", "Feku");
		Customer c4 = new Customer(4l, "Salu", "saMhu");
		List<Customer> list = new ArrayList<Customer>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		customerRepository.save(list);
		return "Hello World!";
	}

	@GetMapping("/all")
	public List<Customer> getAll() {
		return customerRepository.findAll();
	}

	@GetMapping(value = "/movie/findOne")
	public Movie getMovie() {
		return movieRepository.findOne(1l);
	}

	@GetMapping(value = "/rating", produces = MediaType.APPLICATION_JSON)
	public List<Rating> setRating() {
		Movie m = new Movie("DOn1");
		Movie m2 = new Movie("DOn2");
		Movie m3 = new Movie("DOn3");
		Rating r = new Rating(5, 2l, m);
		Rating r2 = new Rating(3, 4l, m2);
		Rating r3 = new Rating(5, 4l, m2);
		Rating r4 = new Rating(2, 2l, m3);
		Rating r5 = new Rating(5, 2l, m2);
		ArrayList<Rating> ratingList = new ArrayList<>();
		ratingList.add(r);
		ratingList.add(r2);
		ratingList.add(r3);
		ratingList.add(r4);
		ratingList.add(r5);

		ratingRepository.save(ratingList);
		return ratingRepository.findAll();
	}
/**
 * Use ResponseEntity to get the appropriate response i.e-only response body
 * Response can be used as output but it will give complete and unnecessary information
 * 
 * */
	@GetMapping(value = "/customer/{customerId}/rate/{rating}")
	public ResponseEntity saveRating(@PathVariable(value = "customerId") Long customerId,
			@PathVariable(value = "rating") Integer value, @RequestHeader(value = "movie") String movieName) {
		Rating rating = null;
		try {
			Movie m = new Movie(movieName);
			rating = new Rating(value, customerId, m);
			rating = ratingRepository.save(rating);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(rating, HttpStatus.OK);
	}

	@GetMapping(value = "/highestRatedMovie/imperative")
	public ResponseEntity highestRatedMovie() {
		Movie highestAverageMovie = null;
		double highestAvg = 0;
		List<Rating> ratings = ratingRepository.findAll();
		HashMap<Movie, List<Rating>> map = new HashMap<Movie, List<Rating>>();
		for (Rating rating : ratings) {
			if (map.containsKey(rating.getMovie())) {
				List<Rating> ratingList = map.get(rating.getMovie());
				ratingList.add(rating);
			} else {
				List<Rating> ratingList = new ArrayList<>();
				ratingList.add(rating);
				map.put(rating.getMovie(), ratingList);
			}
		}
		for (Map.Entry<Movie, List<Rating>> ratingsMap : map.entrySet()) {
			int totalRatings = 0;
			for (Rating r : ratingsMap.getValue())
				totalRatings += r.getValue();
			double doubleAverageRating = ((double) totalRatings) / ratingsMap.getValue().size();
			if (doubleAverageRating > highestAvg) {
				highestAvg = doubleAverageRating;
				highestAverageMovie = ratingsMap.getKey();
			}
		}
		if (highestAverageMovie == null) {
			return new ResponseEntity<>("No Review found for any movie",HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(highestAverageMovie, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/highestRatedMovie/functional", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity highestRatedMovieInFunctionalType() {
		List<Rating> ratings = ratingRepository.findAll();
		Map<Movie, List<Rating>> ratingsGroupByMovie = ratings.stream()
				.collect(Collectors.groupingBy(Rating::getMovie));
		Map<Movie, Double> avarage = ratingsGroupByMovie.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
				e -> e.getValue().stream().mapToInt(Rating::getValue).average().getAsDouble()));
		Movie highestAverageMovie = avarage.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
		if (highestAverageMovie == null) {
			return new ResponseEntity<>("No Review found for any movie",HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(highestAverageMovie, HttpStatus.OK);
		}
	}

	@PostMapping("/highestRatedCustomer/{id}")
	public ResponseEntity highestRatedGivenByCustomerForMovie(Movie movie, @PathVariable(value = "id") Long customerId) {
		List<Rating> ratings = ratingRepository.findAll();
		List<Rating> ratingsListFilteredOnMovie = ratings.stream()
				.filter(p -> p.getMovie().getName().equals(movie.getName())).collect(Collectors.toList());
		int maxRatings = ratingsListFilteredOnMovie.stream().mapToInt(Rating::getValue).max().getAsInt();
		double averageRatingOfMovie = ratings.stream().mapToInt(Rating::getValue).average().getAsDouble();
		List<Rating> ratingsHighestRated = ratingsListFilteredOnMovie.stream().filter(p -> p.getValue() == maxRatings)
				.collect(Collectors.toList());
		ArrayList<Customer> personList = new ArrayList<>();
		ratingsHighestRated.stream().forEach(p -> {
			Customer customer = customerRepository.findOne(p.getCustomerId());
			double averageRatingOfUser = ratings.stream().filter(rating -> rating.getCustomerId() == customerId)
					.mapToInt(Rating::getValue).average().getAsDouble();
			customer.setCustomerAverageRating(averageRatingOfUser);
			customer.setMovieAverageRating(averageRatingOfMovie);
			personList.add(customer);
		});

		return new ResponseEntity<>(personList, HttpStatus.OK);

	}

}
