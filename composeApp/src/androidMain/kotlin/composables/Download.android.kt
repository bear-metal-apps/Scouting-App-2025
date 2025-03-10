package composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream
import java.io.File

var pitsFolder : File = File("Pits")

var pitsPhotosList : List<File> = listOf()

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun downloadPitsPhotos(
    context: Context,
    photoArray: SnapshotStateList<Uri>,
    teamNumber: String,
    photoAmount: Int
) {

    val pitsFolder = File(context.filesDir,"Pits")
    if(!pitsFolder.exists()){
        pitsFolder.mkdirs()
    }

    val pitsPhotosFolder = File(pitsFolder,"Photos")
    if(!pitsPhotosFolder.exists()){
        pitsPhotosFolder.mkdirs()
    }

    val teamFolder = File(pitsPhotosFolder,"Team $teamNumber")
    teamFolder.delete()
    teamFolder.mkdirs()

    for((index, value) in photoArray.withIndex()) {
        val file = File(teamFolder, "Photo${index+1}.png")
        file.delete()
        file.createNewFile()

        var bos = ByteArrayOutputStream()

        // Assuming you're inside a Composable function
        val contentResolver = LocalContext.current.contentResolver
        val bitmap = decodeBitmap(ImageDecoder.createSource(contentResolver, photoArray[0]))
        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos)

        val byteArray = bos.toByteArray()

        file.writeBytes(byteArray)
    }

}