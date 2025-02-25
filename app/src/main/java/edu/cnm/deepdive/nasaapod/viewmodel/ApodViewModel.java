package edu.cnm.deepdive.nasaapod.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.service.ApodRepository;
import java.time.LocalDate;
import java.util.List;

@HiltViewModel
public class ApodViewModel extends ViewModel {

  private final ApodRepository repository;
  private final MutableLiveData<LocalDate> startDate;
  private final MutableLiveData<LocalDate> endDate;
  private final LiveData<List<Apod>> apods;

  public ApodViewModel(ApodRepository repository) {
    this.repository = repository;
    startDate = new MutableLiveData<>();
    endDate = new MutableLiveData<>();
    apods = new MutableLiveData<>(); // FIXME: 2/25/25 Use Transformations
  }

  public LiveData<List<Apod>> getApods() {
    return apods;
  }

  public LiveData<Apod> getApod() {
    return repository.get();
  }

  public void setRange(LocalDate startDate, LocalDate endDate) {
    //this.startDate.setValue(startDate);
    //this.endDate.setValue(endDate);
    repository
        .fetch(startDate, endDate)
        .subscribe(
            // TODO: 2/25/25 perform an empty action on completable and log
        )
  }
}
