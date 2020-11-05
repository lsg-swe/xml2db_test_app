package lsg.demo.xml2db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lsg.demo.xml2db.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	Optional<Client> findByInn(String inn);
	Optional<Client> findByLastName(String lastName);
}
