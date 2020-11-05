package lsg.demo.xml2db.sax;

/**
 * Listener for BankingTransactionHandler events.
 * @author larysa
 *
 * @param <T> Listener object for callbacks
 */
public interface ParseEventsListener<T> {
	public void onParsed(T obj);
}
