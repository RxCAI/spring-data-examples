/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.jpa.java8;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test to show the usage of Java 8 date time APIs with Spring Data JPA auditing.
 * 
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AuditingConfiguration.class)
@Transactional
public class Java8IntegrationTests {

	@Autowired CustomerRepository repository;

	@Test
	public void providesFindOneWithOptional() {

		Customer carter = repository.save(new Customer("Carter", "Beauford"));

		assertThat(repository.findOne(carter.id).isPresent(), is(true));
		assertThat(repository.findOne(carter.id + 1), is(Optional.<Customer> empty()));
	}

	@Test
	public void auditingSetsJdk8DateTimeTypes() {

		Customer customer = repository.save(new Customer("Dave", "Matthews"));

		assertThat(customer.createdDate, is(notNullValue()));
		assertThat(customer.modifiedDate, is(notNullValue()));
	}

	@Test
	public void invokesDefaultMethod() {

		Customer customer = repository.save(new Customer("Dave", "Matthews"));
		Optional<Customer> result = repository.findByLastname(customer);

		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(customer));
	}
}
