package cn.superid.webapp.controller.forms;

/**
 * Created by njuTms on 16/11/8.
 */
public class CalculateSignForm {
    private String verb;
    private String contentMD5;
    private String contentType;
    private String canonicalizedOSSHeaders;
    private String canonicalizedResource;

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public void setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCanonicalizedOSSHeaders() {
        return canonicalizedOSSHeaders;
    }

    public void setCanonicalizedOSSHeaders(String canonicalizedOSSHeaders) {
        this.canonicalizedOSSHeaders = canonicalizedOSSHeaders;
    }

    public String getCanonicalizedResource() {
        return canonicalizedResource;
    }

    public void setCanonicalizedResource(String canonicalizedResource) {
        this.canonicalizedResource = canonicalizedResource;
    }
}
