package composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
expect fun EndGameCheckBox(
    label: String,
    ifChecked: MutableState<Boolean>,
    checkBox1Checked: MutableState<Boolean>,
    checkBox2Checked: MutableState<Boolean>,
    modifier: Modifier
)