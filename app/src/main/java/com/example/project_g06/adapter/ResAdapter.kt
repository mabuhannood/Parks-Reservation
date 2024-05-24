package com.example.project_g06.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_g06.databinding.ResItemBinding
import com.example.project_g06.interfaces.IOnResListner
import com.example.project_g06.models.Reservation
import com.example.project_g06.repository.DataSource


class ResAdapter(
    private val context: Context,
    private var reservationList: ArrayList<Reservation>,
    private val clickListener: IOnResListner
) : RecyclerView.Adapter<ResAdapter.ResViewHolder>() {

    internal val TAG = "ReservationAdapter"


    class ResViewHolder(val binding: ResItemBinding, clickListener: IOnResListner) : RecyclerView.ViewHolder(binding.root) {
        internal val TAG = "ReservationAdapter"
        var dataSource = DataSource.getInstance()

        init {

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = dataSource.dataSourceResList[position]
                    clickListener.ResOnClickListner(item)
                }
            }
        }


        @SuppressLint("SetTextI18n")
        fun bind(currentItem: Reservation) {
            // associate individual view with data

            Log.d(TAG, "bind: ${currentItem.location}")

            binding.tvLocation.text = currentItem.location

            binding.tvParkName.text = currentItem.parkName

            binding.tvTripDate.text = currentItem.tripDate

//            clickListener.ResOnClickListner(currentItem)
        }
    }

    // create the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResAdapter.ResViewHolder {
        return ResAdapter.ResViewHolder(
            ResItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        ,clickListener)
    }

    // binds the data with view
    override fun onBindViewHolder(holder: ResViewHolder, position: Int) {
        holder.bind(reservationList[position])
    }

    // identifies the number of items/elements that we will display
    override fun getItemCount(): Int {
        return reservationList.size
    }
}