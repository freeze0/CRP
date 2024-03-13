package com.example.lesson3.data

import com.google.gson.annotations.SerializedName

class Clients {
    @SerializedName("item") lateinit var items: List<Client>
}