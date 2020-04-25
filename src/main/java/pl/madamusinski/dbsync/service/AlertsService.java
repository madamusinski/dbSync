package pl.madamusinski.dbsync.service;

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


    private final AlertsRepository alertsRepository;
    private final AlertsRepositoryTwo alertsRepositoryTwo;
    @Autowired
    SyncOneDbConfig dbConfig;
    @Autowired
    SyncTwoDbConfig dbConfigTwo;
    @Autowired
    PlatformTransactionManager syncTwoTransactionManager;



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
//        List<Alerts> alertsList = (List<Alerts>)em.createNativeQuery("select * from alerts");
        Query query = em.createQuery("select a from Alerts a");
        List<Alerts> alertsList = query.getResultList();
        alertsList.stream().forEach(System.out::println);
        return alertsList;

//        return em.
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

    public Object getTimeOfLastSync(){
        List maxTime = new ArrayList();
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
        Query q = emTarget.createQuery("select max(a.timeStamp) from Alerts a");
        maxTime = q.getResultList();
        return maxTime.get(0);
    }

    public List<Alerts> getAlertsNotSynced(Date fromTimestamp){
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        List<Alerts> alertsList = new ArrayList<>();
        Query q = emSource.createQuery("select a from Alerts a where a.timeStamp > :timeStamp")
                .setParameter("timeStamp", fromTimestamp);
        alertsList = q.getResultList();
        return alertsList;
    }

    public List<Alerts> getAllAlerts(){
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        List<Alerts> alertsToCopy = new ArrayList<>();
        Query q = emSource.createQuery("select a from Alerts a");
        alertsToCopy = q.getResultList();
        return alertsToCopy;
    }

    public void fillInTargetTable(List<Alerts> alerts){
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
        try{
            emTarget.getTransaction().begin();
            for(Alerts a : alerts){
                emTarget.merge(a);
            }
            emTarget.getTransaction().commit();
        }catch(Exception e){
            emTarget.getTransaction().rollback();
            throw new RuntimeException("Copying entire table didnt succeed", e);
        }
    }

    public void syncTables(List<Alerts> alerts){
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
        try{
            emTarget.getTransaction().begin();
            for(Alerts a : alerts){
                emTarget.merge(a);
            }
            emTarget.getTransaction().commit();
        }catch (Exception e){
            emTarget.getTransaction().rollback();
            throw new RuntimeException("Rollback", e);
        }
    }

    public List<Alerts> complexCopy(Integer id){
        List<Alerts> returnAlertsList = new ArrayList<>();
        EntityManagerFactory emfTwo = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory();
        EntityManagerFactory emf = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory();
        EntityManager emTwo = emfTwo.createEntityManager();
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select a from Alerts a where a.id > :id ")
                .setParameter("id", id);

        List<Alerts> alertsList = q.getResultList();
        alertsList.stream().forEach(System.out::println);
        emTwo.getTransaction().begin();
        for(Alerts a : alertsList){
            try{
                a.setCode(215);
                a.setTimeStamp(new Date());
               Alerts copyA = emTwo.merge(a);
               returnAlertsList.add(copyA);
            }catch(Exception e){
                emTwo.getTransaction().rollback();
                e.printStackTrace();
            }
        }
        emTwo.getTransaction().commit();
        return returnAlertsList;

    }

//    public Alerts insertAlertOne(Alerts a){
//        EntityManag
//    }






}
