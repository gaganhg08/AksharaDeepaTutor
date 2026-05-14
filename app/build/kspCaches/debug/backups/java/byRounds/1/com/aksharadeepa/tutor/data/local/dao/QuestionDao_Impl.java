package com.aksharadeepa.tutor.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.aksharadeepa.tutor.data.local.entity.QuestionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class QuestionDao_Impl implements QuestionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuestionEntity> __insertionAdapterOfQuestionEntity;

  public QuestionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuestionEntity = new EntityInsertionAdapter<QuestionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `questions` (`id`,`subjectId`,`chapterId`,`text`,`optionA`,`optionB`,`optionC`,`optionD`,`correctAnswer`,`explanation`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final QuestionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSubjectId());
        statement.bindLong(3, entity.getChapterId());
        statement.bindString(4, entity.getText());
        statement.bindString(5, entity.getOptionA());
        statement.bindString(6, entity.getOptionB());
        statement.bindString(7, entity.getOptionC());
        statement.bindString(8, entity.getOptionD());
        statement.bindString(9, entity.getCorrectAnswer());
        statement.bindString(10, entity.getExplanation());
      }
    };
  }

  @Override
  public Object insertQuestions(final List<QuestionEntity> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestionEntity.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomQuestionsForSubject(final int subjectId, final int limit,
      final Continuation<? super List<QuestionEntity>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE subjectId = ? ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subjectId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QuestionEntity>>() {
      @Override
      @NonNull
      public List<QuestionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final List<QuestionEntity> _result = new ArrayList<QuestionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuestionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
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
            final String _tmpCorrectAnswer;
            _tmpCorrectAnswer = _cursor.getString(_cursorIndexOfCorrectAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            _item = new QuestionEntity(_tmpId,_tmpSubjectId,_tmpChapterId,_tmpText,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpCorrectAnswer,_tmpExplanation);
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
  public Object getRandomQuestionsForChapter(final int chapterId, final int limit,
      final Continuation<? super List<QuestionEntity>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE chapterId = ? ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QuestionEntity>>() {
      @Override
      @NonNull
      public List<QuestionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final List<QuestionEntity> _result = new ArrayList<QuestionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuestionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
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
            final String _tmpCorrectAnswer;
            _tmpCorrectAnswer = _cursor.getString(_cursorIndexOfCorrectAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            _item = new QuestionEntity(_tmpId,_tmpSubjectId,_tmpChapterId,_tmpText,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpCorrectAnswer,_tmpExplanation);
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
  public Object getQuestionsExcluding(final int chapterId, final List<Integer> excludeIds,
      final int limit, final Continuation<? super List<QuestionEntity>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM questions WHERE chapterId = ");
    _stringBuilder.append("?");
    _stringBuilder.append(" AND id NOT IN (");
    final int _inputSize = excludeIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY RANDOM() LIMIT ");
    _stringBuilder.append("?");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 2 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, chapterId);
    _argIndex = 2;
    for (int _item : excludeIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 2 + _inputSize;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<QuestionEntity>>() {
      @Override
      @NonNull
      public List<QuestionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "subjectId");
          final int _cursorIndexOfChapterId = CursorUtil.getColumnIndexOrThrow(_cursor, "chapterId");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final List<QuestionEntity> _result = new ArrayList<QuestionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuestionEntity _item_1;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSubjectId;
            _tmpSubjectId = _cursor.getInt(_cursorIndexOfSubjectId);
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
            final String _tmpCorrectAnswer;
            _tmpCorrectAnswer = _cursor.getString(_cursorIndexOfCorrectAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            _item_1 = new QuestionEntity(_tmpId,_tmpSubjectId,_tmpChapterId,_tmpText,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpCorrectAnswer,_tmpExplanation);
            _result.add(_item_1);
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
