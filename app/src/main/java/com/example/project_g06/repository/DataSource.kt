package com.example.project_g06.repository

import com.example.project_g06.models.Reservation

open class DataSource {

    companion object {
        @Volatile
        private lateinit var instance: DataSource

        fun getInstance(): DataSource {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = DataSource()
                }
                return instance
            }
        }
    }

    var dataSourceResList: ArrayList<Reservation> = arrayListOf()
}