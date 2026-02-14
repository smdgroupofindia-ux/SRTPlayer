package com.example.streamplayer

import android.net.Uri
import androidx.media3.common.C
import androidx.media3.datasource.BaseDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import io.github.thibaultbee.srtdroid.enums.SockOpt
import io.github.thibaultbee.srtdroid.enums.Transtype
import io.github.thibaultbee.srtdroid.models.Socket
import java.io.IOException
import java.nio.ByteBuffer

/**
 * Optimized SRT DataSource for Media3/ExoPlayer.
 */
class SrtDataSource(
    private val srtUrl: String,
    private val port: Int,
    private val passPhrase: String? = null
) : BaseDataSource(true) {

    private var socket: Socket? = null
    private var leftoverBuffer: ByteBuffer? = null
    private val PAYLOAD_SIZE = 1316

    override fun open(dataSpec: DataSpec): Long {
        socket = Socket().apply {
            setSockFlag(SockOpt.TRANSTYPE, Transtype.LIVE)
            setSockFlag(SockOpt.PAYLOADSIZE, PAYLOAD_SIZE)
            passPhrase?.let { setSockFlag(SockOpt.PASSPHRASE, it) }
            connect(srtUrl, port)
        }
        return C.LENGTH_UNSET.toLong()
    }

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (length == 0) return 0
        
        val currentSocket = socket ?: throw IOException("Socket is null")
        var totalRead = 0

        // 1. Fill from leftover buffer first
        leftoverBuffer?.let {
            val toCopy = minOf(it.remaining(), length)
            it.get(buffer, offset, toCopy)
            totalRead += toCopy
            if (!it.hasRemaining()) leftoverBuffer = null
        }

        // 2. If we still need more data, read from socket
        if (totalRead < length) {
            try {
                val result = currentSocket.recv(PAYLOAD_SIZE)
                val size = result.first
                val data = result.second
                
                if (size <= 0) return if (totalRead > 0) totalRead else -1
                
                val remainingToFill = length - totalRead
                val toCopy = minOf(size, remainingToFill)
                
                System.arraycopy(data, 0, buffer, offset + totalRead, toCopy)
                totalRead += toCopy
                
                // Store leftovers
                if (size > remainingToFill) {
                    val leftoverSize = size - remainingToFill
                    leftoverBuffer = ByteBuffer.allocate(leftoverSize)
                    leftoverBuffer?.put(data, remainingToFill, leftoverSize)
                    leftoverBuffer?.flip()
                }
            } catch (e: Exception) {
                return if (totalRead > 0) totalRead else -1
            }
        }

        return totalRead
    }

    override fun getUri(): Uri = Uri.parse("srt://$srtUrl:$port")

    override fun close() {
        socket?.close()
        socket = null
        leftoverBuffer = null
    }
}

class SrtDataSourceFactory(
    private val srtUrl: String,
    private val port: Int,
    private val passPhrase: String? = null
) : DataSource.Factory {
    override fun createDataSource(): DataSource = SrtDataSource(srtUrl, port, passPhrase)
}
