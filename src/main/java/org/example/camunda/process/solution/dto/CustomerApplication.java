package org.example.camunda.process.solution.dto;

import java.io.Serializable;
import java.util.Date;

public class CustomerApplication implements Serializable {
  private static final long serialVersionUID = 1L;

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String address;
  private String city;
  private String state;
  private String zipCode;
  private Date submitDate;
  private String approvalStatus;

  // No-argument constructor
  public CustomerApplication() {
    this.submitDate = new Date(); // Set submitDate to the current date
  }

  // Getter and setter methods
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public Date getSubmitDate() {
    return submitDate;
  }

  public void setSubmitDate(Date submitDate) {
    this.submitDate = submitDate;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  @Override
  public String toString() {
    return "CustomerApplication{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", address='" + address + '\'' +
        ", city='" + city + '\'' +
        ", state='" + state + '\'' +
        ", zipCode='" + zipCode + '\'' +
        ", submitDate=" + submitDate +
        '}';
  }
}

