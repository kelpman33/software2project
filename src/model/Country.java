package model;

/**
 * Country class
 */
/**
 * @author James Badke
 */

import java.sql.Date;
import java.sql.Timestamp;

public class Country {
    public String id;
    public String country;
    public Date createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;

    /**
     * constructors
     */
    public Country(){

    }
    public Country(String id, String country, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy){

    }
    /**
     * getters
     */
    public String getId(){return id;}
    public String getCountry(){return country;}
    public Date getCreateDate(){return createDate;}
    public String getCreatedBy(){return createdBy;}
    public Timestamp getLastUpdate(){return lastUpdate;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    /**
     * setters
     */
    public void setId(String id){this.id = id;}
    public void setCountry(String country){this.country = country;}
    public void setCreateDate(Date createDate){this.createDate = createDate;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public void setLastUpdate(Timestamp lastUpdate){this.lastUpdate = lastUpdate;}
    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}
}
