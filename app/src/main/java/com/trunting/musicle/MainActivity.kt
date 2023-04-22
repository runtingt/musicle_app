package com.trunting.musicle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trunting.musicle.ui.theme.MusicleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MusicleHome()
                }
            }
        }
    }
}

@Composable
fun MusicleHome() {
    // Overall container
    Column(Modifier.fillMaxWidth()) {
        // Header row
        Row(Modifier.weight(.5f)) {
            // Icon
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ){
                Image(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = "Info icon",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(28.dp)
                )
            }
            // Title
            Column(
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Stats and volume
            Column(
                modifier = Modifier.weight(1f)
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.stats),
                            contentDescription = "Stats icon",
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.loud),
                            contentDescription = "Volume icon",
                        )
                    }
                }
            }
        }
        // Separate header from body
        Divider(
            thickness = 2.dp
        )
        // Keyboard
        Row(Modifier.weight(2f)) {
            PianoOctave(showNoteNames = true, octave = 5)
        }
        // Guesses
        Row(Modifier.weight(2f)) {
            ComposableInfoCard(
                title = "5",
                description = "5",
                backgroundColor = Color.LightGray,
                modifier = Modifier.weight(1f)
            )
            ComposableInfoCard(
                title = "6",
                description = "6",
                backgroundColor = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


// Composable for a piano octave
@Composable
fun PianoOctave(
    showNoteNames: Boolean = false,
    octave: Int? = 0
) {
    Box {
        // Seven white keys
        var whiteKeySize by remember { mutableStateOf(0.dp)}
        val localDensity = LocalDensity.current
        Row {
            repeat(7) { index ->
                Box(
                    modifier = Modifier
                        .padding(
                            start = if (index == 0) 1.dp else 0.dp,
                            end = 0.dp
                        )
                        .background(color = Color.White)
                        .fillMaxHeight()
                        .weight(1f)
                        .onSizeChanged { size ->
                            whiteKeySize = with(localDensity) {size.width.toDp()}
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                BorderStroke(
                                    2.dp,
                                    Color.LightGray
                                )
                            )
                    )
                }
            }
        }
        // Five black keys
        Row {
            val accidentalPaddings = arrayOf(
                whiteKeySize * 0.75F + 1.00F.dp,
                whiteKeySize * 0.50F + 0.00F.dp,
                whiteKeySize * 1.50F + 0.00F.dp,
                whiteKeySize * 0.50F + 0.00F.dp,
                whiteKeySize * 0.50F + 0.00F.dp
            )
            repeat(5) { index ->
                Spacer(modifier = Modifier.width(accidentalPaddings[index]))
                Box(
                    modifier = Modifier
                        .width(whiteKeySize / 2)
                        .height(255.dp)
                        .background(color = Color.Black)
                )
            }
        }
    }
    if(showNoteNames) {
        Row {
            repeat(7) {
                noteIndex ->
                Box(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .size(16.dp, 62.dp)
                        .padding(end = 1.dp)
                ) {
                    // TODO
                    val noteName = formatNoteName(noteIndex, octave)
                    Text(
                        text = noteName,
                        fontSize = 8.sp
                    )
                }
            }
        }
    }
}

// Note name
fun formatNoteName(
    noteIndex: Int = 0,
    octave: Int? = 0)
        : String {
    return noteIndex.toString() + octave.toString()
}

@Composable
private fun ComposableInfoCard(
    title: String,
    description: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = description,
            textAlign = TextAlign.Justify
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicleTheme {
        MusicleHome()
    }
}