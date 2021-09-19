package com.nps.devassessment.general;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.service.WorkflowRepoService;
import com.nps.devassessment.setup.SetupTests;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GeneralTests {

    private static final Logger log = LoggerFactory.getLogger(SetupTests.class);

    @Autowired
    private WorkflowRepoService workflowRepoService;


    @Test
    public void test1_shouldDemonstrateFilteringInLambdas() {
        // Use the query you created during the Setup tests to select all workflows with a workflow state of "IN PROGRESS"
        // Filter the results by task_status  NULL
        // With the resulting set of workflows, concatenate all of the id values into a comma-separated string and
        //    write that string to the log

        List<WorkflowEntity> workflow = this.workflowRepoService.findByWorkflowState("IN PROGRESS");

        String concatenatedIdvalue = workflow.stream()
                .filter(w -> Objects.nonNull(w.getTaskStatus()))
                .map(w -> w.getId())
                .map(Object::toString)
                .collect(Collectors.joining(","));
        Assert.assertNotNull(concatenatedIdvalue);
        log.info("Comma separated concatenated value of all id values is {} ", concatenatedIdvalue);

    }


    @Test
    public void test2_shouldDemonstrateIdentificationOfMinAndMaxValues() {
        // Use the query you created during the Setup Tests to select workflows by status = “placementProcess” and task_status is not “admitted”
        // Given the results of the query, identify the highest and the lowest yjb_yp_id
        // Write those two values to the log

        List<WorkflowEntity> workflow = this.workflowRepoService.findByProcessAndTaskStatusNot("placementProcess","ADMITTED");

        WorkflowEntity minYjbYpIdWF = workflow.stream()
                .min(Comparator.comparing( WorkflowEntity::getYjbYp))
                .get();
        WorkflowEntity maxYjbYpIdWF = workflow.stream()
                .max(Comparator.comparing( WorkflowEntity::getYjbYp))
                .get();
        Assert.assertNotNull(minYjbYpIdWF);
        Assert.assertNotNull(maxYjbYpIdWF);
        log.info("Highest yjb_yp_id is {}", minYjbYpIdWF.getYjbYp());
        log.info("Lowest yjb_yp_id is {}", maxYjbYpIdWF.getYjbYp());

    }


    @Test
    public void test3_shouldDemonstrateModifyingAValueFromWithinALambda() {
        // Identify the lowest yjb_yp_id in the workflow table using whatever means you deem appropriate - store in a variable
        // Using a lambda, loop through all entries in the workflow table
        // For each workflow: If the yjb_yp_id is greater than the value you have stored in the variable, update the variable with the new value
        // After you have looped through all entries in the table, outside of the lambda write the highest yjb_yp_id to the log

        List<WorkflowEntity> workflow = this.workflowRepoService.findAll();

        WorkflowEntity minYjbYpIdWf = workflow.stream()
                .min(Comparator.comparing( WorkflowEntity::getYjbYp))
                .get();
        workflow.stream().forEach(w -> {
            if(w.getYjbYp() > minYjbYpIdWf.getYjbYp() ) {
                minYjbYpIdWf.setYjbYp(w.getYjbYp());
            }
        });
        Assert.assertNotNull(minYjbYpIdWf.getYjbYp());
        log.info("Highest yjb_yp_id is {}", minYjbYpIdWf.getYjbYp());
    }


    @Test
    public void test4_shouldDemonstrateUsingParallelProcesses() throws ExecutionException, InterruptedException, IOException {
        // Create an empty JSON array
        // Query the workflow table for all workflows by process = “placementProcess”
        // In parallel, query the workflow table for all workflows by task_status = “ADMITTED”
        // When both parallel processes are complete, identify workflows that exist in both sets of data
        // For each workflow that exists in both sets of data, convert the workflow to a JSON representation, and add the resulting JSON object to the JSON array
        // Assert that the JSON array is not empty or null
        // Write the resulting JSON to a file called “test4.json” so that it appears in the “resources” package of the project

        JSONArray values = new JSONArray();

        CompletableFuture<List<WorkflowEntity>> workflowByProcessFuture = CompletableFuture.completedFuture(this.workflowRepoService.findByProcess("placementProcess"));
        workflowByProcessFuture.exceptionally(exception -> {
           log.error("Workflow exception while finding record by process: "+ exception);
           throw new CompletionException(exception);
        });

        CompletableFuture<List<WorkflowEntity>> workflowByTaskStatusFuture = CompletableFuture.completedFuture(this.workflowRepoService.findByTaskStatus("ADMITTED"));
        workflowByTaskStatusFuture.exceptionally(exception -> {
            log.error("Workflow exception while finding record by task status: "+ exception);
            throw new CompletionException(exception);
        });

        CompletableFuture.allOf(workflowByProcessFuture, workflowByTaskStatusFuture).join();

        Set<Long> ids = workflowByTaskStatusFuture.get().stream().map(obj -> obj.getId()).collect(Collectors.toSet());

        List<WorkflowEntity> result = workflowByProcessFuture.get().stream()
                .filter(obj -> ids.contains(obj.getId()))
                .collect(Collectors.toList());

        JSONObject label = new JSONObject();
        label.put("workflow", result);
        values.add(label);

        Assert.assertNotNull(values);

        FileWriter file = new FileWriter("src//main//resources//test4.json");
        file.write(values.toJSONString());


    }

}
