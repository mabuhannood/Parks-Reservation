package com.example.project_g06.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project_g06.R
import com.example.project_g06.databinding.FragmentParkMapBinding
import com.example.project_g06.models.data
import com.example.project_g06.models.parkData
import com.example.project_g06.networking.ApiService
import com.example.project_g06.networking.RetrofitInstance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import retrofit2.Response


class parkMap : Fragment(R.layout.fragment_park_map), OnMapReadyCallback {

    private val TAG="Parks"
    // binding variables
    private var _binding: FragmentParkMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var retrivedParks:List<data>
    private val states:MutableList<String> = mutableListOf("Alabama","Alaska","Arizona","Arkansas","California",
        "Colorado","Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana",
        "Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan",
        "Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey",
        "New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania",
        "Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia",
        "Washington","West Virginia","Wisconsin","Wyoming")
    private val statesAbrr:MutableList<String> = mutableListOf("AL","AK","AZ","AR","CA","CO","CT","DE",
        "FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE",
        "NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA",
        "WA","WV","WI","WY")
    private var stateCode = ""

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private lateinit var mMap: GoogleMap

    private var markerid = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkMapBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, states)
        binding.spinStates.adapter = spinnerAdapter

        binding.spinStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                stateCode = statesAbrr[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        binding.butParks.setOnClickListener {
            mMap.clear()
            lifecycleScope.launch {
                var responseFromAPI: parkData? = getAllParks()
                if (responseFromAPI == null) {
                    return@launch
                }
                Log.d(TAG, "Success: Data retrieved from API")

                // get the list of users
                var first:Int  = 0
                retrivedParks = responseFromAPI.data
                for (parks in retrivedParks) {
                    Log.d(TAG, "Park Name: ${parks.fullName}")
                    Log.d(TAG, "Adresses: ${parks.addresses[1]}")
                    Log.d(TAG, "Images: ${parks.images[0].url}")
                    Log.d(TAG,"URL: ${parks.url}")
                    val intialLocation = LatLng(parks.latitude, parks.longitude)
                    val title = "${parks.fullName}"
                    mMap.addMarker(MarkerOptions().position(intialLocation).title(title))
                    if (first == 0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intialLocation, 10.0f))
                        first  = 1
                    }
                }
                Log.d(TAG, "Number of names in adapter data source: ${retrivedParks.size}")
            }
        }

        // get a reference to the map fragment
        val mapFragment =  childFragmentManager.findFragmentById(binding.fragmentMap.id) as? SupportMapFragment

        // debugging messages to help determine if the screen can find the map
        if (mapFragment == null) {
            Log.d(TAG, "++++ map fragment is null")
        }
        else {
            Log.d(TAG, "++++ map fragment is NOT null")
            // assuming the screen can find the map fragment in the xml file, then
            // connect with Google and get whatever information you need from Google
            // to setup the map
            mapFragment?.getMapAsync(this)
        }

    }

    private suspend fun getAllParks(): parkData? {
        var apiService: ApiService = RetrofitInstance.retrofitService

        val response: Response<parkData> = apiService.getParks(stateCode)

        if (response.isSuccessful) {
            val dataFromAPI = response.body()   /// myresponseobject
            if (dataFromAPI == null) {
                Log.d("API", "No data from API or some other error")
                return null
            }
            // if you reach this point, then you must have received a response from the api
            Log.d(TAG, "Here is the data from the API")
            Log.d(TAG, dataFromAPI.toString())
            return dataFromAPI
        }
        else {
            // Handle error
            Log.d(TAG, "An error occurred")
            return null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // map callback - this function is executed when the fragment
    // has the data it needs from Google to instantiate the map
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "+++ Map callback is executing...")
        // initialize the map
        this.mMap = googleMap


        // configure the map's options
        // - set the map type (hybrid, satellite, etc)
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        // - select if traffic data should be displayed
        mMap.isTrafficEnabled = true
        // - add user interface controls to the map (zoom, compass, etc)
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true

        val intialLocation = LatLng(39.8283, -98.5795)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intialLocation, 3.5f))

        mMap.setOnMarkerClickListener { marker ->
            val markerName = marker.title
            var curmarkerid = marker.id
            if (markerid == curmarkerid ) {
                markerid = curmarkerid
                var address: String = ""
                var url: String = ""
                var imageUrl: String = ""
                var description: String = "'"
                for (parks in retrivedParks) {
                    if (parks.fullName == markerName) {
                        description = parks.description
                        Log.d(TAG, "Description: $description")
                        address =
                            "${parks.addresses[0].line1},${parks.addresses[0].city},${parks.addresses[0].stateCode}"
                        Log.d(TAG, "Adresses: $address")
                        imageUrl = parks.images[0].url
                        Log.d(TAG, "Images: ${imageUrl}")
                        url = parks.url
                        Log.d(TAG, "URL: $url")
                    }
                }
                val action = parkMapDirections.actionParkMapToParkDetails(
                    markerName!!,
                    description,
                    url,
                    imageUrl,
                    address
                )
                findNavController().navigate(action)
            }
            else {
                markerid = curmarkerid
            }
            false
        }
    }
}