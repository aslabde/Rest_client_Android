package tk.ebalsa.rest1.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import tk.ebalsa.rest1.R;
import tk.ebalsa.rest1.bo.UserBo;
import tk.ebalsa.rest1.model.MyReturn;
import tk.ebalsa.rest1.model.User;

/**
 * Created by ebalsa.gmail.com on 10/02/14.
 */
public class Register extends ActionBarActivity {

    User user = null;

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


        if(name.matches("") || pass.matches("") || pass2.matches("")){

            toastError("Complete todos los campos");
            return false;
        }

        if(!pass.equals(pass2)){
            toastError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }


    //Parses user fields and tries to register bew user in system.
    public void newUser(View view) throws InterruptedException {
        EditText userField = (EditText)this.findViewById(R.id.user_name_value);
        EditText passField = (EditText)this.findViewById(R.id.pass_value);
        EditText passField2 = (EditText)this.findViewById(R.id.pass_value2);

        String name = userField.getText().toString();
        String pass = passField.getText().toString();
        String pass2 = passField2.getText().toString();

        if(!validate(name, pass, pass2)){

        }
        else{


            User user = new User(name, pass);
            this.user=user;
            UserBo userBo = new UserBo(user);

            try{
                MyReturn ret = userBo.registerNewUser();//Retorno de la operacion contra el server


                //Register OK
                if(ret.getBody()== MyReturn.statusType.OK){
                    //FALTARIA PASAR EL USER COMO PARAMETRO


                    //DIALOG REGISTRO OK
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setIcon(R.drawable.rinky)
                            .setTitle("Te has registrado como " + name)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton)
                                {
                                 closeA();

                                }
                            }
                    );
                    builder.show();

                }

                else{ //Retorna CONFLICT
                    toastError("nombre de usuario no disponible, pruebe con otro");
                 }

            }
            catch (TimeoutException t){
                toastError("Tiempo de espera agotado. Por favor inténtelo más tarde");
            }
            catch (ExecutionException e){
                toastError("Vaya...algo ha fallado. Por favor intentalo más tarde de nuevo");
            }
            catch (Exception e){
                toastError("Vaya...algo ha fallado. Por favor intentalo más tarde de nuevo");
            }

        }

    }

    protected void toastError (String err){
        Toast toast = Toast.makeText(this.getApplicationContext(), err, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public void closeA (){
        Intent i = new Intent("tk.ebalsa.activities.Home");
        i.putExtra("currentUser", this.user);
        startActivity(i);
        this.finish();
    }


}
