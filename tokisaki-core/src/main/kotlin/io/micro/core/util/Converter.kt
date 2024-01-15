package io.micro.core.util

fun <E, R> List<E>.converter(func: (E) -> R): List<R> = map(func)

@JvmName(name = "mutableConverter")
fun <E, R> MutableList<E>.converter(func: (E) -> R): List<R> = map(func)

fun <E, R> Set<E>.converter(func: (E) -> R): Set<R> = map(func).toSet()

@JvmName(name = "mutableConverter")
fun <E, R> MutableSet<E>.converter(func: (E) -> R): List<R> = map(func)
