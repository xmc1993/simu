package cn.superid.webapp.utils;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by njutms on 16/6/1.
 */
public class OSSFileUtil {
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setObject(String object) {
        this.object = object;
    }
    public String getObject(){
        return this.object;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String toJsonString(){
        return toJson().toJSONString();
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Bucket", bucket);
        jsonObject.put("Location", location);
        jsonObject.put("Object", object);

        return jsonObject;
    }

    public OSSFileUtil(){

    }

    public OSSFileUtil(String location, String bucket, String object){
        this.location = location;
        this.bucket = bucket;
        this.object = object;
    }

    private String location;
    private String bucket;
    private String object;
}
