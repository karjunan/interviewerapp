package com.softvision.helper;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.softvision.constant.InterviewConstant;
import com.softvision.exception.ServiceException;
import com.softvision.model.Candidate;
import com.softvision.model.Employee;

import java.util.*;
import javax.inject.Inject;
import javax.swing.text.html.Option;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceClientHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceClientHelper.class);

    @Inject
    RestTemplate restTemplate;

    @Inject
    private EurekaClient eurekaClient;

    @Value("${admin.service}")
    private String adminService;

    @Value("${admin.method}")
    private String serviceMethod;


    @Value("${admin.interviewer.getbyid}")
    private String interviewer;

    @Value("${admin.candidate.method}")
    private String candidateMethod;



    private InstanceInfo getInstance(String serviceName){
        try{
            Application application = eurekaClient.getApplication(serviceName);
            return application.getInstances().get(0);
        }catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }


    public Collection<Employee> getInterviewers(String technology, int experience) {
        List<Employee> searchList = new ArrayList<>();
        if ((technology != null && !technology.isEmpty()) && (experience > 0)) {
            try {
                InstanceInfo instanceInfo= getInstance(adminService);
                String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()
                        + "/" + serviceMethod +"be=" + experience +"&tc="+ technology ;
                System.out.println("URL : " + url);
                Employee[] forNow = restTemplate.getForObject(url, Employee[].class);
                searchList= Arrays.asList(forNow);
                System.out.println("-------------------------searchList----------------------"+searchList);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException(InterviewConstant.INVALID_REQUEST);
        }
        return searchList;
    }

    public Optional<List<Employee>> getInterviewer(String interviewerId) {
        Optional<List<Employee>> forNow  ;
        if ((interviewerId != null && !interviewerId.isEmpty())) {
            try {
                InstanceInfo instanceInfo= getInstance(adminService);
                String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()
                        + "/" + interviewer+"/empid/"+interviewerId ;
                System.out.println("URL : " + url);
                Employee[] employees = restTemplate.getForObject(url, Employee[].class);
                List<Employee> employeeList = Arrays.asList(employees);
                forNow = Optional.ofNullable(employeeList);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException(InterviewConstant.INVALID_REQUEST);
        }
        return forNow;
    }

    public Candidate getCandidateById(String candidateId) {
        Candidate candidate= null;
        if (candidateId != null && !candidateId.isEmpty()) {
            try {
                InstanceInfo instanceInfo= getInstance(adminService);
                String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort()
                        + "/" + candidateMethod+"/" + candidateId;
                System.out.println("URL : " + url);
                 candidate = restTemplate.getForObject(url, Candidate.class);

                System.out.println("-------------------------candidate----------------------"+candidate);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        } else {
            throw new ServiceException(InterviewConstant.INVALID_REQUEST);
        }
        return candidate;
    }
}
