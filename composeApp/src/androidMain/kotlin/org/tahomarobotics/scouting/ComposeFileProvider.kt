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
            val file = File(
                imagesFolder, "$fileName.jpg"
            )
            file.delete()
            file.createNewFile()

            val authority = context.packageName + ".fileprovider"

            return getUriForFile(
                context,
                authority,
                file,
            )
        }

    }
}