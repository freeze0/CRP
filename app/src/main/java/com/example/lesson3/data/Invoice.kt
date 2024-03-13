package com.example.lesson3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "invoices",
        indices = [Index("id"), Index("client_id", "id")],
        foreignKeys = [
            ForeignKey(
                entity = Client::class,
                parentColumns = ["id"],
                childColumns = ["client_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
)

data class Invoice(
    @SerializedName("id") @PrimaryKey val id: Int=0, //UUID = UUID.randomUUID(),
    @SerializedName("date1") var date1: Date = Date(),
    @SerializedName("id_invoice") var id_invoice: String="",
    @SerializedName("sum_total") var sum_total: Int=0,
    @SerializedName("date_exec") var date_exec: Date = Date(),
    @SerializedName("handed") var handed: String="",
    @SerializedName("accepted") var accepted: String="",
    @SerializedName("add_info") var add_info: String="",
    @SerializedName("basis_doc") var basis_doc: String="",
    @SerializedName("client_id") @ColumnInfo(name="client_id") var clientID: Int=0 //UUID?=null

)


