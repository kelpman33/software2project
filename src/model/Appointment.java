package model;

/**
 * Appointment class
 */
/**
 * @author James Badke
 */

import java.util.Date;
import java.sql.Timestamp;

public class Appointment {

    public int id;
    public String title;
    public String description;
    public String location;
    public String type;
    public String start;
    public String end;
    public Date createDate;
    public String createdBy;
    public Date lastUpdate;
    public String lastUpdateBy;
    public String custID;
    public String userID;
    public String contactID;

    /**
     * constructors
     */
    public Appointment(){
    }

    public Appointment(int id, String title, String description, String location, String type, String start, String end, Date createDate, String createdBy, Date lastUpdate,String lastUpdateBy, String custID, String userID, String contactID){
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.custID = custID;
        this.userID = userID;
        this.contactID = contactID;
    }
    /**
     * getters
     */
    public int getId() {
        return id;
    }
    public String getUserID(){return userID;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public String getLocation(){return location;}
    public String getType(){return type;}
    public String getStart(){return start;}
    public String getEnd(){return end;}
    public Date getCreateDate() {return createDate;}
    public String getCreatedBy() {return createdBy;}
    public Date getLastUpdate() {return lastUpdate;}
    public String getLastUpdateBy() {return lastUpdateBy;}
    public String getCustID(){return custID;}
    public String getContactID(){return contactID;}

    /**
     * setters
     */
    public void setID(int id) {this.id = id;}
    public void setUserID(String userID){this.userID = userID;}
    public void setCustID(String custID){this.custID = custID;}
    public void setContactID(String contactID){this.contactID = contactID;}
    public void setTitle(String title){this.title = title;}
    public void setDescription(String description){this.description = description;}
    public void setLocation(String location){this.location = location;}
    public void setType(String type){this.type = type;}
    public void setStart(String start){this.start = start;}
    public void setEnd(String end){this.end = end;}
    public void setCreateDate(Date createDate){this.createDate = createDate;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public void setLastUpdate(Date lastUpdate) {this.lastUpdate = lastUpdate;}
    public void setLastUpdateBy(String lastUpdateBy) {this.lastUpdateBy = lastUpdateBy;}
}
