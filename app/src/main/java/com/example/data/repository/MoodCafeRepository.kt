package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DefaultMenuData
import com.example.data.model.CafeSettingsEntity
import com.example.data.model.CategoryEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MoodCafeRepository(private val database: AppDatabase) {
    private val productDao = database.productDao()
    private val categoryDao = database.categoryDao()
    private val orderDao = database.orderDao()
    private val userDao = database.userDao()
    private val favoriteDao = database.favoriteDao()
    private val settingsDao = database.settingsDao()

    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    val settingsFlow: Flow<CafeSettingsEntity?> = settingsDao.getSettingsFlow()

    suspend fun ensureSeeded() {
        val catCount = categoryDao.getCount()
        if (catCount == 0) {
            categoryDao.insertCategories(DefaultMenuData.initialCategories)
        }
        val count = productDao.getCount()
        if (count == 0) {
            productDao.insertProducts(DefaultMenuData.initialProducts)
        }
        val settings = settingsDao.getSettings()
        if (settings == null) {
            settingsDao.saveSettings(DefaultMenuData.defaultSettings)
        }
        val usersCount = userDao.getCount()
        if (usersCount == 0) {
            for (user in DefaultMenuData.demoUsers) {
                userDao.insertUser(user)
            }
        }
    }

    suspend fun addCategory(category: CategoryEntity): Long {
        return categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(categoryId: Long) {
        categoryDao.deleteCategoryById(categoryId)
    }

    fun getOrdersForUser(email: String): Flow<List<OrderEntity>> {
        return orderDao.getOrdersByEmail(email)
    }

    fun getFavoritesForUser(email: String): Flow<List<Long>> {
        return favoriteDao.getFavoriteProductIds(email)
    }

    suspend fun toggleFavorite(productId: Long, email: String) {
        val isFav = favoriteDao.isFavorite(productId, email)
        if (isFav) {
            favoriteDao.removeFavorite(productId, email)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(productId, email))
        }
    }

    suspend fun createOrder(order: OrderEntity): Long {
        return orderDao.insertOrder(order)
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun addProduct(product: ProductEntity): Long {
        return productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(productId: Long) {
        productDao.deleteProductById(productId)
    }

    suspend fun updateUserRole(userId: Long, role: String) {
        userDao.updateUserRole(userId, role)
    }

    suspend fun registerOrLoginUser(name: String, email: String, phone: String): UserEntity {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            val updated = existing.copy(name = name, phone = phone)
            userDao.updateUser(updated)
            return updated
        } else {
            val isFirst = email.contains("admin", ignoreCase = true) || userDao.getCount() <= 1
            val newUser = UserEntity(
                name = name,
                email = email,
                phone = phone,
                role = if (isFirst) UserRole.ADMIN.name else UserRole.USER.name,
                avatarLetter = name.firstOrNull()?.toString() ?: "م"
            )
            val id = userDao.insertUser(newUser)
            return newUser.copy(id = id)
        }
    }

    suspend fun saveSettings(settings: CafeSettingsEntity) {
        settingsDao.saveSettings(settings)
    }
}
