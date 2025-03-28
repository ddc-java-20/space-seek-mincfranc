package edu.cnm.deepdive.spaceseek.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.spaceseek.R;
import edu.cnm.deepdive.spaceseek.service.AuthService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

  @Inject
  AuthService authService;

  private final CompositeDisposable disposables = new CompositeDisposable();
  private ActivityResultLauncher<Intent> signInLauncher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    signInLauncher = registerForActivityResult(
        new StartActivityForResult(),
        result -> disposables.add(authService.completeSignIn(result)
            .subscribe(
                this::onLoginSuccess,
                throwable -> Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
            ))
    );

    findViewById(R.id.btn_google_sign_in).setOnClickListener(
        v -> authService.startSignIn(signInLauncher));
  }

  private void onLoginSuccess(Object account) {
    Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
    // Navigate to the home screen here.
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposables.clear();
  }
}