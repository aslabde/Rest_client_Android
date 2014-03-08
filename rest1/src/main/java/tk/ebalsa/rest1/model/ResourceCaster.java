package tk.ebalsa.rest1.model;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by ebalsa.gmail.com on 7/03/14.
 */
public class ResourceCaster {


    //Cast incomming resorces to Resources and images
    public Resource cast2resource(ReceivedResource rr) throws InterruptedException,
            ExecutionException, TimeoutException {

        Resource r = new Resource();

        //Cast to Resource
        r.setBody(rr.getBody());
        r.setEndDate(rr.getEndDate());
        r.setMime(rr.getMime());
        r.setPubDate(rr.getPubDate());
        r.setTitle(rr.getTitle());
        r.setResourceId(rr.getResourceId());



        //If VisualResource save image and populate fields
        if(rr.getMime()!=null){
            String name = new String(Long.toString(r.getResourceId()) + "." + r.getMime());

            //save image and write path to path2image
           boolean saveOK = new SaveImage(name).execute(rr.getImage()).get(10, TimeUnit.SECONDS);

            if (saveOK){
            r.setPath2image(name);
            return r;
            }
            return null;
        }

        return r;
    }


    //Returns a image from filesystem
    /*public File getImage(Resource r){

        return r.getPath2image();
    }*/


    //Async task to write image to filesystem
    class SaveImage extends AsyncTask<byte[], String, Boolean> {



        String path;

        public SaveImage(String path){
            super();
            this.path=path;
        }

        protected Boolean doInBackground(byte[]... img) {
            File image=new File(Environment.getExternalStorageDirectory(), path);

            if (image.exists()) {
                image.delete();
            }

            try {
                FileOutputStream fos=new FileOutputStream(image.getPath());

                fos.write(img[0]);
                fos.close();
            }
            catch (java.io.IOException e) {
              return false;
            }

            return true;
        }
    }

    //DELETE-----JUST FOR TESTING
    public boolean saveImage (String path, byte[] img){
        File image=new File(Environment.getExternalStorageDirectory(), path);
                                //AÑADIR DIR/USER
        if (image.exists()) {
            image.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(image.getPath());

            fos.write(img);
            fos.close();
        }
        catch (java.io.IOException e) {
            return false;
        }



        return true;
    }

}
