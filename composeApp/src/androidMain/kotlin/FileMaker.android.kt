import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonObject
import nodes.TeamMatchStartKey
import nodes.jsonObject
import nodes.pitsTeamDataArray
import nodes.teamDataArray
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.tahomarobotics.scouting.Client
import java.io.*
import java.lang.Integer.parseInt

var matchFolder : File? = null

fun createScoutMatchDataFolder(context: Context) {
    matchFolder = File(context.filesDir, "ScoutMatchDataFolder")

    if(!matchFolder!!.exists()) {
        matchFolder!!.mkdirs()
        println("Made match data folder")
    } else {
        println("Match data folder found")
    }
}

fun createScoutMatchDataFile(context: Context, match: String, team: Int, data: String) {
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

fun loadMatchDataFiles(context: Context) {
    val gson = Gson()

    println("loading files...")
    for((index) in (matchFolder?.listFiles()?.withIndex()!!)) {
        jsonObject = gson.fromJson(matchFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java)
        teamDataArray[TeamMatchStartKey(parseInt(jsonObject.get("match").asString), jsonObject.get("team").asInt, jsonObject.get("robotStartPosition").asInt)] = jsonObject.toString()

        println(matchFolder?.listFiles()?.toList()?.get(index).toString())
    }
}

fun deleteScoutMatchData() {
    repeat(10) {
        try {
            for((index) in matchFolder?.listFiles()?.withIndex()!!) {
                matchFolder?.listFiles()?.get(index)?.deleteRecursively()
            }
        } catch (e: IndexOutOfBoundsException) {}
    }
    matchFolder?.delete()
}

fun createFile(context: Context) {
    val file = File(context.filesDir, "match_data.json")
    file.delete()
    file.createNewFile()
    val writer = FileWriter(file)

    matchData?.toString(1)?.let { writer.write(it) }
    writer.close()

    val teamFile = File(context.filesDir,"team_data.json")
    teamFile.delete()
    teamFile.createNewFile()
    val teamWriter = FileWriter(teamFile)

    teamData?.toString(1)?.let { teamWriter.write(it) }
    teamWriter.close()
}

fun openFile(context: Context) {
    matchData = try {
        JSONObject(String(FileInputStream(File(context.filesDir, "match_data.json")).readBytes()))
    } catch (e: JSONException) {
        null
    }
    teamData = try {
        JSONObject(String(FileInputStream(File(context.filesDir, "match_data.json")).readBytes()))
    } catch (e: JSONException) {
        null
    }
    openScoutFile(context)
}

fun openScoutFile(context: Context) {

    var tempScoutData = JSONObject()
    try {
        tempScoutData =
            JSONObject(String(FileInputStream(File(context.filesDir, "match_scouting_data.json")).readBytes()))
    } catch (_: JSONException) {

    } catch (_: FileNotFoundException) {
        return
    }

    repeat (6) {
        try {
            val array = tempScoutData[it.toString()] as JSONArray
            for (i in 0..<array.length()) {
//                teamDataArray.putIfAbsent(it, HashMap())
//                teamDataArray[it]?.set(i, array[i] as String)
            }
        } catch (_: JSONException) {}
    }

}


fun exportScoutData(context: Context) {

//    val file = File(context.filesDir, "match_scouting_data.json")
//    file.delete()
//    file.createNewFile()
//    val jsonObject = getJsonFromMatchHash()

//    matchScoutArray.values
//    val writer = FileWriter(file)
//    writer.write(jsonObject.toString(1))
//    writer.close()
}



fun deleteFile(context: Context){
    val file = File(context.filesDir, "match_scouting_data.json")
    file.delete()
}

/**
 *@param scoutingType should be "match", "strat", or "pit"
 */
fun sendData(context: Context, client: Client) {
    exportScoutData(context)

    val gson = Gson()

    teamDataArray.forEach {
        val jsonObject = gson.fromJson(it.toString(), JsonObject::class.java)

        client.sendData(jsonObject.toString(), "match")

        Log.i("Client", "Message Sent: ${jsonObject.toString()}")
    }

    pitsTeamDataArray.forEach {
        val jsonObject = gson.fromJson(it.toString(), JsonObject::class.java)

        client.sendData(jsonObject.toString(), "pit")

        Log.i("Client", "Message Sent: ${jsonObject.toString()}")
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun sendDataUSB(context: Context, deviceName: String) {
//    exportScoutData(context)
//
//    val jsonObject = getJsonFromMatchHash()
//    val manager = context.getSystemService(USB_SERVICE) as UsbManager
//
//    val deviceList = manager.deviceList
//
//    val device = deviceList[deviceName]
//    val connection = manager.openDevice(device)
//
//    val endpoint = device?.getInterface(0)?.getEndpoint(5)
//    if (endpoint?.direction == USB_DIR_OUT) {
//        Log.i("USB", "Dir is out")
//    } else {
//        Log.i("USB", "Dir is in")
//        return
//    }
//
//
//    val request = UsbRequest()
//    request.initialize(connection, endpoint)
//
//    val buffer = ByteBuffer.wrap(jsonObject.toString().encodeToByteArray())
//
//    request.queue(buffer)
}