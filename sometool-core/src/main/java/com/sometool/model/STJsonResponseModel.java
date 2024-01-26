package com.sometool.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class STJsonResponseModel<T> {
    @SerializedName("Response")
    @Expose
    public T response;
}
