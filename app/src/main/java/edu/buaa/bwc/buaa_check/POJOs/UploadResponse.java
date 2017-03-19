package edu.buaa.bwc.buaa_check.POJOs;

/**
 * Created by airhome on 2017/3/13.
 */

public class UploadResponse {
    private String msg;
    private String suffix;
    private String photoPath;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
