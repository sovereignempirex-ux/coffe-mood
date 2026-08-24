package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CafeSettingsEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        OrderEntity::class,
        UserEntity::class,
        FavoriteEntity::class,
        CafeSettingsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mood_cafe_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            scope.launch(Dispatchers.IO) {
                                database.categoryDao().insertCategories(DefaultMenuData.initialCategories)
                                database.productDao().insertProducts(DefaultMenuData.initialProducts)
                                database.settingsDao().saveSettings(DefaultMenuData.defaultSettings)
                                for (user in DefaultMenuData.demoUsers) {
                                    database.userDao().insertUser(user)
                                }
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
