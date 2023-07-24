package org.example.camunda.process.solution.facade;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.EvaluateDecisionResponse;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import org.example.camunda.process.solution.ProcessConstants;
import org.example.camunda.process.solution.ProcessVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class ProcessController {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);
  private final ZeebeClient zeebe;

  public ProcessController(ZeebeClient client) {
    this.zeebe = client;
  }

  @PostMapping("/start")
  public void startProcessInstance(@RequestBody ProcessVariables variables) {

    LOG.info(
        "Starting process `" + ProcessConstants.BPMN_PROCESS_ID + "` with variables: " + variables);

    zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
        .latestVersion()
        .variables(variables)
        .send();
  }

  @PostMapping("/startWithResults")
  public ProcessInstanceResult startProcessInstanceWithResults(
      @RequestBody ProcessVariables variables) {

    LOG.info(
        "Starting process `" + ProcessConstants.BPMN_PROCESS_ID + "` with variables: " + variables);

    ProcessInstanceResult result =
        zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
            .latestVersion()
            .variables(variables)
            .withResult()
            .send()
            .join();

    return result;
  }

  @PostMapping("/message/{messageName}/{correlationKey}")
  public void publishMessage(
      @PathVariable String messageName,
      @PathVariable String correlationKey,
      @RequestBody ProcessVariables variables) {

    LOG.info(
        "Publishing message `{}` with correlation key `{}` and variables: {}",
        messageName,
        correlationKey,
        variables);

    zeebe
        .newPublishMessageCommand()
        .messageName(messageName)
        .correlationKey(correlationKey)
        .variables(variables)
        .send();
  }

  @PostMapping("/dmn/eval/{decisionId}")
  public EvaluateDecisionResponse evaluateDecision(
      @PathVariable String decisionId, @RequestBody ProcessVariables variables) {

    LOG.info("Evaluating decision `{}` with variables: {}", decisionId, variables);
    long startTime = System.nanoTime();

    EvaluateDecisionResponse response =
        zeebe
            .newEvaluateDecisionCommand()
            .decisionId(decisionId)
            .variables(variables)
            .send()
            .join();

    long endTime = System.nanoTime();
    long duration = (endTime - startTime); // divide by 1000000 to get milliseconds.

    LOG.info("Execution Time (millis): {}", duration / 1000000);
    LOG.info("Response: {}", response.getDecisionOutput());

    return response;
  }
}
