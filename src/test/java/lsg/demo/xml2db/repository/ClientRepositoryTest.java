package lsg.demo.xml2db.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lsg.demo.xml2db.model.Client;

@DataJpaTest
public class ClientRepositoryTest {

	@Autowired
	private ClientRepository clientRepository;

	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		clientRepository.deleteAll();
	}

	@Test
	void injectedComponentsAreNotNull() {
		Assert.assertNotNull(clientRepository);
	}

	@Test
	public void createEntity() throws Exception {
		Client client = createClient("John", "M", "Doe", "998877665544");
		Client clientWithId = clientRepository.save(client);

		Assert.assertNotNull(clientWithId);
		Assert.assertNotNull(clientWithId.getId());
		client.setId(clientWithId.getId());
		Assert.assertEquals(client, clientWithId);
	}

	@Test
	public void deleteAll() throws Exception {
		clientRepository.deleteAll();
		Assert.assertTrue(clientRepository.findAll().isEmpty());
	}

	@Test
	public void deleteEntity() throws Exception {
		Client clientOne = createClient("John", "M", "Doe", "998877665544");
		clientOne.setId(clientRepository.save(clientOne).getId());

		Client clientTwo = createClient("Jane", "F", "Moe", "112233445566");
		clientTwo.setId(clientRepository.save(clientTwo).getId());

		Assert.assertEquals(clientRepository.count(), 2);

		clientRepository.delete(clientOne);

		Assert.assertEquals(clientRepository.count(), 1);

		Assert.assertTrue(clientRepository.findById(clientOne.getId()).isEmpty());
	}

	@Test
	public void findAll() throws Exception {
		Client clientOne = createClient("John", "M", "Doe", "998877665544");
		clientOne.setId(clientRepository.save(clientOne).getId());

		Client clientTwo = createClient("Jane", "F", "Moe", "112233445566");
		clientTwo.setId(clientRepository.save(clientTwo).getId());

		Assert.assertEquals(clientRepository.count(), 2);

		List<Client> foundClients = clientRepository.findAll();
		Assert.assertTrue(foundClients.contains(clientOne) && foundClients.contains(clientTwo));

	}

	private Client createClient(String firstName, String middleName, String lastName, String inn) {
		Client clientOne = new Client();
		clientOne.setFirstName(firstName);
		clientOne.setMiddleName(middleName);
		clientOne.setLastName(lastName);
		clientOne.setInn(inn);
		return clientOne;
	}
}
