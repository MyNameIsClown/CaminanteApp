package com.carrasco.caminante.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.carrasco.caminante.R
import com.carrasco.caminante.databinding.FragmentPublicationListBinding
import kotlinx.coroutines.*


class PublicationListFragment : Fragment(R.layout.fragment_publication_list) {
    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentPublicationListBinding
    private val adapter =  PublicationAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPublicationListBinding.bind(view).apply {
            recycler.adapter = adapter
        }
        viewModel.state.observe(viewLifecycleOwner){state ->
            // binding.progress.visibility =  if (state.loading) VISIBLE else GONE
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    state.publications?.collect() {
                        adapter.publicationList = it
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}