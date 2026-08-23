package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.MockData
import com.example.models.ItemType
import com.example.models.LiveCategory
import com.example.models.LiveChannel
import com.example.models.VodItem
import com.example.ui.components.VideoPlayer
import com.example.ui.theme.RedAccent
import com.example.viewmodel.AppViewModel

@Composable
fun SearchBar(viewModel: AppViewModel, placeholder: String) {
    val query by viewModel.searchQuery.collectAsState()
    
    OutlinedTextField(
        value = query,
        onValueChange = { viewModel.updateSearchQuery(it) },
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
fun HomeScreen(navController: NavController, viewModel: AppViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("Click", "TV")
        SearchBar(viewModel, "ابحث عن فيلم أو مسلسل...")
        
        if (query.isNotEmpty()) {
            val results = viewModel.getSearchResults(query)
            SearchResultsGrid(results, navController)
        } else {
            LazyColumn {
                item {
                    HeroSection(MockData.vodContent.firstOrNull(), navController)
                }
                item {
                    SectionHeader("🎬 أحدث الأفلام")
                    HorizontalList(MockData.vodContent.filter { it.type == ItemType.MOVIE }, navController)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader("📺 المسلسلات")
                    HorizontalList(MockData.vodContent.filter { it.type == ItemType.SERIES }, navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun TopBar(part1: String, part2: String, showBack: Boolean = false, onBack: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = part1,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = part2,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = RedAccent
        )
    }
}

@Composable
fun HeroSection(item: VodItem?, navController: NavController) {
    if (item == null) return
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { navController.navigate("details/${item.id}") }
    ) {
        Image(
            painter = painterResource(id = item.backdrop),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(text = "🔥 حصرياً", color = RedAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = item.title, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = "${item.year} • ${item.category} • ⭐ ${item.rating}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Row(modifier = Modifier.padding(top = 12.dp)) {
                Button(
                    onClick = { 
                        val url = java.net.URLEncoder.encode(item.servers.firstOrNull()?.url ?: "", "UTF-8")
                        navController.navigate("player/$url/${item.title}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("شاهد الآن", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun HorizontalList(items: List<VodItem>, navController: NavController) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            PosterCard(item) {
                navController.navigate("details/${item.id}")
            }
        }
    }
}

@Composable
fun PosterCard(item: VodItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = painterResource(id = item.poster),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("⭐ ${item.rating}", color = Color(0xFFFFD15C), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
        Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
fun SearchResultsGrid(items: List<VodItem>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            PosterCard(item) { navController.navigate("details/${item.id}") }
        }
    }
}

@Composable
fun FilteredScreen(navController: NavController, viewModel: AppViewModel, type: ItemType) {
    val items = MockData.vodContent.filter { it.type == type }
    val title = if (type == ItemType.MOVIE) "الأفلام" else "المسلسلات"
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(title, "")
        SearchResultsGrid(items, navController)
    }
}

@Composable
fun FavoritesScreen(navController: NavController, viewModel: AppViewModel) {
    val items = viewModel.getFavoriteItems()
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("مكتبتي", "المفضلة")
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("المكتبة فارغة", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            SearchResultsGrid(items, navController)
        }
    }
}

@Composable
fun LiveScreen(navController: NavController, viewModel: AppViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar("باقات", "البث المباشر")
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(MockData.liveCategories) { cat ->
                LiveCategoryCard(cat) {
                    navController.navigate("live_category/${cat.id}")
                }
            }
        }
    }
}

@Composable
fun LiveCategoryCard(cat: LiveCategory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(cat.color).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Tv, contentDescription = null, tint = Color(cat.color))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cat.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = cat.desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun LiveCategoryScreen(category: LiveCategory, navController: NavController) {
    val channels = MockData.liveChannels.filter { it.catId == category.id }
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(category.title, "", showBack = true) { navController.popBackStack() }
        if (channels.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("لا توجد قنوات", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(channels) { channel ->
                    ChannelCard(channel) {
                        val url = java.net.URLEncoder.encode(channel.servers.firstOrNull()?.url ?: "", "UTF-8")
                        navController.navigate("player/$url/${channel.title}")
                    }
                }
            }
        }
    }
}

@Composable
fun ChannelCard(channel: LiveChannel, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Tv, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = channel.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun VodDetailsScreen(item: VodItem, navController: NavController, viewModel: AppViewModel) {
    val isFav = viewModel.isFavorite(item.id)
    
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(
                    painter = painterResource(id = item.backdrop),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                                startY = 100f,
                                endY = 800f
                            )
                        )
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(16.dp).background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = item.title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "${item.year} • ${item.category} • ⭐ ${item.rating}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp))
                Text(text = item.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp))
                
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { 
                            val url = java.net.URLEncoder.encode(item.servers.firstOrNull()?.url ?: "", "UTF-8")
                            navController.navigate("player/$url/${item.title}")
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تشغيل الميديا", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { viewModel.toggleFavorite(item.id) },
                        modifier = Modifier.size(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(if (isFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = "Favorite", tint = if (isFav) RedAccent else Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerScreen(url: String, title: String, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        VideoPlayer(url = url, modifier = Modifier.fillMaxSize())
    }
}
