package com.nps.devassessment.service;

import org.hibernate.service.spi.ServiceException;

public class WorkflowNotFoundException extends ServiceException {

    public WorkflowNotFoundException(String message, Throwable root) {
        super(message, root);
    }

    public WorkflowNotFoundException(String message) {
        super(message);
    }
}
