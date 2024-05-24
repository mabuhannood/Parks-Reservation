package com.example.project_g06.views

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.project_g06.R
import com.example.project_g06.databinding.FragmentItinerary2Binding
import com.example.project_g06.interfaces.IOnResListner
import com.example.project_g06.models.Reservation
import com.example.project_g06.repository.ReservationRepo
import kotlinx.coroutines.launch


class ItineraryFragment2 : Fragment(), IOnResListner {

    private val TAG: String = "SCREEN2"
    // binding variables
    private var _binding: FragmentItinerary2Binding? = null
    private val binding get() = _binding!!
    private val args: ItineraryFragment2Args by navArgs()
    lateinit var reservationRepo : ReservationRepo
    var selectedReservation = Reservation()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        _binding = FragmentItinerary2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reservationRepo = ReservationRepo(this)
        selectedReservation = args.selectedReservation

        Log.d(TAG, "$selectedReservation")

        binding.tvDetails.text = "${selectedReservation.parkName}\n${selectedReservation.location}"
        binding.etTripDate.setText(selectedReservation.tripDate)
        binding.etNotes.setText(selectedReservation.note)

     binding.btnSave.setOnClickListener {
         selectedReservation.note = binding.etNotes.text.toString()
         selectedReservation.tripDate = binding.etTripDate.text.toString()
         viewLifecycleOwner.lifecycleScope.launch {
             val isDeleted = reservationRepo.updateReservation(selectedReservation)
             if (isDeleted) {
                 Toast.makeText(
                     requireContext(),
                     "Successfully updated",
                     Toast.LENGTH_SHORT
                 ).show()
                 val action =
                     ItineraryFragment2Directions.actionItineraryFragment2ToItineraryFragment()
                 findNavController().navigate(action)
             } else {
                 Toast.makeText(
                     requireContext(),
                     "Failed to update the reservation",
                     Toast.LENGTH_SHORT
                 ).show()
             }

         }

     }
        binding.btnDelete.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {
                val isDeleted = reservationRepo.deleteReservation(selectedReservation)
                if (isDeleted) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    val action =
                        ItineraryFragment2Directions.actionItineraryFragment2ToItineraryFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete the reservation",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun ResDataChangeListener() {
        TODO("Not yet implemented")
    }

    override fun ResOnClickListner(reservation: Reservation) {
        TODO("Not yet implemented")
    }
}