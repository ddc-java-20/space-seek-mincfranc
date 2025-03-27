package edu.cnm.deepdive.nasaapod.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.adapter.FavoritesAdapter;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

  private RecyclerView favoritesRecyclerView;
  private FavoritesAdapter adapter;
  private final List<String> favoriteApods = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorites, container, false);
    favoritesRecyclerView = view.findViewById(R.id.favorites_recycler_view);
    setupRecyclerView();
    return view;
  }

  private void setupRecyclerView() {
    // Initialize the adapter using the FavoritesAdapter from the adapter package
    adapter = new FavoritesAdapter(favoriteApods);
    favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    favoritesRecyclerView.setAdapter(adapter);
  }

  public void addFavorite(String apodTitle) {
    if (!favoriteApods.contains(apodTitle)) {
      favoriteApods.add(apodTitle);
      adapter.notifyDataSetChanged(); // Notify the adapter of the updated data
    }
  }

}