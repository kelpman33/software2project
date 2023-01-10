package model;

/**
 * Customer class
 */
/**
 * @author James Badke
 */

import java.sql.Date;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String zip;
    private String phone;
    private Date createDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdateBy;
    private String country;
    private int divisionID;

    /**
     * constructors
     */
    public Customer(){
    }

    public Customer(int id, String name, String address, String zip, String phone, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy, String country, int divisionID) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.zip = zip;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.country = country;
        this.divisionID = divisionID;
    }

    public Customer(int id, String name) {
        setID(id);
        setName(name);
    }

    /**
     * getters
     */
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getZip() {
        return zip;
    }
    public String getPhone() {return phone;}
    public Date getCreateDate() {return createDate;}
    public String getCreatedBy() {return createdBy;}
    public Date getLastUpdate() {return lastUpdate;}
    public String getLastUpdateBy() {return lastUpdateBy;}
    public String getCountry() {return country;}
    public int getDivisionID() {return divisionID;}

    /**
     * setters
     */
    public void setID(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setAddress(String address) {this.address = address;}
    public void setZip(String zip) {this.zip = zip;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setCountry(String country) {this.country = country;}
    public void setLastUpdate(Date lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
    public void setDivisionID(int divisionID) {this.divisionID = divisionID;}
}