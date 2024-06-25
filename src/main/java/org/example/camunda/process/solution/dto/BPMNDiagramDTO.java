package org.example.camunda.process.solution.dto;

public class BPMNDiagramDTO {

  String processId;
  String bpmn;

  public BPMNDiagramDTO(String processId, String bpmn) {
    this.processId = processId;
    this.bpmn = bpmn;
  }

  public String getProcessId() {
    return processId;
  }

  public String getBpmn() {
    return bpmn;
  }

}
