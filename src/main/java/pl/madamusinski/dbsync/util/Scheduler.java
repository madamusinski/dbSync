package pl.madamusinski.dbsync.util;

import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.config.SyncOneDbConfig;
import pl.madamusinski.dbsync.config.SyncTwoDbConfig;

import javax.persistence.EntityManager;

@Service
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    private SyncOneDbConfig syncOneEntityManager;

    @Autowired
    private SyncTwoDbConfig syncTwoEntityManager;
    @Timed
//    @Scheduled(fixedRate = 500)
    public void job(){
//        EntityManager em = syncOneEntityManager.syncOneDataSource().getConnection();
        int i = 2;
        System.out.println(i*i);
        log.info("job has been done");
    }
}
