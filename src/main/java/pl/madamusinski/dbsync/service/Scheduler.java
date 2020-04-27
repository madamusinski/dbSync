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
import pl.madamusinski.dbsync.domain.Deletions;

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

    private final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    AlertsService alertsService;
    @Autowired
    AlertsSyncService alertsSyncService;
    /**
     * Main method that runs at fixed rate tasks to sync database
     * whatever is put inside will be executed every given amount of time
     */
    @Timed
    @Scheduled(fixedRate = 1500, initialDelay = 2000)
    public void synchronizeTargetDatabase(){
        List<Alerts> alertsToSync;
        List<Deletions> alertsToDelete = new ArrayList<>();

        Date maxTime = alertsSyncService.lastSyncTime();

        if(Objects.nonNull(maxTime)){
            log.info("Last sync time is {}",
                    (Objects.nonNull(maxTime)) ? maxTime : "unknown");
            alertsToSync = alertsSyncService.findOutOfSyncObjects(maxTime);
            alertsToDelete = alertsSyncService.lookUpDeletedEntries(maxTime);
        }else{
            log.info("There is no max time to compare against, probably there is no data in dbsync2" +
                    ", syncing entire tables right now!");
            alertsToSync = alertsService.getAllAlerts();
        }

        if(alertsToSync.size()!=0 || alertsToDelete.size()!=0){
            if(alertsToDelete.size()!=0)
                alertsSyncService.synchronizeDeletions(alertsToDelete);
            if(alertsToSync.size()!=0)
                alertsSyncService.synchronize(alertsToSync);
            log.info("Synchronization on dbsync2 completed");
        }else{
            log.info("Tables are synchronized");
        }

    }

}
