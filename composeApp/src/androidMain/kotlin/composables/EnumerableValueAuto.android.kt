package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import getCurrentTheme
import nodes.redoList
import nodes.saveData
import nodes.undoList
import org.bridj.util.Tuple

@Composable
actual fun EnumerableValueAuto(
    label: String,
    value: MutableIntState,
    flashColor: Color,
    alignment: Alignment,
    modifier: Modifier
) {

    val interact = remember { MutableInteractionSource() }

    val pressed by interact.collectIsPressedAsState()

    OutlinedButton(
        border = BorderStroke(2.dp, color = getCurrentTheme().primaryVariant),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (pressed) flashColor else Color.Black),
        onClick = {
            undoList.push(arrayOf("number" ,value, value.value))
            value.value += 1
            redoList.push(arrayOf("number" ,value, value.value))
            saveData.value = true
        },
        interactionSource = interact,
        contentPadding = PaddingValues(5.dp, 5.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = label + ": \n" + value.intValue.toString(),
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}