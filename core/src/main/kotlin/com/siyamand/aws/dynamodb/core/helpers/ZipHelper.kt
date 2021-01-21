package com.siyamand.aws.dynamodb.core.helpers

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipHelper {
    companion object{
        fun zip(code: String, fileName: String): ByteArray {
            val bufferOutputStream = ByteArrayOutputStream()
            val zipOutputStream = ZipOutputStream(bufferOutputStream)
            zipOutputStream.putNextEntry(ZipEntry(fileName))
            zipOutputStream.write(code.toByteArray(StandardCharsets.UTF_8))
            zipOutputStream.closeEntry()
            zipOutputStream.flush()
            zipOutputStream.finish()
            val output = bufferOutputStream.toByteArray()
            bufferOutputStream.close()
            return output
        }
    }
}