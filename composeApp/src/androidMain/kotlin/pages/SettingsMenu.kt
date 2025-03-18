package pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import defaultPrimaryVariant
import defaultSecondary
import getCurrentTheme
import nodes.RootNode
import nodes.canChangeRobotStartPosition
import nodes.miniMinus
import theme
import themeDefault

@Composable
actual fun SettingsMenu(
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
) {
    var themeExpanded by remember { mutableStateOf(false) }
    var effectsChecked by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .verticalScroll(ScrollState(0))
        .padding(8.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            androidx.compose.material3.OutlinedButton(
                border = BorderStroke(3.dp, Color.Yellow),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = defaultSecondary),
                onClick = {
                    mainMenuBackStack.pop()
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(8.dp, 0.dp, 8.dp, 0.dp)
            ) {
                androidx.compose.material3.Icon(Icons.Rounded.ChevronLeft, "Back")
            }

            androidx.compose.material3.Text(
                text = "Settings",
                fontSize = 32.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(8.dp)
            )
        }

        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 3.dp,
            modifier = Modifier.padding(8.dp)
        )
//        Text(
//            text = "Current Rank:",
//            fontSize = 35.sp,
//            color = getCurrentTheme().onPrimary,
//        )
//        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
//
//        Text(
//            text = "TODO put ranked photo for the scout right here and an xp bar",
//            fontSize = 18.sp,
//            color = getCurrentTheme().onPrimary,
//            modifier = Modifier,
//        )
//
//        Spacer(modifier = Modifier.padding(20.dp))
//        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        Text(
            text = "Appearance",
            fontSize = 24.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 3.dp,
            modifier = Modifier.padding(8.dp)
        )
//        Row {
//            DropdownMenu(
//                expanded = themeExpanded,
//                onDismissRequest = { themeExpanded = false },
//            ) {
//            }
//
//            OutlinedButton(
//                onClick = {
//                    themeExpanded = true
//                },
//                colors = ButtonDefaults.outlinedButtonColors(
//                    backgroundColor = getCurrentTheme().secondary,
//                    contentColor = getCurrentTheme().onPrimary
//                ),
//                shape = RoundedCornerShape(5.dp),
//            ) {
//                Text(
//                    text = "Theme",
//                    color = getCurrentTheme().onPrimary
//                )
//            }
//        }

        Row {
            Switch(
                checked = effectsChecked,
                onCheckedChange = { effectsChecked = !effectsChecked },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = "Effects",
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }

//        HorizontalDivider(
//            color = defaultPrimaryVariant,
//            thickness = 3.dp,
//            modifier = Modifier.padding(8.dp)
//        )
//        
//        Text(
//            text = "Accessibility",
//            fontSize = 24.sp,
//            color = getCurrentTheme().onPrimary,
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(8.dp)
//        )
//        HorizontalDivider(
//            color = defaultPrimaryVariant,
//            thickness = 3.dp,
//            modifier = Modifier.padding(8.dp)
//        )
        //
//        Text(
//            text = "TODO Put algae and coral column move-ability to here",
//            fontSize = 18.sp,
//            color = getCurrentTheme().onPrimary,
//            modifier = Modifier,
//        )
        Row {
            Switch(
                checked = miniMinus.value,
                onCheckedChange = { miniMinus.value = !miniMinus.value },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)

            )
            Text(
                text = "Mini-Minus Buttons",
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 3.dp,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Functionality",
            fontSize = 24.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        HorizontalDivider(
            color = defaultPrimaryVariant,
            thickness = 3.dp,
            modifier = Modifier.padding(8.dp)
        )

        Row {
            Switch(
                checked = canChangeRobotStartPosition.value,
                onCheckedChange = { canChangeRobotStartPosition.value = !canChangeRobotStartPosition.value },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)

            )
            Text(
                text = "Can change robot start position in match scouting",
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        androidx.compose.material3.OutlinedButton(
            border = BorderStroke(3.dp, Color.Yellow),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = defaultSecondary),
            onClick = {
                effectsChecked = false
                theme = themeDefault()
                canChangeRobotStartPosition.value = false
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
        ) {
            Text(
                text = "Reset to Default",
                color = getCurrentTheme().onPrimary
            )
        }

    }
}
