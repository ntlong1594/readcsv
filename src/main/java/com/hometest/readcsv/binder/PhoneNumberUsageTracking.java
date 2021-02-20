package com.hometest.readcsv.binder;

import com.opencsv.bean.CsvBindByName;

public class PhoneNumberUsageTracking {

    @CsvBindByName(column = "PHONE_NUMBER")
    private String phoneNumber;

    @CsvBindByName(column = "ACTIVATION_DATE")
    private String activationDate;

    @CsvBindByName(column = "DEACTIVATION_DATE")
    private String deactivationDate;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(String deactivationDate) {
        this.deactivationDate = deactivationDate;
    }
}
