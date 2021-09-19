package com.nps.devassessment.service.impl;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.model.WorkflowData;
import com.nps.devassessment.repo.WorkflowRepo;
import com.nps.devassessment.service.WorkflowNotFoundException;
import com.nps.devassessment.service.WorkflowRepoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class WorkflowRepoServiceImpl implements WorkflowRepoService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowRepoServiceImpl.class);

    private WorkflowRepo workflowRepo;

    @Autowired
    WorkflowRepoServiceImpl(WorkflowRepo workflowRepo) {
        this.workflowRepo = workflowRepo;
    }


    @Override
    public WorkflowEntity findWorkflowById(final Long id) {
        final Optional<WorkflowEntity> workflow = this.workflowRepo.findById(id);

        if(!workflow.isPresent()) {
            log.info("No workflow record found for id {}", id);
            throw new WorkflowNotFoundException("Workflow record not found for ID:: "+ id);
        }
        log.info("Found workflow record for id {}", id);
        return workflow.get();
    }

    @Override
    public List<WorkflowEntity> findByYjbYp(Long id) {
        return this.workflowRepo.findByYjbYp(id);
    }

    @Override
    public List<WorkflowEntity> findByWorkflowState(String workflowState) {
        return this.workflowRepo.findByWorkflowState(workflowState);
    }

    @Override
    public List<WorkflowEntity> findByYjbYpIn(List<Long> yjbyp) {
        return this.workflowRepo.findByYjbYpIn(yjbyp);
    }

    @Override
    public List<WorkflowEntity> getWorkflowAfterDate(Timestamp date) {
        return this.workflowRepo.getWorkflowAfterDate(date);
    }

    @Override
    public List<WorkflowEntity> getWorkflowModifiedWithinDate(Timestamp startDate, Timestamp endDate) {
        return this.workflowRepo.getWorkflowModifiedWithinDate(startDate,endDate);
    }

    @Override
    public List<WorkflowEntity> findByProcessAndTaskStatusNot(String process, String taskStatus) {
        return this.workflowRepo.findByProcessAndTaskStatusNot(process, taskStatus);
    }

    @Override
    public List<WorkflowData> getWorkflowData(String createdBy) {
        return this.workflowRepo.getWorkflowData(createdBy);
    }

    @Override
    public List<WorkflowEntity> getFirstTenWorkflowByProcessOrderById(String process) {
        return this.workflowRepo.getFirstTenWorkflowByProcessOrderById(process);
    }

    @Override
    public Page<WorkflowEntity> findAll(Pageable paging) {
        return this.workflowRepo.findAll(paging);
    }

    @Override
    public List<WorkflowEntity> findAll() {
        return (List<WorkflowEntity>) this.workflowRepo.findAll();
    }

    @Override
    public List<WorkflowEntity> findByProcess(String process) {
        return this.workflowRepo.findByProcess(process);
    }

    @Override
    public List<WorkflowEntity> findByTaskStatus(String taskStatus) {
        return this.workflowRepo.findByTaskStatus(taskStatus);
    }

    @Override
    public List<WorkflowEntity> findByProcessAndTaskStatus(String process, String taskStatus) {
        return this.workflowRepo.findByProcessAndTaskStatus(process, taskStatus);
    }
}
