package lsg.demo.xml2db;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lsg.demo.xml2db.model.BankingTransaction;
import lsg.demo.xml2db.repository.BankingTransactionRepository;
import lsg.demo.xml2db.repository.ClientRepository;
import lsg.demo.xml2db.repository.PlaceRepository;

@SpringBootTest(args={"good.xml","bad.xml"})
public class LoadJobRunnerTest {

	@Autowired
	LoadJobRunner jobRunner;
	
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

	/*
	 * Bad XML test (wrong numbers in <amount> node). After test 9 transaction entries are expected in storage, 
	 * other 3 entries with bad numbers shall be skipped.
	 */
	@Test
    public void testBadXML() throws Exception {
		jobRunner.run(new String[]{"bad.xml"});
		
		List<BankingTransaction> found = transactRepository.findAll();
		Assert.assertTrue(found.size() == 9);
    }

	/*
	 * Good XML test. After test 12 transaction entries are expected in storage.
	 */
	@Test
    public void testGoodXML() throws Exception {
		jobRunner.run(new String[]{"good.xml"});
		
		List<BankingTransaction> found = transactRepository.findAll();
		Assert.assertTrue(found.size() == 12);
    }

}
