package edu.cnm.deepdive.spaceseek.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.spaceseek.model.entity.Apod;
import edu.cnm.deepdive.spaceseek.service.ApodRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

@HiltViewModel
public class ApodViewModel extends ViewModel {

  private static final String TAG = ApodViewModel.class.getSimpleName();

  private final ApodRepository repository;
  private final MutableLiveData<YearMonth> yearMonth;
  private final LiveData<List<Apod>> apods;
  private final MutableLiveData<Long> apodId;
  private final MutableLiveData<Throwable> throwable;

  @Inject
  ApodViewModel(ApodRepository repository) {
    this.repository = repository;
    yearMonth = new MutableLiveData<>(YearMonth.now());
    apodId = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    apods = Transformations.switchMap(Transformations.distinctUntilChanged(yearMonth),
        this::transformYearMonthToQuery);
  }

  /**
   * Retrieves the list of APODs for the currently selected YearMonth.
   *
   * @return LiveData containing the list of APODs for the selected range.
   */
  public LiveData<List<Apod>> getApods() {
    return apods;
  }

  /**
   * Retrieves the details of an individual APOD by ID.
   *
   * @return LiveData containing the individual APOD details.
   */
  public LiveData<Apod> getApod() {
    return Transformations.switchMap(Transformations.distinctUntilChanged(apodId), repository::get);
  }

  /**
   * Sets the ID of the APOD for which details are requested.
   *
   * @param apodId The unique identifier of the APOD.
   */
  public void setApodId(long apodId) {
    this.apodId.setValue(apodId);
  }

  /**
   * Retrieves a map of APODs keyed by their LocalDate.
   *
   * @return LiveData containing a map of APODs.
   */
  public LiveData<Map<LocalDate, Apod>> getApodMap() {
    return Transformations.map(apods, (apodList) -> apodList
        .stream()
        .collect(Collectors.toMap(Apod::getDate, Function.identity())));
  }

  /**
   * Retrieves the currently selected YearMonth for filtering APODs.
   *
   * @return LiveData containing the selected YearMonth.
   */
  public LiveData<YearMonth> getYearMonth() {
    return yearMonth;
  }

  /**
   * Sets the currently selected YearMonth for filtering APODs.
   *
   * @param yearMonth The YearMonth to set.
   */
  public void setYearMonth(YearMonth yearMonth) {
    this.yearMonth.setValue(yearMonth);
  }

  /**
   * Retrieves the list of favorite APODs.
   *
   * @return LiveData containing the list of favorite APODs.
   */
  public LiveData<List<Apod>> getFavorites() {
    // Fetch favorites from repository and return LiveData<List<Apod>>.
    return repository.getFavorites();
  }

  /**
   * Fetches APODs for the user's birthdate across all years.
   *
   * @param dob The user's date of birth (formatted as "YYYY-MM-DD").
   */
  @SuppressLint("CheckResult")
  public void fetchApodsForDateAcrossYears(String dob) {
    try {
      LocalDate birthDate = LocalDate.parse(dob); // Parse user's DOB.
      repository.fetchApodsForDateAcrossYears(birthDate)
          .subscribe(
              () -> Log.d(TAG, "Personalized APODs fetched successfully."),
              throwable -> Log.e(TAG, "Error fetching personalized APODs.", throwable)
          );
    } catch (Exception e) {
      Log.e(TAG, "Invalid DOB format: " + dob, e);
    }
  }

  /**
   * Fetches APODs for a specific date range.
   *
   * @param yearMonth The YearMonth for filtering APODs.
   * @return LiveData containing the list of APODs.
   */
  @SuppressLint("CheckResult")
  private LiveData<List<Apod>> transformYearMonthToQuery(YearMonth yearMonth) {
    LocalDate startDate = yearMonth.minusMonths(1).atDay(1);
    LocalDate endDate = yearMonth.plusMonths(2).atDay(1);
    throwable.setValue(null);
    repository
        .fetch(startDate, endDate)
        .subscribe(
            () -> {
            },
            this::postThrowable
        );
    return repository.get(startDate, endDate);
  }

  /**
   * Handles errors and logs them appropriately.
   *
   * @param throwable The error encountered during data fetching.
   */
  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  /**
   * Fetches a random number of APODs.
   *
   * @param count The number of random APODs to fetch.
   */
  @SuppressLint("CheckResult")
  public void fetchRandomApods(int count) {
    repository.fetchRandomApods(count)
        .subscribe(
            () -> Log.d(TAG, "Random APODs fetched successfully."),
            this::postThrowable // Handle errors.
        );
  }
}