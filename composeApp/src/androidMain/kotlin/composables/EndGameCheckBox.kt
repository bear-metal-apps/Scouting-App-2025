package composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.sp
import nodes.park

@Composable
actual fun EndGameCheckBox(
    label: String,
    ifChecked: MutableState<Boolean>,
    checkBox1Checked: MutableState<Boolean>,
    checkBox2Checked: MutableState<Boolean>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
    ){
        Text(
            text = label,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Checkbox(
            checked = ifChecked.value,
            onCheckedChange ={
                ifChecked.value = !ifChecked.value

                checkBox1Checked.value = false
                checkBox2Checked.value = false
            },
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}