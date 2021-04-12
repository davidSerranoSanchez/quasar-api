package co.com.davidserrano.apps.quasar.repository;

import co.com.davidserrano.apps.quasar.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISatelliteRepository extends JpaRepository<Satellite, String> {
}
