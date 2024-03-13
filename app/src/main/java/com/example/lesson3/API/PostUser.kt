package com.example.lesson3.API

import com.google.gson.annotations.SerializedName

class PostUser (
    @SerializedName("login") val login:String,
    @SerializedName("pwd") val pwd:String
)