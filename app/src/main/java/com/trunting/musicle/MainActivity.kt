package com.trunting.musicle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
        // Buttons
        Row {
            // Backspace
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    disabledBackgroundColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                ),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                elevation = null
            ) {
                Text(
                    text = stringResource(id = R.string.backspace_text),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(8.dp)
                )
            }
            // Submit
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    disabledBackgroundColor = Color.Gray,
                    disabledContentColor = Color.LightGray
                ),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                elevation = null
            ) {
                Text(
                    text = stringResource(id = R.string.submit_text),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        // Guesses
        Row(Modifier.weight(2f)) {
            ComposableInfoCard(
                title = "1",
                description = "Guesses",
                backgroundColor = Color.LightGray,
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
        var whiteKeyWidth by remember { mutableStateOf(0.dp)}
        var whiteKeyHeight by remember { mutableStateOf(0.dp)}
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
                            whiteKeyWidth = with(localDensity) { size.width.toDp() }
                            whiteKeyHeight = with(localDensity) { size.height.toDp() }
                        }
                ) {
                    // Use a second box so that the border doesn't cause jitter
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                BorderStroke(
                                    2.dp,
                                    Color.LightGray
                                )
                            )
                    ) {
                        // Wrap note names in a column to align to bottom of keys
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Print names
                            if (showNoteNames) {
                                Text(
                                    text = formatNoteName(index, octave, true),
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        // Five black keys
        Row {
            val blackKeyWidth = whiteKeyWidth / 1.75F
            val blackKeyHeight = whiteKeyHeight / 1.25F
            val accidentalPaddings = arrayOf(
                (whiteKeyWidth - blackKeyWidth) * 1.50F + 4.00F.dp,
                (whiteKeyWidth - blackKeyWidth) * 1.00F + 1.00F.dp,
                (whiteKeyWidth - blackKeyWidth) * 3.00F + 7.00F.dp,
                (whiteKeyWidth - blackKeyWidth) * 1.00F + 1.00F.dp,
                (whiteKeyWidth - blackKeyWidth) * 1.00F + 1.00F.dp
            )
            repeat(5) { index ->
                Spacer(modifier = Modifier.width(accidentalPaddings[index]))
                Box(
                    modifier = Modifier
                        .width(blackKeyWidth)
                        .height(blackKeyHeight)
                        .background(color = Color.Black)
                ) {
                    // Wrap note names in a column to align to bottom of keys
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Print names
                        if (showNoteNames) {
                            Text(
                                text = formatNoteName(index, octave, false),
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Note name
fun formatNoteName(
    noteIndex: Int = 0,
    octave: Int? = 0,
    isWhite: Boolean = true
): String {
    val labels = if(isWhite) {
        arrayOf("C", "D", "E", "F", "G", "A", "B")
    } else {
        arrayOf("C#", "D#", "F#", "G#", "A#")
    }
    return labels[noteIndex] + octave.toString()
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