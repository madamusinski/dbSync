package pl.madamusinski.dbsync.service;

import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.syncOne.AlertsRepository;
import pl.madamusinski.dbsync.repository.syncTwo.AlertsRepositoryTwo;

import java.util.List;

@Service
public class AlertsService {

    private final AlertsRepository alertsRepository;
    private final AlertsRepositoryTwo alertsRepositoryTwo;


    public AlertsService(AlertsRepository alertsRepository, AlertsRepositoryTwo alertsRepositoryTwo){
        this.alertsRepository = alertsRepository;
        this.alertsRepositoryTwo = alertsRepositoryTwo;
    }

    public List<Alerts> findAlerts(){
        return alertsRepository.findAll();
    }

    public List<Alerts> findAlertsTwo() {return alertsRepositoryTwo.findAll();}

}
