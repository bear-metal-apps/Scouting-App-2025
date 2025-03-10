
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import nodes.Team
import okhttp3.OkHttpClient
import org.json.JSONObject


var compKey : String = ""


expect val matchData: JSONObject?
expect val teamData: JSONObject?

const val url = "https://www.thebluealliance.com/api/v3"
private val client = OkHttpClient()

expect fun setTeam(teamNum: MutableIntState, match: MutableState<String>, robotStartPosition: Int)

expect fun getTeamsOnAlliance(matchNumber: Int, isRedAlliance: Boolean): List<Team>

fun isSynced(): Boolean {
    if(teamData != null && matchData != null) {
        return true
    }
    return false
}

private const val PATTERN_FORMAT = "dd/MM/yyyy @ hh:mm"

const val apiKeyEncoded = "eEtWS2RkemlRaTlJWkJhYXMxU0M0cUdlVkVTMXdaams3VDhUckZ1amFSODFmQlFIUXgybTdzTGJoZ0lnQVNPRw=="