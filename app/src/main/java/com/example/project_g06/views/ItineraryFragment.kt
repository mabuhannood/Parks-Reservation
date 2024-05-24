package com.example.project_g06.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_g06.R
import com.example.project_g06.adapter.ResAdapter
import com.example.project_g06.databinding.FragmentItineraryBinding
import com.example.project_g06.interfaces.IOnResListner
import com.example.project_g06.models.Reservation
import com.example.project_g06.repository.DataSource
import com.example.project_g06.repository.ReservationRepo
import com.google.api.LogDescriptor
import kotlinx.coroutines.launch


class ItineraryFragment : Fragment(R.layout.fragment_itinerary), IOnResListner {
    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!
    private val TAG = "SCREEN1"

    lateinit var reservationRepo : ReservationRepo
    lateinit var dataSource: DataSource
    lateinit var resArrayList: ArrayList<Reservation>
    var resAdapter: ResAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        val view = binding.root
        dataSource = DataSource.getInstance()
        resArrayList = dataSource.dataSourceResList

        // Create the adapter to convert the array to views
        resAdapter = ResAdapter(requireContext(), resArrayList, this)
        binding.rvRes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRes.adapter = resAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "3- fragment onViewCreated() running")
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        resArrayList.clear()
        reservationRepo = ReservationRepo(this)
        reservationRepo.syncReservations()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun ResDataChangeListener() {

        Log.d(TAG, "Notifying the adapter: ${dataSource.dataSourceResList.size}")
        resArrayList = dataSource.dataSourceResList
        Log.d(TAG, "postArrayList size: ${resArrayList.size}")
        resAdapter?.notifyDataSetChanged()
        Log.d(TAG, "ResDataChangeListener: ${resArrayList.size}")

    }

    override fun ResOnClickListner(reservation: Reservation) {
        val action = ItineraryFragmentDirections.actionItineraryFragmentToItineraryFragment2(reservation)
        findNavController().navigate(action)
    }


}