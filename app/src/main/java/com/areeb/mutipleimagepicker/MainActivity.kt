package com.areeb.mutipleimagepicker

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.areeb.mutipleimagepicker.ui.theme.MutipleIMagePickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MutipleIMagePickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Home()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Multiple Image Selector",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    ProfilePic(context = context)
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(
                            context,
                            "menu button click",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description",
                        )
                    }
                },

            )
        },
        content = {
            Body()
        },

    )
}

@Composable
private fun ProfilePic(context: Context) {
    var profilePicUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val profilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            profilePicUri = it
        },
    )

    Image(
        painter = if (profilePicUri == null) {
            painterResource(id = R.drawable.ic_launcher_foreground)
        } else {
            rememberAsyncImagePainter(profilePicUri)
        },
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .padding(start = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                Toast
                    .makeText(context, "profile view ", Toast.LENGTH_SHORT)
                    .show()
                profilePickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),

                )
            },
        contentDescription = "image",
        contentScale = ContentScale.Crop,

    )
}

@Composable
private fun Body() {
    var photoList by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {
            photoList = it
        },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 10.dp, end = 10.dp),
    ) {
        Box() {
            Button(
                onClick = {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
            ) {
                Text(
                    text = "choose multiple photos",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                )
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))
        }
        LazyColumn(content = {
            items(photoList) {
                AsyncImage(
                    model = it,
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(10.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        })
    }
}
