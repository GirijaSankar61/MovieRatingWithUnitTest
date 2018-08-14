package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		Customer c1 = new Customer(1l, "Ram", "sahu");
		Customer c2 = new Customer(2l, "Ram2", "sahu");
		Customer c3 = new Customer(3l, "Ram3", "sahu");
		Customer c4 = new Customer(4l, "Ram4", "sahu");
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

	@GetMapping("/movie/findOne")
	public Movie getMovie() {
		return movieRepository.findOne(1l);
	}

	@GetMapping("/rating")
	public List<Rating> setRating() {
		Movie m = new Movie("DOn1");
		Movie m2 = new Movie("DOn2");
		Movie m3 = new Movie("DOn3");
		Rating r = new Rating(3, 2l, m);
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

	@GetMapping("/customer/{customerId}/rate/{rating}")
	public Response saveRating(@PathParam(value = "customerId") long customerId, @PathParam(value = "rating") int value,
			@HeaderParam(value = "movie") String movieName) {
		Rating r = null;
		try {
			Movie m = new Movie(movieName);
			r = new Rating(value, customerId, m);
			r = ratingRepository.save(r);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
		}

		return Response.ok(r, MediaType.APPLICATION_JSON).build();
	}

	@GetMapping("/highestRatedMovie/imperative")
	public Response highestRatedMovie() {
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
				highestAvg=doubleAverageRating;
				highestAverageMovie=ratingsMap.getKey();
			}
		}
		if (highestAverageMovie==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Review found for any movie").build();
		}else {
			return Response.ok(highestAverageMovie).build();
		}
	}

	@PostMapping("/highestRatedMovie/functional")
	public Response highestRatedMovieInFunctionalType() {
		List<Rating> ratings = ratingRepository.findAll();
		double highRatingValue = 0;
		Map<Movie, List<Rating>> ratingsGroupByMovie = ratings.stream().collect(Collectors.groupingBy(Rating::getMovie));
		Map<Movie,Double> avarage = 
				ratingsGroupByMovie.entrySet()
			              .stream()
			              .collect(Collectors.toMap(Map.Entry::getKey,
			                                        e-> e.getValue()
			                                             .stream()
			                                             .mapToInt(Rating::getValue)
			                                             .average()
			                                             .getAsDouble()));
		Movie highestAverageMovie = avarage.entrySet().stream()
		  .max(Map.Entry.comparingByValue()).get().getKey();
		if (highestAverageMovie==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("No Review found for any movie").build();
		}else {
			return Response.ok(highestAverageMovie).build();
		}
	}
	
	@PostMapping("/highestRatedCustomer/{id}")
	public Response highestRatedGivenByCustomerForMovie(Movie movie,@PathParam(value = "id") long customerId) {
		List<Rating> ratings = ratingRepository.findAll();
		List<Rating> ratingsListFilteredOnMovie = ratings.stream()
				.filter(p->p.getMovie().getName().equals(movie.getName())).collect(Collectors.toList());
		int maxRatings = ratingsListFilteredOnMovie.stream().mapToInt(Rating::getValue).max().getAsInt();
		double averageRatingOfMovie = ratings.stream().mapToInt(Rating::getValue).average().getAsDouble();
		List<Rating> ratingsHighestRated = ratingsListFilteredOnMovie.stream().filter(p->p.getValue()==maxRatings).collect(Collectors.toList());
		ArrayList<Customer> personList= new ArrayList<>();
		ratingsHighestRated.stream().forEach(p->{
			Customer customer=customerRepository.findOne(p.getCustomerId());
			double averageRatingOfUser =ratings.stream().filter(rating->rating.getCustomerId()==customerId).mapToInt(Rating::getValue).average().getAsDouble();
			customer.setCustomerAverageRating(averageRatingOfUser);
			customer.setMovieAverageRating(averageRatingOfMovie);
			personList.add(customer);
		});
		
		 return Response.ok(personList).build();
		
	}

}
