package composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compKey
import getCurrentTheme
import nodes.TeamMatchStartKey
import nodes.autoStop
import nodes.betterParseInt
import nodes.createOutput
import nodes.jsonObject
import nodes.match
import nodes.redoList
import nodes.saveData
import nodes.teamDataArray
import nodes.undoList
import pages.tempRobotStart

@Composable
actual fun TriStateCheckBox(
    label: String,
    color: Color,
    ifChecked: MutableState<ToggleableState>,
    modifier: Modifier
){

    var backgroundColor = remember { mutableStateOf(color) }
    var textColor = remember { mutableStateOf(Color.White) }

    fun getNewState(state: ToggleableState) = when (state) {
        ToggleableState.Off -> ToggleableState.On
        ToggleableState.Indeterminate -> ToggleableState.Off
        ToggleableState.On -> ToggleableState.Indeterminate
    }

    if(ifChecked.value == ToggleableState.On) {
        backgroundColor.value = Color(0, 255, 0)
        textColor.value = Color.White
    } else if(ifChecked.value == ToggleableState.Indeterminate) {
        backgroundColor.value = Color.Yellow
        textColor.value = Color.Black
    } else {
        backgroundColor.value = color
        textColor.value = Color.White
    }

    OutlinedButton (
        border = BorderStroke(2.dp, color = getCurrentTheme().primaryVariant),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor.value),
        onClick = {
            undoList.push(arrayOf("tristate", ifChecked, ifChecked.value, backgroundColor, backgroundColor.value, textColor, textColor.value))
            ifChecked.value = getNewState(ifChecked.value)

            if(ifChecked.value == ToggleableState.On) {
                backgroundColor.value = Color(0, 255, 0)
                textColor.value = Color.White
            } else if(ifChecked.value == ToggleableState.Indeterminate) {
                backgroundColor.value = Color.Yellow
                textColor.value = Color.Black
            } else {
                backgroundColor.value = color
                textColor.value = Color.White
            }

            saveData.value = true
            teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(jsonObject.get("robotStartPosition").asInt, createOutput(mutableIntStateOf(jsonObject.get("team").asInt), mutableIntStateOf(
                jsonObject.get("robotStartPosition").asInt)))
        },
        contentPadding = PaddingValues(5.dp, 5.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = label,
                color = textColor.value,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterStart) // Does nothing?
            )
        }
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
            checked = if(ifChecked.value == 0){ false }else{ true },
            onCheckedChange = {
                if(ifChecked.value == 0){
                    ifChecked.value = 1
                }else{
                    ifChecked.value = 0
                }
                saveData.value = true
                teamDataArray.get(compKey)?.get(match.value.betterParseInt())?.set(jsonObject.get("robotStartPosition").asInt, createOutput(mutableIntStateOf(jsonObject.get("team").asInt), mutableIntStateOf(
                    jsonObject.get("robotStartPosition").asInt)))
            },
            modifier = Modifier.align(Alignment.Center).fillMaxSize(),
        )
    }

}