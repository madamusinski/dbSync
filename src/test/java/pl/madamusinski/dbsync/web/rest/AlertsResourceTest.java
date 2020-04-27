package pl.madamusinski.dbsync.web.rest;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.madamusinski.dbsync.DbsyncApplication;
import pl.madamusinski.dbsync.domain.Alerts;
import pl.madamusinski.dbsync.repository.syncOne.AlertsRepository;
import pl.madamusinski.dbsync.repository.syncTwo.AlertsRepositoryTwo;
import pl.madamusinski.dbsync.service.AlertsService;
import pl.madamusinski.dbsync.service.AlertsSyncService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DbsyncApplication.class)
@RunWith(SpringRunner.class)
public class AlertsResourceTest {


//    @Autowired
//    AlertsRepository alertsRepository;
//    @Autowired
//    AlertsRepositoryTwo alertsRepositoryTwo;
//    @Autowired
//    AlertsService alertsService;
    @Autowired
    AlertsSyncService alertsSyncService;
//    private MockMvc restAlertMockMvc;
//    private Alerts alert;
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    @Before
//    public void setup(){
//        MockitoAnnotations.initMocks(this);
//        this.restAlertMockMvc = webAppContextSetup(webApplicationContext).build();
//        final AlertsResource alertsResource = new AlertsResource(alertsService);
//
//    }
//    @Before
//    public void initTest(){
//        //Create Alert
//        alert = new Alerts(1, "test message", 1, new Date());
//    }

    @Test
    public void checkForAlert(){
        Alerts alert = new Alerts();
        alert.setId(10);
        alert.setMessage("test");
        alert.setCode(1);
        alert.setTimeStamp(new Date());

        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.find(Alerts.class,1L)).thenReturn(alert);

        assertEquals("test", alert.getMessage());
    }

}
