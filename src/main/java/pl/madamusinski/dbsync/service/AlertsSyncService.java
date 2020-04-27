package pl.madamusinski.dbsync.service;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.config.SyncOneDbConfig;
import pl.madamusinski.dbsync.config.SyncTwoDbConfig;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.domain.Deletions;
import pl.madamusinski.dbsync.repository.SyncService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Mateusz Adamusiński
 * Service used for providing methods to synchronize two tables of same type of Entity
 * This is implementation of SyncService interface
 */
@Service
public class AlertsSyncService implements SyncService<Alerts> {

    private final Logger logger = LoggerFactory.getLogger(AlertsSyncService.class);
    @Autowired
    SyncOneDbConfig dbConfig;
    @Autowired
    SyncTwoDbConfig dbConfigTwo;

    @Override
    public Date lastSyncTime() {
        List<Date> maxTime;
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
        maxTime = emTarget.createQuery("select max(a.timeStamp) from Alerts a").getResultList();
        emTarget.close();
        return maxTime.get(0);
    }

    @Override
    public List<Alerts> findOutOfSyncObjects(Date from) {
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        List<Alerts> resultSet = emSource.createQuery("select a from Alerts a where a.timeStamp > :from ")
                .setParameter("from", from).getResultList();
        emSource.close();
        return resultSet;
    }

    @Override
    public List<Deletions> lookUpDeletedEntries(Date from) {
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        List<Deletions> resultSet =  emSource.createQuery("select d from Deletions d where d.timeStamp > :from")
                .setParameter("from", from).getResultList();
        if(emSource.isOpen())
            emSource.close();
        return resultSet;
    }

    @Override
    public void synchronizeDeletions(List<Deletions> toSynchronizeDeletions) {
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        Session session = emTarget.unwrap(Session.class);
        List<Alerts> alertsToDelete = new ArrayList<>();
        List<Deletions> outOfDateDeletes = new ArrayList<>();
        List<Deletions> afterCommitDeletes = new ArrayList<>();
        StringBuilder idNulls = new StringBuilder();
        try{
            session.getTransaction().begin();
            toSynchronizeDeletions.stream().forEach(id ->{
                Alerts temp = emTarget.find(Alerts.class, id.getIdDeleted());
                if(Objects.isNull(temp)){
                    idNulls.append(id.getIdDeleted() + ", ");
                    outOfDateDeletes.add(id);
                }else{
                    alertsToDelete.add(temp);
                }
            });

            if(idNulls.length()>2)
                idNulls.delete(idNulls.length()-2, idNulls.length()-1);
            alertsToDelete.stream().filter(Objects::nonNull).forEach(emTarget::remove);
            session.getTransaction().commit();
            if(Objects.nonNull(toSynchronizeDeletions)){
                removeOutOfDateDeletions(toSynchronizeDeletions);
            }
            if(Objects.nonNull(outOfDateDeletes)){
                removeOutOfDateDeletions(outOfDateDeletes);
            }

            outOfDateDeletes.stream().forEach(System.out::println);
            logger.info("{}"
                    , Objects.nonNull(alertsToDelete)
                            ? "Synchonization of deleted items on target table succesful. Entries deleted"
                            + alertsToDelete.toString()
                            :"Attempting to delete entries that do not exist having id: "
                            + idNulls);
        }catch(Exception e){
            emTarget.getTransaction().rollback();
            logger.error("Transaction rollback, error {}",
                    e);
            throw new RuntimeException(e);
        }finally{
            if(emTarget.isOpen())
                emTarget.close();
        }
    }

    public void removeOutOfDateDeletions(List<Deletions> outOfDateDeletions){
        EntityManager emSource = dbConfig.syncOneEntityManager().getNativeEntityManagerFactory().createEntityManager();
        Session session = emSource.unwrap(Session.class);
        try{
            session.getTransaction().begin();
           outOfDateDeletions.stream().forEach(d->session.update(d));
            outOfDateDeletions.stream().forEach(session::remove);
            session.getTransaction().commit();
            logger.info("Succesfuly removed out of date deletions");
        }catch (Exception e){
            session.getTransaction().rollback();
            logger.info("Something went wrong with removing deletions, rolling back changes. {}", e);
        }finally {
            if(session.isOpen())
                session.close();
            if(emSource.isOpen())
                emSource.close();

        }
    }

    @Override
    public void synchronize(List<Alerts> toSynchronize) {
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
        try{
            emTarget.getTransaction().begin();
            toSynchronize.stream().forEach(emTarget::merge);
            emTarget.getTransaction().commit();
            logger.info("Succesfuly synchronized targetDatabase, objects synced {}", toSynchronize.toString());
        }catch(Exception e){
            emTarget.getTransaction().rollback();
            logger.error("Synchronziation error. Executing rollback. Error while commiting changes to target table: {}", e);
            throw new RuntimeException(e);
        }finally {
            if(emTarget.isOpen())
                emTarget.close();
        }
    }
}
