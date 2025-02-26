import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonObject
import nodes.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.tahomarobotics.scouting.Client
import java.io.*
import java.lang.Integer.parseInt

var matchFolder : File? = null
var stratFolder : File? = null
var pitsFolder: File? = null

var imagesFolder: File? = null

fun createScoutMatchDataFolder(context: Context) {
    matchFolder = File(context.filesDir, "ScoutMatchDataFolder")

    if(!matchFolder!!.exists()) {
        matchFolder!!.mkdirs()
        println("Made match data folder")
    } else {
        println("Match data folder found")
    }
}

fun createScoutStratDataFolder(context: Context) {
    stratFolder = File(context.filesDir, "ScoutStratDataFolder")

    if(!stratFolder!!.exists()) {
        stratFolder!!.mkdirs()
        println("Made strat data folder")
    } else {
        println("Strat data folder found")
    }
}

fun createScoutPitsDataFolder(context: Context) {
    pitsFolder = File(context.filesDir, "ScoutPitsDataFolder")

    if(!pitsFolder!!.exists()) {
        pitsFolder!!.mkdirs()
        println("Made pits data folder")
    } else {
        println("Pits data folder found")
    }

    imagesFolder = File(context.filesDir, "images")

    if(!imagesFolder!!.exists()) {
        imagesFolder!!.mkdirs()
        println("Made pits images folder")
    } else {
        println("Pits images folder found")
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

fun createScoutStratDataFile(context: Context, match: String, isRed: Boolean, data: String) {
    val file = File(stratFolder, "Match${match}${if(isRed) "RedAlliance" else "BlueAlliance"}.json")
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

fun createScoutPitsDataFile(context: Context, team: Int, data: String) {
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

//fun createScoutPitsImageLocationsFile(context: Context, data: String) {
//    val file = File(imagesFolder, "ImageLocations.json")
//    file.delete()
//    file.createNewFile()
//
//    file.writeText(data)
//
//    file.forEachLine {
//        try {
//            println("Saved file ImageLocations.json: $it")
//        } catch (e: Exception) {
//            println(e.message)
//        }
//    }
//
//}


fun loadMatchDataFiles(context: Context) {
    val gson = Gson()

    println("loading match files...")
    for((index) in (matchFolder?.listFiles()?.withIndex()!!)) {
        if(gson.fromJson(matchFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java) != null) {
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

fun loadStratDataFiles(context: Context) {
    val gson = Gson()

    println("loading strat files...")
    for((index) in (stratFolder?.listFiles()?.withIndex()!!)) {
        if(gson.fromJson(stratFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java) != null) {
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

fun loadPitsDataFiles(context: Context) {
    val gson = Gson()

    println("loading pits files...")
    for((index, value) in (pitsFolder?.listFiles()?.withIndex()!!)) {
        if(value?.name != "ImagesFolder" && gson.fromJson(pitsFolder?.listFiles()?.toList()?.get(index)?.readText(), JsonObject::class.java) != null) {
            jsonObject = gson.fromJson(
                pitsFolder?.listFiles()?.toList()?.get(index)?.readText(),
                JsonObject::class.java
            )
            pitsTeamDataArray[
                jsonObject.get("team").asInt
            ] = jsonObject.toString()

            println(pitsFolder?.listFiles()?.toList()?.get(index).toString())
            println(pitsTeamDataArray[
                jsonObject.get("team").asInt
            ])
        }
    }

    println("Loading pits image paths...")

    for ((outerIndex, value) in imagesFolder?.listFiles()?.withIndex()!!) {
        permPhotosList.add(value.path)
        println(permPhotosList[outerIndex])
    }
}


fun deleteScoutMatchData() {
    repeat(10) {
        try {
            for((index) in matchFolder?.listFiles()?.withIndex()!!) {
                matchFolder?.listFiles()?.get(index)?.deleteRecursively()
            }
            teamDataArray.clear()
        } catch (e: IndexOutOfBoundsException) {}
    }
}

fun deleteScoutPitsData() {
    pitsTeamDataArray.clear()
    permPhotosList.clear()
    repeat(10) {
        try {
            for((index) in pitsFolder?.listFiles()?.withIndex()!!) {
                if(pitsFolder?.listFiles()?.toList()?.get(index)?.name != "ImagesFolder") {
                    pitsFolder?.listFiles()?.get(index)?.deleteRecursively()
                }
            }
            for((index, value) in imagesFolder?.listFiles()?.withIndex()!!) {
                value.deleteRecursively()
            }
        } catch (e: IndexOutOfBoundsException) {}
    }
}

fun deleteScoutStratData() {
    repeat(10) {
        try {
            for((index) in stratFolder?.listFiles()?.withIndex()!!) {
                stratFolder?.listFiles()?.get(index)?.deleteRecursively()
            }
            stratTeamDataArray.clear()
        } catch (e: IndexOutOfBoundsException) {}
    }
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
fun sendMatchData(context: Context, client: Client) {
//    println("reached beginning of sendData")
    exportScoutData(context) // does nothing

    val gson = Gson()

    for((key, value) in teamDataArray.entries) {
        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "match")

        Log.i("Client", "Message Sent: ${jsonObject.toString()}")
    }

}

fun sendStratData(context: Context, client: Client) {
//    println("reached beginning of sendData")
    exportScoutData(context) // does nothing

    val gson = Gson()

    for((key, value) in stratTeamDataArray.entries) {
        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "strat")

        Log.i("Client", "Message Sent: ${jsonObject.toString()}")
    }

}

fun sendPitsData(context: Context, client: Client) {
    exportScoutData(context) // does nothing

    val gson = Gson()

    for((key, value) in pitsTeamDataArray.entries) {

        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        client.sendData(jsonObject.toString(), "pit")

        Log.i("Client", "Message Sent: ${jsonObject}")

//        var bitmap: Bitmap? = null

//        println(permPhotosList.toString())
//        for((index) in permPhotosList.withIndex()) {
//            println("reached2")
//            var file = File(ComposeFileProvider.getImageUri(context, parseInt(jsonObject.get("scoutedTeamNumber").asString), "Photo${index}").toString())
////            file.delete()
////            file = Uri.parse(permPhotosList[index]).toFile()
//
//            var bos = ByteArrayOutputStream()
//
//            // Assuming you're inside a Composable function
//            val contentResolver = context.contentResolver
//            val uri = /*Uri.parse(permPhotosList[index])*/ComposeFileProvider.getImageUri(context, parseInt(jsonObject.get("scoutedTeamNumber").asString), "Photo${index}")
//            println(uri.toString())
//            val bitmap = decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
//            bitmap.compress(Bitmap.CompressFormat.JPEG,0, bos)
//
//            val byteArray = bos.toByteArray()
//
//            file.writeBytes(byteArray)
//
//            client.sendData(file)
//            println("Image sent")
//        }
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