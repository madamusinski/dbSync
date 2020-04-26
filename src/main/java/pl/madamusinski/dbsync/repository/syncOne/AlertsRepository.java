package pl.madamusinski.dbsync.repository.syncOne;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.madamusinski.dbsync.domain.Alerts;

/**
 * @author Mateusz Adamusiński
 * Jpa repo used for faster accessing / saving / editing Alert entities for restcontroller use only
 * isn't used for replication process
 */
@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Integer> {
}
