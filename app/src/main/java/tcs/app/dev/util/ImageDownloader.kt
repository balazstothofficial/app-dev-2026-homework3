package tcs.app.dev.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageDownloader(
    val connectionTimeout: Duration,
    val readTimeout: Duration,
    val maxImageBytes: Long
) {
    suspend fun getImage(
        url: URL,
        targetWidth: Int,
        targetHeight: Int
    ): Result<Bitmap> = withContext(Dispatchers.IO) {
        try {
            val bytes = download(url)
            Result.success(decode(bytes, targetWidth, targetHeight))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun download(url: URL): ByteArray {
        if (!url.protocol.equals("https", ignoreCase = true)) {
            throw SecurityException("Only HTTPS is allowed: $url")
        }

        val connection = (url.openConnection() as HttpURLConnection).apply {
            connectTimeout = connectionTimeout.toInt(DurationUnit.MILLISECONDS)
            readTimeout = this@ImageDownloader.readTimeout.toInt(DurationUnit.MILLISECONDS)
            useCaches = false
        }

        try {
            val code = connection.responseCode
            if (code !in 200..299) {
                connection.errorStream?.close()
                throw IOException("HTTP $code ${connection.responseMessage} for $url")
            }

            return connection.inputStream.use { input ->
                readAllBytesCapped(input, maxImageBytes)
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun decode(bytes: ByteArray, targetWidth: Int, targetHeight: Int): Bitmap {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
            throw IOException("Failed to read image bounds")
        }

        val options = BitmapFactory.Options().apply {
            inSampleSize = calculateSampleSize(
                sourceWidth = bounds.outWidth,
                sourceHeight = bounds.outHeight,
                targetWidth = targetWidth,
                targetHeight = targetHeight
            )
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            ?: throw IOException("Failed to decode bitmap")
    }

    private fun readAllBytesCapped(input: InputStream, cap: Long): ByteArray {
        val buffer = ByteArray(8 * 1024)

        return ByteArrayOutputStream().use { out ->
            var total: Long = 0

            while (true) {
                val n = input.read(buffer)
                if (n == -1) break
                total += n
                if (total > cap) throw IOException("Image exceeds cap of $cap bytes")
                out.write(buffer, 0, n)
            }

            out.toByteArray()
        }
    }

    private fun calculateSampleSize(
        sourceWidth: Int,
        sourceHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        var sampleSize = 1
        if (targetWidth <= 0 || targetHeight <= 0) return sampleSize

        val halfHeight = sourceHeight / 2
        val halfWidth = sourceWidth / 2

        while (targetHeight <= halfHeight / sampleSize && targetWidth <= halfWidth / sampleSize) {
            sampleSize *= 2
        }

        return sampleSize.coerceAtLeast(1)
    }

    companion object {
        val TCSAppDevImages = ImageDownloader(
            connectionTimeout = 5.seconds,
            readTimeout = 10.seconds,
            maxImageBytes = 10 * 1024 * 1024
        )
    }
}
