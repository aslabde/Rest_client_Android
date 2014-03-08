package tk.ebalsa.rest1.model;

import java.util.Date;

/**
 * Created by ebalsa.gmail.com on 20/02/14.
 */
public class ReceivedResource {
    private long resourceId;
    private Date pubDate;
    private Date endDate;
    private String title;
    private String body;
    private String mime;
    private byte[] image;

    public ReceivedResource(){};

    public ReceivedResource(long resourceId, Date pubDate, Date endDate, String title, String body,
                            String mime, byte[] image) {
        this.resourceId = resourceId;
        this.pubDate = pubDate;
        this.endDate = endDate;
        this.title = title;
        this.body = body;
        this.mime = mime;
        this.image = image;
    }


    public long getResourceId() {
        return resourceId;
    }


    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }


    public Date getPublishingDate() {
        return pubDate;
    }


    public void setPublishingDate(Date publishingDate) {
        this.pubDate = publishingDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public Date getPubDate() {
        return pubDate;
    }


    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
