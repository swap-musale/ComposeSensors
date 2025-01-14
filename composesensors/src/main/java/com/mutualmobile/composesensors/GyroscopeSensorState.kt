package com.mutualmobile.composesensors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Measures a device's rate of rotation in rad/s around each of the three physical axes
 * (x, y, and z).
 * @param xRotation Rate of rotation around the x axis. Defaults to 0f.
 * @param yRotation Rate of rotation around the y axis. Defaults to 0f.
 * @param zRotation Rate of rotation around the z axis. Defaults to 0f.
 * @param isAvailable Whether the current device has a gyroscope sensor. Defaults to false.
 * @param accuracy Accuracy factor of the gyroscope sensor. Defaults to 0.
 */
@Immutable
class GyroscopeSensorState internal constructor(
    val xRotation: Float = 0f,
    val yRotation: Float = 0f,
    val zRotation: Float = 0f,
    val isAvailable: Boolean = false,
    val accuracy: Int = 0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GyroscopeSensorState) return false

        if (xRotation != other.xRotation) return false
        if (yRotation != other.yRotation) return false
        if (zRotation != other.zRotation) return false
        if (isAvailable != other.isAvailable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = xRotation.hashCode()
        result = 31 * result + yRotation.hashCode()
        result = 31 * result + zRotation.hashCode()
        result = 31 * result + isAvailable.hashCode()
        return result
    }

    override fun toString(): String {
        return "GyroscopeSensorState(xRotation=$xRotation, yRotation=$yRotation, " +
                "zRotation=$zRotation, isAvailable=$isAvailable, accuracy=$accuracy)"
    }
}

/**
 * Creates and [remember]s an instance of [GyroscopeSensorState].
 * @param sensorDelay The rate at which the raw sensor data should be received.
 * Defaults to [SensorDelay.Normal].
 * @param onError Callback invoked on every error state.
 */
@Composable
fun rememberGyroscopeSensorState(
    sensorDelay: SensorDelay = SensorDelay.Normal,
    onError: (throwable: Throwable) -> Unit = {},
): GyroscopeSensorState {
    val sensorState = rememberSensorState(
        sensorType = SensorType.Gyroscope,
        sensorDelay = sensorDelay,
        onError = onError,
    )
    val gyroscopeSensorState = remember { mutableStateOf(GyroscopeSensorState()) }

    LaunchedEffect(
        key1 = sensorState,
        block = {
            val sensorStateValues = sensorState.data
            if (sensorStateValues.isNotEmpty()) {
                gyroscopeSensorState.value = GyroscopeSensorState(
                    xRotation = sensorStateValues[0],
                    yRotation = sensorStateValues[1],
                    zRotation = sensorStateValues[2],
                    isAvailable = sensorState.isAvailable,
                    accuracy = sensorState.accuracy
                )
            }
        }
    )

    return gyroscopeSensorState.value
}
