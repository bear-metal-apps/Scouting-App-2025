package org.tahomarobotics.scouting

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class  ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context, fileName: String): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                fileName,
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,

            )
        }

        }
    }
