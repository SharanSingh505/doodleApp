package com.example.doodleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BrushPattern {
    SOLID,
    DOTTED
}

data class DrawingStroke(
    val path: Path,
    val color: Color,
    val width: Float,
    val pattern: BrushPattern = BrushPattern.SOLID
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DoodleApp()
            }
        }
    }
}

@Composable
fun DoodleApp() {
    var brushSize by remember { mutableFloatStateOf(10f) }
    var brushColor by remember { mutableStateOf(Color.Black) }
    var brushPattern by remember { mutableStateOf(BrushPattern.SOLID) }
    val strokes = remember { mutableStateListOf<DrawingStroke>() }
    val undoStack = remember { mutableStateListOf<DrawingStroke>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        ToolPanel(
            brushSize = brushSize,
            brushColor = brushColor,
            brushPattern = brushPattern,
            onBrushSizeChange = { brushSize = it },
            onBrushColorChange = { brushColor = it },
            onBrushPatternChange = { brushPattern = it },
            onClear = {
                undoStack.addAll(strokes)
                strokes.clear()
            },
            onUndo = {
                if (strokes.isNotEmpty()) {
                    val lastStroke = strokes.removeLast()
                    undoStack.add(lastStroke)
                }
            },
            canUndo = strokes.isNotEmpty()
        )

        DrawingCanvas(
            strokes = strokes,
            brushColor = brushColor,
            brushSize = brushSize,
            brushPattern = brushPattern,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        )
    }
}

@Composable
fun ToolPanel(
    brushSize: Float,
    brushColor: Color,
    brushPattern: BrushPattern,
    onBrushSizeChange: (Float) -> Unit,
    onBrushColorChange: (Color) -> Unit,
    onBrushPatternChange: (BrushPattern) -> Unit,
    onClear: () -> Unit,
    onUndo: () -> Unit,
    canUndo: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Doodle",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Brush Size: ${brushSize.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Slider(
                value = brushSize,
                onValueChange = onBrushSizeChange,
                valueRange = 5f..50f,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Brush Color",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ColorPicker(
                selectedColor = brushColor,
                onColorSelected = onBrushColorChange,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Brush Pattern",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            PatternPicker(
                selectedPattern = brushPattern,
                onPatternSelected = onBrushPatternChange,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onClear,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE57373)
                    )
                ) {
                    Text("Clear Canvas")
                }

                Button(
                    onClick = onUndo,
                    enabled = canUndo,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF64B5F6)
                    )
                ) {
                    Text("Undo")
                }
            }
        }
    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        Color.Black,
        Color.Red,
        Color(0xFFFF6B6B),
        Color(0xFFFFA500),
        Color(0xFFFFD700),
        Color.Green,
        Color(0xFF4ECDC4),
        Color.Blue,
        Color(0xFF9B59B6),
        Color(0xFFFF1493)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, CircleShape)
                    .then(
                        if (color == selectedColor) {
                            Modifier.padding(2.dp)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { onColorSelected(color) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (color == selectedColor) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun PatternPicker(
    selectedPattern: BrushPattern,
    onPatternSelected: (BrushPattern) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        listOf(BrushPattern.SOLID, BrushPattern.DOTTED).forEach { pattern ->
            Button(
                onClick = { onPatternSelected(pattern) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPattern == pattern)
                        Color(0xFF64B5F6)
                    else Color(0xFFE0E0E0)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pattern.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun DrawingCanvas(
    strokes: MutableList<DrawingStroke>,
    brushColor: Color,
    brushSize: Float,
    brushPattern: BrushPattern,
    modifier: Modifier = Modifier
) {
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentStroke by remember { mutableStateOf<DrawingStroke?>(null) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .pointerInput(brushColor, brushSize) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val newPath = Path().apply {
                            moveTo(offset.x, offset.y)
                        }
                        currentPath = newPath
                        currentStroke = DrawingStroke(newPath, brushColor, brushSize, brushPattern)
                    },
                    onDrag = { change, _ ->
                        currentPath?.lineTo(change.position.x, change.position.y)
                        change.consume()
                    },
                    onDragEnd = {
                        currentStroke?.let { strokes.add(it) }
                        currentPath = null
                        currentStroke = null
                    }
                )
            }
    ) {
        strokes.forEach { stroke ->
            drawPath(
                path = stroke.path,
                color = stroke.color,
                style = Stroke(
                    width = stroke.width,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = if (stroke.pattern == BrushPattern.DOTTED)
                        androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                            floatArrayOf(stroke.width, stroke.width)
                        )
                    else null
                )

            )
        }

        currentStroke?.let { stroke ->
            drawPath(
                path = stroke.path,
                color = stroke.color,
                style = Stroke(
                    width = stroke.width,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = if (stroke.pattern == BrushPattern.DOTTED)
                        androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                            floatArrayOf(stroke.width, stroke.width)
                        )
                    else null
                )

            )
        }
    }
}