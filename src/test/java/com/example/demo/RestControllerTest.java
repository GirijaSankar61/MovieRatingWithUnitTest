/**
 * 
 */
package com.example.demo;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;


import io.restassured.RestAssured;

/**
 * @author Ultron
 *
 */
public class RestControllerTest {
	@BeforeClass
	public static void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8084;
	}

	@Test
	public void test() {
		int x = given().
				  header("movie", "agnipath").get("api/rest/customer/4/rate/5").getStatusCode();
		 assertEquals(x, Response.Status.BAD_REQUEST.getStatusCode());
	}

}
