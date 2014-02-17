package tk.ebalsa.rest1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
            Toast.makeText(getApplicationContext(), "Complete ambos campos"
                    , Toast.LENGTH_LONG).show();
        }

        else {


            //Crea instancia de userBo con los datos introducidos de usuario
            User user = new User(name, pass);
            UserBo userBo = new UserBo(user);


            try{
                MyReturn ret = userBo.loginUser();//Retorno de la operacion contra el server


                //Login OK
                if(ret.getBody()== MyReturn.statusType.OK){
                   //FALTARIA PASAR EL USER COMO PARAMETRO
                   startActivity(new Intent("tk.ebalsa.activities.Home"));
                   this.finish();
                }
                else{ //(Login error)

                   Toast.makeText(getApplicationContext(), "error en el login" , Toast.LENGTH_LONG).show();
                }
            }
            catch (TimeoutException t){
                Toast.makeText(getApplicationContext(), "timeout" , Toast.LENGTH_LONG).show();
            }
            catch (ExecutionException e){
                Toast.makeText(getApplicationContext(), "Vaya, algo ha fallado....inténtalo mas tarde"
                        , Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "Vaya, algo ha fallado....inténtalo mas tarde"
                        , Toast.LENGTH_LONG).show();
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



}
