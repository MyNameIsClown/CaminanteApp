package com.carrasco.caminante.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.carrasco.caminante.R
import com.carrasco.caminante.databinding.FragmentPublicationListBinding
import com.carrasco.caminante.ui.detail.DetailFragment
import kotlinx.coroutines.*


class PublicationListFragment : Fragment(R.layout.fragment_publication_list) {
    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentPublicationListBinding
    private val adapter =  PublicationAdapter(){publication -> viewModel.navigateTo(publication) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPublicationListBinding.bind(view).apply {
            recycler.adapter = adapter
        }
        viewModel.state.observe(viewLifecycleOwner){state ->
            binding.progress.visibility =  if (state.loading) VISIBLE else GONE

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    state.publications?.collect() {
                        adapter.publicationList = it
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            state.navigateTo?.let {
                findNavController().navigate(
                    R.id.action_mainFragment_to_detailFragment,
                    bundleOf(DetailFragment.EXTRA_PUBLICATION to it)
                )
                viewModel.onNavigateDone()
            }

            state.navigateToCreate?.let{
                if (it) {
                    findNavController().navigate(
                        R.id.action_mainFragment_to_createPublicationFragment,
                    )
                    viewModel.navigateToCreateDone()
                }
            }

            state.navigateToProfile?.let{
                if (it) {
                    findNavController().navigate(
                        R.id.action_mainFragment_to_profileFragment,
                    )
                    viewModel.navigateToProfileDone()
                }
            }

        }

        binding.createButton.setOnClickListener {
            viewModel.navigateToCreate()
        }

        binding.profileButton.setOnClickListener {
            viewModel.navigateToProfile()
        }

    }
}