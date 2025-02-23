package org.tahomarobotics.scouting

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import imagesFolder
import java.io.File

class  ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context, teamNumber: Int, fileName: String): Uri {
            val teamFile = File(imagesFolder, "Team${teamNumber}PhotoFolder")
            if(!teamFile.exists()) {
                teamFile.mkdirs()
            }

            val file = File(
                teamFile, "$fileName.jpg"
            )
            file.delete()
            file.createNewFile()

            return file.path.toUri()

//            val authority = context.packageName + ".fileprovider"
//            return getUriForFile(
//                context,
//                authority,
//                file,
//            )
        }

    }
}