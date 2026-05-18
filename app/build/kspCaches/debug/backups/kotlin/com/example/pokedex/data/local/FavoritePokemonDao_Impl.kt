package com.example.pokedex.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FavoritePokemonDao_Impl(
  __db: RoomDatabase,
) : FavoritePokemonDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFavoritePokemonEntity: EntityInsertAdapter<FavoritePokemonEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFavoritePokemonEntity = object : EntityInsertAdapter<FavoritePokemonEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `favorite_pokemon` (`id`,`name`,`imageUrl`,`types`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FavoritePokemonEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.imageUrl)
        statement.bindText(4, entity.types)
      }
    }
  }

  public override suspend fun insertFavorite(pokemon: FavoritePokemonEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfFavoritePokemonEntity.insert(_connection, pokemon)
  }

  public override fun getAllFavorites(): Flow<List<FavoritePokemonEntity>> {
    val _sql: String = "SELECT * FROM favorite_pokemon ORDER BY id ASC"
    return createFlow(__db, false, arrayOf("favorite_pokemon")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfTypes: Int = getColumnIndexOrThrow(_stmt, "types")
        val _result: MutableList<FavoritePokemonEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: FavoritePokemonEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpTypes: String
          _tmpTypes = _stmt.getText(_columnIndexOfTypes)
          _item = FavoritePokemonEntity(_tmpId,_tmpName,_tmpImageUrl,_tmpTypes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun isFavorite(pokemonId: Int): Flow<Boolean> {
    val _sql: String = "SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = ?)"
    return createFlow(__db, false, arrayOf("favorite_pokemon")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, pokemonId.toLong())
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteFavorite(pokemonId: Int) {
    val _sql: String = "DELETE FROM favorite_pokemon WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, pokemonId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
