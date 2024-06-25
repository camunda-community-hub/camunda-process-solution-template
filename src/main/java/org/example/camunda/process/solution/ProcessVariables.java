package org.example.camunda.process.solution;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.camunda.process.solution.dto.Customer;
import org.example.camunda.process.solution.dto.CustomerApplication;

@JsonInclude(Include.NON_NULL)
public class ProcessVariables {

  private String processId;
  private Boolean result;
  private String businessKey;
  private Customer customer;
  private CustomerApplication customerApplication;

  public ProcessVariables() {
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public CustomerApplication getCustomerApplication() {
    return customerApplication;
  }

  public void setCustomerApplication(CustomerApplication customerApplication) {
    this.customerApplication = customerApplication;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public ProcessVariables setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
    return this;
  }

  public Boolean getResult() {
    return result;
  }

  public ProcessVariables setResult(Boolean result) {
    this.result = result;
    return this;
  }

  public String getProcessId() {
    return processId;
  }

  public void setProcessId(String processId) {
    this.processId = processId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(
        this,
        new MultilineRecursiveToStringStyle() {
          public ToStringStyle withShortPrefixes() {
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            return this;
          }
        }.withShortPrefixes());
  }
}
