package com.nps.devassessment.setup;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.service.WorkflowRepoService;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SetupTests {

    private static final Logger log = LoggerFactory.getLogger(SetupTests.class);

    @Autowired
    private WorkflowRepoService workflowRepoService;


    // NOTE - This is a sample
    @Test
    public void test0_shouldProvideASampleOfAWorkingRepoCall() {

        // start test
        log.info("Starting test0 to demonstrate working repo call...");
        WorkflowEntity workflowEntity = this.workflowRepoService.findWorkflowById(66176L);

        // Assert
        Assert.assertNotNull(workflowEntity);
        Assert.assertEquals("IN PROGRESS", workflowEntity.getWorkflowState());

        // end test
        log.info("Workflow {} found.  yjb_yp_id = {}, workflow_state = {}", workflowEntity.getId(), workflowEntity.getYjbYp(), workflowEntity.getWorkflowState());
        log.info("test0 complete");
    }



    @Test
    public void test1_shouldDemonstrateRequestedRepoQueries() {
        // implement queries as per the word document
        // assert that the results of each of the query calls is not null/empty
        // write the count of each of the queries to the log

        // Select workflows by workflow_state = a given status  (e.g. “IN PROGRESS”, “CANCELLED”, “ADMITTED”)

        // Select workflows by a given list of yjb_yp_id values  (e.g. 30848, 32524, 28117)

        // Select workflows by 'created' column is after a given date (e.g. 01/02/2021)

        // Select workflows by 'modified' column is after a given date (e.g. 01/01/20) but before another given date (e.g. 01/03/2021)

        // Select workflows by process = a given value (e.g. “placementProcess”) AND task_status != a given value (e.g.  “ADMITTED”)

        // Select id, yjb_yp_id and task_status columns for all workflows where created_by = a given value (e.g. “lee.everitt”)

        // Select the first 10 rows where process = a given value (e.g. “transferPlanned”).  Order the results by id in descending order

        throw new NotYetImplementedException();
    }


    @Test
    public void test2_shouldDemonstratePageableRepoCapability() {
        // Page through the entire workflow repo using a page size of 20
        // For each page: write the count of each distinct workflow_status to the log
        // Once you have paged through the entire table, write the amount of pages to the log

        throw new NotYetImplementedException();
    }
}
