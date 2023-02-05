package com.carrasco.caminante.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.carrasco.caminante.R
import com.carrasco.caminante.data.dao.PublicationDao
import com.carrasco.caminante.databinding.FragmentPublicationListBinding
import kotlinx.coroutines.*


class PublicationListFragment : Fragment(R.layout.fragment_publication_list) {
    private lateinit var binding: FragmentPublicationListBinding
    private val adapter =  PublicationAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPublicationListBinding.bind(view).apply {
            recycler.adapter = adapter
        }
        if(adapter.itemCount == 0){
            loadItems()
        }
    }

    private fun loadItems(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            binding.progress.visibility=View.VISIBLE
            val publications = async(Dispatchers.IO){PublicationDao.getAll()}
            Log.d("Prueba", publications.await().toString())
            adapter.publicationList = publications.await()
            adapter.notifyDataSetChanged()
            binding.progress.visibility = View.GONE
        }
    }
}