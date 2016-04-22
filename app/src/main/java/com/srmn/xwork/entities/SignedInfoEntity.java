package com.srmn.xwork.entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kiler on 2016/4/21.
 */
@Table(name = "SignedInf")
public class SignedInfoEntity implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private Integer id;
    @Column(name = "userCode")
    public String userCode;
    @Column(name = "checkDate")
    public Date checkDate;
    @Column(name = "checkOk")
    public Boolean checkOk;
    @Column(name = "checkTime")
    public Date checkTime;
    @Column(name = "locationCheckTime")
    public Date locationCheckTime;
    @Column(name = "faceCheckTime")
    public Date faceCheckTime;
    @Column(name = "voiceCheckTime")
    public Date voiceCheckTime;
    @Column(name = "locationChecked")
    public boolean locationChecked;
    @Column(name = "faceChecked")
    public boolean faceChecked;
    @Column(name = "voiceChecked")
    public boolean voiceChecked;
    @Column(name = "uploaded")
    public boolean uploaded;
    @Column(name = "uploadTime")
    public Date uploadTime;

    @Column(name = "signLocation")
    public String signLocation;
    @Column(name = "signlng")
    public double signlng;
    @Column(name = "signlat")
    public double signlat;

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Boolean getCheckOk() {
        return checkOk;
    }

    public void setCheckOk(Boolean checkOk) {
        this.checkOk = checkOk;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public boolean isFaceChecked() {
        return faceChecked;
    }

    public void setFaceChecked(boolean faceChecked) {
        this.faceChecked = faceChecked;
    }

    public Date getFaceCheckTime() {
        return faceCheckTime;
    }

    public void setFaceCheckTime(Date faceCheckTime) {
        this.faceCheckTime = faceCheckTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isLocationChecked() {
        return locationChecked;
    }

    public void setLocationChecked(boolean locationChecked) {
        this.locationChecked = locationChecked;
    }

    public Date getLocationCheckTime() {
        return locationCheckTime;
    }

    public void setLocationCheckTime(Date locationCheckTime) {
        this.locationCheckTime = locationCheckTime;
    }

    public double getSignlat() {
        return signlat;
    }

    public void setSignlat(double signlat) {
        this.signlat = signlat;
    }

    public double getSignlng() {
        return signlng;
    }

    public void setSignlng(double signlng) {
        this.signlng = signlng;
    }

    public String getSignLocation() {
        return signLocation;
    }

    public void setSignLocation(String signLocation) {
        this.signLocation = signLocation;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isVoiceChecked() {
        return voiceChecked;
    }

    public void setVoiceChecked(boolean voiceChecked) {
        this.voiceChecked = voiceChecked;
    }

    public Date getVoiceCheckTime() {
        return voiceCheckTime;
    }

    public void setVoiceCheckTime(Date voiceCheckTime) {
        this.voiceCheckTime = voiceCheckTime;
    }
}
