@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.android.preinterview.assignment

import android.app.DownloadManager
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable(
            route = "list"
        ) {
            List(navController)
        }
        composable(
            route = "cart"
        ) {
            Cart(navController)
        }
    }
}

@Composable
fun List(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val pager = remember { viewModel.listPager }
    val items = pager.collectAsLazyPagingItems()
    val state = rememberSwipeRefreshState(false)

    val activity = navController.context as ComponentActivity
    val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val scope = rememberCoroutineScope()

    SwipeRefresh(
        state = state,
        onRefresh = {
            items.refresh()
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("List")
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate("cart")
                            }
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                        }
                    }
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it),
            ) {
                items(items) { item ->
                    item?.let {
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            val id = item.id
                            val name = item.name
                            val thumbnail = "${item.thumbnail.path}.${item.thumbnail.extension}"

                            ConstraintLayout {
                                val (btnClose) = createRefs()

                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .clickable {
                                            downloadManager.download(activity, thumbnail)
                                        },
                                    model = thumbnail,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )

                                IconButton(
                                    modifier = Modifier
                                        .constrainAs(btnClose) {
                                            top.linkTo(parent.top)
                                            end.linkTo(parent.end)
                                        },
                                    onClick = {
                                        scope.launch {
                                            viewModel.addCart(
                                                id,
                                                name,
                                                thumbnail
                                            )

                                            Toast.makeText(activity, "추가되었습니다. 카트에 가서 확인해보세요!", Toast.LENGTH_SHORT).show()

                                            navController.navigate("cart")
                                        }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = null
                                    )
                                }
                            }
                            Column {
                                Text(name)
                                Text(item.description)
                                Text("Size\n-Comic : ${item.comics.available}, Series : ${item.series.available}, Stories : ${item.stories.available}, Events : ${item.events.available}, Urls : ${item.urls.size}")
                            }
                        }
                    }
                }
                items.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            state.isRefreshing = true
                        }
                        loadState.refresh is LoadState.NotLoading -> {
                            state.isRefreshing = false

                            if (loadState.append.endOfPaginationReached) {
                                item {
                                    Text("더 이상 불러올 데이터가 없습니다")
                                }
                            }
                        }
                        loadState.append is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Cart(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {

    val pager = remember { viewModel.cartPager }
    val items = pager.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text("Cart")
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
        ) {
            items(items) { item ->
                item?.let {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        ConstraintLayout {
                            val (btnClose) = createRefs()

                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                model = it.thumbnail,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )

                            IconButton(
                                modifier = Modifier
                                    .constrainAs(btnClose) {
                                        top.linkTo(parent.top)
                                        end.linkTo(parent.end)
                                    },
                                onClick = {
                                    scope.launch {
                                        viewModel.removeCart(
                                            it.id
                                        )
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}