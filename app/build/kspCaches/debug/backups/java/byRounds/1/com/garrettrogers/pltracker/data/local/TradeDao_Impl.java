package com.garrettrogers.pltracker.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
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
import com.garrettrogers.pltracker.data.model.Trade;
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
public final class TradeDao_Impl implements TradeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Trade> __insertionAdapterOfTrade;

  private final EntityDeletionOrUpdateAdapter<Trade> __deletionAdapterOfTrade;

  private final EntityDeletionOrUpdateAdapter<Trade> __updateAdapterOfTrade;

  public TradeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrade = new EntityInsertionAdapter<Trade>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trades` (`id`,`ticker`,`entryDate`,`expirationDate`,`strikePrice`,`entryOptionPrice`,`entryStockPrice`,`quantity`,`exitDate`,`exitOptionPrice`,`exitStockPrice`,`isClosed`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Trade entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTicker());
        statement.bindLong(3, entity.getEntryDate());
        statement.bindLong(4, entity.getExpirationDate());
        statement.bindString(5, entity.getStrikePrice());
        statement.bindString(6, entity.getEntryOptionPrice());
        statement.bindString(7, entity.getEntryStockPrice());
        statement.bindLong(8, entity.getQuantity());
        if (entity.getExitDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getExitDate());
        }
        if (entity.getExitOptionPrice() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getExitOptionPrice());
        }
        if (entity.getExitStockPrice() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getExitStockPrice());
        }
        final int _tmp = entity.isClosed() ? 1 : 0;
        statement.bindLong(12, _tmp);
      }
    };
    this.__deletionAdapterOfTrade = new EntityDeletionOrUpdateAdapter<Trade>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `trades` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Trade entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTrade = new EntityDeletionOrUpdateAdapter<Trade>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `trades` SET `id` = ?,`ticker` = ?,`entryDate` = ?,`expirationDate` = ?,`strikePrice` = ?,`entryOptionPrice` = ?,`entryStockPrice` = ?,`quantity` = ?,`exitDate` = ?,`exitOptionPrice` = ?,`exitStockPrice` = ?,`isClosed` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Trade entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTicker());
        statement.bindLong(3, entity.getEntryDate());
        statement.bindLong(4, entity.getExpirationDate());
        statement.bindString(5, entity.getStrikePrice());
        statement.bindString(6, entity.getEntryOptionPrice());
        statement.bindString(7, entity.getEntryStockPrice());
        statement.bindLong(8, entity.getQuantity());
        if (entity.getExitDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getExitDate());
        }
        if (entity.getExitOptionPrice() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getExitOptionPrice());
        }
        if (entity.getExitStockPrice() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getExitStockPrice());
        }
        final int _tmp = entity.isClosed() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getId());
      }
    };
  }

  @Override
  public Object insertTrade(final Trade trade, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrade.insert(trade);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTrade(final Trade trade, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTrade.handle(trade);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Trade trade, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTrade.handle(trade);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTrade(final Trade trade, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTrade.handle(trade);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Trade>> getActiveTrades() {
    final String _sql = "SELECT * FROM trades WHERE isClosed = 0 ORDER BY entryDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trades"}, new Callable<List<Trade>>() {
      @Override
      @NonNull
      public List<Trade> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTicker = CursorUtil.getColumnIndexOrThrow(_cursor, "ticker");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfStrikePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "strikePrice");
          final int _cursorIndexOfEntryOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryOptionPrice");
          final int _cursorIndexOfEntryStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryStockPrice");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfExitDate = CursorUtil.getColumnIndexOrThrow(_cursor, "exitDate");
          final int _cursorIndexOfExitOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitOptionPrice");
          final int _cursorIndexOfExitStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitStockPrice");
          final int _cursorIndexOfIsClosed = CursorUtil.getColumnIndexOrThrow(_cursor, "isClosed");
          final List<Trade> _result = new ArrayList<Trade>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Trade _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTicker;
            _tmpTicker = _cursor.getString(_cursorIndexOfTicker);
            final long _tmpEntryDate;
            _tmpEntryDate = _cursor.getLong(_cursorIndexOfEntryDate);
            final long _tmpExpirationDate;
            _tmpExpirationDate = _cursor.getLong(_cursorIndexOfExpirationDate);
            final String _tmpStrikePrice;
            _tmpStrikePrice = _cursor.getString(_cursorIndexOfStrikePrice);
            final String _tmpEntryOptionPrice;
            _tmpEntryOptionPrice = _cursor.getString(_cursorIndexOfEntryOptionPrice);
            final String _tmpEntryStockPrice;
            _tmpEntryStockPrice = _cursor.getString(_cursorIndexOfEntryStockPrice);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final Long _tmpExitDate;
            if (_cursor.isNull(_cursorIndexOfExitDate)) {
              _tmpExitDate = null;
            } else {
              _tmpExitDate = _cursor.getLong(_cursorIndexOfExitDate);
            }
            final String _tmpExitOptionPrice;
            if (_cursor.isNull(_cursorIndexOfExitOptionPrice)) {
              _tmpExitOptionPrice = null;
            } else {
              _tmpExitOptionPrice = _cursor.getString(_cursorIndexOfExitOptionPrice);
            }
            final String _tmpExitStockPrice;
            if (_cursor.isNull(_cursorIndexOfExitStockPrice)) {
              _tmpExitStockPrice = null;
            } else {
              _tmpExitStockPrice = _cursor.getString(_cursorIndexOfExitStockPrice);
            }
            final boolean _tmpIsClosed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsClosed);
            _tmpIsClosed = _tmp != 0;
            _item = new Trade(_tmpId,_tmpTicker,_tmpEntryDate,_tmpExpirationDate,_tmpStrikePrice,_tmpEntryOptionPrice,_tmpEntryStockPrice,_tmpQuantity,_tmpExitDate,_tmpExitOptionPrice,_tmpExitStockPrice,_tmpIsClosed);
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
  public Flow<List<Trade>> getClosedTrades() {
    final String _sql = "SELECT * FROM trades WHERE isClosed = 1 ORDER BY exitDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trades"}, new Callable<List<Trade>>() {
      @Override
      @NonNull
      public List<Trade> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTicker = CursorUtil.getColumnIndexOrThrow(_cursor, "ticker");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfStrikePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "strikePrice");
          final int _cursorIndexOfEntryOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryOptionPrice");
          final int _cursorIndexOfEntryStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryStockPrice");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfExitDate = CursorUtil.getColumnIndexOrThrow(_cursor, "exitDate");
          final int _cursorIndexOfExitOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitOptionPrice");
          final int _cursorIndexOfExitStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitStockPrice");
          final int _cursorIndexOfIsClosed = CursorUtil.getColumnIndexOrThrow(_cursor, "isClosed");
          final List<Trade> _result = new ArrayList<Trade>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Trade _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTicker;
            _tmpTicker = _cursor.getString(_cursorIndexOfTicker);
            final long _tmpEntryDate;
            _tmpEntryDate = _cursor.getLong(_cursorIndexOfEntryDate);
            final long _tmpExpirationDate;
            _tmpExpirationDate = _cursor.getLong(_cursorIndexOfExpirationDate);
            final String _tmpStrikePrice;
            _tmpStrikePrice = _cursor.getString(_cursorIndexOfStrikePrice);
            final String _tmpEntryOptionPrice;
            _tmpEntryOptionPrice = _cursor.getString(_cursorIndexOfEntryOptionPrice);
            final String _tmpEntryStockPrice;
            _tmpEntryStockPrice = _cursor.getString(_cursorIndexOfEntryStockPrice);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final Long _tmpExitDate;
            if (_cursor.isNull(_cursorIndexOfExitDate)) {
              _tmpExitDate = null;
            } else {
              _tmpExitDate = _cursor.getLong(_cursorIndexOfExitDate);
            }
            final String _tmpExitOptionPrice;
            if (_cursor.isNull(_cursorIndexOfExitOptionPrice)) {
              _tmpExitOptionPrice = null;
            } else {
              _tmpExitOptionPrice = _cursor.getString(_cursorIndexOfExitOptionPrice);
            }
            final String _tmpExitStockPrice;
            if (_cursor.isNull(_cursorIndexOfExitStockPrice)) {
              _tmpExitStockPrice = null;
            } else {
              _tmpExitStockPrice = _cursor.getString(_cursorIndexOfExitStockPrice);
            }
            final boolean _tmpIsClosed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsClosed);
            _tmpIsClosed = _tmp != 0;
            _item = new Trade(_tmpId,_tmpTicker,_tmpEntryDate,_tmpExpirationDate,_tmpStrikePrice,_tmpEntryOptionPrice,_tmpEntryStockPrice,_tmpQuantity,_tmpExitDate,_tmpExitOptionPrice,_tmpExitStockPrice,_tmpIsClosed);
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
  public Object getTradeById(final long id, final Continuation<? super Trade> $completion) {
    final String _sql = "SELECT * FROM trades WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Trade>() {
      @Override
      @Nullable
      public Trade call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTicker = CursorUtil.getColumnIndexOrThrow(_cursor, "ticker");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfStrikePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "strikePrice");
          final int _cursorIndexOfEntryOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryOptionPrice");
          final int _cursorIndexOfEntryStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryStockPrice");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfExitDate = CursorUtil.getColumnIndexOrThrow(_cursor, "exitDate");
          final int _cursorIndexOfExitOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitOptionPrice");
          final int _cursorIndexOfExitStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitStockPrice");
          final int _cursorIndexOfIsClosed = CursorUtil.getColumnIndexOrThrow(_cursor, "isClosed");
          final Trade _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTicker;
            _tmpTicker = _cursor.getString(_cursorIndexOfTicker);
            final long _tmpEntryDate;
            _tmpEntryDate = _cursor.getLong(_cursorIndexOfEntryDate);
            final long _tmpExpirationDate;
            _tmpExpirationDate = _cursor.getLong(_cursorIndexOfExpirationDate);
            final String _tmpStrikePrice;
            _tmpStrikePrice = _cursor.getString(_cursorIndexOfStrikePrice);
            final String _tmpEntryOptionPrice;
            _tmpEntryOptionPrice = _cursor.getString(_cursorIndexOfEntryOptionPrice);
            final String _tmpEntryStockPrice;
            _tmpEntryStockPrice = _cursor.getString(_cursorIndexOfEntryStockPrice);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final Long _tmpExitDate;
            if (_cursor.isNull(_cursorIndexOfExitDate)) {
              _tmpExitDate = null;
            } else {
              _tmpExitDate = _cursor.getLong(_cursorIndexOfExitDate);
            }
            final String _tmpExitOptionPrice;
            if (_cursor.isNull(_cursorIndexOfExitOptionPrice)) {
              _tmpExitOptionPrice = null;
            } else {
              _tmpExitOptionPrice = _cursor.getString(_cursorIndexOfExitOptionPrice);
            }
            final String _tmpExitStockPrice;
            if (_cursor.isNull(_cursorIndexOfExitStockPrice)) {
              _tmpExitStockPrice = null;
            } else {
              _tmpExitStockPrice = _cursor.getString(_cursorIndexOfExitStockPrice);
            }
            final boolean _tmpIsClosed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsClosed);
            _tmpIsClosed = _tmp != 0;
            _result = new Trade(_tmpId,_tmpTicker,_tmpEntryDate,_tmpExpirationDate,_tmpStrikePrice,_tmpEntryOptionPrice,_tmpEntryStockPrice,_tmpQuantity,_tmpExitDate,_tmpExitOptionPrice,_tmpExitStockPrice,_tmpIsClosed);
          } else {
            _result = null;
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
  public Flow<List<Trade>> getClosedTradesByTicker(final String ticker) {
    final String _sql = "SELECT * FROM trades WHERE ticker = ? AND isClosed = 1 ORDER BY exitDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, ticker);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trades"}, new Callable<List<Trade>>() {
      @Override
      @NonNull
      public List<Trade> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTicker = CursorUtil.getColumnIndexOrThrow(_cursor, "ticker");
          final int _cursorIndexOfEntryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "entryDate");
          final int _cursorIndexOfExpirationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expirationDate");
          final int _cursorIndexOfStrikePrice = CursorUtil.getColumnIndexOrThrow(_cursor, "strikePrice");
          final int _cursorIndexOfEntryOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryOptionPrice");
          final int _cursorIndexOfEntryStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "entryStockPrice");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfExitDate = CursorUtil.getColumnIndexOrThrow(_cursor, "exitDate");
          final int _cursorIndexOfExitOptionPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitOptionPrice");
          final int _cursorIndexOfExitStockPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "exitStockPrice");
          final int _cursorIndexOfIsClosed = CursorUtil.getColumnIndexOrThrow(_cursor, "isClosed");
          final List<Trade> _result = new ArrayList<Trade>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Trade _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTicker;
            _tmpTicker = _cursor.getString(_cursorIndexOfTicker);
            final long _tmpEntryDate;
            _tmpEntryDate = _cursor.getLong(_cursorIndexOfEntryDate);
            final long _tmpExpirationDate;
            _tmpExpirationDate = _cursor.getLong(_cursorIndexOfExpirationDate);
            final String _tmpStrikePrice;
            _tmpStrikePrice = _cursor.getString(_cursorIndexOfStrikePrice);
            final String _tmpEntryOptionPrice;
            _tmpEntryOptionPrice = _cursor.getString(_cursorIndexOfEntryOptionPrice);
            final String _tmpEntryStockPrice;
            _tmpEntryStockPrice = _cursor.getString(_cursorIndexOfEntryStockPrice);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final Long _tmpExitDate;
            if (_cursor.isNull(_cursorIndexOfExitDate)) {
              _tmpExitDate = null;
            } else {
              _tmpExitDate = _cursor.getLong(_cursorIndexOfExitDate);
            }
            final String _tmpExitOptionPrice;
            if (_cursor.isNull(_cursorIndexOfExitOptionPrice)) {
              _tmpExitOptionPrice = null;
            } else {
              _tmpExitOptionPrice = _cursor.getString(_cursorIndexOfExitOptionPrice);
            }
            final String _tmpExitStockPrice;
            if (_cursor.isNull(_cursorIndexOfExitStockPrice)) {
              _tmpExitStockPrice = null;
            } else {
              _tmpExitStockPrice = _cursor.getString(_cursorIndexOfExitStockPrice);
            }
            final boolean _tmpIsClosed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsClosed);
            _tmpIsClosed = _tmp != 0;
            _item = new Trade(_tmpId,_tmpTicker,_tmpEntryDate,_tmpExpirationDate,_tmpStrikePrice,_tmpEntryOptionPrice,_tmpEntryStockPrice,_tmpQuantity,_tmpExitDate,_tmpExitOptionPrice,_tmpExitStockPrice,_tmpIsClosed);
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
