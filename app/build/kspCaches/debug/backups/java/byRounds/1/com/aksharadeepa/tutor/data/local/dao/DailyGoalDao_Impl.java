package com.aksharadeepa.tutor.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DailyGoalDao_Impl implements DailyGoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailyGoalEntity> __insertionAdapterOfDailyGoalEntity;

  private final EntityDeletionOrUpdateAdapter<DailyGoalEntity> __updateAdapterOfDailyGoalEntity;

  public DailyGoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailyGoalEntity = new EntityInsertionAdapter<DailyGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_goals` (`dateStr`,`completedQuizzes`,`chaptersRead`,`targetQuizzes`,`targetChapters`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyGoalEntity entity) {
        statement.bindString(1, entity.getDateStr());
        statement.bindLong(2, entity.getCompletedQuizzes());
        statement.bindLong(3, entity.getChaptersRead());
        statement.bindLong(4, entity.getTargetQuizzes());
        statement.bindLong(5, entity.getTargetChapters());
      }
    };
    this.__updateAdapterOfDailyGoalEntity = new EntityDeletionOrUpdateAdapter<DailyGoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `daily_goals` SET `dateStr` = ?,`completedQuizzes` = ?,`chaptersRead` = ?,`targetQuizzes` = ?,`targetChapters` = ? WHERE `dateStr` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyGoalEntity entity) {
        statement.bindString(1, entity.getDateStr());
        statement.bindLong(2, entity.getCompletedQuizzes());
        statement.bindLong(3, entity.getChaptersRead());
        statement.bindLong(4, entity.getTargetQuizzes());
        statement.bindLong(5, entity.getTargetChapters());
        statement.bindString(6, entity.getDateStr());
      }
    };
  }

  @Override
  public Object insertGoal(final DailyGoalEntity goal,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyGoalEntity.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGoal(final DailyGoalEntity goal,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDailyGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<DailyGoalEntity> getGoalByDate(final String dateStr) {
    final String _sql = "SELECT * FROM daily_goals WHERE dateStr = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, dateStr);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_goals"}, new Callable<DailyGoalEntity>() {
      @Override
      @Nullable
      public DailyGoalEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateStr = CursorUtil.getColumnIndexOrThrow(_cursor, "dateStr");
          final int _cursorIndexOfCompletedQuizzes = CursorUtil.getColumnIndexOrThrow(_cursor, "completedQuizzes");
          final int _cursorIndexOfChaptersRead = CursorUtil.getColumnIndexOrThrow(_cursor, "chaptersRead");
          final int _cursorIndexOfTargetQuizzes = CursorUtil.getColumnIndexOrThrow(_cursor, "targetQuizzes");
          final int _cursorIndexOfTargetChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "targetChapters");
          final DailyGoalEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDateStr;
            _tmpDateStr = _cursor.getString(_cursorIndexOfDateStr);
            final int _tmpCompletedQuizzes;
            _tmpCompletedQuizzes = _cursor.getInt(_cursorIndexOfCompletedQuizzes);
            final int _tmpChaptersRead;
            _tmpChaptersRead = _cursor.getInt(_cursorIndexOfChaptersRead);
            final int _tmpTargetQuizzes;
            _tmpTargetQuizzes = _cursor.getInt(_cursorIndexOfTargetQuizzes);
            final int _tmpTargetChapters;
            _tmpTargetChapters = _cursor.getInt(_cursorIndexOfTargetChapters);
            _result = new DailyGoalEntity(_tmpDateStr,_tmpCompletedQuizzes,_tmpChaptersRead,_tmpTargetQuizzes,_tmpTargetChapters);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
