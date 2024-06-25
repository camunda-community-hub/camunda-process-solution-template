package org.example.camunda.process.solution.service;

import org.camunda.bpmn.generator.BPMNGenFromJSON;
import org.example.camunda.process.solution.dto.BPMNDiagramDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonToBpmnService {

  public BPMNDiagramDTO convertJson(String jsonString) throws IOException {
    BPMNGenFromJSON bpmnGenFromJSON = new BPMNGenFromJSON();
    bpmnGenFromJSON.convertToBpmnString(jsonString);
    String bpmnString = bpmnGenFromJSON.getBpmnString();
    String processId = bpmnGenFromJSON.getProcessId();
    BPMNDiagramDTO result = new BPMNDiagramDTO(processId, bpmnString);
    return result;
  }
}
