package com.example.lesson3.API

import com.example.lesson3.data.Managers
import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Clients
import com.example.lesson3.data.Invoice
import com.example.lesson3.data.Invoices
import com.example.lesson3.data.Spasibo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface ListAPI{
    @GET("manager")
    fun getManagers(): Call<Managers>

    @Headers("Content-Type: application/json")
    @POST("manager")
    fun updateManager(@Body faculty: Manager): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("manager/delete")
    fun deleteManager(@Body id: PostId): Call<PostResult>

    @Headers("Content-Type: application/json")
    @PUT("manager")
    fun insertManager(@Body faculty: Manager): Call<PostResult>

    @GET("client")
    fun getClients(): Call<Clients>
    @Headers("Content-Type: application/json")
    @POST("client")
    fun updateClients(@Body client: Client): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("client/delete")
    fun deleteClient(@Body id: PostId): Call<PostResult>

    @Headers("Content-Type: application/json")
    @PUT("client")
    fun insertClient(@Body client: Client): Call<PostResult>

    @GET("invoice")
    fun getInvoices(): Call<Invoices>

    @Headers("Content-Type: application/json")
    @POST("invoice")
    fun updateInvoice(@Body invoice: Invoice): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("invoice/delete")
    fun deleteInvoice(@Body id: PostId): Call<PostResult>

    @Headers("Content-Type: application/json")
    @PUT("invoice")
    fun insertInvoice(@Body invoice: Invoice): Call<PostResult>

    @Headers("Content-Type: application/json")
    @POST("user")
    fun login(@Body user: PostUser): Call<Spasibo>

}