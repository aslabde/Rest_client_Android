package tk.ebalsa.rest1.bo;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import tk.ebalsa.rest1.model.MyReturn;
import tk.ebalsa.rest1.model.User;

/**
 * Created by ebalsa.gmail.com on 10/02/14.
 */
public class UserBo extends BaseBo{

    //protected String server_path = ;
    private User user;

    public UserBo(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MyReturn registerNewUser() throws InterruptedException, ExecutionException, TimeoutException {
        return new HttpRequestTask(this.user).execute().get(10, TimeUnit.SECONDS);

    }

    public MyReturn loginUser() throws ExecutionException, InterruptedException, TimeoutException {

     return new HttpRequestTask2(this.user).execute().get(10, TimeUnit.SECONDS);
    }


    //Class to make register task
    private class HttpRequestTask extends AsyncTask<Void, Void, MyReturn> {

       private User user;

        public HttpRequestTask(User user){
           super();
           this.user=user;
       }

        @Override
        protected MyReturn doInBackground(Void... params) {
            try {
                String url =PATH_TO_SERVER + "/register";
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                MyReturn myret = restTemplate.postForObject(url, this.user, MyReturn.class);
                return myret;

            } catch (Exception e) {
                //Alert "server unavailable"
                Log.e("MainActivity", e.getMessage(), e);
            }


            return null;
        }
    }

    //Class to  login
    private class HttpRequestTask2 extends AsyncTask<User, Void, MyReturn> {

        private User user;

        public HttpRequestTask2(User user){
            super();
            this.user=user;
        }

        @Override
        protected MyReturn doInBackground(User... user)  {

            try {
                String url = PATH_TO_SERVER + "/login";
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

               MyReturn myret = restTemplate.postForObject(url, this.user, MyReturn.class);


                return myret;

            } catch (Exception e) {
                //Alert "server unavailable"
                Log.e("MainActivity", e.getMessage(), e);
            }


            return null;
        }


    }

}

