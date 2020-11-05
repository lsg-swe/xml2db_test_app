package lsg.demo.xml2db.sax;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lsg.demo.xml2db.model.Client;
import lsg.demo.xml2db.model.Place;
import lsg.demo.xml2db.service.BankEventProcessingException;
import lsg.demo.xml2db.model.BankingTransaction;

/**
 * Handles SAX parser events. When <transaction> element is parsed from the input stream,
 * the callback listener receives the composed banking transaction object. 
 * If banking transaction object cannot be created due to invalid <amount> value in node,
 * BankingTransactionHandler logs a message and proceeds with the rest of input stream.
 * 
 * @author larysa
 */
public class BankingTransactionHandler extends DefaultHandler {
	private static final String AMOUNT = "amount";
	private static final String CURRENCY = "currency";
	private static final String CARD = "card";
	private static final String CLIENT = "client";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String MIDDLE_NAME = "middleName";
	private static final String INN = "inn";
	private static final String TRANSACTION = "transaction";
	private static final String PLACE = "place";

	private static final Logger logger = LoggerFactory.getLogger(BankingTransactionHandler.class);

	private final ParseEventsListener<BankingTransaction> eventsListener;

	private BankingTransaction transaction;
	private String nodeValue;
	private boolean wrongDataFlag;

	public BankingTransactionHandler(ParseEventsListener<BankingTransaction> eventsListener) {
		this.eventsListener = eventsListener;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		nodeValue = new String(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
		case CLIENT:
			transaction.setClient(new Client());
			break;
		case TRANSACTION:
			transaction = new BankingTransaction();
			wrongDataFlag = false;
			break;
		case PLACE:
			transaction.setPlace(new Place());
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case AMOUNT:
			try {
				transaction.setAmount(new BigDecimal(nodeValue));
			} catch (NumberFormatException ex) {
				wrongDataFlag = true;
				logger.error("NumberFormatException parsing decimal data from uinput XML file: " + nodeValue);
			}
			break;
		case CURRENCY:
			transaction.setCurrency(nodeValue);
			break;
		case CARD:
			transaction.setCard(nodeValue);
			break;
		case FIRST_NAME:
			transaction.getClient().setFirstName(nodeValue);
			break;
		case LAST_NAME:
			transaction.getClient().setLastName(nodeValue);
			break;
		case MIDDLE_NAME:
			transaction.getClient().setMiddleName(nodeValue);
			break;
		case INN:
			transaction.getClient().setInn(nodeValue);
			break;
		case PLACE:
			transaction.getPlace().setLocationName(nodeValue);
			break;
		case TRANSACTION:
			try {
				if (!wrongDataFlag) {
					eventsListener.onParsed(transaction);
				} else {
					wrongDataFlag = false;
					logger.error("Wrong XML data will not be saved for node: " + transaction);
				}
			} catch (BankEventProcessingException ex) {
				logger.error("Exception from notified listener on parsed object processing:" + transaction
						+ "\nException :" + ex.getMessage());
			}
		}
	}

}
