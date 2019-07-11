package com.google.codeu.data;

public class User {
  private String firstName = "";
  private String lastName = "";
  private String city = "";
  private String stateProvince = "";
  private String country = "";
  private String email = "";
  private String aboutMe = "";


  public User(String firstName, String lastName, String city, String stateProvince, String country, String email, String aboutMe) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.city = city;
    this.stateProvince = stateProvince;
    this.country = country;
    this.email = email;
    this.aboutMe = aboutMe;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getCity() {
    return city;
  }

  public String getStateProvince() {
    return stateProvince;
  }

  public String getCountry() {
    return country;
  }

  public String getEmail() {
    return email;
  }

  public String getAboutMe() {
    return aboutMe;
  }
}
