package com.aksharadeepa.tutor.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.aksharadeepa.tutor.data.local.dao.ChapterDao;
import com.aksharadeepa.tutor.data.local.dao.ChapterDao_Impl;
import com.aksharadeepa.tutor.data.local.dao.DailyGoalDao;
import com.aksharadeepa.tutor.data.local.dao.DailyGoalDao_Impl;
import com.aksharadeepa.tutor.data.local.dao.ProfileDao;
import com.aksharadeepa.tutor.data.local.dao.ProfileDao_Impl;
import com.aksharadeepa.tutor.data.local.dao.QuestionDao;
import com.aksharadeepa.tutor.data.local.dao.QuestionDao_Impl;
import com.aksharadeepa.tutor.data.local.dao.QuizResultDao;
import com.aksharadeepa.tutor.data.local.dao.QuizResultDao_Impl;
import com.aksharadeepa.tutor.data.local.dao.SubSubjectDao;
import com.aksharadeepa.tutor.data.local.dao.SubSubjectDao_Impl;
import com.aksharadeepa.tutor.data.local.dao.SubjectDao;
import com.aksharadeepa.tutor.data.local.dao.SubjectDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SubjectDao _subjectDao;

  private volatile SubSubjectDao _subSubjectDao;

  private volatile ChapterDao _chapterDao;

  private volatile QuestionDao _questionDao;

  private volatile QuizResultDao _quizResultDao;

  private volatile DailyGoalDao _dailyGoalDao;

  private volatile ProfileDao _profileDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `subjects` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `progressPercentage` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sub_subjects` (`id` INTEGER NOT NULL, `subjectId` INTEGER NOT NULL, `name` TEXT NOT NULL, `progressPercentage` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sub_subjects_subjectId` ON `sub_subjects` (`subjectId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chapters` (`id` INTEGER NOT NULL, `subjectId` INTEGER NOT NULL, `subSubjectId` INTEGER NOT NULL, `title` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `readProgress` REAL NOT NULL, `completionTimestamp` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`subSubjectId`) REFERENCES `sub_subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chapters_subjectId` ON `chapters` (`subjectId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_chapters_subSubjectId` ON `chapters` (`subSubjectId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `questions` (`id` INTEGER NOT NULL, `subjectId` INTEGER NOT NULL, `chapterId` INTEGER NOT NULL, `text` TEXT NOT NULL, `optionA` TEXT NOT NULL, `optionB` TEXT NOT NULL, `optionC` TEXT NOT NULL, `optionD` TEXT NOT NULL, `correctAnswer` TEXT NOT NULL, `explanation` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_subjectId` ON `questions` (`subjectId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_questions_chapterId` ON `questions` (`chapterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subjectId` INTEGER NOT NULL, `chapterId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `totalQuestions` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, FOREIGN KEY(`subjectId`) REFERENCES `subjects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`chapterId`) REFERENCES `chapters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quiz_results_subjectId` ON `quiz_results` (`subjectId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_quiz_results_chapterId` ON `quiz_results` (`chapterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_goals` (`dateStr` TEXT NOT NULL, `completedQuizzes` INTEGER NOT NULL, `chaptersRead` INTEGER NOT NULL, `targetQuizzes` INTEGER NOT NULL, `targetChapters` INTEGER NOT NULL, PRIMARY KEY(`dateStr`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `student_profile` (`id` INTEGER NOT NULL, `studentName` TEXT NOT NULL, `schoolName` TEXT NOT NULL, `medium` TEXT NOT NULL, `targetPercentage` INTEGER NOT NULL, `preferredSubjectId` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'de3db7acec10b17e5500a7821e606f41')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `subjects`");
        db.execSQL("DROP TABLE IF EXISTS `sub_subjects`");
        db.execSQL("DROP TABLE IF EXISTS `chapters`");
        db.execSQL("DROP TABLE IF EXISTS `questions`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_results`");
        db.execSQL("DROP TABLE IF EXISTS `daily_goals`");
        db.execSQL("DROP TABLE IF EXISTS `student_profile`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSubjects = new HashMap<String, TableInfo.Column>(4);
        _columnsSubjects.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("progressPercentage", new TableInfo.Column("progressPercentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubjects = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSubjects = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSubjects = new TableInfo("subjects", _columnsSubjects, _foreignKeysSubjects, _indicesSubjects);
        final TableInfo _existingSubjects = TableInfo.read(db, "subjects");
        if (!_infoSubjects.equals(_existingSubjects)) {
          return new RoomOpenHelper.ValidationResult(false, "subjects(com.aksharadeepa.tutor.data.local.entity.SubjectEntity).\n"
                  + " Expected:\n" + _infoSubjects + "\n"
                  + " Found:\n" + _existingSubjects);
        }
        final HashMap<String, TableInfo.Column> _columnsSubSubjects = new HashMap<String, TableInfo.Column>(4);
        _columnsSubSubjects.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubSubjects.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubSubjects.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubSubjects.put("progressPercentage", new TableInfo.Column("progressPercentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubSubjects = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSubSubjects.add(new TableInfo.ForeignKey("subjects", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSubSubjects = new HashSet<TableInfo.Index>(1);
        _indicesSubSubjects.add(new TableInfo.Index("index_sub_subjects_subjectId", false, Arrays.asList("subjectId"), Arrays.asList("ASC")));
        final TableInfo _infoSubSubjects = new TableInfo("sub_subjects", _columnsSubSubjects, _foreignKeysSubSubjects, _indicesSubSubjects);
        final TableInfo _existingSubSubjects = TableInfo.read(db, "sub_subjects");
        if (!_infoSubSubjects.equals(_existingSubSubjects)) {
          return new RoomOpenHelper.ValidationResult(false, "sub_subjects(com.aksharadeepa.tutor.data.local.entity.SubSubjectEntity).\n"
                  + " Expected:\n" + _infoSubSubjects + "\n"
                  + " Found:\n" + _existingSubSubjects);
        }
        final HashMap<String, TableInfo.Column> _columnsChapters = new HashMap<String, TableInfo.Column>(7);
        _columnsChapters.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("subSubjectId", new TableInfo.Column("subSubjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("readProgress", new TableInfo.Column("readProgress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("completionTimestamp", new TableInfo.Column("completionTimestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChapters = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysChapters.add(new TableInfo.ForeignKey("subjects", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        _foreignKeysChapters.add(new TableInfo.ForeignKey("sub_subjects", "CASCADE", "NO ACTION", Arrays.asList("subSubjectId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesChapters = new HashSet<TableInfo.Index>(2);
        _indicesChapters.add(new TableInfo.Index("index_chapters_subjectId", false, Arrays.asList("subjectId"), Arrays.asList("ASC")));
        _indicesChapters.add(new TableInfo.Index("index_chapters_subSubjectId", false, Arrays.asList("subSubjectId"), Arrays.asList("ASC")));
        final TableInfo _infoChapters = new TableInfo("chapters", _columnsChapters, _foreignKeysChapters, _indicesChapters);
        final TableInfo _existingChapters = TableInfo.read(db, "chapters");
        if (!_infoChapters.equals(_existingChapters)) {
          return new RoomOpenHelper.ValidationResult(false, "chapters(com.aksharadeepa.tutor.data.local.entity.ChapterEntity).\n"
                  + " Expected:\n" + _infoChapters + "\n"
                  + " Found:\n" + _existingChapters);
        }
        final HashMap<String, TableInfo.Column> _columnsQuestions = new HashMap<String, TableInfo.Column>(10);
        _columnsQuestions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("chapterId", new TableInfo.Column("chapterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("optionA", new TableInfo.Column("optionA", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("optionB", new TableInfo.Column("optionB", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("optionC", new TableInfo.Column("optionC", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("optionD", new TableInfo.Column("optionD", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("correctAnswer", new TableInfo.Column("correctAnswer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("explanation", new TableInfo.Column("explanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuestions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysQuestions.add(new TableInfo.ForeignKey("subjects", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        _foreignKeysQuestions.add(new TableInfo.ForeignKey("chapters", "CASCADE", "NO ACTION", Arrays.asList("chapterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesQuestions = new HashSet<TableInfo.Index>(2);
        _indicesQuestions.add(new TableInfo.Index("index_questions_subjectId", false, Arrays.asList("subjectId"), Arrays.asList("ASC")));
        _indicesQuestions.add(new TableInfo.Index("index_questions_chapterId", false, Arrays.asList("chapterId"), Arrays.asList("ASC")));
        final TableInfo _infoQuestions = new TableInfo("questions", _columnsQuestions, _foreignKeysQuestions, _indicesQuestions);
        final TableInfo _existingQuestions = TableInfo.read(db, "questions");
        if (!_infoQuestions.equals(_existingQuestions)) {
          return new RoomOpenHelper.ValidationResult(false, "questions(com.aksharadeepa.tutor.data.local.entity.QuestionEntity).\n"
                  + " Expected:\n" + _infoQuestions + "\n"
                  + " Found:\n" + _existingQuestions);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizResults = new HashMap<String, TableInfo.Column>(6);
        _columnsQuizResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("chapterId", new TableInfo.Column("chapterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("totalQuestions", new TableInfo.Column("totalQuestions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizResults = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysQuizResults.add(new TableInfo.ForeignKey("subjects", "CASCADE", "NO ACTION", Arrays.asList("subjectId"), Arrays.asList("id")));
        _foreignKeysQuizResults.add(new TableInfo.ForeignKey("chapters", "CASCADE", "NO ACTION", Arrays.asList("chapterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesQuizResults = new HashSet<TableInfo.Index>(2);
        _indicesQuizResults.add(new TableInfo.Index("index_quiz_results_subjectId", false, Arrays.asList("subjectId"), Arrays.asList("ASC")));
        _indicesQuizResults.add(new TableInfo.Index("index_quiz_results_chapterId", false, Arrays.asList("chapterId"), Arrays.asList("ASC")));
        final TableInfo _infoQuizResults = new TableInfo("quiz_results", _columnsQuizResults, _foreignKeysQuizResults, _indicesQuizResults);
        final TableInfo _existingQuizResults = TableInfo.read(db, "quiz_results");
        if (!_infoQuizResults.equals(_existingQuizResults)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_results(com.aksharadeepa.tutor.data.local.entity.QuizResultEntity).\n"
                  + " Expected:\n" + _infoQuizResults + "\n"
                  + " Found:\n" + _existingQuizResults);
        }
        final HashMap<String, TableInfo.Column> _columnsDailyGoals = new HashMap<String, TableInfo.Column>(5);
        _columnsDailyGoals.put("dateStr", new TableInfo.Column("dateStr", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyGoals.put("completedQuizzes", new TableInfo.Column("completedQuizzes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyGoals.put("chaptersRead", new TableInfo.Column("chaptersRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyGoals.put("targetQuizzes", new TableInfo.Column("targetQuizzes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyGoals.put("targetChapters", new TableInfo.Column("targetChapters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailyGoals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailyGoals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailyGoals = new TableInfo("daily_goals", _columnsDailyGoals, _foreignKeysDailyGoals, _indicesDailyGoals);
        final TableInfo _existingDailyGoals = TableInfo.read(db, "daily_goals");
        if (!_infoDailyGoals.equals(_existingDailyGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_goals(com.aksharadeepa.tutor.data.local.entity.DailyGoalEntity).\n"
                  + " Expected:\n" + _infoDailyGoals + "\n"
                  + " Found:\n" + _existingDailyGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsStudentProfile = new HashMap<String, TableInfo.Column>(6);
        _columnsStudentProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentProfile.put("studentName", new TableInfo.Column("studentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentProfile.put("schoolName", new TableInfo.Column("schoolName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentProfile.put("medium", new TableInfo.Column("medium", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentProfile.put("targetPercentage", new TableInfo.Column("targetPercentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudentProfile.put("preferredSubjectId", new TableInfo.Column("preferredSubjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudentProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudentProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudentProfile = new TableInfo("student_profile", _columnsStudentProfile, _foreignKeysStudentProfile, _indicesStudentProfile);
        final TableInfo _existingStudentProfile = TableInfo.read(db, "student_profile");
        if (!_infoStudentProfile.equals(_existingStudentProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "student_profile(com.aksharadeepa.tutor.data.local.entity.ProfileEntity).\n"
                  + " Expected:\n" + _infoStudentProfile + "\n"
                  + " Found:\n" + _existingStudentProfile);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "de3db7acec10b17e5500a7821e606f41", "a204c610ad5080ba762d7d846685d18c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "subjects","sub_subjects","chapters","questions","quiz_results","daily_goals","student_profile");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `subjects`");
      _db.execSQL("DELETE FROM `sub_subjects`");
      _db.execSQL("DELETE FROM `chapters`");
      _db.execSQL("DELETE FROM `questions`");
      _db.execSQL("DELETE FROM `quiz_results`");
      _db.execSQL("DELETE FROM `daily_goals`");
      _db.execSQL("DELETE FROM `student_profile`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SubjectDao.class, SubjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubSubjectDao.class, SubSubjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChapterDao.class, ChapterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuestionDao.class, QuestionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuizResultDao.class, QuizResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DailyGoalDao.class, DailyGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SubjectDao subjectDao() {
    if (_subjectDao != null) {
      return _subjectDao;
    } else {
      synchronized(this) {
        if(_subjectDao == null) {
          _subjectDao = new SubjectDao_Impl(this);
        }
        return _subjectDao;
      }
    }
  }

  @Override
  public SubSubjectDao subSubjectDao() {
    if (_subSubjectDao != null) {
      return _subSubjectDao;
    } else {
      synchronized(this) {
        if(_subSubjectDao == null) {
          _subSubjectDao = new SubSubjectDao_Impl(this);
        }
        return _subSubjectDao;
      }
    }
  }

  @Override
  public ChapterDao chapterDao() {
    if (_chapterDao != null) {
      return _chapterDao;
    } else {
      synchronized(this) {
        if(_chapterDao == null) {
          _chapterDao = new ChapterDao_Impl(this);
        }
        return _chapterDao;
      }
    }
  }

  @Override
  public QuestionDao questionDao() {
    if (_questionDao != null) {
      return _questionDao;
    } else {
      synchronized(this) {
        if(_questionDao == null) {
          _questionDao = new QuestionDao_Impl(this);
        }
        return _questionDao;
      }
    }
  }

  @Override
  public QuizResultDao quizResultDao() {
    if (_quizResultDao != null) {
      return _quizResultDao;
    } else {
      synchronized(this) {
        if(_quizResultDao == null) {
          _quizResultDao = new QuizResultDao_Impl(this);
        }
        return _quizResultDao;
      }
    }
  }

  @Override
  public DailyGoalDao dailyGoalDao() {
    if (_dailyGoalDao != null) {
      return _dailyGoalDao;
    } else {
      synchronized(this) {
        if(_dailyGoalDao == null) {
          _dailyGoalDao = new DailyGoalDao_Impl(this);
        }
        return _dailyGoalDao;
      }
    }
  }

  @Override
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }
}
