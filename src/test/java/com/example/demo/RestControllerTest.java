/**
 * 
 */
package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Ultron
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootApplicationBootStrap.class)
@WebAppConfiguration
public class RestControllerTest {

	private MockMvc restLogsMockMvc;
	
	@Autowired
	  private WebApplicationContext webContext;

	@Before
	public void setup() {
		this.restLogsMockMvc = MockMvcBuilders.webAppContextSetup(webContext)
        .build();
	}

	 @Test
	    public void testGetHello()throws Exception {
	        restLogsMockMvc.perform(get("/rest/hello"))
	            .andExpect(status().isOk());
	    }
	 @Test
	    public void getAllLogs()throws Exception {
	        restLogsMockMvc.perform(get("/rest/all"))
	            .andExpect(status().isOk());
	    }

}
