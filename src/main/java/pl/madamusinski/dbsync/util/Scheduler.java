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
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.service.AlertsService;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@Service
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    private SyncOneDbConfig syncOneEntityManager;
    @Autowired
    AlertsService alertsService;
    @Autowired
    private SyncTwoDbConfig syncTwoEntityManager;
    @Timed
    @Scheduled(fixedRate = 5000, initialDelay = 5000)
    public void job(){
       Date maxTime = (Date)alertsService.getTimeOfLastSync();
        List<Alerts> alertsToSync = new ArrayList<>();

        log.info("maxTime is {0}", maxTime);
        if(Objects.nonNull(maxTime)){
            alertsToSync = alertsService.getAlertsNotSynced(maxTime);
            alertsService.syncTables(alertsToSync);
        }else{
            log.info("Max time does not exist on target table, copying entire table");
            alertsToSync = alertsService.getAllAlerts();
            if(Objects.nonNull(alertsToSync)){
                alertsService.fillInTargetTable(alertsToSync);
            }else{
                log.error("Source Table is empty", new Exception("Empty source table"));
            }
        }
        log.info("Synchronization on dbsync2 completed");
    }

}
