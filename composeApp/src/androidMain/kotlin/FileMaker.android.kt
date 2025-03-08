import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import nodes.*
import org.json.JSONObject
import org.tahomarobotics.scouting.Client
import java.io.File
import java.lang.Integer.parseInt

var matchFolder: File? = null
var stratFolder: File? = null
var pitsFolder: File? = null

var tbaDataFolder: File? = null
var TBAMatchDataFolder = File(tbaDataFolder, "MatchData")
var TBATeamDataFolder = File(tbaDataFolder, "TeamData")

var imagesFolder: File? = null

var tabletDatafile: File? = null

fun createScoutMatchDataFolder(context: Context) {
    matchFolder = File(context.filesDir, "ScoutMatchDataFolder")

    if (!matchFolder!!.exists()) {
        matchFolder!!.mkdirs()
        println("Made match data folder")
    } else {
        println("Match data folder found")
    }
}

fun createScoutStratDataFolder(context: Context) {
    stratFolder = File(context.filesDir, "ScoutStratDataFolder")

    if (!stratFolder!!.exists()) {
        stratFolder!!.mkdirs()
        println("Made strat data folder")
    } else {
        println("Strat data folder found")
    }
}

fun createScoutPitsDataFolder(context: Context) {
    pitsFolder = File(context.filesDir, "ScoutPitsDataFolder")

    if (!pitsFolder!!.exists()) {
        pitsFolder!!.mkdirs()
        println("Made pits data folder")
    } else {
        println("Pits data folder found")
    }

    imagesFolder = File(context.filesDir, "images")

    if (!imagesFolder!!.exists()) {
        imagesFolder!!.mkdirs()
        println("Made pits images folder")
    } else {
        println("Pits images folder found")
    }

}


