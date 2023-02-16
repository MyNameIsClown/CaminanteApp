package com.carrasco.caminante.ui.detail

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.carrasco.caminante.R
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.databinding.FragmentDetailBinding
import com.carrasco.caminante.loadUrl


class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(arguments?.getParcelable<Publication>(EXTRA_PUBLICATION)!!)
    }
    companion object{
        const val EXTRA_PUBLICATION = "DetailActivity:Publication"
    }

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
        }
    }
}