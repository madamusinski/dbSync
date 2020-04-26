package pl.madamusinski.dbsync.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value="/")
    public String homePage(){
        return "Welcome to sync web service, the service gets triggered every few seconds";
    }
}
