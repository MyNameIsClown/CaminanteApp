package com.carrasco.caminante.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carrasco.caminante.data.model.Publication
import com.carrasco.caminante.databinding.ViewPublicationBinding
import com.carrasco.caminante.loadUrl

class PublicationAdapter()
    : RecyclerView.Adapter<PublicationAdapter.ViewHolder>(){

    var publicationList: List<Publication> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewPublicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(publicationList[position])
    }

    override fun getItemCount(): Int = publicationList.size

    class ViewHolder(val binding: ViewPublicationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(publication: Publication){
            if(publication.imageURL!!.isEmpty()){
                binding.publicationImage.loadUrl("https://vivecamino.com/img/noti/av/simbolos-camino-santiago_595.jpg")
            }else{
                binding.publicationImage.loadUrl(publication.imageURL)

            }
            binding.publicationTitle.text = publication.title
            binding.publicationCategory.text = publication.category
            binding.publicationRoute.text = publication.route
        }
    }

}