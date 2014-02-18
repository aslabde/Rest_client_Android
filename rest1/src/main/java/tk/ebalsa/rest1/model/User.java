package tk.ebalsa.rest1.model;

import java.io.Serializable;

/**
 * Created by ebalsa.gmail.com on 10/02/14.
 */
public class User implements Serializable {


    private String name;
    private String pass;

    public User(){};

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
