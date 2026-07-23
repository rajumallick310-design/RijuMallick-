package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String = "current_user"): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserOnce(id: String = "current_user"): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("UPDATE users SET followerCount = :count, hasReached10k = :has10k, rewardCouponCode = :coupon WHERE id = :id")
    suspend fun updateFollowerCount(id: String, count: Int, has10k: Boolean, coupon: String)

    @Query("UPDATE users SET displayName = :name, username = :username, bio = :bio, passwordPin = :pin, plantBadges = :badges WHERE id = :id")
    suspend fun updateProfile(id: String, name: String, username: String, bio: String, pin: String, badges: String)

    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE id = :id")
    suspend fun updateLoginState(id: String, isLoggedIn: Boolean)
}
