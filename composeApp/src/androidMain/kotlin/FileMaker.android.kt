import android.content.Context
import android.content.Context.USB_SERVICE
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.decodeBitmap
import android.hardware.usb.UsbConstants.USB_DIR_OUT
import android.hardware.usb.UsbManager
import android.hardware.usb.UsbRequest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import com.google.gson.Gson
import com.google.gson.JsonObject
import nodes.jsonObject
import nodes.permPhotosList
import nodes.photoArray
import nodes.pitsTeamDataArray
import nodes.teamDataArray
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.tahomarobotics.scouting.Client
import java.io.*
import java.nio.ByteBuffer

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

    pitsTeamDataArray.forEach { (key, value) ->
        val jsonObject = gson.fromJson(value, JsonObject::class.java)

        var bitmap: Bitmap? = null

        var index = 0
        while(true) {
            try {
                var file = File(Uri.parse(permPhotosList[index]).toString(), "Photo${index}.jpg")
                file.delete()
                file = Uri.parse(permPhotosList[index]).toFile()

                var bos = ByteArrayOutputStream()

                // Assuming you're inside a Composable function
                val contentResolver = context.contentResolver
                val uri = Uri.parse(permPhotosList[index])
                val bitmap = decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                bitmap.compress(Bitmap.CompressFormat.JPEG,0, bos)

                val byteArray = bos.toByteArray()

                file.writeBytes(byteArray)

                client.sendData(file)
                println("Image sent")

                index++
            } catch (e: IndexOutOfBoundsException) {
                println("reached")
                break
            }
        }

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