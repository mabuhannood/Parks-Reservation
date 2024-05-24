package com.example.project_g06.models

import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

data class Reservation(
    var id: String = UUID.randomUUID().toString(),
    var location: String = "",
    var parkName: String = "",
    var tripDate: String = "",
    var note: String = "",
) : Serializable {
    override fun toString(): String {
        return "Reservation(id='$id', location='$location', parkName='$parkName', tripDate='$tripDate', note='$note')"
    }

    fun toHashMap(): HashMap<String, Any> {
        val map = hashMapOf<String, Any>(
            "id" to id,
            "location" to location,
            "parkName" to parkName,
            "tripDate" to tripDate,
            "note" to note
        )
        return map
    }
}
