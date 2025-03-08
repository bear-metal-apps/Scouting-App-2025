
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import nodes.Team
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


var compKey : String = ""


expect val matchData: JSONObject?
expect val teamData: JSONObject?

const val url = "https://www.thebluealliance.com/api/v3"
private val client = OkHttpClient()

expect fun setTeam(teamNum: MutableIntState, match: MutableState<String>, robotStartPosition: Int)

expect fun getTeamsOnAlliance(matchNumber: Int, isRedAlliance: Boolean): List<Team>

var lastSynced = mutableStateOf(Instant.now())

fun getLastSynced() : String {
    val formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
        .withZone(ZoneId.systemDefault())

    return formatter.format(lastSynced.value)
}

fun isSynced(): Boolean {
    if(teamData != null && matchData != null) {
        return true
    }
    return false
}

private const val PATTERN_FORMAT = "dd/MM/yyyy @ hh:mm"




const val apiKeyEncoded = "eEtWS2RkemlRaTlJWkJhYXMxU0M0cUdlVkVTMXdaams3VDhUckZ1amFSODFmQlFIUXgybTdzTGJoZ0lnQVNPRw=="