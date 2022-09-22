package com.example.page.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.page.databinding.FragmentFavouriteBinding
import com.example.page.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : Fragment(), UserPagingDataAdapter.Interaction {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var userPagingDataAdapter: UserPagingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            userPagingDataAdapter = UserPagingDataAdapter(this@FavouriteFragment)
            adapter = userPagingDataAdapter
        }

        binding.content.retryButton.setOnClickListener {
            userPagingDataAdapter.retry()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.favouriteUsers.collectLatest { pagingData ->
                        userPagingDataAdapter.submitData(pagingData)
                    }
                }

                launch {
                    userPagingDataAdapter.loadStateFlow
                        .distinctUntilChangedBy { it.refresh }
                        .collectLatest { loadStates ->
                            // Show loading spinner during initial load or refresh.
//                            binding.root.isRefreshing =
//                                loadStates.refresh is LoadState.Loading

                            // Show the retry state if initial load or refresh fails.
                            binding.content.retryButton.isVisible =
                                loadStates.refresh is LoadState.Error

                            binding.content.tvEmptyList.isVisible =
                                loadStates.refresh is LoadState.Error
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(item: User) {
        val action = FavouriteFragmentDirections.actionFavouriteFragmentToDetailsFragment(item)
        findNavController().navigate(action)
    }

    override fun updateCheck(item: User, isChecked: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.updateFavourite(isChecked, item)
        }
    }
}