package com.rank.kuber.Model;


public class RegisterRequest {

    /**
     * category : Urgent
     * language : English
     * service : LOAN
     * customerName : apurva
     * email : apurva.maity@gmail.com
     * cellPhone : 9051926974
     * nationality : Indian
     */

    private String category;
    private String language;
    private String service;
    private String customerName;
    private String email;
    private String cellPhone;
    private String nationality;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
