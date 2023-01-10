package model;

/**
 * User class
 */
/**
 * @author James Badke
 */

import java.sql.Date;
import java.sql.Timestamp;

public class User {
    public String id;
    public String username;
    public String password;
    public Date createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;

    /**
     * constructors
     */
    public User(){

    }

    /**
     * setters
     */
    public void setId(String user_id){this.id = id;}
    public void setUsername(String username){this.username = this.username;}
    public void setPassword(String password){this.password = this.password;}
    public void setCreateDate(Date createDate){this.createDate = createDate;}
    public void setLastUpdate(Timestamp lastUpdate){this.lastUpdate = lastUpdate;}
    public void setCreatedBy(String createdBy){this.createdBy = createdBy;}
    public void setLastUpdatedBy(String lastUpdatedBy){this.lastUpdatedBy = lastUpdatedBy;}

    /**
     * getters
     */
    public String getId(){return id;}
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public Date getCreateDate(){return createDate;}
    public Timestamp getLastUpdate(){return lastUpdate;}
    public String getCreatedBy(){return createdBy;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}
}
