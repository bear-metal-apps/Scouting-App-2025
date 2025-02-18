package composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.decodeBitmap
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.compiler.plugins.kotlin.write
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

var rootDirectory : File = File("Scouting Data")

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun downloadPitsPhotos(
    context: Context,
    photoArray: SnapshotStateList<Uri>,
    teamNumber: String,
    photoAmount: Int
) {
//    var file = File(context.cacheDir, "photo_0.jpg")
//    context.cacheDir

    rootDirectory = File(context.getExternalFilesDir(null),"Scouting Data")

    if(!rootDirectory.exists()){
        rootDirectory.mkdirs()
//        rootDirectory.createNewFile()
    }
//    rootDirectory.isDirectory

    val pitsFolder = File(rootDirectory,"Pits")

    if(!pitsFolder.exists()){
        pitsFolder.mkdirs()
//        pitsFolder.createNewFile()
    }
//    pitsFolder.isDirectory

    val pitsPhotosFolder = File(pitsFolder,"Photos")

    if(!pitsPhotosFolder.exists()){
        pitsPhotosFolder.mkdirs()
//        pitsPhotosFolder.createNewFile()
    }
//    pitsFolder.isDirectory

    val teamFolder = File(pitsPhotosFolder,"Team $teamNumber")
    teamFolder.delete()
    teamFolder.mkdirs()

    //var byte = ByteArray(photoArray[0].size)

//    for((index, value) in photoArray.withIndex()) {
//        val file = File(teamFolder, "Photo${index+1}.bmp")
//        file.delete()
//        file.createNewFile()

//        var bos = ByteArrayOutputStream()
//
//        // Assuming you're inside a Composable function
//        val contentResolver = LocalContext.current.contentResolver
//        val bitmap = decodeBitmap(ImageDecoder.createSource(contentResolver, photoArray[0]))
//        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos)

//        val byteArray = bos.toByteArray()
//
//        val fileOutputStream = FileOutputStream(file)
//        fileOutputStream.write(byteArray) // Stores ByteArray that contains the image in the file
//        fileOutputStream.close()

//    }

}