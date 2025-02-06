import nodes.teamDataArray
import org.json.JSONArray
import org.json.JSONObject

fun getJsonFromMatchHash(): JSONObject {
    val jsonObject = JSONObject()
    teamDataArray.keys.forEach {
        jsonObject.put(it.toString(), teamDataArray[it])
    }

    return jsonObject
}
