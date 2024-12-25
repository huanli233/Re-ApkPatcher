package com.huanli233.dex_mixin.core.utils

fun <T> MutableSet<T>.addOrReplace(element: T, areEqual: (T, T) -> Boolean): Boolean {
    for (existingElement in this) {
        if (areEqual(existingElement, element)) {
            this.remove(existingElement)
            this.add(element)
            return true
        }
    }
    return this.add(element)
}

fun <T> MutableSet<T>.addOrReplaceAll(elements: Iterable<T>, areEqual: (T, T) -> Boolean): Boolean {
    var modified = false
    for (element in elements) {
        if (this.addOrReplace(element, areEqual)) {
            modified = true
        }
    }
    return modified
}