fun createScoutMatchDataFile(match: String, team: Int, data: String) {
    val file = File(matchFolder, "Match${match}Team${team}.json")
    file.delete()
    file.createNewFile()

    file.writeText(data)

    file.forEachLine {
        try {
            println("Saved file Match${match}Team${team}.json: $it")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun createScoutStratDataFile(match: String, isRed: Boolean, data: String) {
    val file = File(stratFolder, "Match${match}${if (isRed) "RedAlliance" else "BlueAlliance"}.json")
    file.delete()
    file.createNewFile()

    file.writeText(data)

    file.forEachLine {
        try {
            println("Saved file Match${match}${if (isRed) "RedAlliance" else "BlueAlliance"}.json: $it")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun createScoutPitsDataFile(team: Int, data: String) {
    val file = File(pitsFolder, "Team${team}.json")
    file.delete()
    file.createNewFile()

    file.writeText(data)

    file.forEachLine {
        try {
            println("Saved file Team${team}.json: $it")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun createTabletDataFile(context: Context) {
    tabletDatafile = File(context.filesDir, "TabletData.json")
    if (!tabletDatafile!!.exists())
        tabletDatafile!!.createNewFile()
}

fun writeTabletDataFile(context: Context, data: String) {
    createTabletDataFile(context = context)
    val gson = Gson()

    tabletDatafile!!.writeText(data)

    tabletDatafile!!.forEachLine {
        try {
            println("Saved data to TabletData.json: $it")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}


fun loadMatchDataFiles() {
    val gson = Gson()

    println("loading match files...")
    for ((index) in (matchFolder?.listFiles()?.withIndex()!!)) {
        if (gson.fromJson(matchFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java) != null) {
            jsonObject = gson.fromJson(
                matchFolder?.listFiles()?.toList()?.get(index)?.readText(),
                JsonObject::class.java
            )
            teamDataArray[TeamMatchStartKey(
                parseInt(jsonObject.get("match").asString),
                jsonObject.get("team").asInt,
                jsonObject.get("robotStartPosition").asInt
            )] = jsonObject.toString()

            println(matchFolder?.listFiles()?.toList()?.get(index).toString())
        }
    }
}

fun loadStratDataFiles() {
    val gson = Gson()

    println("loading strat files...")
    for ((index) in (stratFolder?.listFiles()?.withIndex()!!)) {
        if (gson.fromJson(stratFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java) != null) {
            stratJsonObject = gson.fromJson(
                stratFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java
            )
            stratTeamDataArray[TeamsAllianceKey(
                stratJsonObject.get("match").asInt,
                stratJsonObject.get("is_red_alliance").asBoolean
            )] = stratJsonObject.toString()

            println(stratFolder?.listFiles()?.toList()?.get(index).toString())
        }
    }
}

fun loadPitsDataFiles() {
    val gson = Gson()

    println("loading pits files...")
    for ((index, value) in (pitsFolder?.listFiles()?.withIndex()!!)) {
        if (value?.name != "ImagesFolder" && gson.fromJson(
                pitsFolder?.listFiles()?.toList()?.get(index)?.readText(),
                JsonObject::class.java
            ) != null
        ) {
            jsonObject = gson.fromJson(
                pitsFolder?.listFiles()?.toList()?.get(index)?.readText(),
                JsonObject::class.java
            )
            pitsTeamDataArray[
                jsonObject.get("team").asInt
            ] = jsonObject.toString()

            println(pitsFolder?.listFiles()?.toList()?.get(index).toString())
            println(
                pitsTeamDataArray[
                    jsonObject.get("team").asInt
                ]
            )
        }
    }

    println("Loading pits image paths...")

    for ((outerIndex, value) in imagesFolder?.listFiles()?.withIndex()!!) {
        permPhotosList.add(value.path)
        println(permPhotosList[outerIndex])
    }
}

fun grabTabletDataFile(): String {
    val gson = Gson()

    if (tabletDatafile?.exists()!!) {
        return tabletDatafile?.readText()!!
    } else {
        return ""
    }
}


fun deleteScoutMatchData() {
    repeat(10) {
        try {
            for ((index) in matchFolder?.listFiles()?.withIndex()!!) {
                matchFolder?.listFiles()?.get(index)?.deleteRecursively()
            }
            teamDataArray.clear()
        } catch (_: IndexOutOfBoundsException) {
        }
    }
}

fun deleteScoutPitsData() {
    pitsTeamDataArray.clear()
    permPhotosList.clear()
    repeat(10) {
        try {
            for ((index) in pitsFolder?.listFiles()?.withIndex()!!) {
                if (pitsFolder?.listFiles()?.toList()?.get(index)?.name != "ImagesFolder") {
                    pitsFolder?.listFiles()?.get(index)?.deleteRecursively()
                }
            }
            for ((_, value) in imagesFolder?.listFiles()?.withIndex()!!) {
                value.deleteRecursively()
            }
        } catch (_: IndexOutOfBoundsException) {
        }
    }
}

fun deleteScoutStratData() {
    repeat(10) {
        try {
            for ((index) in stratFolder?.listFiles()?.withIndex()!!) {
                stratFolder?.listFiles()?.get(index)?.deleteRecursively()
            }
            stratTeamDataArray.clear()
        } catch (_: IndexOutOfBoundsException) {
        }
    }
}

fun setupTBAFolders(context: Context) {
    println("Setting up TBA folders")
    tbaDataFolder = File(context.filesDir, "TBAData")

    if (!tbaDataFolder!!.exists()) {
        tbaDataFolder!!.mkdirs()
        println("Made tba data folder")
    } else {
        println("tba data folder found")
    }

    TBAMatchDataFolder = File(tbaDataFolder, "MatchData")
    TBATeamDataFolder = File(tbaDataFolder, "TeamData")

    if (!TBAMatchDataFolder.exists()) {
        TBAMatchDataFolder.mkdirs()
        println("Made match data subfolder")
    } else {
        println("Match data subfolder found")
    }

    if (!TBATeamDataFolder.exists()) {
        TBATeamDataFolder.mkdirs()
        println("Made team data subfolder")
    } else {
        println("Team data subfolder found")
    }
}

fun storeTBAMatchData(context: Context, eventKey: String, data: JSONObject, eTag: String) {
    setupTBAFolders(context)
    val file = File(TBAMatchDataFolder, "${eventKey}.json")

    file.createNewFile()
    if (!(file.exists() && file.length() > 0)) {
        file.writeText("{ }")
    }
    val oldJson = JSONObject(file.readText())

    if (oldJson.optString("eTag") != eTag) {
        data.put("eTag", eTag)
        data.put("timestamp", System.currentTimeMillis())

        file.delete()
        file.createNewFile()

        file.writeText(data.toString(4))
    } else {
        oldJson.put("timestamp", System.currentTimeMillis())

        file.delete()
        file.createNewFile()

        file.writeText(oldJson.toString(4))
    }
}

fun storeTBATeamData(context: Context, eventKey: String, data: JSONObject, eTag: String) {
    setupTBAFolders(context)
    val file = File(TBATeamDataFolder, "${eventKey}.json")

    file.createNewFile()
    if (!(file.exists() && file.length() > 0)) {
        file.writeText("{ }")
    }
    val oldJson = JSONObject(file.readText())

    if (oldJson.optString("eTag") != eTag) {
        data.put("eTag", eTag)
        data.put("timestamp", System.currentTimeMillis())

        file.delete()
        file.createNewFile()

        file.writeText(data.toString(4))
    } else {
        oldJson.put("timestamp", System.currentTimeMillis())

        file.delete()
        file.createNewFile()

        file.writeText(oldJson.toString(4))
    }
}

fun updateTBAMatchDataTimestamp(context: Context, eventKey: String) {
    setupTBAFolders(context)
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    if (file.exists()) {
        val oldJson = JSONObject(file.readText())
        oldJson.put("timestamp", System.currentTimeMillis())

        file.delete()
        file.createNewFile()

        file.writeText(oldJson.toString(4))
    }
}

fun updateTBATeamDataTimestamp(context: Context, eventKey: String) {
    setupTBAFolders(context)
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    if (file.exists()) {
        val oldJson = JSONObject(file.readText())
        oldJson.put("timestamp", System.currentTimeMillis())

        file.delete()
        file.createNewFile()

        file.writeText(oldJson.toString(4))
    }
}

fun getTBAMatchData(): JSONObject {
    val file = File(TBAMatchDataFolder, "${compKey}.json")
    return if (file.exists()) {
        JSONObject(file.readText())
    } else {
        JSONObject()
    }
}

fun getTBATeamData(): JSONObject {
    val file = File(TBATeamDataFolder, "${compKey}.json")
    return if (file.exists()) {
        JSONObject(file.readText())
    } else {
        JSONObject()
    }
}

fun getTBATeamDataETag(context: Context, eventKey: String): String {
    val data = getTBATeamData()
    return if (data.has("eTag")) {
        data.getString("eTag")
    } else {
        ""
    }
}

fun getTBAMatchDataETag(context: Context, eventKey: String): String {
    val data = getTBAMatchData()
    return if (data.has("eTag")) {
        data.getString("eTag")
    } else {
        ""
    }
}

fun isTBAMTeamDataSynced(context: Context, eventKey: String): Boolean {
    setupTBAFolders(context)
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    return file.exists() && JSONObject(file.readText()).getJSONArray("teams").length() > 0
}

fun isTBAMatchDataSynced(context: Context, eventKey: String): Boolean {
    setupTBAFolders(context)
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    return file.exists() && JSONObject(file.readText()).getJSONArray("matches").length() > 0
}

fun deleteTBAMatchData(context: Context, eventKey: String) {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    file.delete()
}

fun deleteAllTBAMatchData(context: Context) {
    for (file in TBAMatchDataFolder.listFiles()!!) {
        file.delete()
    }
}

fun deleteTBATeamData(context: Context, eventKey: String) {
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    file.delete()
}

fun deleteAllTBATeamData(context: Context) {
    for (file in TBATeamDataFolder.listFiles()!!) {
        file.delete()
    }
}

fun isTBAMatchDataOld(context: Context, eventKey: String): Boolean {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    if (file.exists()) {
        val json = JSONObject(file.readText())

        return System.currentTimeMillis() - json.getLong("timestamp") > 3600000
    } else {
        return false
    }
}

//fun openTBATeamData(context: Context, eventKey: String) {
//    matchData = try {
//        JSONObject(String(FileInputStream(File(TBATeamDataFolder, "${eventKey}.json")).readBytes()))
//    } catch (e: JSONException) {
//        null
//    }
//}

//fun openTBAMatchData(context: Context, eventKey: String) {
//    teamData = try {
//        JSONObject(String(FileInputStream(File(TBAMatchDataFolder, "${eventKey}.json")).readBytes()))
//    } catch (e: JSONException) {
//        null
//    }
//}

fun sendMatchData(client: Client) {
//    println("reached beginning of sendData")

    val gson = Gson()

    for ((_, value) in teamDataArray.entries) {
        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "match")

        Log.i("Client", "Message Sent: $jsonObject")
    }

}

fun sendStratData(client: Client) {
//    println("reached beginning of sendData")

    val gson = Gson()

    for ((_, value) in stratTeamDataArray.entries) {
        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "strat")

        Log.i("Client", "Message Sent: $jsonObject")
    }

}

fun sendPitsData(client: Client) {

    val gson = Gson()

    for ((_, value) in pitsTeamDataArray.entries) {

        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "pit")

        Log.i("Client", "Message Sent: $jsonObject")
    }
}