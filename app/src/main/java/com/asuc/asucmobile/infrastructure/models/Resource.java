package com.asuc.asucmobile.infrastructure.models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class Resource {

    @PropertyName("address")
    public String address;

    @PropertyName("description")
    public String description;

    @PropertyName("email")
    public String email;

    @PropertyName("keycard")
    public boolean keycard;

    @PropertyName("latitude")
    public double latitude;

    @PropertyName("longitude")
    public double longitude;

    @PropertyName("name")
    public String name;

    @PropertyName("on_campus")
    public boolean onCampus;

    @PropertyName("open_close_array")
    public ArrayList<OpenClose> openCloses;

    @PropertyName("phone")
    public String phone;

    @PropertyName("picture")
    public String picture;

}
