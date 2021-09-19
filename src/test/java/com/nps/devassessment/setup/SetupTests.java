package com.nps.devassessment.setup;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.model.WorkflowData;
import com.nps.devassessment.service.WorkflowRepoService;
import org.assertj.core.util.DateUtil;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public void test1_shouldDemonstrateRequestedRepoQueries() throws ParseException {
        // implement queries as per the word document
        // assert that the results of each of the query calls is not null/empty
        // write the count of each of the queries to the log

        // Select workflows by workflow_state = a given status  (e.g. “IN PROGRESS”, “CANCELLED”, “ADMITTED”)
        List<WorkflowEntity> workflowStateInProgressList = this.workflowRepoService.findByWorkflowState("IN PROGRESS");
        Assert.assertNotNull(workflowStateInProgressList);
        log.info("Count of Workflow state (IN PROGRESS) is {}",workflowStateInProgressList.size());

        List<WorkflowEntity> workflowStateCancelledList = this.workflowRepoService.findByWorkflowState("CANCELLED");
        Assert.assertNotNull(workflowStateCancelledList);
        log.info("Count of Workflow state (CANCELLED) is {}",workflowStateCancelledList.size());

        List<WorkflowEntity> workflowStateAdmittedList = this.workflowRepoService.findByWorkflowState("ADMITTED");
        Assert.assertNotNull(workflowStateAdmittedList);
        log.info("Count of Workflow state (ADMITTED) is {}",workflowStateAdmittedList.size());

        // Select workflows by a given list of yjb_yp_id values  (e.g. 30848, 32524, 28117)
        List<Long> yjbYpList = new ArrayList<>();
        yjbYpList.add(30848L);
        yjbYpList.add(32524L);
        yjbYpList.add(28117L);

        List<WorkflowEntity> workflowByYjbYpList = this.workflowRepoService.findByYjbYpIn(yjbYpList);
        Assert.assertNotNull(workflowByYjbYpList);
        log.info("Count of Workflow by a given list of yjb_yp_id values is {}",workflowByYjbYpList.size());

        // Select workflows by 'created' column is after a given date (e.g. 01/02/2021)

        String inDate = "01/02/2021";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp timeStamp = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());

        List<WorkflowEntity> workflowAfterDateList = this.workflowRepoService.getWorkflowAfterDate(timeStamp);
        Assert.assertNotNull(workflowAfterDateList);
        log.info("Count of Workflow created after a given date 01/02/2021 is {}",workflowAfterDateList.size());

        // Select workflows by 'modified' column is after a given date (e.g. 01/01/20) but before another given date (e.g. 01/03/2021)

        String date1 = "01/01/2021";
        String date2 = "01/03/2021";
        Timestamp timeStamp1 = new Timestamp(((java.util.Date)df.parse(date1)).getTime());
        Timestamp timeStamp2 = new Timestamp(((java.util.Date)df.parse(date2)).getTime());

        List<WorkflowEntity> workflowBetweenDatesList = this.workflowRepoService.getWorkflowModifiedWithinDate(timeStamp1, timeStamp2);
        Assert.assertNotNull(workflowBetweenDatesList);
        log.info("Count of Workflow modified after a given date (01/01/20) but before another given date (01/03/2021) is {}",workflowBetweenDatesList.size());


        // Select workflows by process = a given value (e.g. “placementProcess”) AND task_status != a given value (e.g.  “ADMITTED”)
        List<WorkflowEntity> workflowByProcessAndTaskStatusList = this.workflowRepoService.findByProcessAndTaskStatusNot("placementProcess", "ADMITTED");
        Assert.assertNotNull(workflowByProcessAndTaskStatusList);
        log.info("Count of Workflow by process = “placementProcess” AND task_status != “ADMITTED” is {}",workflowByProcessAndTaskStatusList.size());


        // Select id, yjb_yp_id and task_status columns for all workflows where created_by = a given value (e.g. “lee.everitt”)
        List<WorkflowData> workflowDataList = this.workflowRepoService.getWorkflowData("lee.everitt");
        Assert.assertNotNull(workflowDataList);
        log.info("Count of Workflow data (id, yjb_yp_id and task_status) created_by “lee.everitt” is {}",workflowDataList.size());


        // Select the first 10 rows where process = a given value (e.g. “transferPlanned”).  Order the results by id in descending order

        List<WorkflowEntity> firstTenWorkflowList = this.workflowRepoService.getFirstTenWorkflowByProcessOrderById("placementProcess");
        Assert.assertNotNull(firstTenWorkflowList);
        log.info("Count of Workflow where process = transferPlanned is {}",firstTenWorkflowList.size());

    }


    @Test
    public void test2_shouldDemonstratePageableRepoCapability() {
        // Page through the entire workflow repo using a page size of 20
        // For each page: write the count of each distinct workflow_status to the log
        // Once you have paged through the entire table, write the amount of pages to the log

        Pageable paging = PageRequest.of(0,20);
        Page<WorkflowEntity> pageWorkFlow;
        pageWorkFlow = this.workflowRepoService.findAll(paging);

        for(int i=0; i< pageWorkFlow.getTotalPages() ;i++ ) {
            List<WorkflowEntity> workflow = new ArrayList<>();
            Page<WorkflowEntity> pageWorkFlow2;
            pageWorkFlow2 = this.workflowRepoService.findAll(PageRequest.of(i,20));

            workflow = pageWorkFlow2.getContent();

            List<String> taskStatusList = workflow.stream()
                    .map(WorkflowEntity::getTaskStatus)
                    .collect(Collectors.toList());

            long distinctTaskStatusCount = taskStatusList.stream().distinct().count();
            log.info("Count of distinct task status for page {}::{}", i, distinctTaskStatusCount);
        }
        Assert.assertNotNull(pageWorkFlow.getTotalPages());
        log.info("Total number of pages ::" + pageWorkFlow.getTotalPages());
    }
}
