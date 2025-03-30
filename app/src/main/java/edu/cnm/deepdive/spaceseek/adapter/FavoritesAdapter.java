package edu.cnm.deepdive.spaceseek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.spaceseek.databinding.ItemApodBinding;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<Apod> apods;
  private final LayoutInflater inflater;

  public FavoritesAdapter(Context context, List<Apod> apods) {
    inflater = LayoutInflater.from(context);
    this.apods = apods;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    return new Holder(ItemApodBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((Holder) holder).bind(apods.get(position));
  }

  @Override
  public int getItemCount() {
    return apods.size();
  }

  private static class Holder extends ViewHolder {

    private final ItemApodBinding binding;

    Holder(@NonNull ItemApodBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(Apod apod) {
      binding.title.setText(apod.getTitle());
      // TODO: 3/29/2025 Set values of any other widgets, set listeners, ie turn off, and set date
    }
  }


}