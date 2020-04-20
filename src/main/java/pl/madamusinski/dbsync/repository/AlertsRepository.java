package pl.madamusinski.dbsync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.madamusinski.dbsync.domain.Alerts;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Integer> {
}
