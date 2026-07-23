package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE category = :category ORDER BY timestamp DESC")
    fun getPostsByCategory(category: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY timestamp DESC")
    fun getPostsByAuthor(authorId: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET isLiked = :isLiked, likeCount = :likeCount WHERE id = :postId")
    suspend fun updateLike(postId: Long, isLiked: Boolean, likeCount: Int)

    @Query("UPDATE posts SET isBookmarked = :isBookmarked WHERE id = :postId")
    suspend fun updateBookmark(postId: Long, isBookmarked: Boolean)

    @Query("UPDATE posts SET commentCount = commentCount + 1 WHERE id = :postId")
    suspend fun incrementCommentCount(postId: Long)

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostCount(): Int
}
