package com.tankraj.profiledemo.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "profiles")
public class ProfileEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String phone;
    private String bio;
    private String deviceType;
    private String imageUri;
    private String gender;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @Ignore
    public ProfileEntity(int id, String name, String email, String phone, String bio, String deviceType, String imageUri, String gender, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.deviceType = deviceType;
        this.imageUri = imageUri;
        this.gender = gender;
        this.updatedAt = updatedAt;
    }


    public ProfileEntity(String name, String email, String phone, String bio, String deviceType, String imageUri, String gender, Date updatedAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.deviceType = deviceType;
        this.imageUri = imageUri;
        this.gender = gender;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


}
