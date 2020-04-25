package pl.madamusinski.dbsync.web.rest;

import org.springframework.web.bind.annotation.*;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.service.AlertsService;

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
//        return alertsService.findAlertsTwo();
        return alertsService.findAlertsTwoTwo();
    }

    @PostMapping("/alerts")
    public Alerts addAlert(@RequestBody Alerts alert){
        return alertsService.save(alert);
    }

    @PutMapping("/alerts")
    public Alerts updateAlert(@RequestBody Alerts alert){
        return alertsService.save(alert);
    }

    @PostMapping("/alertstwo")
    public Alerts addAlertTwo(@RequestBody Alerts alert) throws Exception {
        return alertsService.savetwo(alert);
    }

    @GetMapping("/copy/{id}")
    public List<Alerts> copy(@PathVariable(name = "id") Integer id){
//        return alertsService.complexCopy(id);
        return null;
    }

    @GetMapping("/copytwo/{id}")
    public List<Alerts> copyTwo(@PathVariable(name = "id") Integer id){
       return alertsService.complexCopy(id);
    }
}
