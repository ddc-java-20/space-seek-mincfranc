package edu.cnm.deepdive.spaceseek.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;

public class FavoritesAdapter extends ListAdapter<Apod, FavoritesAdapter.ViewHolder> {

  public FavoritesAdapter() {
    super(DIFF_CALLBACK);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_favorite, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView favoriteTitle;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      favoriteTitle = itemView.findViewById(R.id.favorite_title);
    }

    public void bind(Apod apod) {
      favoriteTitle.setText(apod.getTitle());
    }
  }

  private static final DiffUtil.ItemCallback<Apod> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<Apod>() {
        @Override
        public boolean areItemsTheSame(@NonNull Apod oldItem, @NonNull Apod newItem) {
          return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Apod oldItem, @NonNull Apod newItem) {
          return oldItem.equals(newItem);
        }
      };
}