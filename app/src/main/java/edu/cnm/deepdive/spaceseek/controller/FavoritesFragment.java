package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.adapter.FavoritesAdapter;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import edu.cnm.deepdive.spaceseek.utils.RecyclerViewUtils;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;
import java.util.ArrayList;
import java.util.List;

@AndroidEntryPoint
public class FavoritesFragment extends Fragment {

  private RecyclerView favoritesRecyclerView;
  private FavoritesAdapter adapter;
  private final List<String> favoriteApods = new ArrayList<>();
  private ApodViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorites, container, false);
    favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);
    setupRecyclerView();
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ApodViewModel.class);

    // Observe favorite APODs and update UI
    viewModel.getFavorites().observe(getViewLifecycleOwner(), this::updateFavorites);
  }

  private void setupRecyclerView() {
    adapter = new FavoritesAdapter(favoriteApods);
    RecyclerViewUtils.setupRecyclerView(getContext(), favoritesRecyclerView, adapter);
  }

  private void updateFavorites(List<Apod> apods) {
    favoriteApods.clear();
    for (Apod apod : apods) {
      favoriteApods.add(apod.getTitle());
    }
    adapter.notifyDataSetChanged();
  }
}