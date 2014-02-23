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
import tk.ebalsa.rest1.model.Resource;

/**
 * Created by ebalsa.gmail.com on 10/02/14.
 */
public class ResourceBo {



    public ResourceBo() {}


    public List<CatalogUnit> getCatalog() throws InterruptedException, ExecutionException, TimeoutException {
        return new HttpRequestTask().execute().get(10, TimeUnit.SECONDS);

    }

    public Resource getResource(String link) throws ExecutionException, InterruptedException, TimeoutException {

     return new HttpRequestTask2(link).execute().get(10, TimeUnit.SECONDS);
    }


    //Class to make register task
    private class HttpRequestTask extends AsyncTask<Void, Void, List<CatalogUnit>> {


        public HttpRequestTask(){
           super();

       }

        @Override
        protected  List<CatalogUnit> doInBackground(Void... params) {
            try {
                String url = "http://192.168.1.130:8080/Rest1/catalog";
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
    private class HttpRequestTask2 extends AsyncTask<Void, Void, Resource> {

        private String link;

        public HttpRequestTask2(String link){
            super();
          this.link = link;
        }

        @Override
        protected Resource doInBackground(Void... params){
            try {

                StringBuilder sb = new StringBuilder().append("http://192.168.1.130:8080/Rest1")
                        .append(link);
                String url =sb.toString();
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                Resource resource = restTemplate.getForObject(url, Resource.class );

                return resource;


            } catch (Exception e) {
                //Alert "server unavailable"
                Log.e("MainActivity", e.getMessage(), e);
            }


            return null;
        }


    }

}

