package edu.cnm.deepdive.spaceseek.controller;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import edu.cnm.deepdive.spaceseek.viewmodel.ApodViewModel;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

  private ApodViewModel viewModel;
  private TextView titleView;
  private ImageView imageView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    titleView = findViewById(R.id.apod_title);
    imageView = findViewById(R.id.apod_image);

    viewModel = new ViewModelProvider(this).get(ApodViewModel.class);

    observeApod();
  }

  private void observeApod() {
    // Observe LiveData for today's APOD
    viewModel.getApod().observe(this, this::displayApod);
  }

  private void displayApod(Apod apod) {
    if (apod != null) {
      titleView.setText(apod.getTitle());
      Picasso.get().load(apod.getLowDefUrl().toString()).into(imageView);
    } else {
      titleView.setText(R.string.error_loading_apod);
    }
  }
}