package model;

/**
 * Contact class
 */
/**
 * @author James Badke
 */

public class Contact {
    public String id;
    public String name;
    public String email;

    /**
     * constructors
     */
    public Contact(){

    }
    public Contact(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * getters
     */
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){return email;}

    /**
     * setters
     */
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){this.name = name;}
    public void setEmail(){
        this.email = email;
    }
}
