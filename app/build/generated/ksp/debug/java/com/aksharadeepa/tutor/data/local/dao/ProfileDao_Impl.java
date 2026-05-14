package com.aksharadeepa.tutor.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aksharadeepa.tutor.data.local.entity.ProfileEntity;
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
public final class ProfileDao_Impl implements ProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProfileEntity> __insertionAdapterOfProfileEntity;

  public ProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProfileEntity = new EntityInsertionAdapter<ProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `student_profile` (`id`,`studentName`,`schoolName`,`medium`,`targetPercentage`,`preferredSubjectId`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProfileEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getStudentName());
        statement.bindString(3, entity.getSchoolName());
        statement.bindString(4, entity.getMedium());
        statement.bindLong(5, entity.getTargetPercentage());
        statement.bindLong(6, entity.getPreferredSubjectId());
      }
    };
  }

  @Override
  public Object insertProfile(final ProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ProfileEntity> getProfile() {
    final String _sql = "SELECT * FROM student_profile WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"student_profile"}, new Callable<ProfileEntity>() {
      @Override
      @Nullable
      public ProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfSchoolName = CursorUtil.getColumnIndexOrThrow(_cursor, "schoolName");
          final int _cursorIndexOfMedium = CursorUtil.getColumnIndexOrThrow(_cursor, "medium");
          final int _cursorIndexOfTargetPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "targetPercentage");
          final int _cursorIndexOfPreferredSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredSubjectId");
          final ProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpSchoolName;
            _tmpSchoolName = _cursor.getString(_cursorIndexOfSchoolName);
            final String _tmpMedium;
            _tmpMedium = _cursor.getString(_cursorIndexOfMedium);
            final int _tmpTargetPercentage;
            _tmpTargetPercentage = _cursor.getInt(_cursorIndexOfTargetPercentage);
            final int _tmpPreferredSubjectId;
            _tmpPreferredSubjectId = _cursor.getInt(_cursorIndexOfPreferredSubjectId);
            _result = new ProfileEntity(_tmpId,_tmpStudentName,_tmpSchoolName,_tmpMedium,_tmpTargetPercentage,_tmpPreferredSubjectId);
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
