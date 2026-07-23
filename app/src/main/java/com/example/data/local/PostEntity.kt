package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String = "",
    val postType: String = "PHOTO", // "PHOTO", "VIDEO", "GRAFT_GUIDE"
    val category: String = "Grafting", // "Grafting", "Plant Care", "Propagation", "Pest Control", "Succulents", "Video Guides"
    val title: String,
    val content: String,
    val mediaDrawableName: String = "", // Reference to drawable name (e.g. "img_grafting_hero_1784810598121") or custom image URI
    val timestamp: Long = System.currentTimeMillis(),
    val likeCount: Int = 124,
    val commentCount: Int = 18,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val videoDurationSec: Int = 0, // e.g. 45 for 45s video
    val graftingSteps: String = "" // Pipe-separated steps for grafting posts
)
