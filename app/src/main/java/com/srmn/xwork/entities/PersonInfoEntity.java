package com.srmn.xwork.entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by kiler on 2016/4/13.
 */
@Table(name = "PersonInfo")
public class PersonInfoEntity implements Serializable {
    @Column(name = "id", isId = true, autoGen = true)
    private Integer id;
    @Column(name = "name")
    public String name;
    @Column(name = "code")
    public String code;
    @Column(name = "enableLocationCheck")
    public Boolean enableLocationCheck;
    @Column(name = "locationName")
    public String locationName;
    @Column(name = "locationAddress")
    public String locationAddress;
    @Column(name = "locationLat")
    public double locationLat;
    @Column(name = "locationLng")
    public double locationLng;
    @Column(name = "checkRange")
    public int checkRange;
    @Column(name = "checkStartTime")
    public Time checkStartTime;
    @Column(name = "checkEndTime")
    public Time checkEndTime;
    @Column(name = "enableFaceCheck")
    public Boolean enableFaceCheck;
    @Column(name = "faceCheckID")
    public String faceCheckID;
    @Column(name = "enableVoiceCheck")
    public Boolean enableVoiceCheck;

    @Column(name = "voiceCheckID")
    public String voiceCheckID;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getEnableFaceCheck() {
        return enableFaceCheck;
    }

    public void setEnableFaceCheck(Boolean enableFaceCheck) {
        this.enableFaceCheck = enableFaceCheck;
    }

    public Boolean getEnableLocationCheck() {
        return enableLocationCheck;
    }

    public void setEnableLocationCheck(Boolean enableLocationCheck) {
        this.enableLocationCheck = enableLocationCheck;
    }

    public Boolean getEnableVoiceCheck() {
        return enableVoiceCheck;
    }

    public void setEnableVoiceCheck(Boolean enableVoiceCheck) {
        this.enableVoiceCheck = enableVoiceCheck;
    }

    public String getFaceCheckID() {
        return faceCheckID;
    }

    public void setFaceCheckID(String faceCheckID) {
        this.faceCheckID = faceCheckID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoiceCheckID() {
        return voiceCheckID;
    }

    public void setVoiceCheckID(String voiceCheckID) {
        this.voiceCheckID = voiceCheckID;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
}
