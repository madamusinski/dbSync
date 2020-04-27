package pl.madamusinski.dbsync.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import pl.madamusinski.dbsync.config.SyncOneDbConfig;
import pl.madamusinski.dbsync.config.SyncTwoDbConfig;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.syncOne.AlertsRepository;
import pl.madamusinski.dbsync.repository.syncTwo.AlertsRepositoryTwo;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AlertsService {


    private final Logger logger = LoggerFactory.getLogger(AlertsService.class);
    private final AlertsRepository alertsRepository;
    private final AlertsRepositoryTwo alertsRepositoryTwo;
    @Autowired
    SyncOneDbConfig dbConfig;
    @Autowired
    SyncTwoDbConfig dbConfigTwo;




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
        alert.setTimeStamp(new Date());
        return alertsRepository.save(alert);
    }

    public List<Alerts> findAlertsTwoTwo(){
        EntityManagerFactory emftwo = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory();
        EntityManager em = emftwo.createEntityManager();
        Query query = em.createQuery("select a from Alerts a");
        List<Alerts> alertsList = query.getResultList();
        alertsList.stream().forEach(System.out::println);
        return alertsList;
    }
    public Alerts savetwo(Alerts alert) throws Exception {
        EntityManagerFactory emf = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Alerts persistedAlert = new Alerts();
        if(Objects.nonNull(alert)&&Objects.nonNull(alert.getId())){
            try{
                alert.setTimeStamp(new Date());
                em.getTransaction().begin();

                persistedAlert = em.merge(alert);
                em.getTransaction().commit();
            }catch(Exception e){
                em.getTransaction().rollback();
                throw new Exception("Jakiś problem z prawdopodobie pusty ID", e);
            }
        }
        return persistedAlert;
    }

    public List<Alerts> getAllAlerts(){
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        List<Alerts> alertsToCopy = new ArrayList<>();
        Query q = emSource.createQuery("select a from Alerts a");
        alertsToCopy = q.getResultList();
        emSource.close();
        return alertsToCopy;
    }

    public void deleteTest(Integer id){
        EntityManager em = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        Alerts alert = em.find(Alerts.class, id);
        if(Objects.nonNull(alert)){
            try{
                em.getTransaction().begin();
                em.remove(alert);
                em.getTransaction().commit();
                logger.info("Succesfuly deleted item of {} with id {}", getClass(), id);
            }catch(Exception e){
                em.getTransaction().rollback();
                logger.error("Cannot delete alert with id {0}, {1}", id, e );
                throw new RuntimeException(e);
            }finally{
                if(em.isOpen())
                    em.close();
            }
        }else{
            logger.error("Cannot delete non existing entity");
        }
        if(em.isOpen())
            em.close();
    }

//    public Alerts insertAlertOne(Alerts a){
//        EntityManag
//    }






}
