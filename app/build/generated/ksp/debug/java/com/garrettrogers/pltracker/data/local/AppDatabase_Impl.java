package com.garrettrogers.pltracker.data.local;

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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TradeDao _tradeDao;

  private volatile PortfolioDao _portfolioDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `trades` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ticker` TEXT NOT NULL, `entryDate` INTEGER NOT NULL, `expirationDate` INTEGER NOT NULL, `strikePrice` TEXT NOT NULL, `entryOptionPrice` TEXT NOT NULL, `entryStockPrice` TEXT NOT NULL, `quantity` INTEGER NOT NULL, `exitDate` INTEGER, `exitOptionPrice` TEXT, `exitStockPrice` TEXT, `optionType` TEXT NOT NULL, `isClosed` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `portfolio_snapshots` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `totalValue` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `account_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `amount` TEXT NOT NULL, `type` TEXT NOT NULL, `note` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0c96cf96ea2d3cc977cf84efb6fe494a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `trades`");
        db.execSQL("DROP TABLE IF EXISTS `portfolio_snapshots`");
        db.execSQL("DROP TABLE IF EXISTS `account_transactions`");
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
        final HashMap<String, TableInfo.Column> _columnsTrades = new HashMap<String, TableInfo.Column>(13);
        _columnsTrades.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("ticker", new TableInfo.Column("ticker", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("entryDate", new TableInfo.Column("entryDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("expirationDate", new TableInfo.Column("expirationDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("strikePrice", new TableInfo.Column("strikePrice", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("entryOptionPrice", new TableInfo.Column("entryOptionPrice", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("entryStockPrice", new TableInfo.Column("entryStockPrice", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("exitDate", new TableInfo.Column("exitDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("exitOptionPrice", new TableInfo.Column("exitOptionPrice", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("exitStockPrice", new TableInfo.Column("exitStockPrice", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("optionType", new TableInfo.Column("optionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrades.put("isClosed", new TableInfo.Column("isClosed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrades = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrades = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrades = new TableInfo("trades", _columnsTrades, _foreignKeysTrades, _indicesTrades);
        final TableInfo _existingTrades = TableInfo.read(db, "trades");
        if (!_infoTrades.equals(_existingTrades)) {
          return new RoomOpenHelper.ValidationResult(false, "trades(com.garrettrogers.pltracker.data.model.Trade).\n"
                  + " Expected:\n" + _infoTrades + "\n"
                  + " Found:\n" + _existingTrades);
        }
        final HashMap<String, TableInfo.Column> _columnsPortfolioSnapshots = new HashMap<String, TableInfo.Column>(3);
        _columnsPortfolioSnapshots.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPortfolioSnapshots.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPortfolioSnapshots.put("totalValue", new TableInfo.Column("totalValue", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPortfolioSnapshots = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPortfolioSnapshots = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPortfolioSnapshots = new TableInfo("portfolio_snapshots", _columnsPortfolioSnapshots, _foreignKeysPortfolioSnapshots, _indicesPortfolioSnapshots);
        final TableInfo _existingPortfolioSnapshots = TableInfo.read(db, "portfolio_snapshots");
        if (!_infoPortfolioSnapshots.equals(_existingPortfolioSnapshots)) {
          return new RoomOpenHelper.ValidationResult(false, "portfolio_snapshots(com.garrettrogers.pltracker.data.model.PortfolioSnapshot).\n"
                  + " Expected:\n" + _infoPortfolioSnapshots + "\n"
                  + " Found:\n" + _existingPortfolioSnapshots);
        }
        final HashMap<String, TableInfo.Column> _columnsAccountTransactions = new HashMap<String, TableInfo.Column>(5);
        _columnsAccountTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountTransactions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountTransactions.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAccountTransactions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAccountTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAccountTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAccountTransactions = new TableInfo("account_transactions", _columnsAccountTransactions, _foreignKeysAccountTransactions, _indicesAccountTransactions);
        final TableInfo _existingAccountTransactions = TableInfo.read(db, "account_transactions");
        if (!_infoAccountTransactions.equals(_existingAccountTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "account_transactions(com.garrettrogers.pltracker.data.model.AccountTransaction).\n"
                  + " Expected:\n" + _infoAccountTransactions + "\n"
                  + " Found:\n" + _existingAccountTransactions);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0c96cf96ea2d3cc977cf84efb6fe494a", "d9ed8f5acde21ca0bc26e6cded5443b2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "trades","portfolio_snapshots","account_transactions");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `trades`");
      _db.execSQL("DELETE FROM `portfolio_snapshots`");
      _db.execSQL("DELETE FROM `account_transactions`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
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
    _typeConvertersMap.put(TradeDao.class, TradeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PortfolioDao.class, PortfolioDao_Impl.getRequiredConverters());
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
  public TradeDao tradeDao() {
    if (_tradeDao != null) {
      return _tradeDao;
    } else {
      synchronized(this) {
        if(_tradeDao == null) {
          _tradeDao = new TradeDao_Impl(this);
        }
        return _tradeDao;
      }
    }
  }

  @Override
  public PortfolioDao portfolioDao() {
    if (_portfolioDao != null) {
      return _portfolioDao;
    } else {
      synchronized(this) {
        if(_portfolioDao == null) {
          _portfolioDao = new PortfolioDao_Impl(this);
        }
        return _portfolioDao;
      }
    }
  }
}
