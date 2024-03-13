package com.example.lesson3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "clients",
        indices = [Index("id"),Index("manager_id")],
        foreignKeys = [
            ForeignKey(
                entity = Manager::class,
                parentColumns = ["id"],
                childColumns = ["manager_id"],
                onDelete = ForeignKey.CASCADE
            )
        ]
)

data class Client(
    @SerializedName("id") @PrimaryKey var id: Int=0, //UUID = UUID.randomUUID(),
    @SerializedName("name") @ColumnInfo(name= "name") var name: String="",
    @SerializedName("manager_id") @ColumnInfo(name= "manager_id") var mangerID: Int=0 //UUID?=null
)
