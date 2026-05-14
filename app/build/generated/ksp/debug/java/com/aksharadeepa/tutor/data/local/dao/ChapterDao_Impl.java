package com.aksharadeepa.tutor.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aksharadeepa.tutor.data.local.entity.ChapterEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ChapterDao_Impl implements ChapterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChapterEntity> __insertionAdapterOfChapterEntity;

  private final EntityDeletionOrUpdateAdapter<ChapterEntity> __updateAdapterOfChapterEntity;

  public ChapterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChapterEntity = new EntityInsertionAdapter<ChapterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chapters` (`id`,`subjectId`,`subSubjectId`,`title`,`isCompleted`,`readProgress`,`completionTimestamp`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindLong(3, entity.getSubSubjectId());
        statement.bindString(4, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindDouble(6, entity.getReadProgress());
        if (entity.getCompletionTimestamp() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCompletionTimestamp());
        }
      }
    };
    this.__updateAdapterOfChapterEntity = new EntityDeletionOrUpdateAdapter<ChapterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chapters` SET `id` = ?,`subjectId` = ?,`subSubjectId` = ?,`title` = ?,`isCompleted` = ?,`readProgress` = ?,`completionTimestamp` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChapterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindLong(3, entity.getSubSubjectId());
        statement.bindString(4, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindDouble(6, entity.getReadProgress());
        if (entity.getCompletionTimestamp() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCompletionTimestamp());
        }
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertChapters(final List<ChapterEntity> chapters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChapterEntity.insert(chapters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChapter(final ChapterEntity chapter,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChapterEntity.handle(chapter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ChapterEntity>> getAllChapters() {
    final String _sql = "SELECT * FROM chapters";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfSubSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subSubjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfReadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "readProgress");
          final int _cursorIndexOfCompletionTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "completionTimestamp");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final int _tmpSubSubjectId;
            _tmpSubSubjectId = _cursor.getInt(_cursorIndexOfSubSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final float _tmpReadProgress;
            _tmpReadProgress = _cursor.getFloat(_cursorIndexOfReadProgress);
            final Long _tmpCompletionTimestamp;
            if (_cursor.isNull(_cursorIndexOfCompletionTimestamp)) {
              _tmpCompletionTimestamp = null;
            } else {
              _tmpCompletionTimestamp = _cursor.getLong(_cursorIndexOfCompletionTimestamp);
            }
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpSubSubjectId,_tmpTitle,_tmpIsCompleted,_tmpReadProgress,_tmpCompletionTimestamp);
            _result.add(_item);
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

  @Override
  public Flow<List<ChapterEntity>> getChaptersBySubject(final int subjectId) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfSubSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subSubjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfReadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "readProgress");
          final int _cursorIndexOfCompletionTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "completionTimestamp");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final int _tmpSubSubjectId;
            _tmpSubSubjectId = _cursor.getInt(_cursorIndexOfSubSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final float _tmpReadProgress;
            _tmpReadProgress = _cursor.getFloat(_cursorIndexOfReadProgress);
            final Long _tmpCompletionTimestamp;
            if (_cursor.isNull(_cursorIndexOfCompletionTimestamp)) {
              _tmpCompletionTimestamp = null;
            } else {
              _tmpCompletionTimestamp = _cursor.getLong(_cursorIndexOfCompletionTimestamp);
            }
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpSubSubjectId,_tmpTitle,_tmpIsCompleted,_tmpReadProgress,_tmpCompletionTimestamp);
            _result.add(_item);
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

  @Override
  public Object getChaptersListBySubject(final int subjectId,
      final Continuation<? super List<ChapterEntity>> $completion) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ChapterEntity>>() {
      @Override
      @NonNull
      public List<ChapterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfSubSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subSubjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfReadProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "readProgress");
          final int _cursorIndexOfCompletionTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "completionTimestamp");
          final List<ChapterEntity> _result = new ArrayList<ChapterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ChapterEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final int _tmpSubSubjectId;
            _tmpSubSubjectId = _cursor.getInt(_cursorIndexOfSubSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final float _tmpReadProgress;
            _tmpReadProgress = _cursor.getFloat(_cursorIndexOfReadProgress);
            final Long _tmpCompletionTimestamp;
            if (_cursor.isNull(_cursorIndexOfCompletionTimestamp)) {
              _tmpCompletionTimestamp = null;
            } else {
              _tmpCompletionTimestamp = _cursor.getLong(_cursorIndexOfCompletionTimestamp);
            }
            _item = new ChapterEntity(_tmpId,_tmpSubjectId,_tmpSubSubjectId,_tmpTitle,_tmpIsCompleted,_tmpReadProgress,_tmpCompletionTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
