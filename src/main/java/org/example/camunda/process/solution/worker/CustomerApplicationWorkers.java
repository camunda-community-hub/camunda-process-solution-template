package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import org.example.camunda.process.solution.ProcessVariables;
import org.example.camunda.process.solution.dto.CustomerApplication;
import org.example.camunda.process.solution.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomerApplicationWorkers {

  private static final Logger LOG = LoggerFactory.getLogger(CustomerApplicationWorkers.class);

  private final MyService myService;

  public CustomerApplicationWorkers(MyService myService) {
    this.myService = myService;
  }

  @JobWorker(type = "CheckCustomerInfo")
  public void checkCustomerInfo(final JobClient client, final ActivatedJob job) {
    LOG.info("Invoking checkCustomerInfo");
    ProcessVariables variables = job.getVariablesAsType(ProcessVariables.class);

    variables.getCustomer().setName("Dave");

    client.newCompleteCommand(job.getKey())
        .variables(variables)
        .send().join();
  }

  @JobWorker(type = "UpdateApplicationInfo")
  public void updateApplicationInfo(final JobClient client, final ActivatedJob job) {
    LOG.info("Invoking UpdateApplicationInfo");
    ProcessVariables variables = job.getVariablesAsType(ProcessVariables.class);

    CustomerApplication customerApplication = new CustomerApplication();
    variables.setCustomerApplication(customerApplication);

    client.newCompleteCommand(job.getKey())
        .variables(variables)
        .send().join();
  }

  @JobWorker(type = "ApproveApplication")
  public void approveApplication(final JobClient client, final ActivatedJob job) {
    LOG.info("Invoking ApproveApplication");
    ProcessVariables variables = job.getVariablesAsType(ProcessVariables.class);

    variables.getCustomerApplication().setApprovalStatus("approved");

    client.newCompleteCommand(job.getKey())
        .variables(variables)
        .send().join();
  }

  @JobWorker(type = "RejectApplication")
  public void rejectApplication(final JobClient client, final ActivatedJob job) {
    LOG.info("Invoking RejectApplication");
    ProcessVariables variables = job.getVariablesAsType(ProcessVariables.class);

    variables.getCustomerApplication().setApprovalStatus("rejected");

    client.newCompleteCommand(job.getKey())
        .variables(variables)
        .send().join();
  }
}
