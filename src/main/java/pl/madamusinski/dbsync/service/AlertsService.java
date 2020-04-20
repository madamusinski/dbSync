package pl.madamusinski.dbsync.service;

import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.config.JpaConfig;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.AlertsRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AlertsService {

    private final AlertsRepository alertsRepository;
    private final JpaConfig syncOneEntityManager;

    public AlertsService(AlertsRepository alertsRepository, JpaConfig syncOneEntityManager){
        this.alertsRepository = alertsRepository;
        this.syncOneEntityManager = syncOneEntityManager;
    }

    public List<Alerts> findAlerts(){
        return alertsRepository.findAll();
    }
}
