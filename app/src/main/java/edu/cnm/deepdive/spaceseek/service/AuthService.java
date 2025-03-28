package edu.cnm.deepdive.spaceseek.service;

import android.content.Context;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthService {

  private final GoogleSignInClient client;

  @Inject
  public AuthService(@ApplicationContext Context context) {
    GoogleSignInOptions options = new GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestProfile()
        .requestIdToken(context.getString(R.string.client_id))
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  public Single<GoogleSignInAccount> silentSignIn() {
    return Single.create(emitter -> client.silentSignIn()
            .addOnSuccessListener(emitter::onSuccess)
            .addOnFailureListener(emitter::onError))
        .observeOn(Schedulers.io());
  }

  public Single<String> refreshBearerToken() {
    return silentSignIn()
        .map(GoogleSignInAccount::getIdToken);
  }

  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent());
  }

  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) {
    return Single.create(emitter -> {
      try {
        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
            .getResult(ApiException.class);
        emitter.onSuccess(account);
      } catch (ApiException e) {
        emitter.onError(e);
      }
    }).observeOn(Schedulers.io());
  }

  public Completable signOut() {
    return Completable.create(emitter -> client.signOut()
            .addOnSuccessListener((ignored) -> emitter.onComplete())
            .addOnFailureListener(emitter::onError))
        .observeOn(Schedulers.io());
  }
}