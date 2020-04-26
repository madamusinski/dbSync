package pl.madamusinski.dbsync.service;

import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.madamusinski.dbsync.config.SyncOneDbConfig;
import pl.madamusinski.dbsync.config.SyncTwoDbConfig;
import pl.madamusinski.dbsync.domain.Alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Mateusz Adamusiński
 * Main service responsible for scheduled with certain interval sync logic
 * between db 1 and db2 alert tables
 */

@Service
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    AlertsService alertsService;
    /**
     * Main method that runs every fixed rate to sync database
     * whatever is put inside will be executed every given amount of time
     */
    @Timed
    @Scheduled(fixedRate = 5000, initialDelay = 5000)
    public void synchronizeDatabase(){
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
