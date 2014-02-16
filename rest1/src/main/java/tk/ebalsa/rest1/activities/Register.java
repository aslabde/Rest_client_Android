package tk.ebalsa.rest1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import tk.ebalsa.rest1.R;
import tk.ebalsa.rest1.bo.UserBo;
import tk.ebalsa.rest1.model.User;

/**
 * Created by ebalsa.gmail.com on 10/02/14.
 */
public class Register extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Checks if form is correctly filled
    protected  boolean validate(String name, String pass, String pass2){


        if(name.length()==0 || pass.length()==0 || pass2.length()==0){

            return false;
        }



        return true;
    }


    //Parses user fields and tries to register bew user in system.
    public void newUser(View view){
        EditText userField = (EditText)this.findViewById(R.id.user_name_value);
        EditText passField = (EditText)this.findViewById(R.id.pass_value);
        EditText passField2 = (EditText)this.findViewById(R.id.pass_value2);

        String name = userField.getText().toString();
        String pass = passField.getText().toString();
        String pass2 = passField2.getText().toString();

        if(validate(name, pass, pass2)){

        User user = new User(name, pass);

        UserBo userBo = new UserBo(user);
        userBo.registerNewUser();
        }



        startActivity(new Intent("tk.ebalsa.activities.Home"));
        this.finish();

    }



}
