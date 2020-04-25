package pl.madamusinski.dbsync.repository.syncTwo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.madamusinski.dbsync.domain.Alerts;


@Repository
public interface AlertsRepositoryTwo extends JpaRepository<Alerts, Integer> {
}
