package tk.ebalsa.rest1.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

import tk.ebalsa.rest1.R;
import tk.ebalsa.rest1.bo.ResourceBo;
import tk.ebalsa.rest1.model.CatalogUnit;
import tk.ebalsa.rest1.model.Resource;
import tk.ebalsa.rest1.model.User;
import tk.ebalsa.rest1.persistence.DBCache;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Home extends ActionBarActivity {


    private User currentUser;
    private LinearLayout home;
    private ResourceBo resourceBo = new ResourceBo();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private List<Resource> localResources = new ArrayList<Resource>();

    //Scheduled update snippet
    public static final Long UPDATE_RATE = Long.parseLong("10");

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    final Runnable updater = new Runnable() {
        public void run() {updateResources();
        }
    };
    final ScheduledFuture updaterHandle =
            scheduler.scheduleAtFixedRate(updater, 0, UPDATE_RATE, SECONDS);

    //DB Cache
    private DBCache dbcache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home = (LinearLayout) findViewById(R.id.home);

        //Recover user passed as arg
        Intent i = getIntent();
        currentUser = (User)i.getExtras().getSerializable("currentUser");

        //Display welcome msg
        Toast toast = Toast.makeText(this.getApplicationContext(), "Bienvenid@ " + currentUser
                .getName()
                , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //Recover resources from local cache
        this.dbcache =new DBCache(this);
        this.localResources = this.getResourcesFromLocalCache();
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
    protected List<Resource> getResources(List<String> urls) throws InterruptedException,
            ExecutionException, TimeoutException {

        List<Resource> resources = new ArrayList<Resource>();


        for (String u: urls){
            if (this.resourceBo.getResource(u)!=null){
                resources.add(this.resourceBo.getResource(u));
            }
        }

        return resources;
    }

    //Do layout showing resources
    public void showResources(View view){

        //DELETE...JUST FOR TESTS
        for(Resource r: this.localResources){
            System.out.println(r.getBody());
            System.out.println(dateFormat.format(r.getPubDate()));

        }

    }


    protected void updateResources(){

        try {
            List<CatalogUnit> catalog = this.getCatalog();
            List<String> links = new ArrayList<String>();
            List<Resource> resourcesFetched = new ArrayList<Resource>();

            //Parse dates from catalog and get link if new is discovered.
            if(!catalog.isEmpty()){
                    for (CatalogUnit u: catalog){

                        if (this.getLastUpdate().getTime() < u.getPubDate().getTime()){
                            links.add(u.getLink2resource());
                        }
                    }
            }

                        if(!links.isEmpty()){
              resourcesFetched = this.getResources(links);
            }

            if (!resourcesFetched.isEmpty()){

                //DBA CONDITIONAL SAVE
                this.conditionalSaveResources(resourcesFetched);
                this.localResources.addAll(resourcesFetched);

                //FIRE UI

                //DISPLAY ON STATUS BAR NOTIFICATION (FALTARIA  AGRUPAR)
                this.displayOnStatusBar(resourcesFetched.get(0).getTitle()
                        , resourcesFetched.get(0).getResourceId());
            }

        //Upate last update
        this.setLastUpdate(new Date());

           //MODIFICAR Y PONER TOAST
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

    private void setLastUpdate(Date newUpdate){
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        // Saved on file with users name
        SharedPreferences settings = getSharedPreferences(this.currentUser.getName(), 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putLong("lastUpdate", newUpdate.getTime());

        // Commit the edits!
        editor.commit();

    }

    private Date getLastUpdate(){
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(this.currentUser.getName(), 0);
        Long lastUpdate = settings.getLong("lastUpdate", 0);

        return new Date(lastUpdate);
    }

    private void displayOnStatusBar(String title, long id){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)//<-AÑADIR ICONO
                        .setContentTitle("nuevo recurso!!")
                        .setContentText(title)
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Home.class);
        resultIntent.putExtra("currentUser", currentUser);

        //ARREGLAR ESTA PARTE.
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Home.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Id allows you to update the notification later on.
        mNotificationManager.notify((int)id , mBuilder.build());
    }


    //MODEL-PERSISTENCE OPS:

    //get resources from DB.
    public List<Resource> getResourcesFromLocalCache(){
        List<Resource> savedResources = new ArrayList<Resource>();

        dbcache.open();
        Cursor c = dbcache.getAllResources();
        if (c.moveToFirst())
        {
            do {
                Resource res =getResourceFromDB(c);
                if(res!=null)
                savedResources.add(getResourceFromDB(c));
            } while (c.moveToNext());
        }
        dbcache.close();

        return savedResources;


    }

    //Save or update fetched Resources
    private void conditionalSaveResources(List<Resource> resourcesFetched){
        //TODO:
        //Add logic to save/update resources

        this.saveNewResources(resourcesFetched);
    }



    //DATABASE ACCESS CODE
    private Resource getResourceFromDB(Cursor c){
        //Get pars from Cursor
        long id = c.getLong(0);
        long pudDateLong = c.getLong(3);
        Date pubdate = new Date(pudDateLong);
        long endDateLong = c.getLong(4);
        Date endDate = new Date(endDateLong);
        String title = c.getString(1);
        String body = c.getString(2);
        String mime =c.getString(5);
        String path2image = c.getString(6);

        //Cast pars from DB to model resource
        Resource res = new Resource(id, pubdate, endDate, title , body, mime, path2image);


        //Ensures resource belongs to current user
        if(c.getString(5).equals(currentUser.getName())){
            return res;
        }

        return null;
    }

    //Save resurces to DB
    public void saveNewResources(List<Resource> newResources){

        for(Resource r: newResources){
            long id = r.getResourceId();
            String title = r.getTitle();
            String body = r.getBody();
            long pubDate = r.getPubDate().getTime();
            long endDate = r.getEndDate().getTime();
            String userActive = currentUser.getName();
            String mime = r.getMime();
            String path2image = r.getPath2image();

            dbcache.open();
            dbcache.insertResource(id, title, body, pubDate, endDate, userActive, mime, path2image);
            dbcache.close();
        }

    }

    protected void toastError (String err){
        Toast toast = Toast.makeText(this.getApplicationContext(), err, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
}
