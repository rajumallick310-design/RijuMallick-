package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.SabujKathaTheme
import com.example.ui.viewmodel.SabujKathaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SabujKathaTheme {
                SabujKathaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SabujKathaApp(viewModel: SabujKathaViewModel = viewModel()) {
    var selectedScreen by remember { mutableStateOf("FEED") } // "FEED", "GRAFTING", "CREATE", "REWARDS", "PROFILE"

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val bookmarkedPosts by viewModel.bookmarkedPosts.collectAsStateWithLifecycle()
    val coupons by viewModel.coupons.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedPostForComments by viewModel.selectedPostForComments.collectAsStateWithLifecycle()
    val commentsForSelectedPost by viewModel.commentsForSelectedPost.collectAsStateWithLifecycle()
    val selectedPostForDetail by viewModel.selectedPostForDetail.collectAsStateWithLifecycle()
    val unlockedRewardCoupon by viewModel.unlockedRewardCoupon.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    val user = currentUser

    // Authentication Guard
    if (user == null || !user.isLoggedIn) {
        AuthScreen(
            onLoginSuccess = { phoneOrEmail, authType, pin, name ->
                viewModel.loginOrSignUp(phoneOrEmail, authType, pin, name)
            }
        )
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = selectedScreen == "FEED",
                        onClick = { selectedScreen = "FEED" },
                        icon = { Icon(if (selectedScreen == "FEED") Icons.Default.Home else Icons.Outlined.Home, contentDescription = "Feed") },
                        label = { Text("Feed") },
                        modifier = Modifier.testTag("nav_feed")
                    )

                    NavigationBarItem(
                        selected = selectedScreen == "GRAFTING",
                        onClick = { selectedScreen = "GRAFTING" },
                        icon = { Icon(if (selectedScreen == "GRAFTING") Icons.Default.ContentCut else Icons.Outlined.ContentCut, contentDescription = "Grafting") },
                        label = { Text("Grafting") },
                        modifier = Modifier.testTag("nav_grafting")
                    )

                    NavigationBarItem(
                        selected = selectedScreen == "CREATE",
                        onClick = { selectedScreen = "CREATE" },
                        icon = { Icon(if (selectedScreen == "CREATE") Icons.Default.AddCircle else Icons.Outlined.AddCircle, contentDescription = "Create") },
                        label = { Text("Post") },
                        modifier = Modifier.testTag("nav_create")
                    )

                    NavigationBarItem(
                        selected = selectedScreen == "REWARDS",
                        onClick = { selectedScreen = "REWARDS" },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (user.hasReached10k) {
                                        Badge { Text("10K") }
                                    }
                                }
                            ) {
                                Icon(if (selectedScreen == "REWARDS") Icons.Default.CardGiftcard else Icons.Outlined.CardGiftcard, contentDescription = "Rewards")
                            }
                        },
                        label = { Text("10K Rewards") },
                        modifier = Modifier.testTag("nav_rewards")
                    )

                    NavigationBarItem(
                        selected = selectedScreen == "PROFILE",
                        onClick = { selectedScreen = "PROFILE" },
                        icon = { Icon(if (selectedScreen == "PROFILE") Icons.Default.Person else Icons.Outlined.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedScreen) {
                    "FEED" -> FeedScreen(
                        posts = posts,
                        currentUser = user,
                        selectedCategory = selectedCategory,
                        couponCount = coupons.size,
                        onCategorySelected = { category -> viewModel.selectCategory(category) },
                        onToggleLike = { post -> viewModel.toggleLike(post) },
                        onToggleBookmark = { post -> viewModel.toggleBookmark(post) },
                        onOpenComments = { post -> viewModel.openCommentsForPost(post) },
                        onOpenDetail = { post -> viewModel.openPostDetail(post) },
                        onNavigateToCreatePost = { selectedScreen = "CREATE" },
                        onNavigateToRewards = { selectedScreen = "REWARDS" }
                    )
                    "GRAFTING" -> GraftingHubScreen()
                    "CREATE" -> CreatePostScreen(
                        onNavigateBack = { selectedScreen = "FEED" },
                        onSubmitPost = { postType, category, title, content, mediaDrawable, graftingSteps ->
                            viewModel.createPost(postType, category, title, content, mediaDrawable, graftingSteps)
                            selectedScreen = "FEED"
                        }
                    )
                    "REWARDS" -> RewardsScreen(
                        currentUser = user,
                        coupons = coupons,
                        onRedeemCoupon = { code -> viewModel.redeemCoupon(code) },
                        onAddFollowers = { count -> viewModel.addFollowersAndCheckReward(count) }
                    )
                    "PROFILE" -> ProfileScreen(
                        currentUser = user,
                        userPosts = posts.filter { it.authorId == user.id },
                        bookmarkedPosts = bookmarkedPosts,
                        coupons = coupons,
                        onAddFollowers = { count -> viewModel.addFollowersAndCheckReward(count) },
                        onUpdateProfile = { name, username, bio, pin, badges ->
                            viewModel.updateProfile(name, username, bio, pin, badges)
                        },
                        onLogout = { viewModel.logout() },
                        onNavigateToRewards = { selectedScreen = "REWARDS" }
                    )
                }
            }
        }

        // Active Comments Modal Sheet
        selectedPostForComments?.let { post ->
            CommentsSheet(
                post = post,
                comments = commentsForSelectedPost,
                onDismiss = { viewModel.openCommentsForPost(null) },
                onAddComment = { postId, text -> viewModel.addComment(postId, text) }
            )
        }

        // Active Post Detail Dialog
        selectedPostForDetail?.let { post ->
            PostDetailDialog(
                post = post,
                onDismiss = { viewModel.openPostDetail(null) }
            )
        }

        // 10,000 Follower Celebration Reward Modal
        unlockedRewardCoupon?.let { coupon ->
            RewardMilestoneModal(
                coupon = coupon,
                onDismiss = { viewModel.dismissRewardDialog() },
                onNavigateToRewards = {
                    viewModel.dismissRewardDialog()
                    selectedScreen = "REWARDS"
                }
            )
        }
    }
}
