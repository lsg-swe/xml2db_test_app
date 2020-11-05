package lsg.demo.xml2db.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lsg.demo.xml2db.model.BankingTransaction;
import lsg.demo.xml2db.model.Client;
import lsg.demo.xml2db.model.Place;
import lsg.demo.xml2db.repository.BankingTransactionRepository;
import lsg.demo.xml2db.repository.ClientRepository;
import lsg.demo.xml2db.repository.PlaceRepository;

@SpringBootTest(properties = { "command.line.runner.enabled=false", "application.runner.enabled=false" })
public class BankEventProcessingServiceTest {

	@Autowired
	BankEventProcessingService service;

	@Autowired
	BankingTransactionRepository transactRepository;

	@Autowired
	PlaceRepository placeRepository;

	@Autowired
	ClientRepository clientRepository;

	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		transactRepository.deleteAll();
		clientRepository.deleteAll();
		placeRepository.deleteAll();
	}

	@Test
	public void createOne() {
		BankingTransaction transaction = createTransaction(new BigDecimal(26.66), "UAH", "222***777", "John", "M",
				"Doe", "998877665544", "office 1");

		service.saveBankingTransaction(transaction);

		List<BankingTransaction> found = transactRepository.findAll();
		Assert.assertTrue(found.size() == 1);
		Assert.assertTrue(found.get(0).getCard().equals(transaction.getCard())
				&& found.get(0).getClient().getInn().equals(transaction.getClient().getInn()));

		Optional optClient = clientRepository.findByInn("998877665544");
		Assert.assertTrue(optClient.isPresent());
	}

	private BankingTransaction createTransaction(BigDecimal amount, String currency, String card, String firstName,
			String middleName, String lastName, String inn, String location) {
		BankingTransaction transaction = new BankingTransaction();
		transaction.setAmount(amount);
		transaction.setCurrency(currency);
		transaction.setCard(card);
		Client clientOne = new Client();
		clientOne.setFirstName(firstName);
		clientOne.setMiddleName(middleName);
		clientOne.setLastName(lastName);
		clientOne.setInn(inn);
		transaction.setClient(clientOne);
		Place place = new Place();
		place.setLocationName(location);
		transaction.setPlace(place);
		return transaction;
	}

}
