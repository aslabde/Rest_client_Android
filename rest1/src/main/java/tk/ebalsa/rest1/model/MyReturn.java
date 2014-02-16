package tk.ebalsa.rest1.model;

/**
 * Created by ebalsa.gmail.com on 15/02/14.
 */

public class MyReturn {

    public MyReturn(){};

    public enum statusType{
        OK, ERROR, CONFLICT
    }

    private statusType body;

    public statusType getBody() {
        return body;
    }

    public void setBody(statusType body) {
        this.body = body;
    }

    public String ToString(){
        return this.getBody().toString();
    }


}
