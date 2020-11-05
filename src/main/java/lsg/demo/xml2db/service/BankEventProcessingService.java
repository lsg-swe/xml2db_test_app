package lsg.demo.xml2db.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lsg.demo.xml2db.model.Client;
import lsg.demo.xml2db.model.Place;
import lsg.demo.xml2db.model.BankingTransaction;
import lsg.demo.xml2db.repository.ClientRepository;
import lsg.demo.xml2db.repository.PlaceRepository;
import lsg.demo.xml2db.repository.BankingTransactionRepository;
import lsg.demo.xml2db.sax.ParseEventsListener;

/**
 * Persists single bank-transaction entry in single db-level
 * transaction (serializable). Rollbacks on Exception for the single
 * bank-transaction entry. Checks whether client (by inn) and place (by location
 * name) are present in the storage, if not - inserts new entries.
 * 
 * @author larysa
 */
@Component
@EnableTransactionManagement
public class BankEventProcessingService implements ParseEventsListener<BankingTransaction> {
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private BankingTransactionRepository transactionRepository;

	@Autowired
	private ProcessingStatistics statistics;

	static final Logger logger = LoggerFactory.getLogger(BankEventProcessingService.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = BankEventProcessingException.class)
	public void onParsed(BankingTransaction transationVO) {
		saveBankingTransaction(transationVO);
	}

	public void saveBankingTransaction(BankingTransaction bankingTransaction) throws BankEventProcessingException {
		try {
			// Look for client by inn. if client is found, sets FK and update db 'client'
			// table with new info
			Optional<Client> client = clientRepository.findByInn(bankingTransaction.getClient().getInn());

			if (client.isPresent()) {
				Client foundClient = client.get();
				bankingTransaction.getClient().setId(foundClient.getId());
			} else {
				Client newClient = clientRepository.save(bankingTransaction.getClient());
				bankingTransaction.getClient().setId(newClient.getId());
				statistics.incrementClientInserted();
			}

			// Look for place by location name. if place is found, sets FK and update db
			// place table with new info
			Optional<Place> place = placeRepository.findByLocationName(bankingTransaction.getPlace().getLocationName());

			if (place.isPresent()) {
				Place foundPlace = place.get();
				bankingTransaction.getPlace().setId(foundPlace.getId());
			} else {
				Place newPlace = placeRepository.save(bankingTransaction.getPlace());
				bankingTransaction.getPlace().setId(newPlace.getId());
				statistics.incrementPlaceInserted();
			}
			transactionRepository.save(bankingTransaction);
			
			statistics.incrementBankingTransactionInserted();
			statistics.incrementEntriesProcessed();

		} catch (Exception ex) {
			statistics.incrementEntriesFailed();
			logger.error("Error saving banking transaction data for object :" + bankingTransaction + ". Banking transaction data are rollbacked...");
			logger.error(ex.getMessage());
			throw new BankEventProcessingException(ex.getMessage());
		}
	}

}
