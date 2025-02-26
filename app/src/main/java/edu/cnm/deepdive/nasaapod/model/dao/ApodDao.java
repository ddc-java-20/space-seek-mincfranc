package edu.cnm.deepdive.nasaapod.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import io.reactivex.rxjava3.core.Completable;
import java.time.LocalDate;
import java.util.List;

//@Dao annotation indicates that this interface will define methods for interacting with the database.
@Dao
public interface ApodDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Completable insert(List<Apod> apods);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Completable insert(Apod apod);

  @Query("DELETE FROM apod")
  Completable deleteAll();

  @Query("SELECT * FROM apod WHERE apod_id = :id")
  LiveData<Apod> select(long id);

  @Query("SELECT * FROM apod WHERE date = :date")
  LiveData<Apod> select(LocalDate date);

  @Query("SELECT * FROM apod WHERE date >= :startDate AND date < :endDate ORDER BY date ASC")
  LiveData<List<Apod>> selectRange(LocalDate startDate, LocalDate endDate);

  @Query("SELECT * FROM apod WHERE date >= :startDate ORDER BY date ASC")
  LiveData<List<Apod>> selectOpenRange(LocalDate startDate);

}
