package com.example.project_g06.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.project_g06.R
import com.example.project_g06.databinding.FragmentParkDetailsBinding
import com.example.project_g06.interfaces.IOnResListner
import com.example.project_g06.models.Reservation
import com.example.project_g06.repository.ReservationRepo
import kotlinx.coroutines.launch

class parkDetails : Fragment(R.layout.fragment_park_details), IOnResListner {

    private val TAG="Parks"
    // binding variables
    private var _binding: FragmentParkDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var reservationRepo: ReservationRepo

    private val args:parkDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationRepo = ReservationRepo(this)
        binding.parkName.setText(args.parkName)
        Glide.with(requireContext())
            .load(args.parkImage)
//            .fitCenter()
            .into(binding.parkImage);
        binding.parkAddress.setText(args.parkAddress)
        binding.parkUrl.setText(args.parkURL)
        binding.parkDesciprtion.setText(args.parkDescription)

        binding.butBook.setOnClickListener {
            // send the data to firestorm
            viewLifecycleOwner.lifecycleScope.launch {
                val reservationToAdd = Reservation()
                reservationToAdd.parkName = binding.parkName.text.toString()
                reservationToAdd.location = binding.parkAddress.text.toString()
                val isAdded = reservationRepo.addReservation(reservationToAdd)
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        "Successfully added",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to add the reservation",
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