package com.example.project_g06.repository


import android.util.Log
import com.example.project_g06.interfaces.IOnResListner
import com.example.project_g06.models.Reservation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.tasks.asDeferred

import org.w3c.dom.Comment

class ReservationRepo (private val clickListener: IOnResListner){

    private val TAG = this.toString()
    private val db = Firebase.firestore
    private val COLLECTION_NAME = "reservations"
    private val FIELD_LOCATION = "location"
    private val FIELD_PARK_NAME = "parkName"
    private val FIELD_TRIP_DATE = "tripDate"


    fun syncReservations() {
        try {
            db.collection(COLLECTION_NAME).addSnapshotListener(EventListener { snapshot, error ->

                if (error != null) {
                    Log.e(TAG, "Sync: Listening to collection documents failed $error")
                    return@EventListener
                }
                if (snapshot != null) {
                    Log.d(
                        TAG,
                        "Sync: ${snapshot.size()} Received the documents from collection $snapshot"
                    )

                    // process the received documents
                    val reservationsList: MutableList<Reservation> = mutableListOf<Reservation>()

                    for (documentChange in snapshot.documentChanges) {

                        val currentRes: Reservation =
                            documentChange.document.toObject(Reservation::class.java)
                        currentRes.id = documentChange.document.id
                        when (documentChange.type) {
                            DocumentChange.Type.ADDED -> {
                                Log.d(TAG, "Sync: added a document $currentRes")
                                reservationsList.add(currentRes)
                                var dataSourceArrayList: ArrayList<Reservation> =
                                    DataSource.getInstance().dataSourceResList
                                dataSourceArrayList.add(currentRes)

                            }
                            DocumentChange.Type.MODIFIED -> {
                                var dataSourceArrayList: ArrayList<Reservation> =
                                    DataSource.getInstance().dataSourceResList
                                for ((i, res) in dataSourceArrayList.withIndex()) {
                                    if (res.id == documentChange.document.id) {
                                        dataSourceArrayList[i].note =
                                            documentChange.document.toObject(Reservation::class.java).note
                                        dataSourceArrayList[i].tripDate =
                                            documentChange.document.toObject(Reservation::class.java).tripDate
                                    } else {
                                        Log.d(TAG, "Sync: did not find the reservation")
                                    }
                                }
                            }
                            DocumentChange.Type.REMOVED -> {
                                reservationsList.remove(currentRes)
                                var dataSourceArrayList: ArrayList<Reservation> =
                                    DataSource.getInstance().dataSourceResList
                                dataSourceArrayList.remove(currentRes)
                            }
                        }
                        clickListener.ResDataChangeListener()
                    }
                    Log.d(TAG, "Sync: $reservationsList")
                } else {
                    Log.d(TAG, "Sync: No documents received from collection $COLLECTION_NAME")
                }
            })
        } catch (ex: Exception) {
            Log.d(TAG, "SyncPosts: exception: $ex")
        }
    }

    suspend fun updateReservation(reservation:Reservation):Boolean{
        var isSuccess = false
        val task: Task<Void>  = db.collection(COLLECTION_NAME).document(reservation.id)
            .set(reservation.toHashMap(), SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "Reservation updated successfully!")
                isSuccess = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating reservation", e)
                isSuccess = false
            }
        val deferredDataSnapshot: Deferred<Void> =
            task.asDeferred()
        val result: Void = deferredDataSnapshot.await()
        return isSuccess
    }

    suspend fun deleteReservation(reservation: Reservation): Boolean {
        var isSuccess = false
        val task: Task<Void> = db.collection(COLLECTION_NAME).document(reservation.id)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
                isSuccess = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
        val deferredDataSnapshot: Deferred<Void> =
            task.asDeferred()
        val result: Void = deferredDataSnapshot.await()
        return isSuccess
    }

    suspend fun addReservation(reservation:Reservation) :Boolean{
            var isSuccess = false
            val task: Task<Void> =
                db.collection(COLLECTION_NAME).document(reservation.id).set(reservation).addOnSuccessListener {
                }.addOnFailureListener { ex ->
                    Log.d(TAG, "addReservation: $ex")
                    isSuccess  =false

                }
                    .addOnSuccessListener {
                        isSuccess  =true
                    }
            val deferredDataSnapshot: Deferred<Void> =
                task.asDeferred()
            val result: Void = deferredDataSnapshot.await()
        return isSuccess
    }


}
