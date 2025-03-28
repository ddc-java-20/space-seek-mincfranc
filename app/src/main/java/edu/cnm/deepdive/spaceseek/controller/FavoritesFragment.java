package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.adapter.FavoritesAdapter;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;
@AndroidEntryPoint
public class FavoritesFragment extends Fragment {

  private RecyclerView favoritesRecyclerView;
  private FavoritesAdapter adapter;
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorites, container,
        false); // Corrected layout reference
    favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);
    setupRecyclerView();
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);
    viewModel.getFavorites().observe(getViewLifecycleOwner(),
        (apods) -> adapter.submitList(apods)); // Updated method call
  }

  private void setupRecyclerView() {
    adapter = new FavoritesAdapter();
    favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    favoritesRecyclerView.setAdapter(adapter);
  }
}