package com.example.project_g06.interfaces

import com.example.project_g06.models.Reservation

interface IOnResListner {
    fun ResDataChangeListener()

//    fun onDeletePostListener(post: Post)

    fun ResOnClickListner(reservation: Reservation)
}

