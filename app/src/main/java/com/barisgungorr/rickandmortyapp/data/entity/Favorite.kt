package com.barisgungorr.rickandmortyapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "character")
data class Favorite(@PrimaryKey (autoGenerate = true)
                    @ColumnInfo(name = "character_id")  var characterId:Int,
                    @ColumnInfo(name = "character_name")  var characterName:String,
                    @ColumnInfo(name = "character_alive")  var characterAlive:String,
                    @ColumnInfo(name = "character_status") var characterStatus:String,
                    @ColumnInfo(name = "character_species") var characterSpecies:String,
                    @ColumnInfo(name = "character_gender") var characterGender:String,
                    @ColumnInfo(name = "character_location") var characterLocation:String,
                    @ColumnInfo(name = "character_image")val characterImage:String)
    : Serializable
