package com.ccs.ccs81.wya_arlnwise_bus_eco;

/**
 * Created by Banke Bihari on 10/5/2016.
 */

public class Movie {
    private int imgsrc;
    private String airlineCode;
    private String branch;
    private String business;
    private String businessAmount;
    private String eco;
    private String ecoAmount;
    private String first;
    private String firstAmount;
    private String premium;
    private String premiumAmount;
    private String airlineName;


    public Movie() {
    }

    public Movie(int imgsrc, String airlineCode, String branch, String business, String businessAmount, String eco, String ecoAmount, String first, String firstAmount, String premium, String premiumAmount,String airlineName) {
        this.imgsrc = imgsrc;
        this.airlineCode = airlineCode;
        this.branch = branch;
        this.business = business;
        this.businessAmount = businessAmount;
        this.eco = eco;
        this.ecoAmount = ecoAmount;
        this.first = first;
        this.firstAmount = firstAmount;
        this.premium = premium;
        this.premiumAmount = premiumAmount;
        this.airlineName = airlineName;

    }

    public int getImage() {
        return imgsrc;
    }

    public void setImage(int imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String name) {
        this.branch = name;
    }

    public String getBusiness() {
        return business;
    }

    public void setBaseFare(String business) {
        this.business = business;
    }


    public String getBusinessAmount() {
        return businessAmount;
    }

    public void setBusinessAmount(String businessAmount) {
        this.businessAmount = businessAmount;
    }


    public String getEco() {
        return eco;
    }

    public void setEco(String eco) {
        this.eco = eco;
    }



    public String getEcoAmount() {
        return ecoAmount;
    }

    public void setEcoAmount(String ecoAmount) {
        this.ecoAmount = ecoAmount;
    }


    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }


    public String getFirstAmount() {
        return firstAmount;
    }

    public void setFirstAmount(String firstAmount) {
        this.firstAmount = firstAmount;
    }


    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }


    public String getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(String premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

}
