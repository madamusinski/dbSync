package pl.madamusinski.dbsync.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.config.SyncOneDbConfig;
import pl.madamusinski.dbsync.config.SyncTwoDbConfig;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.SyncService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AlertsSyncService implements SyncService<Alerts> {

    private final Logger logger = LoggerFactory.getLogger(AlertsSyncService.class);
    @Autowired
    SyncOneDbConfig dbConfig;
    @Autowired
    SyncTwoDbConfig dbConfigTwo;

    @Override
    public Date lastSyncTime() {
        List<Alerts> maxTime;
        EntityManager emTarget = dbConfigTwo.syncTwoEntityManager().getNativeEntityManagerFactory().createEntityManager();
//        CriteriaBuilder cb = emTarget.getCriteriaBuilder();
//        CriteriaQuery<Alerts> q = cb.createQuery(Alerts.class);
//        Root<Alerts> a = q.from(Alerts.class);
//        Expression<Date>
//        q.select(cb.max())


        //        Query q = emTarget.createQuery("select max(a.timeStamp) from Alerts a");
//        maxTime = q.getResultList();
//        emTarget.close();
//        return maxTime.get(0).getTimeStamp();
        return emTarget.cr
    }

    @Override
    public List<Alerts> findOutOfSyncObjects(Date from) {
        return null;
    }

    @Override
    public void synchronize(List<Alerts> toSynchronize) {

    }
}
