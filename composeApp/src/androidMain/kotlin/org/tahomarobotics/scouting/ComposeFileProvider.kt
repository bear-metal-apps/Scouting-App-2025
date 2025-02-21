package org.tahomarobotics.scouting

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class  ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context, teamNumber: Int, fileName: String): Uri {
            val directory = File(context.cacheDir, "images")
            if(!directory.exists()) {
                directory.mkdirs()
            }

            val teamFile = File(directory, "Pits$teamNumber")
            if(!teamFile.exists()) {
                teamFile.mkdirs()
            }

            val file = File(
                teamFile, "$fileName.jpg"
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