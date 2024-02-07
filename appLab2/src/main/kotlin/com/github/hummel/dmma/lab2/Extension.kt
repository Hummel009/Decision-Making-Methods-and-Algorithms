package com.github.hummel.dmma.lab2

fun <T> Array<T>.randomElement(): T? = (if (isEmpty()) null else this[RANDOM.nextInt(size)])