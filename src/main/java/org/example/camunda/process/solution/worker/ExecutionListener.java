package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.CustomHeaders;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExecutionListener {

  private static final Logger LOG = LoggerFactory.getLogger(ExecutionListener.class);

  @JobWorker(type = "sampleExecListener")
  public void execListenWorker(
      final JobClient client,
      final ActivatedJob job,
      @VariablesAsType Map<String, Object> variables,
      @CustomHeaders Map<String, String> headers) {

    try {

      String execListenerName = "sampleExecListener";

      LOG.info("Execution Listener Worker triggered with variables: " + variables);

      String processDefinitionKey = Long.toString(job.getProcessDefinitionKey());
      LOG.info(execListenerName + ": processDefinitionKey: " + processDefinitionKey);

      String formKey = headers.get("io.camunda.zeebe:formKey");
      LOG.info(execListenerName + ": formKey: " + formKey);

      String jobKey = Long.toString(job.getKey());
      LOG.info(execListenerName + ": jobKey: " + jobKey);

      String elementInstanceKey = Long.toString(job.getElementInstanceKey());
      LOG.info(execListenerName + ": elementInstanceKey: " + elementInstanceKey);

      String bpmnProcessId = job.getBpmnProcessId();
      LOG.info(execListenerName + ": bpmnProcessId: " + bpmnProcessId);

      String taskActivityId = job.getElementId();
      LOG.info(execListenerName + ": taskActivityId: " + taskActivityId);

      String assignee = "";
      String groups = "";

      if (!job.getCustomHeaders().isEmpty()) {
        if (job.getCustomHeaders().containsKey("io.camunda.zeebe:assignee")) {
          assignee = job.getCustomHeaders().get("io.camunda.zeebe:assignee");
        }
        if (job.getCustomHeaders().containsKey("io.camunda.zeebe:candidateGroups")) {
          groups = job.getCustomHeaders().get("io.camunda.zeebe:candidateGroups");
          //JsonUtils.toParametrizedObject(groups, new TypeReference<List<String>>() {})
        }
      }

    } catch (Exception e) {
      LOG.error("Exception occured in UserTaskWorker", e);
      client
          .newFailCommand(job.getKey())
          .retries(0)
          .errorMessage("Exception occured in UserTaskWorker - " + e.getMessage())
          .send();
    }
  }
}
