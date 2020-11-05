package lsg.demo.xml2db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lsg.demo.xml2db.model.BankingTransaction;

public interface BankingTransactionRepository extends JpaRepository<BankingTransaction, Long> {
}
