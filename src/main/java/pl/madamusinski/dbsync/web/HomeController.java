package pl.madamusinski.dbsync.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.madamusinski.dbsync.util.Scheduler;

@RestController
public class HomeController {

    @Autowired
    Environment env;
    private final Scheduler job;
    public HomeController(Scheduler job){
        this.job = job;
    }
    @GetMapping(value="/")
    public String homePage(){
        job.job();
        return env.getProperty("spring.datasource.url");
    }
}
