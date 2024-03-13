package com.example.lesson3.data

import com.google.gson.annotations.SerializedName

class Invoices {
    @SerializedName("item") lateinit var items: List<Invoice>
}