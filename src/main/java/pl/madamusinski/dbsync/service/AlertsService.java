package pl.madamusinski.dbsync.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.config.SyncOneDbConfig;
import pl.madamusinski.dbsync.config.SyncTwoDbConfig;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.syncOne.AlertsRepository;
import pl.madamusinski.dbsync.repository.syncTwo.AlertsRepositoryTwo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class AlertsService {


    private final AlertsRepository alertsRepository;
    private final AlertsRepositoryTwo alertsRepositoryTwo;
    @Autowired
    SyncOneDbConfig dbConfig;


    public AlertsService(AlertsRepository alertsRepository, AlertsRepositoryTwo alertsRepositoryTwo){
        this.alertsRepository = alertsRepository;
        this.alertsRepositoryTwo = alertsRepositoryTwo;
    }

    public List<Alerts> findAlerts(){
        return alertsRepository.findAll();
    }

    public List<Alerts> findAlertsTwo() {return alertsRepositoryTwo.findAll();}

    @Transactional
    public Alerts save(Alerts alert){
        EntityManagerFactory emf = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
       return alertsRepository.save(alert);
    }





}
