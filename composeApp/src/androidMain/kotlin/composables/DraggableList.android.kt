package composables

import androidx.collection.intFloatMapOf
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
actual fun DraggableList(redAliance: Boolean) {
    var color : Color;
    var color2 : Color;
    var color3 = if(redAliance){
        color = Color(255,0,0)
        color2 = Color(255,50,50)
        Color(255,75,75)
    } else {
        color = Color(0,0,255)
        color2 = Color(50,50,255)
        Color(75,75,255)
    }
    var locations = intArrayOf(1,2,3);

    DraggableText("Team 1", 0..900, color, 1/8f, locations,1)
    DraggableText("Team 2", 0..900,color2, 1/8f, locations,2)
    DraggableText("Team 3", 0..900,color3,1/8f, locations,3)
}
@Composable
fun DraggableText(label: String, range: IntRange, color : Color, fillAmount : Float, intArray : IntArray, team: Int) {
    var offsetY by remember { mutableStateOf(0f)}
    var lastIndex = 1
    Text(
        modifier = Modifier
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .background(color)
            .draggable(
                orientation = Orientation.Vertical,
                onDragStopped = {
                    when{
                        offsetY < range.last/3 -> {
                            offsetY = range.last/6f
                            intArray[lastIndex] = intArray[0]
                            intArray[0] = team
                            lastIndex = 0
                        }
                        offsetY > range.last/3 && offsetY < range.last*2/3 ->{
                            offsetY = range.last/2f
                            intArray[lastIndex] = intArray[1]
                            intArray[1] = team
                            lastIndex = 1
                        }
                        offsetY > range.last*2/3 -> {
                            offsetY = (range.last*5)/6f
                            intArray[lastIndex] = intArray[2]
                            intArray[2] = team
                            lastIndex = 2
                        }
                    }
                },
                state = rememberDraggableState { delta ->
                    if (offsetY > range.first && offsetY < range.last){
                        offsetY += delta
                        if (!(offsetY >= range.first && offsetY <= range.last))
                            offsetY -= delta
                    }
                    if(offsetY == range.last/2f || offsetY == range.last/6f || offsetY == (range.last*5)/6f ){
                        when(intArray.indexOf(team)){
                            0 -> offsetY = range.last/6f
                            1 -> offsetY = range.last/2f
                            2 -> offsetY = (range.last*5)/6f
                        }
                    }
                }
            )
            .fillMaxHeight(fillAmount),
        text = label,
        textAlign = TextAlign.Center
    )
}