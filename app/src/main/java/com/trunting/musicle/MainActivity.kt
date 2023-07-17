package com.trunting.musicle

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.Dp
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
    val numRows = 5
    val numCols = 4
    val guessesGrid: MutableList<MutableList<String>> =
        remember { MutableList(numRows) { MutableList(numCols) { "" } } }
    var lastClickedName: String? by remember { mutableStateOf(null) }
    var guessNumber: Int by remember { mutableStateOf(0) }
    var noteNumber: Int by remember { mutableStateOf(0) }

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
        Row(Modifier.weight(1.5f)) {
            PianoOctave(
                showNoteNames = true,
                octave = 5,
                lastClicked = { lastClickedName = it },
                guessNumber = guessNumber,
                noteNumber = noteNumber,
                guessesGrid = guessesGrid,
                updateNoteNumber = { noteNumber = it }
            )
        }
        // Buttons
        Row {
            // Backspace
            Button(
                onClick = {
                    lastClickedName = null
                    noteNumber = maxOf(noteNumber - 1, 0)
                    guessesGrid[guessNumber][noteNumber] = ""
                },
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
                onClick = {
                    Log.d("SUBMIT", "$guessesGrid")
                    validateGuess(
                    guessNumber = guessNumber,
                    noteNumber = noteNumber,
                    updateGuessNumber = { guessNumber = it },
                    updateNoteNumber = {noteNumber = it}
                ) },
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
        Row(
            modifier = Modifier
                .weight(2f)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            // Container for guesses
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Build grid
                Row {
                    for (i in 0 until numCols) {
                        Column {
                            for (j in 0 until numRows) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp)
                                        .aspectRatio(1f)
                                        .background(Color.LightGray, RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = guessesGrid[j][i],
                                        textAlign = TextAlign.Justify
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Validate the submitted guess
fun validateGuess(
    guessNumber: Int,
    noteNumber: Int,
    updateGuessNumber: (Int) -> Unit,
    updateNoteNumber: (Int) -> Unit
) {
    // Each guess must have 4 notes in it
    if ( noteNumber == 4 ) {
        if ( guessNumber < 4 ) {  // Normal case
            // Reset and increment
            updateGuessNumber(guessNumber + 1)
            updateNoteNumber(0)
        } else if ( guessNumber == 4 ) {  // End case
            // Reset
            updateNoteNumber(0)
        }
    }
}

// Composable for a piano octave
@Composable
fun PianoOctave(
    showNoteNames: Boolean = false,
    octave: Int? = 0,
    lastClicked: (String) -> Unit,
    guessNumber: Int,
    noteNumber: Int,
    guessesGrid: MutableList<MutableList<String>>,
    updateNoteNumber: (Int) -> Unit,
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
                            val newWidth = with(localDensity) { size.width.toDp() }
                            val newHeight = with(localDensity) { size.height.toDp() }

                            // Have to test both positive and negative cases as abs can't take
                            // dp values
                            if ((whiteKeyWidth - newWidth) > 3.dp ||
                                (whiteKeyHeight - newHeight) > 3.dp ||
                                (newWidth - whiteKeyWidth) > 3.dp ||
                                (newHeight - whiteKeyHeight) > 3.dp
                            ) {
                                whiteKeyWidth = newWidth
                                whiteKeyHeight = newHeight
                            }
                        }
                        .clickable {
                            lastClicked(formatNoteName(index, octave, true))
                            if (noteNumber <= 3) {
                                guessesGrid[guessNumber][noteNumber] =
                                    formatNoteName(index, octave, true)
                            }
                            updateNoteNumber(minOf(noteNumber + 1, 4))
                        }
                        .focusable(false)

                ) {
                    Log.d("KEYS", "$whiteKeyWidth")
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
                        // Wrap note names in a scaffold to reduce jitter
                        Scaffold(
                            backgroundColor = Color.Transparent,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Print names
                                if (showNoteNames) {
                                    Text(
                                        text = formatNoteName(index, octave, true),
                                        color = Color.Black
                                    )
                                }
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
                (whiteKeyWidth - blackKeyWidth) * 3.00F + 10.00F.dp,
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
                        .clickable {
                            lastClicked(formatNoteName(index, octave, false))
                            if (noteNumber <= 3) {
                                guessesGrid[guessNumber][noteNumber] =
                                    formatNoteName(index, octave, false)
                            }
                            updateNoteNumber(minOf(noteNumber + 1, 4))
                        }
                        .focusable(false)
                ) {
                    // Wrap note names in a scaffold to reduce jitter
                    Scaffold(
                        backgroundColor = Color.Transparent,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Print names
                            if (showNoteNames) {
                                Text(
                                    text = formatNoteName(index, octave, false),
                                    color = Color.White
                                )
                            }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicleTheme {
        MusicleHome()
    }
}