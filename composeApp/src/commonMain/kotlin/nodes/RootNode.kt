package nodes

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import androidx.compose.runtime.*
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize
import org.tahomarobotics.scouting.Client
import pages.*
import java.lang.Integer.parseInt


class RootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<NavTarget> = BackStack(
        model = BackStackModel(
            initialTarget = NavTarget.LoginPage,
            savedStateMap = buildContext.savedStateMap
        ),
        visualisation = { BackStackFader(it) }
    )
) : ParentNode<RootNode.NavTarget>(
    appyxComponent = backStack,
    buildContext = buildContext
){
    private var team = mutableIntStateOf(1)
    private var robotStartPosition = mutableIntStateOf(0)
    private var pitsPerson = mutableStateOf("P1")
    var comp = mutableStateOf("")


    sealed class NavTarget : Parcelable {
        @Parcelize
        data object MainMenu : NavTarget()

        @Parcelize
        data object MatchScouting : NavTarget()

        @Parcelize
        data object PitsScouting : NavTarget()

        @Parcelize
        data object LoginPage : NavTarget()
    }

    override fun resolve(interactionTarget: NavTarget, buildContext: BuildContext): Node =
        when (interactionTarget) {
            NavTarget.LoginPage -> LoginNode(buildContext, backStack, scoutName, comp, numOfPitsPeople)
            NavTarget.MainMenu -> MainMenu(buildContext, backStack, robotStartPosition,scoutName, comp, team)
            NavTarget.MatchScouting -> AutoTeleSelectorNode(buildContext,robotStartPosition, team, backStack)
            NavTarget.PitsScouting -> PitsNode(buildContext,backStack,pitsPerson, numOfPitsPeople)
        }

    @Composable
    override fun View(modifier: Modifier) {
        Column {
            AppyxComponent(
                appyxComponent = backStack,
                modifier = Modifier.weight(0.9f)
            )

        }
    }
}

var numOfPitsPeople = mutableIntStateOf(0)
var scoutName =  mutableStateOf("")
val teamDataArray : HashMap<TeamMatchKey, String> = hashMapOf<TeamMatchKey, String>()
var client : Client? = null


public fun String.betterParseInt(): Int{
    for(index in this.indices){
        if (this[index] < '0' || this[index] > '9') {
            this.replace("${this[index]}"," ")
        }
    }
    if (this != ""){
        return parseInt(this.replace(" ",""))
    }else{
        return 0
    }

}
