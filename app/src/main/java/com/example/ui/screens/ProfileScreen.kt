package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CouponEntity
import com.example.data.local.PostEntity
import com.example.data.local.UserEntity
import com.example.ui.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    currentUser: UserEntity?,
    userPosts: List<PostEntity>,
    bookmarkedPosts: List<PostEntity>,
    coupons: List<CouponEntity>,
    onAddFollowers: (Int) -> Unit,
    onUpdateProfile: (name: String, username: String, bio: String, pin: String, badges: String) -> Unit,
    onLogout: () -> Unit,
    onNavigateToRewards: () -> Unit
) {
    val context = LocalContext.current
    var activeTab by remember { mutableStateOf("POSTS") } // "POSTS", "BOOKMARKS", "COUPONS"
    var showEditProfileDialog by remember { mutableStateOf(false) }

    val user = currentUser ?: UserEntity()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom User Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showEditProfileDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Profile Banner & Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                ) {
                    Image(
                        painter = painterResource(id = ImageUtils.getDrawableResId(context, "img_garden_banner_1784810613294")),
                        contentDescription = "Profile Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                                )
                            )
                    )
                }
            }

            // Avatar & Main Info Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-40).dp)
                            .size(90.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.displayName.take(1).uppercase(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.offset(y = (-30).dp)
                    ) {
                        Text(
                            text = user.displayName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user.username,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = user.bio,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Security PIN & Auth info chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Outlined.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Password PIN: ${user.passwordPin}  |  ${user.authType}: ${if (user.authType == "PHONE") user.phoneNumber else user.email}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Followers & Following Metrics
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "%,d".format(user.followerCount),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("Followers", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Divider(modifier = Modifier.height(30.dp).width(1.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${user.followingCount}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Following", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Divider(modifier = Modifier.height(30.dp).width(1.dp))

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${userPosts.size}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Posts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 10,000 Follower Milestone Tracker Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFC107)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.EmojiEvents,
                                            contentDescription = null,
                                            tint = Color(0xFFE5A100),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "10,000 Follower Lucky Reward",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF5D4037)
                                        )
                                    }

                                    if (user.hasReached10k) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF4CAF50)
                                        ) {
                                            Text(
                                                text = "UNLOCKED 🎉",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                val progress = (user.followerCount / 10000f).coerceIn(0f, 1f)
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = Color(0xFFE5A100),
                                    trackColor = Color(0xFFFFE082)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = if (user.hasReached10k)
                                        "Lucky Coupon Code: ${user.rewardCouponCode}"
                                    else
                                        "${10000 - user.followerCount} followers needed to generate your 10K Lucky Coupon!",
                                    fontSize = 12.sp,
                                    color = Color(0xFF5D4037),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Simulator Button to add followers & trigger milestone
                                Button(
                                    onClick = {
                                        val add = if (user.followerCount < 10000) (10000 - user.followerCount) else 100
                                        onAddFollowers(add)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5A100)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("boost_followers_button")
                                ) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.Black)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (user.hasReached10k) "Add +100 Followers (Boost)" else "🚀 Reach 10,000 Followers Milestone!",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Profile Tabs (Posts / Bookmarks / Coupons)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(4.dp)
                        ) {
                            listOf(
                                Triple("POSTS", "My Posts", Icons.Default.GridOn),
                                Triple("BOOKMARKS", "Bookmarks", Icons.Outlined.BookmarkBorder),
                                Triple("COUPONS", "Coupons (${coupons.size})", Icons.Default.ConfirmationNumber)
                            ).forEach { (tab, label, icon) ->
                                val isSelected = activeTab == tab
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                        .clickable { activeTab = tab }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tab Content
            when (activeTab) {
                "POSTS" -> {
                    items(userPosts) { post ->
                        PostCard(
                            post = post,
                            onToggleLike = {},
                            onToggleBookmark = {},
                            onOpenComments = {},
                            onOpenDetail = {}
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                "BOOKMARKS" -> {
                    if (bookmarkedPosts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No bookmarked posts yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        items(bookmarkedPosts) { post ->
                            PostCard(
                                post = post,
                                onToggleLike = {},
                                onToggleBookmark = {},
                                onOpenComments = {},
                                onOpenDetail = {}
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
                "COUPONS" -> {
                    items(coupons) { coupon ->
                        CouponCardItem(
                            coupon = coupon,
                            onRedeem = {}
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        // Edit Profile Dialog
        if (showEditProfileDialog) {
            EditProfileDialog(
                user = user,
                onDismiss = { showEditProfileDialog = false },
                onSave = { name, username, bio, pin, badges ->
                    onUpdateProfile(name, username, bio, pin, badges)
                    showEditProfileDialog = false
                }
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onSave: (name: String, username: String, bio: String, pin: String, badges: String) -> Unit
) {
    var name by remember { mutableStateOf(user.displayName) }
    var username by remember { mutableStateOf(user.username) }
    var bio by remember { mutableStateOf(user.bio) }
    var pin by remember { mutableStateOf(user.passwordPin) }
    var badges by remember { mutableStateOf(user.plantBadges) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, username, bio, pin, badges)
                },
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text("Save Profile", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Custom Profile", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username (@handle)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.height(80.dp)
                )
                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Security PIN / Password") },
                    singleLine = true
                )
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
