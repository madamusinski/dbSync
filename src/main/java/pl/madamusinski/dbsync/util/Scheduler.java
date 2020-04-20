package pl.madamusinski.dbsync.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

//    @Scheduled(fixedRate = 500)
    public void job(){
        int i = 2;
        System.out.println(i*i);
        log.info("job has been done");
    }
}
