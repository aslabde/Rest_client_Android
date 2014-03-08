package tk.ebalsa.rest1.model;

import java.io.File;

/**
 * Created by ebalsa.gmail.com on 7/03/14.
 */
public class ResourceCaster {

    public Resource cast2resource(ReceivedResource rr){

        Resource r = new Resource();

        //Cast to Resource
        r.setBody(rr.getBody());
        r.setEndDate(rr.getEndDate());
        r.setMime(rr.getMime());
        r.setPubDate(rr.getPubDate());
        r.setTitle(rr.getTitle());
        r.setResourceId(rr.getResourceId());



        //If VisualResource save image and populate fields
        if(!rr.getMime().equals("")){
            //save image and write path to path2image
        }



        return r;
    }

    public File getImage(Resource r){

        return r.getPath2image();
    }
}
