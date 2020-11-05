package lsg.demo.xml2db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Test application. Parses input XML file (soap elements tags skipped) and stores parsed data to a persistence layer.
 * XML file element example
 * <transaction>
					<place>A PLACE 1</place>
					<amount>10.01</amount>
					<currency>UAH</currency>
					<card>123456****1234</card>
					<client>
						<firstName>Ivan</firstName>
						<lastName>Ivanoff</lastName>
						<middleName>Ivanoff</middleName>
						<inn>1234567890</inn>
					</client>
				</transaction>
 * Input files can be huge therefore they are processed as a stream.
 * Data entities for persistence: Client, Place, BankingTransaction. 
 * Uses Spring Data JPA (database configuration /resources/application.properties). 
 * 
 * Single BankingTransaction object is processed as a single db transaction. 
 * If the CLient in the transaction is found in the DB (search by inn), its id (PK) is used. Otherwise new Client is added to the DB.
 * If the Place in the transaction is found in the DB (search by place name), its id (PK) is used. Otherwise new Place is added to the DB.
 * If one BankingTransaction entry processing fails, rollback is issued for this entry + related Client and Place updates.
 * 
 * Unit tests are configured to use in-memory H2 database (/resources/application-test.properties).
 * 
 * @author larysa
 */
@SpringBootApplication
public class Xml2DbApplication {

	static final Logger logger = LoggerFactory.getLogger(Xml2DbApplication.class);

	public static void main(String[] args) {
		logger.info("\nSTARTING XML2DB APPLICATION");
		
		SpringApplication.exit(SpringApplication.run(Xml2DbApplication.class, args));
		
		logger.info("\nAPPLICATION FINISHED");
	}
}
