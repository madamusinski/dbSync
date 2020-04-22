package pl.madamusinski.dbsync.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.syncTwo.AlertsRepositoryTwo;
import pl.madamusinski.dbsync.service.AlertsService;
import pl.madamusinski.dbsync.service.AlertsServiceTwo;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class AlertsResource {

    private final AlertsService alertsService;
//    private final AlertsServiceTwo alertsServiceTwo;


    public AlertsResource(AlertsService alertsService
//            , AlertsServiceTwo alertsServiceTwo
    ){
        this.alertsService = alertsService;
//        this.alertsServiceTwo = alertsServiceTwo;
    }

    @GetMapping("/alerts")
    public List<Alerts> getAlerts(){
        return alertsService.findAlerts();
    }

    @GetMapping("alertstwo")
    public List<Alerts> getAlertsTwo(){
        return alertsService.findAlertsTwo();
    }
}
