package com.example.lesson3.data

import com.google.gson.annotations.SerializedName

class Managers {
    @SerializedName("item") lateinit var items: List<Manager>
}