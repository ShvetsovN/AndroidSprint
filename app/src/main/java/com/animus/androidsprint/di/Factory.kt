package com.animus.androidsprint.di

interface Factory<T> {
    fun create(): T
}