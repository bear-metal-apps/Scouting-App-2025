
import android.content.Context
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nodes.Team
import org.json.JSONArray
import org.json.JSONObject
import org.tahomarobotics.scouting.TBAInterface
import java.lang.Integer.parseInt

actual val teamData: JSONObject?
    get() = getTBATeamData()
actual val matchData: JSONObject?
    get() = getTBAMatchData()

suspend fun syncTeams(context: Context): Boolean {
    var teamSuccessful = false
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        val response = TBAInterface.getTBAResponse(
            "/event/$compKey/teams/simple", getTBATeamDataETag(context, compKey)
        )

        if (response != null) {
            when (response.code) {
                200 -> {
                    println("Fetching new team data")
                    storeTBATeamData(
                        context,
                        compKey,
                        JSONObject().put("teams", JSONArray(response.body?.string() ?: "[]")),
                        response.header("ETag") ?: ""
                    )
                    teamSuccessful = true
                }

                304 -> {
                    updateTBATeamDataTimestamp(context, compKey)
                    teamSuccessful = true
                    println("Used cached team data")
                }

                else -> teamSuccessful = false
            }
        }
    }
    job.join()
//    openTBATeamData(context, compKey)
    return teamSuccessful
}

suspend fun syncMatches(context: Context): Boolean {
    var matchSuccessful = false
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        val response = TBAInterface.getTBAResponse(
            "/event/$compKey/matches", getTBAMatchDataETag(context, compKey)
        )

        if (response != null) {
            when (response.code) {
                200 -> {
                    println("Fetching new match data")
                    storeTBAMatchData(
                        context,
                        compKey,
                        JSONObject().put("matches", JSONArray(response.body?.string() ?: "[]")),
                        response.header("ETag") ?: ""
                    )
                    matchSuccessful = true
                }

                304 -> {
                    updateTBAMatchDataTimestamp(context, compKey)
                    matchSuccessful = true
                    println("Used cached match data")
                }

                else -> matchSuccessful = false
            }
        }
    }
    job.join()
//    openTBAMatchData(context, compKey)
    return matchSuccessful
}

actual fun setTeam(
    teamNum: MutableIntState, match: MutableState<String>, robotStartPosition: Int
) {
    // Retrieve match data via the new FileMaker method.
    val matchData = getTBAMatchData()
    if (!matchData.has("matches")) {
        return
    }
    val matches = matchData.getJSONArray("matches")
    // Use "1" as default when match.value is empty
    val matchNumber = parseInt(match.value.ifEmpty { "1" })
    for (i in 0 until matches.length()) {
        val matchObj = matches.getJSONObject(i)
        // Process only qualification matches ("qm") and the correct match number
        if (matchObj.getString("comp_level") != "qm" ||
            matchObj.getInt("match_number") != matchNumber) {
            continue
        }
        val alliances = matchObj.getJSONObject("alliances")
        val redAlliance = alliances.getJSONObject("red").getJSONArray("team_keys")
        val blueAlliance = alliances.getJSONObject("blue").getJSONArray("team_keys")
        val teamKey = when (robotStartPosition) {
            0 -> redAlliance.getString(0)
            1 -> redAlliance.getString(1)
            2 -> redAlliance.getString(2)
            3 -> blueAlliance.getString(0)
            4 -> blueAlliance.getString(1)
            5 -> blueAlliance.getString(2)
            else -> ""
        }
        if (teamKey.isNotEmpty()) {
            teamNum.intValue = teamKey.substring(3).toInt()
        }
        break
    }
}

actual fun getTeamsOnAlliance(matchNumber: Int, isRedAlliance: Boolean): List<Team> {
    val teams = mutableListOf<Team>()
    // Retrieve match data via the new FileMaker method.
    val matchData = getTBAMatchData() ?: return teams
    if (!matchData.has("matches")) {
        return teams
    }
    val matches = matchData.getJSONArray("matches")
    for (i in 0 until matches.length()) {
        val matchObj = matches.getJSONObject(i)
        // Only process qualification matches ("qm") with the given match number
        if (matchObj.getString("comp_level") != "qm" ||
            matchObj.getInt("match_number") != matchNumber) {
            continue
        }
        val alliance = if (isRedAlliance) {
            matchObj.getJSONObject("alliances").getJSONObject("red").getJSONArray("team_keys")
        } else {
            matchObj.getJSONObject("alliances").getJSONObject("blue").getJSONArray("team_keys")
        }
        for (j in 0 until alliance.length()) {
            val teamKey = alliance.getString(j)
            val teamNumber = teamKey.substring(3).toInt()
            // Retrieve team nickname via the new FileMaker team data method.
            val nickname = getTBATeamData()?.let { teamData ->
                val teamsArray = teamData.getJSONArray("teams")
                (0 until teamsArray.length()).map { index ->
                    teamsArray.getJSONObject(index)
                }.find { team ->
                    team.getString("key") == teamKey
                }?.getString("nickname") ?: "Unknown"
            } ?: "Unknown"
            teams.add(Team(number = teamNumber, name = nickname))
        }
        break
    }
    return teams
}