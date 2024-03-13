package com.example.lesson3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "managers",
    indices = [Index("id")]
)

data class Manager(
    @SerializedName("id") @PrimaryKey val id: Int=0, //UUID=UUID.randomUUID(),
    @SerializedName("name") @ColumnInfo(name="name") var name: String=""
)
