package lsg.demo.xml2db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import lsg.demo.xml2db.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
	Optional<Place> findByLocationName(String name);
}