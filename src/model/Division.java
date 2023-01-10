package model;

/**
 * Division class
 */
/**
 * @author James Badke
 */

import java.sql.Date;
import java.sql.Timestamp;

public class Division {
    public String id;
    public String division;
    public Date createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public String countryId;

    /**
     * constructors
     */
    public Division(){

    }
    public Division(String id, String division, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, String countryId){}

    /**
     * getters
     */
    public String getId(){return id;}
    public String getDivision(){return division;}
    public Date getCreateDate(){return createDate;}
    public String getCreatedBy(){return createdBy;}
    public Timestamp getLastUpdate(){return lastUpdate;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    public String getCountryId(){return countryId;}

    /**
     * setters
     */
    public void setId(String id){this.id = id;}
    public void setDivision(String division){this.division = division;}
    public void setCountryId(String countryId){this.countryId = countryId;}
    public void setCreateDate(Date createDate){this.createDate = createDate;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public void setLastUpdate(Timestamp lastUpdate){this.lastUpdate = lastUpdate;}
    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}
}
