package com.nps.devassessment.repo;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.model.WorkflowData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkflowRepo extends PagingAndSortingRepository<WorkflowEntity, Long> {

    Optional<WorkflowEntity> findWorkflowById(Long id);

    List<WorkflowEntity> findByYjbYp(Long yjbYp);

    List<WorkflowEntity> findByWorkflowState(String workflowState);

    List<WorkflowEntity> findByProcess(String process);

    List<WorkflowEntity> findByTaskStatus(String taskStatus);

    List<WorkflowEntity> findByProcessAndTaskStatus(String process, String taskStatus);

    List<WorkflowEntity> findByYjbYpIn(@Param("yjbYp")List<Long> yjbYp);

    @Query(value = "SELECT * FROM WORKFLOW wor "
            + "WHERE wor.CREATED > :date", nativeQuery = true)
    List<WorkflowEntity> getWorkflowAfterDate(@Param("date") Timestamp date);

    @Query(value = "SELECT * FROM WORKFLOW wor "
            + "WHERE wor.MODIFIED > :startDate AND wor.MODIFIED < :endDate ", nativeQuery = true)
    List<WorkflowEntity> getWorkflowModifiedWithinDate(@Param("startDate")Timestamp startDate,
                                                 @Param("endDate")Timestamp endDate);


    List<WorkflowEntity> findByProcessAndTaskStatusNot(@Param("process")String process,@Param("taskStatus") String taskStatus );


    @Query(value = "SELECT wor.ID as id, wor.YJB_YP_ID as yjbYpId, wor.TASK_STATUS as taskStatus FROM WORKFLOW wor "
            + "WHERE wor.CREATED_BY = :createdBy", nativeQuery = true)
    List<WorkflowData> getWorkflowData(@Param("createdBy")String createdBy);


    @Query(value = "SELECT * "
            + "FROM (select * from workflow wor where wor.process= :process ORDER BY wor.id DESC) "
            + "WHERE ROWNUM <= 10 ", nativeQuery = true)
    List<WorkflowEntity> getFirstTenWorkflowByProcessOrderById(@Param("process")String process);

}
