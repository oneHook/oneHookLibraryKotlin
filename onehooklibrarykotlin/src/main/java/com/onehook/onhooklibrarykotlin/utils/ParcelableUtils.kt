package com.onehook.onhooklibrarykotlin.utils

import android.os.Parcel
import android.os.Parcelable

fun toByteArray(parcelable: Parcelable): ByteArray {
    val parcel = Parcel.obtain()

    parcelable.writeToParcel(parcel, 0)

    val result = parcel.marshall()

    parcel.recycle()

    return result
}

fun <T> toParcelable(
    bytes: ByteArray,
    creator: Parcelable.Creator<T>
): T {
    val parcel = Parcel.obtain()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)
    val result = creator.createFromParcel(parcel)
    parcel.recycle()
    return result
}
