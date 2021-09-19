package com.nps.devassessment.service;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.model.WorkflowData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkflowRepoService {

    // fetch an individual workflow by its 'id'
    WorkflowEntity findWorkflowById(Long id);

    List<WorkflowEntity> findByYjbYp(Long id);

    List<WorkflowEntity> findByWorkflowState(String workflowState);

    List<WorkflowEntity> findByYjbYpIn(List<Long> yjbYp);


    List<WorkflowEntity> getWorkflowAfterDate(Timestamp date);


    List<WorkflowEntity> getWorkflowModifiedWithinDate(Timestamp startDate,
                                                       Timestamp endDate);


    List<WorkflowEntity> findByProcessAndTaskStatusNot(String process,String taskStatus );


    List<WorkflowData> getWorkflowData(String createdBy);


    List<WorkflowEntity> getFirstTenWorkflowByProcessOrderById(String process);

    Page<WorkflowEntity> findAll(Pageable paging);

    List<WorkflowEntity> findAll();

    List<WorkflowEntity> findByProcess(String process);

    List<WorkflowEntity>findByTaskStatus(String taskStatus);

    List<WorkflowEntity>findByProcessAndTaskStatus(String process, String taskStatus);

}
