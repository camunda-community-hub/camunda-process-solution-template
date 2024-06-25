package org.example.camunda.process.solution.facade;

import io.camunda.zeebe.client.ZeebeClient;
import org.example.camunda.process.solution.ProcessConstants;
import org.example.camunda.process.solution.ProcessVariables;
import org.example.camunda.process.solution.dto.BPMNDiagramDTO;
import org.example.camunda.process.solution.service.JsonToBpmnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/process")
public class ProcessController {

  private JsonToBpmnService jsonToBpmnService;

  private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);
  private final ZeebeClient zeebe;

  public ProcessController(ZeebeClient client,
                           JsonToBpmnService jsonToBpmnService) {
    this.zeebe = client;
    this.jsonToBpmnService = jsonToBpmnService;
  }

  @PostMapping("/start")
  public void startProcessInstance(
      @RequestBody ProcessVariables variables) {
    String processId = ProcessConstants.BPMN_PROCESS_ID;
    if(variables.getProcessId() != null && !variables.getProcessId().isEmpty()) {
       processId = variables.getProcessId();
    }

    LOG.info(
        "Starting process `" + processId + "` with variables: " + variables);

    zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(processId)
        .latestVersion()
        .variables(variables)
        .send();
  }

  @PostMapping("/jsonToBpmn")
  public Object startProcessInstanceWithJson(
      @RequestBody String jsonString) throws IOException {

    BPMNDiagramDTO result = jsonToBpmnService.convertJson(jsonString);
    LOG.info("Converting process `" + result.getProcessId() + "`");
    return result;
  }

  @PostMapping("/jsonToBpmnDeploy")
  public void startProcessInstanceWithJsonDeploy(
      @RequestBody String jsonString) throws IOException {

    BPMNDiagramDTO result = jsonToBpmnService.convertJson(jsonString);
    LOG.info("Deploying process `" + result.getProcessId() + "`");

    zeebe
        .newDeployResourceCommand().addResourceStringUtf8(result.getBpmn(), result.getProcessId() + ".bpmn")
        .send()
        .join();
  }

  @PostMapping("/jsonToBpmnDeployRun/{customerAge}")
  public void startProcessInstanceWithJsonDeployRun(
      @PathVariable Integer customerAge,
      @RequestBody String jsonString) throws IOException {

    BPMNDiagramDTO result = jsonToBpmnService.convertJson(jsonString);
    LOG.info("Deploying and Running process `" + result.getProcessId() + "`");

    zeebe
        .newDeployResourceCommand().addResourceStringUtf8(result.getBpmn(), result.getProcessId() + ".bpmn")
        .send()
        .join();

    if(customerAge == null) {
      customerAge = 18;
    }

    Map<String, Integer> customer = new HashMap<>();
    customer.put("age", customerAge);
    Map<String, Object> variables = new HashMap<>();
    variables.put("customer", customer);

    zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId(result.getProcessId())
        .latestVersion()
        .variables(variables)
        .send();
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
}
