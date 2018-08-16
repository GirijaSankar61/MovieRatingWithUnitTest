package com.example.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.example.demo.controller.MovieController;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RatingRepository;

@RunWith(MockitoJUnitRunner.class)
public class MovieControlleUnitTest {
	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	RatingRepository ratingRepository;
	
	@InjectMocks
	private MovieController movieController= new MovieController();

	@Before
	public void setup() {

	}

	@Test
	public void testLoadCustomer() {
		String result = movieController.loadCustomer();
		assertEquals("Hello World!", result);
	}

}
