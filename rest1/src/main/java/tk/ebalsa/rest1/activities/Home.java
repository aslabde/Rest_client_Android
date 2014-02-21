package tk.ebalsa.rest1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import tk.ebalsa.rest1.R;
import tk.ebalsa.rest1.bo.ResourceBo;
import tk.ebalsa.rest1.model.CatalogUnit;
import tk.ebalsa.rest1.model.Resource;
import tk.ebalsa.rest1.model.User;

public class Home extends ActionBarActivity {

    private User currentUser;
    private LinearLayout home;
    private ResourceBo resourceBo = new ResourceBo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home = (LinearLayout) findViewById(R.id.home);

        //Recover user passed as arg
        Intent i = getIntent();
        currentUser = (User)i.getExtras().getSerializable("currentUser");

        Toast toast = Toast.makeText(this.getApplicationContext(), "Bienvenid@ " + currentUser
                .getName()
                , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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


    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    //Connect to server and fetch catalog
    protected List<CatalogUnit> getCatalog() throws InterruptedException, ExecutionException, TimeoutException {
        List<CatalogUnit> catalog = new ArrayList<CatalogUnit>();

        catalog=resourceBo.getCatalog();
        return catalog;
    }

    //Extract current pending urls from resources
    protected List<String> parseUrls(List<CatalogUnit> catalog){
        List<String> urls = new ArrayList<String>();


        for(CatalogUnit c: catalog){
            urls.add(c.getLink2resource());
        }

        return urls;

    }

    //Connect to server and fetch new resources
    protected List<Resource> getResources(List<String> urls) throws InterruptedException, ExecutionException, TimeoutException {
        List<Resource> resources = new ArrayList<Resource>();

        for (String u: urls){
            resources.add(this.resourceBo.getResource(u));
        }

        return resources;
    }

    //Do layout showing resources
    public void showResources(View view){
        this.doResourcesLayout();
    }


    protected void doResourcesLayout(){

        //Need change to append only new. Local variable?
        try {
        /*List<CatalogUnit> catalog = this.getCatalog();
        List<String> links = this.parseUrls(catalog);
        List<Resource> resources = this.getResources(links);*/



        List<Resource> resources2show = new ArrayList<Resource>();



        resources2show=this.getResources(parseUrls(getCatalog()));

        if(!resources2show.isEmpty()){

                for(Resource r: resources2show){
                   System.out.println(r.getBody());

                }
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

}
