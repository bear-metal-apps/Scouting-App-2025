package pages

import androidx.compose.foundation.BorderStroke
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import defaultPrimaryVariant
import defaultSecondary
import getCurrentTheme
import nodes.RootNode
import nodes.miniMinus
import kotlinx.coroutines.delay
import nodes.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.tahomarobotics.scouting.algaeColor
import org.tahomarobotics.scouting.coralColor
import theme
import minus
import org.tahomarobotics.scouting.R
import themeDefault

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun SettingsMenu(
    mainMenuBackStack: BackStack<RootNode.NavTarget>,
) {
    var themeExpanded by remember { mutableStateOf(false) }
    var totalScoutXP = mutableStateOf(5000f)
    var rankImage = mutableStateOf("")
    var updatedXP = mutableStateOf(false)

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
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        Text(
            text = "Appearance",
            fontSize = 35.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
//        Row {
//            DropdownMenu(
//                expanded = themeExpanded,
//                onDismissRequest = { themeExpanded = false },
//            ) {
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
                checked = highContrast.value,
                onCheckedChange = {
                    highContrast.value = !highContrast.value
                    if (highContrast.value) {
                        algaeColor = Color(0, 131, 52)
                        coralColor = Color(60, 0, 255)
                    } else {
                        algaeColor = Color(2, 78, 85)
                        coralColor = Color(85, 70, 50)//#FFE5B4
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .scale(2f)
                    .padding(25.dp)

            )
            Text(
                text = "High Contrast",
                fontSize = 25.sp,
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(28.dp)
            )
        }

        Row {

            Switch(
                checked = effects.value,
                onCheckedChange = { effects.value = !effects.value },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .scale(2f)
                    .padding(25.dp)

            )
            Text(
                text = "Effects",
                fontSize = 25.sp,
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(28.dp)
            )
        }
        Row {

            Switch(
                checked = teleFlash.value,
                onCheckedChange = { teleFlash.value = !teleFlash.value },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .scale(2f)
                    .padding(25.dp)

            )
            Text(
                text = "Tele Flash",
                fontSize = 25.sp,
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(28.dp)
            )
        }
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        Text(
            text = "Current Rank:",
            fontSize = 35.sp,
            color = getCurrentTheme().onPrimary,
        )
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
//        AsyncImage()
        var xpInRank = 500f
        var rankIndex = 0
        OutlinedButton(
            onClick = {
                rankIndex = updateScoutXP(totalScoutXP, rankImage, updatedXP)
                xpInRank =  totalScoutXP.value - maxXpList[rankIndex-1]
            },
            modifier = Modifier.fillMaxWidth((xpInRank/ maxXpList[rankIndex])),
            shape = CircleShape,
            border = BorderStroke(2.dp, getCurrentTheme().primaryVariant),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.Green
            ),
        ){
            if (!updatedXP.value){
                Text("$xpInRank/${maxXpList[rankIndex]} XP")
                updatedXP.value = !updatedXP.value
            }

        }
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        Text(
            text = "Accessibility",
            fontSize = 35.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        Text(
            text = "TODO Put algae and coral column move-ability to here",
            fontSize = 18.sp,
            color = getCurrentTheme().onPrimary,
            modifier = Modifier,
        )
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
                    .scale(2f)
                    .padding(25.dp)

            )
            Text(
                text = "Mini-Minus Buttons",
                fontSize = 25.sp,
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(28.dp)
            )
        }
        Row {
            Switch(
                checked = matchNumberButtons.value,
                onCheckedChange = { matchNumberButtons.value = !matchNumberButtons.value },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getCurrentTheme().primaryVariant,
                    checkedTrackColor = getCurrentTheme().secondaryVariant,
                    uncheckedThumbColor = getCurrentTheme().error,
                    uncheckedTrackColor = getCurrentTheme().onPrimary,
                ),
                modifier = Modifier
                    .scale(2f)
                    .padding(25.dp)

            )
            Text(
                text = "Match Increment Buttons",
                fontSize = 25.sp,
                color = getCurrentTheme().onPrimary,
                modifier = Modifier
                    .padding(28.dp)
            )
        }
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        OutlinedButton(
            border = BorderStroke(3.dp, Color.Yellow),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = defaultSecondary),
            onClick = {
                theme = themeDefault()
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
        HorizontalDivider(thickness = 2.dp, color = getCurrentTheme().primaryVariant)
        //TODO subway surfers gameplay goes here
//        var context = LocalContext.current
//        val mediaPlayer = MediaPlayer.create(context, R.)
//        LaunchedEffect(key1 = true) {  // LaunchedEffect needs a key; true is simple and static
//            mediaPlayer.start()         // Play the jingle
//            delay(3000)                 // Adjust to match the length of your jingle
//            mediaPlayer
//            mediaPlayer.release()       // Free resources after playing
//            }
        }
    }

/**
 * @return the function returns Max Xp for current rank
 * */
fun updateScoutXP(totalScoutXp : MutableState<Float>, rankImage : MutableState<String>, updateXP: MutableState<Boolean>): Int {
    updateXP.value = !updateXP.value
    if(totalScoutXp.value < maxXpList[0]){
    //PolyCarb
        rankImage.value = "PolyCarb.png"
        xpPerMatch = 100f
        return 0

    } else if(totalScoutXp.value < maxXpList[1]){
    //Copper
        rankImage.value = "Copper.png"
        xpPerMatch = 90f
        return 1

    }else if(totalScoutXp.value < maxXpList[2]){
    //Aluminum
        rankImage.value = "Aluminum.png"
        xpPerMatch = 75f
        return 2

    } else if(totalScoutXp.value < maxXpList[3]){
    //Titanium
        rankImage.value = "Titanium.png"
        xpPerMatch = 60f
        return 3

    }else if(totalScoutXp.value < maxXpList[4]){
    //Gold
        rankImage.value = "Gold.png"
        xpPerMatch = 45f
        return 4

    }else if(totalScoutXp.value < maxXpList[5]){
    //StainlessSteel
        rankImage.value = "StainlessSteel.png"
        xpPerMatch = 30f
        return 5

    }else{
    //BearMetalRank
        rankImage.value = "BearMetalRank.png"
        xpPerMatch = 15f
        return 6
    }
}



