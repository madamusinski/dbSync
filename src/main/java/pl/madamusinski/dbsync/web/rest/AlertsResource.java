package pl.madamusinski.dbsync.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.service.AlertsService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class AlertsResource {

    private final AlertsService alertsService;

    public AlertsResource(AlertsService alertsService){
        this.alertsService = alertsService;
    }

    @GetMapping("/alerts")
    public List<Alerts> getAlerts(){
        return alertsService.findAlerts();
    }
}
