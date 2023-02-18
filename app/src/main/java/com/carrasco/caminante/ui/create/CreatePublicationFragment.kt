package com.carrasco.caminante.ui.create

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.carrasco.caminante.R
import com.carrasco.caminante.data.dao.PublicationDao
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.databinding.FragmentCreatePublicationBinding
import com.carrasco.caminante.toast
import com.google.firebase.Timestamp
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.carrasco.caminante.ui.detail.DetailFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



class CreatePublicationFragment : Fragment() {
    private val viewModel: CreateViewModel by viewModels()
    private var _binding: FragmentCreatePublicationBinding? = null
    private val binding get() = _binding!!
    private var localImageUri : Uri? = Uri.EMPTY
    private var remoteImageUri : Uri? = Uri.parse("https://vivecamino.com/img/noti/av/simbolos-camino-santiago_595.jpg")
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

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

        binding.selectImageButton.setOnClickListener{
            // Accedo al la galeria para subir una imagen
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startForActivityImage.launch(intent)
        }

        binding.submitButton.setOnClickListener {
            val title = binding.titleInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val category = binding.categoryInput.selectedItem.toString()
            val route = binding.routeInput.selectedItem.toString()
            if (title.isEmpty()||description.isEmpty()){
                requireContext().toast("El campo Titulo y el campo necesitan un valor")
            }else{
                viewModel.viewModelScope.launch {
                    val location = getCurrentLocation(requireContext())
                    if(localImageUri!=Uri.EMPTY&&localImageUri!=null){
                        remoteImageUri = PublicationDao.uploadImage(localImageUri!!)
                    }
                    PublicationDao.save(
                        Publication(
                            null,title, description, category, route, remoteImageUri.toString(),
                            Timestamp.now(), location?.latitude, location?.longitude
                        )
                    )
                }
                requireContext().toast("Publicando")
                Thread.sleep(1000)
                findNavController().navigate(
                    R.id.action_createPublicationFragment_to_mainFragment
                )
            }
        }
    }
    private val startForActivityImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== RESULT_OK){
            val data = it.data?.data
            Log.d("Prueba", data.toString())
            if(data!=null){
                localImageUri = data
            }
        }
    }
    private suspend fun getCurrentLocation(context: Context): Location? = suspendCoroutine { continuation ->
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Cuando se recibe una ubicación válida, se completa la continuación
                locationManager.removeUpdates(this)
                continuation.resume(location)
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                // Si se encuentra una ubicación válida en el caché, se completa la continuación
                continuation.resume(lastKnownLocation)
            } else {
                // Si no hay una ubicación válida en el caché, se solicita una actualización de ubicación
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
                // También se establece un temporizador para cancelar la solicitud después de 30 segundos
                Handler(Looper.getMainLooper()).postDelayed({
                    locationManager.removeUpdates(locationListener)
                    continuation.resume(null)
                }, 30000L)
            }
        } else {
            // Si no se han concedido los permisos, se completa la continuación con valor nulo
            continuation.resume(null)
        }
    }




}