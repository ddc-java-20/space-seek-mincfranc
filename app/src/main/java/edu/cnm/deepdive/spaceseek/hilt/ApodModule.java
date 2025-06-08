package edu.cnm.deepdive.spaceseek.hilt;

import androidx.lifecycle.MutableLiveData;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import java.util.List;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class ApodModule {

  @Provides
  @Singleton
  static MutableLiveData<List<Apod>> provideBirthdayApods() {
    return new MutableLiveData<>(); // default instance to avoid missing binding error
  }
}