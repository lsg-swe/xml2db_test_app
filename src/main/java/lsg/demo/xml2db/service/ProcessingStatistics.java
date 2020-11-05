package lsg.demo.xml2db.service;

import org.springframework.stereotype.Component;

/**
 * Holds simple processing statistics (banking transactions processed/failed, number of
 * new clients/places/banking transactions inserted)
 * 
 * @author larysa
 */
@Component
public class ProcessingStatistics {
	private int clientInserted;
	private int placeInserted;
	private int bankingTransactionInserted;
	private int entriesFailed;
	private int entriesProcessed;
	
	public int getClientInserted() {
		return clientInserted;
	}

	public void incrementClientInserted() {
		clientInserted++;
	}

	public int getPlaceInserted() {
		return placeInserted;
	}

	public void incrementPlaceInserted() {
		this.placeInserted ++;
	}

	public int getBankingTransactionInserted() {
		return bankingTransactionInserted;
	}

	public void incrementBankingTransactionInserted() {
		this.bankingTransactionInserted ++;
	}

	public int getEntriesFailed() {
		return entriesFailed;
	}

	public void incrementEntriesFailed() {
		this.entriesFailed ++;
	}

	public int getEntriesProcessed() {
		return entriesProcessed;
	}

	public void incrementEntriesProcessed() {
		this.entriesProcessed ++;
	}

	public void setClientInserted(int clientInserted) {
		this.clientInserted = clientInserted;
	}

	public void setPlaceInserted(int placeInserted) {
		this.placeInserted = placeInserted;
	}

	public void setTransactionInserted(int transactionInserted) {
		this.bankingTransactionInserted = transactionInserted;
	}

	public void setEntriesFailed(int entriesFailed) {
		this.entriesFailed = entriesFailed;
	}

	public void setEntriesProcessed(int entriesProcessed) {
		this.entriesProcessed = entriesProcessed;
	}
	
	public void resetStatistics() {
		clientInserted = 0;
		placeInserted = 0;
		bankingTransactionInserted = 0;
		entriesFailed = 0;
		entriesProcessed = 0;
	}

	@Override
	public String toString() {
		return "ProcessingStatistics [clientInserted=" + clientInserted + ", placeInserted=" + placeInserted
				+ ", bankingTransactionInserted=" + bankingTransactionInserted + ", total entriesProcessed=" + entriesProcessed
				+ ", entriesFailed=" + entriesFailed + "]";
	}

}
