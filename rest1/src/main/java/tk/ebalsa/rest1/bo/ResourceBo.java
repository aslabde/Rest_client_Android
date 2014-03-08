package tk.ebalsa.rest1.bo;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import tk.ebalsa.rest1.model.CatalogUnit;
import tk.ebalsa.rest1.model.ReceivedResource;
import tk.ebalsa.rest1.model.Resource;
import tk.ebalsa.rest1.model.ResourceCaster;

/**
 * Created by ebalsa.gmail.com on 10/02/14.
 */
public class ResourceBo extends BaseBo{

    public ResourceBo() {}


    public List<CatalogUnit> getCatalog() throws InterruptedException, ExecutionException, TimeoutException {
        return new HttpRequestTask().execute().get(10, TimeUnit.SECONDS);

    }

    public Resource getResource(String link) throws ExecutionException, InterruptedException, TimeoutException {
        ResourceCaster rc = new ResourceCaster();

        ReceivedResource rr = new HttpRequestTask2(link).execute().get(10, TimeUnit.SECONDS);
        //Cast to Resource and  write image to filesystem

        return rc.cast2resource(rr);
    }


    //Class to make register task
    private class HttpRequestTask extends AsyncTask<Void, Void, List<CatalogUnit>> {


        public HttpRequestTask(){
           super();

       }

        @Override
        protected  List<CatalogUnit> doInBackground(Void... params) {
            try {
                String url = PATH_TO_SERVER + "/catalog";
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                List<CatalogUnit> catalog = new ArrayList<CatalogUnit>();

                CatalogUnit[] catalogFetched = restTemplate.getForObject(url, CatalogUnit[].class );
                catalog.addAll(Arrays.asList(catalogFetched));
                return catalog;

            } catch (Exception e) {
                //Alert "server unavailable"
                Log.e("MainActivity", e.getMessage(), e);
            }


            return null;
        }
    }

    //Class to  login
    private class HttpRequestTask2 extends AsyncTask<Void, Void, ReceivedResource> {

        private String link;

        public HttpRequestTask2(String link){
            super();
          this.link = link;
        }

        @Override
        protected ReceivedResource doInBackground(Void... params){
            try {

                String url = PATH_TO_SERVER + link;
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ReceivedResource rr = restTemplate.getForObject(url, ReceivedResource.class );

                return rr;


            } catch (Exception e) {
                //Alert "server unavailable"
                Log.e("MainActivity", e.getMessage(), e);
            }


            return null;
        }


    }

}

