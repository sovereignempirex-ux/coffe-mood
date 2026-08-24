package com.example.ui.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

/**
 * MoodSoundManager - Ultra-lightweight synthesized audio & haptic engine.
 * Generates crisp, pleasant micro-sounds entirely in memory via PCM synthesis.
 * Zero external assets needed, 0MB disk space, 100% smooth on low-end devices.
 */
object MoodSoundManager {
    private const val SAMPLE_RATE = 22050
    private var isSoundEnabled = true
    private var isHapticEnabled = true
    private val scope = CoroutineScope(Dispatchers.Default)

    // Cached PCM Audio Buffers for instantaneous playback
    private val bufferClick: ByteArray by lazy { generateClickPCM() }
    private val bufferPop: ByteArray by lazy { generatePopPCM() }
    private val bufferAddToCart: ByteArray by lazy { generateAddToCartPCM() }
    private val bufferFavorite: ByteArray by lazy { generateFavoritePCM() }
    private val bufferSuccess: ByteArray by lazy { generateOrderSuccessPCM() }
    private val bufferCoffeeBrew: ByteArray by lazy { generateCoffeeBrewPCM() }
    private val bufferAdminUnlock: ByteArray by lazy { generateAdminUnlockPCM() }
    private val bufferTick: ByteArray by lazy { generateTickPCM() }

    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
    }

    fun isSoundEnabled(): Boolean = isSoundEnabled

    fun setHapticEnabled(enabled: Boolean) {
        isHapticEnabled = enabled
    }

    fun isHapticEnabled(): Boolean = isHapticEnabled

    fun playClick(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferClick)
        context?.let { triggerHaptic(it, 10) }
    }

    fun playPop(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferPop)
        context?.let { triggerHaptic(it, 15) }
    }

    fun playAddToCart(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferAddToCart)
        context?.let { triggerHaptic(it, 25) }
    }

    fun playFavorite(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferFavorite)
        context?.let { triggerHaptic(it, 20) }
    }

    fun playOrderSuccess(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferSuccess)
        context?.let { triggerHaptic(it, 50) }
    }

    fun playCoffeeBrew(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferCoffeeBrew)
        context?.let { triggerHaptic(it, 30) }
    }

    fun playAdminUnlock(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferAdminUnlock)
        context?.let { triggerHaptic(it, 35) }
    }

    fun playTick(context: Context? = null) {
        if (!isSoundEnabled) return
        playPcmAsync(bufferTick)
        context?.let { triggerHaptic(it, 8) }
    }

    private fun playPcmAsync(pcmData: ByteArray) {
        scope.launch {
            try {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                val audioFormat = AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(pcmData.size)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(pcmData, 0, pcmData.size)
                audioTrack.play()

                // Release track after playback duration
                val durationMs = (pcmData.size / 2 * 1000L) / SAMPLE_RATE
                kotlinx.coroutines.delay(durationMs + 50)
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {
                // Graceful fallback for low-spec audio servers
            }
        }
    }

    private fun triggerHaptic(context: Context, durationMs: Long) {
        if (!isHapticEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {
            // Ignored on devices without vibrator motor
        }
    }

    // ==========================================
    // PCM Waveform Synthesizers (16-bit Mono)
    // ==========================================

    private fun generateClickPCM(): ByteArray {
        val duration = 0.035 // 35 ms
        val numSamples = (duration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)
        val freq = 880.0 // A5

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val envelope = exp(-t * 120.0) // fast exponential decay
            val sample = (sin(2.0 * PI * freq * t) * envelope * 0.45 * Short.MAX_VALUE).toInt()
            val shortVal = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generatePopPCM(): ByteArray {
        val duration = 0.06 // 60 ms
        val numSamples = (duration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val freq = 550.0 + (t / duration) * 450.0 // rising pitch 550Hz -> 1000Hz
            val envelope = sin(PI * (t / duration)) // smooth bell curve
            val sample = (sin(2.0 * PI * freq * t) * envelope * 0.5 * Short.MAX_VALUE).toInt()
            val shortVal = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generateAddToCartPCM(): ByteArray {
        // Uplifting 2-tone melodic chime: C6 (1046Hz) -> E6 (1318Hz)
        val note1Duration = 0.08
        val note2Duration = 0.12
        val totalDuration = note1Duration + note2Duration
        val numSamples = (totalDuration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val (freq, noteT, noteDur) = if (t < note1Duration) {
                Triple(1046.5, t, note1Duration)
            } else {
                Triple(1318.5, t - note1Duration, note2Duration)
            }
            val envelope = exp(-noteT * 18.0) * sin(PI * (noteT / noteDur).coerceIn(0.0, 1.0))
            val sample = (sin(2.0 * PI * freq * t) * envelope * 0.6 * Short.MAX_VALUE).toInt()
            val shortVal = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generateFavoritePCM(): ByteArray {
        // Sparkle shimmer chime: G6 (1568Hz) -> C7 (2093Hz) with warm harmonic
        val duration = 0.18
        val numSamples = (duration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val freq1 = 1568.0
            val freq2 = 2093.0
            val env1 = exp(-t * 22.0)
            val env2 = exp(-((t - 0.04).coerceAtLeast(0.0)) * 18.0)
            val wave = (sin(2.0 * PI * freq1 * t) * env1 * 0.35) +
                    (sin(2.0 * PI * freq2 * t) * env2 * 0.35)
            val shortVal = (wave * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generateOrderSuccessPCM(): ByteArray {
        // Triumphant 4-note harp arpeggio: C5 (523Hz), E5 (659Hz), G5 (784Hz), C6 (1046Hz)
        val noteDur = 0.08
        val totalDuration = noteDur * 4 + 0.15
        val numSamples = (totalDuration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)
        val notes = doubleArrayOf(523.25, 659.25, 783.99, 1046.50)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            var sumWave = 0.0
            for (n in notes.indices) {
                val startT = n * noteDur
                if (t >= startT) {
                    val localT = t - startT
                    val env = exp(-localT * 12.0)
                    sumWave += sin(2.0 * PI * notes[n] * localT) * env * 0.25
                }
            }
            val shortVal = (sumWave * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generateCoffeeBrewPCM(): ByteArray {
        // Warm harmonic percolating water & aroma bubble wave
        val duration = 0.22
        val numSamples = (duration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val f1 = 440.0 + sin(t * 50.0) * 80.0
            val f2 = 880.0 + cos(t * 30.0) * 120.0
            val env = (1.0 - t / duration) * (0.5 + 0.5 * sin(t * 120.0))
            val wave = (sin(2.0 * PI * f1 * t) * 0.3 + sin(2.0 * PI * f2 * t) * 0.2) * env
            val shortVal = (wave * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generateAdminUnlockPCM(): ByteArray {
        // High-tech security unlock dual beep: 1200Hz -> 1800Hz
        val duration = 0.15
        val numSamples = (duration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val freq = if (t < 0.07) 1200.0 else 1800.0
            val env = exp(-(t % 0.07) * 25.0)
            val wave = sin(2.0 * PI * freq * t) * env * 0.4
            val shortVal = (wave * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }

    private fun generateTickPCM(): ByteArray {
        val duration = 0.02
        val numSamples = (duration * SAMPLE_RATE).toInt()
        val buffer = ByteArray(numSamples * 2)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            val env = exp(-t * 200.0)
            val wave = sin(2.0 * PI * 1400.0 * t) * env * 0.35
            val shortVal = (wave * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            buffer[i * 2] = (shortVal.toInt() and 0xFF).toByte()
            buffer[i * 2 + 1] = ((shortVal.toInt() shr 8) and 0xFF).toByte()
        }
        return buffer
    }
}
