package com.example.anuraag.twosquare;

/**
 * Created by Ankita on 1/14/2017.
 */

public class RestaurantName {
    private String id;
    private String Address;
    private String Rname;
    //private String photoUrl;

    public RestaurantName() {
    }

    public RestaurantName(String Rname, String Address) {
        this.Address = Address;
        this.Rname = Rname;
        //this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRaddress(String Address) {
        this.Address = Address;
    }
    public void setName(String name) {
        this.Rname = Rname;
    }

    public String getName() {
        return Rname;
    }

    public String getAddress() {
        return Address;
    }

}
