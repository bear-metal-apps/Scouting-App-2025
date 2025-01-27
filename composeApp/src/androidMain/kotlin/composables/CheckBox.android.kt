package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import getCurrentTheme
import nodes.autoStop
import nodes.autos
import nodes.redoList
import nodes.undoList

@Composable
actual fun TriStateCheckBox(
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

@Composable
actual fun CheckBox(
    label: String,
    ifChecked: MutableIntState,
    modifier: Modifier
){
    Box(modifier = modifier.border(2.dp,getCurrentTheme().primaryVariant)){
        Text(
            text = label,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Checkbox(
            checked = if(autoStop.value == 0){ false }else{ true },
            onCheckedChange = {
                if(autoStop.value == 0){
                    autoStop.value = 1
                }else{
                    autoStop.value = 0
                }
            },
            modifier = Modifier.align(Alignment.Center).fillMaxSize(),
        )
    }

}