package lsg.demo.xml2db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lsg.demo.xml2db.sax.BankingTransactionHandler;
import lsg.demo.xml2db.service.BankEventProcessingService;
import lsg.demo.xml2db.service.ProcessingStatistics;

/**
 * Runs job for single file. Takes the first application command line argument
 * as a filename for parsing candidate.
 * 
 * @author larysa
 */
@ConditionalOnProperty(prefix = "application.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
@Component
public class LoadJobRunner implements CommandLineRunner {

	@Autowired
	BankEventProcessingService service = new BankEventProcessingService();
	@Autowired
	private ProcessingStatistics statistics;

	static final Logger logger = LoggerFactory.getLogger(LoadJobRunner.class);

	@Override
	public void run(String... args) throws Exception {
		if (args.length == 0) {
			logger.error(
					"\n\nNo Input File is Defined! Please specify file name in arguments. Example:\n $ mvn spring-boot:run -Dspring-boot.run.arguments=<file.name>\n "
					+ "or \n $ java -jar xml2db-0.0.1-SNAPSHOT.jar <file.name>\n\n");			
			throw new FileNotFoundException("No Input File is Defined!");
		}
		String fileName = args[0];

		logger.info("\nPARSING - " + fileName + "\n");

		try (FileInputStream fis = new FileInputStream(new File(fileName));) {
			statistics.resetStatistics();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(fis, new BankingTransactionHandler(service));
		} catch (FileNotFoundException ex) {
			logger.error("\n Cannot find input file : " + fileName + "\n");
			throw ex;
		} finally {
			logger.info("\n PROCESSED ENTRIES:  " + statistics + "\n");
		}
	}

}