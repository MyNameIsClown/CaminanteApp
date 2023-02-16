package com.carrasco.caminante.ui.create

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.carrasco.caminante.R
import com.carrasco.caminante.data.dao.PublicationDao
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.databinding.FragmentCreatePublicationBinding
import com.carrasco.caminante.toast
import com.carrasco.caminante.ui.HomeActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint


class CreatePublicationFragment : Fragment() {
    private var _binding: FragmentCreatePublicationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePublicationBinding.inflate(inflater, container, false)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categoryInput.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.routes  ,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.routeInput.adapter = adapter
        }
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            val location = getCurrentLocation()
            val currentLocation = GeoPoint(location?.latitude!!, location?.longitude!!)
            val title = binding.titleInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val category = binding.categoryInput.selectedItem.toString()
            val route = binding.routeInput.selectedItem.toString()
            PublicationDao.save(
                Publication(
                    title, description, category, route, null,
                    Timestamp.now(), currentLocation.latitude, currentLocation.longitude
                )
            )
            requireContext().toast("Publicando")
        }
    }
    private fun getCurrentLocation(): Location? {
        var location: Location? = Location("")
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                requireContext().toast("Error al obtener la ubicacion")
            }else{
                location = Location("España")
            }
        }
        return location
    }
}