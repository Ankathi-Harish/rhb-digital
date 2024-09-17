package com.rhb.digital;

import com.rhb.digital.service.impl.AccountServiceImpl;
import com.rhb.digital.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DigitalApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Verify that the application context is loaded
		assertNotNull(applicationContext, "Application context should not be null");
	}

	@Test
	void testBeanLoading() {
		// Example of checking if a specific bean is loaded in the context
		AccountServiceImpl accountService = applicationContext.getBean(AccountServiceImpl.class);
		CustomerServiceImpl customerService = applicationContext.getBean(CustomerServiceImpl.class);

		// Optionally, perform some assertions or operations on the retrieved beans
		assertNotNull(accountService, "AccountServiceImpl bean should not be null");
		assertNotNull(customerService, "CustomerServiceImpl bean should not be null");
	}
}