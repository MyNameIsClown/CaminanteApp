package com.carrasco.caminante.ui.detail

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.carrasco.caminante.R
import com.carrasco.caminante.data.dao.UserDao
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.data.model.User
import com.carrasco.caminante.databinding.FragmentDetailBinding
import com.carrasco.caminante.loadUrl
import com.carrasco.caminante.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(arguments?.getParcelable<Publication>(EXTRA_PUBLICATION)!!)
    }
    companion object{
        const val EXTRA_PUBLICATION = "DetailActivity:Publication"
    }
    private var isSaved: Boolean = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)


        viewModel.publication.observe(viewLifecycleOwner){ publication ->
            (requireActivity() as AppCompatActivity).supportActionBar?.title = publication.title
            var imageURL = publication.imageURL;
            if(imageURL==null){
                imageURL = "https://vivecamino.com/img/noti/av/simbolos-camino-santiago_595.jpg"
            }
            binding.publicationImage.loadUrl(imageURL)
            binding.publicationTitle.text = publication.title
            binding.publicationDesciption.text = publication.description
            binding.publicationRoute.text = publication.route
            binding.publicationCategory.text = publication.category
            viewLifecycleOwner.lifecycleScope.launch{
                isSaved = UserDao.isSaved(publication)
                if(isSaved){
                    Log.d("Detail Fragment", "Cambiando color")
                    binding.favButton.drawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.amarilloPantone), PorterDuff.Mode.SRC_IN)
                }
            }
        }

        binding.favButton.setOnClickListener{
            val publication = viewModel.publication.value!!
            viewLifecycleOwner.lifecycleScope.launch{
                isSaved = UserDao.isSaved(publication)
            }
            if(isSaved){
                Log.d("Detail Fragment", "Cambiando color")
                requireContext().toast("Publicacion ya guardada")
            }else{
                Log.d("Añadiendo Publicacion", publication.toString())
                UserDao.savePublication(publication, requireContext())
                binding.favButton.drawable.setColorFilter(ContextCompat.getColor(requireContext(), R.color.amarilloPantone), PorterDuff.Mode.SRC_IN)
            }
        }

        binding.ubicacion.setOnClickListener{
            val publication = viewModel.publication.value!!
            val gmmIntentUri = Uri.parse("geo:${publication.latitude},${publication.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}