package com.garrettrogers.pltracker.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot;
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
public final class PortfolioDao_Impl implements PortfolioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PortfolioSnapshot> __insertionAdapterOfPortfolioSnapshot;

  public PortfolioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPortfolioSnapshot = new EntityInsertionAdapter<PortfolioSnapshot>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `portfolio_snapshots` (`id`,`date`,`totalValue`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PortfolioSnapshot entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDate());
        statement.bindString(3, entity.getTotalValue());
      }
    };
  }

  @Override
  public Object insertSnapshot(final PortfolioSnapshot snapshot,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPortfolioSnapshot.insert(snapshot);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PortfolioSnapshot>> getSnapshots() {
    final String _sql = "SELECT * FROM portfolio_snapshots ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"portfolio_snapshots"}, new Callable<List<PortfolioSnapshot>>() {
      @Override
      @NonNull
      public List<PortfolioSnapshot> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTotalValue = CursorUtil.getColumnIndexOrThrow(_cursor, "totalValue");
          final List<PortfolioSnapshot> _result = new ArrayList<PortfolioSnapshot>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PortfolioSnapshot _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final String _tmpTotalValue;
            _tmpTotalValue = _cursor.getString(_cursorIndexOfTotalValue);
            _item = new PortfolioSnapshot(_tmpId,_tmpDate,_tmpTotalValue);
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
