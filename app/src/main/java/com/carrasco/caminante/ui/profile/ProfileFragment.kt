package com.carrasco.caminante.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.carrasco.caminante.R
import com.carrasco.caminante.data.dao.UserDao
import com.carrasco.caminante.databinding.FragmentProfileBinding
import com.carrasco.caminante.databinding.FragmentPublicationListBinding
import com.carrasco.caminante.ui.MainViewModel
import com.carrasco.caminante.ui.PublicationAdapter
import com.carrasco.caminante.ui.detail.DetailFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private val adapter =  PublicationAdapter(){publication -> viewModel.navigateTo(publication) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view).apply {
            userEmail.text = UserDao.getCurrentUserEmail()
            recycler.adapter = adapter
        }
        viewModel.state.observe(viewLifecycleOwner){state ->

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    state.publications?.collect() {
                        Log.d("Flow collect", it.toString())
                        adapter.publicationList = it
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            state.navigateTo?.let {
                findNavController().navigate(
                    R.id.action_profileFragment_to_detailFragment,
                    bundleOf(DetailFragment.EXTRA_PUBLICATION to it)
                )
                viewModel.onNavigateDone()
            }

        }
    }
}