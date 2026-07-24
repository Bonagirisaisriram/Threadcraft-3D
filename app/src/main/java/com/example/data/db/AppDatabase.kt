package com.example.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.InventoryEntity
import com.example.data.model.SavedCostumeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CostumeDao {
    @Query("SELECT * FROM saved_costumes ORDER BY timestamp DESC")
    fun getAllSavedCostumes(): Flow<List<SavedCostumeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCostume(costume: SavedCostumeEntity)

    @Query("DELETE FROM saved_costumes WHERE id = :costumeId")
    suspend fun deleteCostume(costumeId: String)
}

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items")
    fun getAllInventoryItems(): Flow<List<InventoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInventory(items: List<InventoryEntity>)

    @Query("SELECT * FROM inventory_items WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR materialName LIKE '%' || :query || '%'")
    fun searchInventory(query: String): Flow<List<InventoryEntity>>
}

@Database(
    entities = [SavedCostumeEntity::class, InventoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun costumeDao(): CostumeDao
    abstract fun inventoryDao(): InventoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "threadcraft_3d_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
