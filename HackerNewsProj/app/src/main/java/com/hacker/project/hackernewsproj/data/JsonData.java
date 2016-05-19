
package com.hacker.project.hackernewsproj.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonData implements Parcelable{

    private String by;
    private Long descendants;
    private Long id;
    private List<Integer> kids = new ArrayList<Integer>();
    private List<Integer> parts = new ArrayList<Integer>();
    private Long score;
    private String text;
    private String time;
    private String title;
    private String type;
    private String url;
    private Integer parent;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public JsonData (Parcel data) {
        this.by = data.readString();
        this.descendants = data.readLong();
        this.id = data.readLong();
        this.score = data.readLong();
        this.title = data.readString();
        this.text = data.readString();
        this.time = data.readString();
        this.url = data.readString();
        this.parent = data.readInt();
        this.type = data.readString();
    }

    public JsonData (){

    }

    /**
     * 
     * @return
     *     The by
     */
    public String getBy() {
        return by;
    }

    /**
     * 
     * @param by
     *     The by
     */
    public void setBy(String by) {
        this.by = by;
    }

    /**
     * 
     * @return
     *     The descendants
     */
    public Long getDescendants() {
        return descendants;
    }

    /**
     * 
     * @param descendants
     *     The descendants
     */
    public void setDescendants(Long descendants) {
        this.descendants = descendants;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The kids
     */
    public List<Integer> getKids() {
        return kids;
    }

    /**
     * 
     * @param kids
     *     The kids
     */
    public void setKids(List<Integer> kids) {
        this.kids = kids;
    }

    /**
     * 
     * @return
     *     The parts
     */
    public List<Integer> getParts() {
        return parts;
    }

    /**
     * 
     * @param parts
     *     The parts
     */
    public void setParts(List<Integer> parts) {
        this.parts = parts;
    }

    /**
     * 
     * @return
     *     The score
     */
    public Long getScore() {
        return score;
    }

    /**
     * 
     * @param score
     *     The score
     */
    public void setScore(Long score) {
        this.score = score;
    }

    /**
     * 
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *     The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * 
     * @param parent
     *     The parent
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.by);
        dest.writeString(this.text);
        dest.writeLong(this.score);
        dest.writeLong(this.descendants);
        dest.writeString(this.time);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeInt(this.parent);
    }


    public static final Creator<JsonData> CREATOR = new Creator<JsonData>() {
        public JsonData createFromParcel(Parcel source) {
            return new JsonData(source);
        }

        public JsonData[] newArray(int size) {
            return new JsonData[size];
        }
    };

}
