package com.asuc.asucmobile.infrastructure.models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gym {

    @PropertyName("address")
    public String address;

    @PropertyName("description")
    public String description;

    @PropertyName("keycard")
    public boolean keycard;

    @PropertyName("latitude")
    public double latitude;

    @PropertyName("longitude")
    public double longitude;

    @PropertyName("name")
    public String name;

    @PropertyName("open_close_array")
    public ArrayList<OpenClose> openCloses;

    @PropertyName("phone_number")
    public String phoneNumber;

    @PropertyName("picture")
    public String picture;
}
