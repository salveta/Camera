package com.salvaperez.camerarecord.commons.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun Activity.open(cls: Class<*>, extras: Bundle? = null) {
    val intent = Intent(this, cls)
    extras?.let { intent.putExtras(it) }
    startActivity(intent)
}