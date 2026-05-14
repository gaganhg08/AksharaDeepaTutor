package com.example.aksharadeepa.data.dao;

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
import com.example.aksharadeepa.data.entity.Chapter;
import com.example.aksharadeepa.data.entity.Question;
import com.example.aksharadeepa.data.entity.QuizResult;
import com.example.aksharadeepa.data.entity.Subject;
import java.lang.Class;
import java.lang.Exception;
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
public final class TutorDao_Impl implements TutorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Subject> __insertionAdapterOfSubject;

  private final EntityInsertionAdapter<Chapter> __insertionAdapterOfChapter;

  private final EntityInsertionAdapter<Question> __insertionAdapterOfQuestion;

  private final EntityInsertionAdapter<QuizResult> __insertionAdapterOfQuizResult;

  private final EntityDeletionOrUpdateAdapter<Chapter> __updateAdapterOfChapter;

  public TutorDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubject = new EntityInsertionAdapter<Subject>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subjects` (`id`,`name`,`iconResId`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Subject entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getIconResId());
      }
    };
    this.__insertionAdapterOfChapter = new EntityInsertionAdapter<Chapter>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chapters` (`id`,`subjectId`,`title`,`isCompleted`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chapter entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindString(3, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
      }
    };
    this.__insertionAdapterOfQuestion = new EntityInsertionAdapter<Question>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `questions` (`id`,`chapterId`,`text`,`optionA`,`optionB`,`optionC`,`optionD`,`correctOptionIndex`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Question entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getChapterId());
        statement.bindString(3, entity.getText());
        statement.bindString(4, entity.getOptionA());
        statement.bindString(5, entity.getOptionB());
        statement.bindString(6, entity.getOptionC());
        statement.bindString(7, entity.getOptionD());
        statement.bindLong(8, entity.getCorrectOptionIndex());
      }
    };
    this.__insertionAdapterOfQuizResult = new EntityInsertionAdapter<QuizResult>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `quiz_results` (`id`,`subjectId`,`chapterId`,`score`,`totalQuestions`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuizResult entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindLong(3, entity.getChapterId());
        statement.bindLong(4, entity.getScore());
        statement.bindLong(5, entity.getTotalQuestions());
        statement.bindLong(6, entity.getTimestamp());
      }
    };
    this.__updateAdapterOfChapter = new EntityDeletionOrUpdateAdapter<Chapter>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chapters` SET `id` = ?,`subjectId` = ?,`title` = ?,`isCompleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chapter entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindString(3, entity.getTitle());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insertSubjects(final List<Subject> subjects,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSubject.insert(subjects);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertChapters(final List<Chapter> chapters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChapter.insert(chapters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertQuestions(final List<Question> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestion.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertQuizResult(final QuizResult result,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuizResult.insert(result);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChapter(final Chapter chapter, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChapter.handle(chapter);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Subject>> getAllSubjects() {
    final String _sql = "SELECT * FROM subjects";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subjects"}, new Callable<List<Subject>>() {
      @Override
      @NonNull
      public List<Subject> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIconResId = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResId");
          final List<Subject> _result = new ArrayList<Subject>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Subject _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpIconResId;
            _tmpIconResId = _cursor.getInt(_cursorIndexOfIconResId);
            _item = new Subject(_tmpId,_tmpName,_tmpIconResId);
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
  public Flow<List<Chapter>> getChaptersForSubject(final int subjectId) {
    final String _sql = "SELECT * FROM chapters WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<Chapter>>() {
      @Override
      @NonNull
      public List<Chapter> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final List<Chapter> _result = new ArrayList<Chapter>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Chapter _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            _item = new Chapter(_tmpId,_tmpSubjectId,_tmpTitle,_tmpIsCompleted);
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
  public Flow<List<Chapter>> getAllChapters() {
    final String _sql = "SELECT * FROM chapters";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"chapters"}, new Callable<List<Chapter>>() {
      @Override
      @NonNull
      public List<Chapter> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final List<Chapter> _result = new ArrayList<Chapter>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Chapter _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            _item = new Chapter(_tmpId,_tmpSubjectId,_tmpTitle,_tmpIsCompleted);
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
  public Object getQuestionsForChapter(final int chapterId,
      final Continuation<? super List<Question>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE chapterId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectOptionIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctOptionIndex");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final String _tmpOptionA;
            _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            final String _tmpOptionB;
            _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            final String _tmpOptionC;
            _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            final String _tmpOptionD;
            _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            final int _tmpCorrectOptionIndex;
            _tmpCorrectOptionIndex = _cursor.getInt(_cursorIndexOfCorrectOptionIndex);
            _item = new Question(_tmpId,_tmpChapterId,_tmpText,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpCorrectOptionIndex);
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

  @Override
  public Flow<List<QuizResult>> getQuizResultsForSubject(final int subjectId) {
    final String _sql = "SELECT * FROM quiz_results WHERE subjectId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_results"}, new Callable<List<QuizResult>>() {
      @Override
      @NonNull
      public List<QuizResult> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<QuizResult> _result = new ArrayList<QuizResult>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizResult _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new QuizResult(_tmpId,_tmpSubjectId,_tmpChapterId,_tmpScore,_tmpTotalQuestions,_tmpTimestamp);
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
  public Flow<List<QuizResult>> getAllQuizResults() {
    final String _sql = "SELECT * FROM quiz_results";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quiz_results"}, new Callable<List<QuizResult>>() {
      @Override
      @NonNull
      public List<QuizResult> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<QuizResult> _result = new ArrayList<QuizResult>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizResult _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
            final int _tmpChapterId;
            _tmpChapterId = _cursor.getInt(_cursorIndexOfChapterId);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new QuizResult(_tmpId,_tmpSubjectId,_tmpChapterId,_tmpScore,_tmpTotalQuestions,_tmpTimestamp);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
