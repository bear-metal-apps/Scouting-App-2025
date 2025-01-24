package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import defaultPrimaryVariant
import getCurrentTheme
import nodes.redoList
import nodes.undoList

@Composable
actual fun CheckBox(
    label: String,
    ifChecked: MutableState<ToggleableState>,
    modifier: Modifier
){

    var backgroundColor = remember { mutableStateOf(Color.Black) }
    var textColor = remember { mutableStateOf(Color.White) }

    fun getNewState(state: ToggleableState) = when (state) {
        ToggleableState.Off -> ToggleableState.On
        ToggleableState.Indeterminate -> ToggleableState.Off
        ToggleableState.On -> ToggleableState.Indeterminate
    }

    OutlinedButton (
        border = BorderStroke(2.dp, color = getCurrentTheme().primaryVariant),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor.value),
        onClick = {
            undoList.push(arrayOf("tristate", ifChecked, ifChecked.value, backgroundColor, backgroundColor.value, textColor, textColor.value))
            ifChecked.value = getNewState(ifChecked.value)

            if(ifChecked.value == ToggleableState.On) {
                backgroundColor.value = Color(0, 204, 102)
                textColor.value = Color.White
            } else if(ifChecked.value == ToggleableState.Indeterminate) {
                backgroundColor.value = Color.Yellow
                textColor.value = Color.Black
            } else {
                backgroundColor.value = Color.Black
                textColor.value = Color.White
            }
        },
        modifier = modifier
    ) {
        Text(
            text = label,
            color = textColor.value
        )
    }
}