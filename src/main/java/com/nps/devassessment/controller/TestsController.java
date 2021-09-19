package com.nps.devassessment.controller;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.service.WorkflowNotFoundException;
import com.nps.devassessment.service.WorkflowRepoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class TestsController {

    private static final Logger log = LoggerFactory.getLogger(TestsController.class);

    @Autowired
    private WorkflowRepoService workflowRepoService;

    @RequestMapping(method = RequestMethod.GET, value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkflowEntity> getWorkflowDetails(@PathVariable Long id) {

        try {
            WorkflowEntity workflow = workflowRepoService.findWorkflowById(id);
            return ResponseEntity.status(HttpStatus.OK).body(workflow);
        } catch (WorkflowNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


    }


    @RequestMapping(method = RequestMethod.GET, value = "/yjbYb/{yjbYb}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WorkflowEntity>> getWorkflowDetailsForYjbYb(@PathVariable Long yjbYb) {

        try {
            List<WorkflowEntity> workflow = workflowRepoService.findByYjbYp(yjbYb);
            return ResponseEntity.status(HttpStatus.OK).body(workflow);
        } catch (WorkflowNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


    }

}