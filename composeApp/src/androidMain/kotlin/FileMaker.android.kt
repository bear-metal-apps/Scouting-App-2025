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

fun initFileMaker(context: Context) {
    // Existing folders for scouting data.
    createScoutMatchDataFolder(context)
    createScoutStratDataFolder(context)
    createScoutPitsDataFolder(context)

    // Initialize TBA folders.
    tbaDataFolder = File(context.filesDir, "TBAData")
    if (!tbaDataFolder!!.exists()) {
        tbaDataFolder!!.mkdirs()
        println("Made tba data folder")
    } else {
        println("tba data folder found")
    }
    TBAMatchDataFolder = File(tbaDataFolder, "MatchData")
    if (!TBAMatchDataFolder.exists()) {
        TBAMatchDataFolder.mkdirs()
        println("Made match data subfolder")
    } else {
        println("Match data subfolder found")
    }
    TBATeamDataFolder = File(tbaDataFolder, "TeamData")
    if (!TBATeamDataFolder.exists()) {
        TBATeamDataFolder.mkdirs()
        println("Made team data subfolder")
    } else {
        println("Team data subfolder found")
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


fun createScoutMatchDataFile(compKey: String, match: String, team: Int, data: String) {
    val file = File(matchFolder, "Comp${compKey}Match${match}Team${team}.json")
    file.delete()
    file.createNewFile()

    file.writeText(data)

    file.forEachLine {
        try {
            println("Saved file Comp${compKey}Match${match}Team${team}.json: $it")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun createScoutStratDataFile(compKey: String, match: String, isRed: Boolean, data: String) {
    val file = File(stratFolder, "Comp${compKey}Match${match}${if (isRed) "RedAlliance" else "BlueAlliance"}.json")
    file.delete()
    file.createNewFile()

    file.writeText(data)

    file.forEachLine {
        try {
            println("Saved file Comp${compKey}Match${match}${if (isRed) "RedAlliance" else "BlueAlliance"}.json: $it")
        } catch (e: Exception) {
            println(e.message)
        }
    }
}

fun createScoutPitsDataFile(compKey: String, team: Int, data: String) {
    val file = File(pitsFolder, "Comp${compKey}Team${team}.json")
    file.delete()
    file.createNewFile()

    file.writeText(data)

    file.forEachLine {
        try {
            println("Saved file Comp${compKey}Team${team}.json: $it")
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
            teamDataArray.getOrPut(jsonObject.get("event_key").asString) { hashMapOf() }.getOrPut(jsonObject.get("match").asInt) { hashMapOf() } .set(jsonObject.get("robotStartPosition").asInt, jsonObject.toString())

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
            stratTeamDataArray.getOrPut(stratJsonObject.get("event_key").asString) { hashMapOf() }.getOrPut(
                stratJsonObject.get("match").asInt){ hashMapOf() }.set(stratJsonObject.get("is_red_alliance").asBoolean, stratJsonObject.toString())

            println(stratFolder?.listFiles()?.toList()?.get(index).toString())
            println(stratFolder?.listFiles()?.toList()?.get(index)?.readText())
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
            pitsTeamDataArray.getOrPut(compKey){ hashMapOf() }.set(scoutedTeamNumber.value.betterParseInt(), jsonObject.toString())

            println(pitsFolder?.listFiles()?.toList()?.get(index).toString())
            println(pitsFolder?.listFiles()?.toList()?.get(index)?.readText())
        }
    }

    println("Loading pits image paths...")

    for ((outerIndex, value) in imagesFolder?.listFiles()?.withIndex()!!) {
        permPhotosList.getOrPut(compKey){ mutableListOf() }.add(value.path)
//        println(permPhotosList[outerIndex])
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

object TBAFileManager {
    fun readJsonFile(file: File, default: JSONObject = JSONObject()): JSONObject {
        if (!file.exists() || file.length() == 0L) return default
        return try {
            JSONObject(file.readText())
        } catch (e: Exception) {
            default
        }
    }

    fun writeJsonFile(file: File, json: JSONObject) {
        if (file.exists()) file.delete()
        file.createNewFile()
        file.writeText(json.toString(4))
    }
}



// Private helper functions to centralize JSON file handling.
private fun readJsonFile(file: File, default: JSONObject = JSONObject()): JSONObject {
    if (!file.exists() || file.length() == 0L) return default
    return try {
        JSONObject(file.readText())
    } catch (e: Exception) {
        default
    }
}

private fun writeJsonFile(file: File, json: JSONObject) {
    if (file.exists()) file.delete()
    file.createNewFile()
    file.writeText(json.toString(4))
}

// TBA methods using the new init and JSON helpers.
fun storeTBAMatchData(eventKey: String, data: JSONObject, eTag: String) {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    if (!file.exists() || file.length() == 0L) {
        file.createNewFile()
        file.writeText("{}")
    }
    val current = readJsonFile(file)
    if (current.optString("eTag") != eTag) {
        data.put("eTag", eTag)
        data.put("timestamp", System.currentTimeMillis())
        writeJsonFile(file, data)
    } else {
        current.put("timestamp", System.currentTimeMillis())
        writeJsonFile(file, current)
    }
}

fun storeTBATeamData(eventKey: String, data: JSONObject, eTag: String) {
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    if (!file.exists() || file.length() == 0L) {
        file.createNewFile()
        file.writeText("{}")
    }
    val current = readJsonFile(file)
    if (current.optString("eTag") != eTag) {
        data.put("eTag", eTag)
        data.put("timestamp", System.currentTimeMillis())
        writeJsonFile(file, data)
    } else {
        current.put("timestamp", System.currentTimeMillis())
        writeJsonFile(file, current)
    }
}

//fun updateTBAMatchDataTimestamp(context: Context, eventKey: String) {
//    ensureTBAInitialized(context)
//    val file = File(TBAMatchDataFolder, "${eventKey}.json")
//    if (file.exists()) {
//        val current = readJsonFile(file)
//        current.put("timestamp", System.currentTimeMillis())
//        writeJsonFile(file, current)
//    }
//}
fun updateTBAMatchDataTimestamp(eventKey: String) {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    val current = if (file.exists()) readJsonFile(file) else JSONObject()
    current.put("timestamp", System.currentTimeMillis())
    writeJsonFile(file, current)
}

fun updateTBATeamDataTimestamp(eventKey: String) {
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    val current = if (file.exists()) readJsonFile(file) else JSONObject()
    current.put("timestamp", System.currentTimeMillis())
    writeJsonFile(file, current)
}

fun getTBAMatchData(eventKey: String): JSONObject {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    return readJsonFile(file)
}

fun getTBATeamData(eventKey: String): JSONObject {
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    return readJsonFile(file)
}

fun getTBATeamDataETag(eventKey: String): String {
    return getTBATeamData(eventKey).optString("eTag", "")
}

fun getTBAMatchDataETag(eventKey: String): String {
    return getTBAMatchData(eventKey).optString("eTag", "")
}

fun isTBAMTeamDataSynced(eventKey: String): Boolean {
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    val json = readJsonFile(file)
    return file.exists() && ((json.optJSONArray("teams")?.length() ?: 0) > 0)
}

fun isTBAMatchDataSynced(eventKey: String): Boolean {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    if (!file.exists() || file.length() == 0L) return false
    return (readJsonFile(file).optJSONArray("matches")?.length() ?: 0) > 0
}

fun deleteTBAMatchData(eventKey: String) {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    if (file.exists()) file.delete()
}

fun deleteAllTBAMatchData() {
    TBAMatchDataFolder.listFiles()?.forEach { it.delete() }
}

fun deleteTBATeamData(eventKey: String) {
    val file = File(TBATeamDataFolder, "${eventKey}.json")
    if (file.exists()) file.delete()
}

fun deleteAllTBATeamData() {
    TBATeamDataFolder.listFiles()?.forEach { it.delete() }
}

fun isTBAMatchDataOld(eventKey: String): Boolean {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    if (file.exists()) {
        val json = readJsonFile(file)
        return System.currentTimeMillis() - json.optLong("timestamp", 0L) > 3600000
    }
    return false
}

fun getTBAMatchDataTimestamp(eventKey: String): Long {
    val file = File(TBAMatchDataFolder, "${eventKey}.json")
    return if (file.exists()) readJsonFile(file).optLong("timestamp", 0L) else 0L
}


fun sendMatchData(client: Client) {
    println("reached beginning of sendData")

    val gson = Gson()

    val array = teamDataArray.getOrPut(compKey) { hashMapOf() }
    for ((key, value) in array.entries) {
        for ((key1, value1) in value.entries) {
            val jsonObject = gson.fromJson(value1, JsonObject::class.java)

            client.sendData(jsonObject.toString(), "match")

            Log.i("Client", "Message Sent: ${jsonObject.toString()}")
        }
    }
}

fun sendStratData(client: Client) {
    val gson = Gson()

    val array = stratTeamDataArray.getOrPut(compKey) { hashMapOf() }
    for((key, value) in array.entries) {
        for((key1, value1) in value.entries) {
            val jsonObject = gson.fromJson(value1, JsonObject::class.java)

            client.sendData(jsonObject.toString(), "strat")

            Log.i("Client", "Message Sent: $jsonObject")

        }
    }

}

fun sendPitsData(client: Client) {

    val gson = Gson()

//    println(pitsTeamDataArray)
    val array = pitsTeamDataArray.getOrPut(compKey) { hashMapOf() }
    for((key, value) in array.entries) {
        println(pitsTeamDataArray)
        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "pit")

        Log.i("Client", "Message Sent: $jsonObject")
    }
}