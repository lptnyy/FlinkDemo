package com.flink.entity;

public class IpEntity {
  String city;
  String province;
  String operators;
  String country;

  public IpEntity(String value) {
      String[] values = value.split("\\|");
      this.country = values[1];
      this.province = values[3];
      this.city = values[4];
      this.operators = values[5];
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getOperators() {
    return operators;
  }

  public void setOperators(String operators) {
    this.operators = operators;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
