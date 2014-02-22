package tk.ebalsa.rest1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences("user_preferences", 0);
        String userName = settings.getString("userName","");

        //Set prefered user name
        EditText userField = (EditText)this.findViewById(R.id.user_name_value1);
        userField.setText(userName);
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


  //Conecta con server, comprueba credencialees, y entra al menu ppal o devuelve error(Boton entrar)
    public void enter(View view) throws ExecutionException, InterruptedException {
        EditText userField = (EditText)this.findViewById(R.id.user_name_value1);
        EditText passField = (EditText)this.findViewById(R.id.user_pass_value1);


        String name = userField.getText().toString();
        String pass = passField.getText().toString();

        if (!validate(name, pass)){
            toastError("Complete ambos campos");

        }

        else {


            //Crea instancia de userBo con los datos introducidos de usuario
            User user = new User(name, pass);
            UserBo userBo = new UserBo(user);


            try{
                MyReturn ret = userBo.loginUser();//Retorno de la operacion contra el server


                //Login OK
                if(ret.getBody()== MyReturn.statusType.OK){

                    //Guardar usuario como preferido
                    // We need an Editor object to make preference changes.
                    // All objects are from android.context.Context
                    SharedPreferences settings = getSharedPreferences("user_preferences", 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putString("userName",name);

                    // Commit the edits!
                    editor.commit();


                   //PASAR EL USER COMO PARAMETRO
                   Intent i = new Intent("tk.ebalsa.activities.Home");
                    i.putExtra("currentUser", user);
                    startActivity(i);
                    this.finish();

                }
                else{ //(Login error)
                     toastError("No existe esa combinacion usuario/contraseña");
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

    //Pasa a la screen de register
    public void register(View view){
        startActivity(new Intent("tk.ebalsa.activities.Register"));
        this.finish();
    }

    //Comprueba que se ha rellenado correctamente el formulario
    protected  boolean validate(String name, String pass){

        if(name.length()==0 || pass.length()==0){

            return false;
        }

        return true;
    }

    protected void toastError (String err){
        Toast toast = Toast.makeText(this.getApplicationContext(), err, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


}
