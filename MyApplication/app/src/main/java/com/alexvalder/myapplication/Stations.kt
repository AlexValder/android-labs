package com.alexvalder.myapplication

enum class Stations {
    Park,
    Theatre,
    School1,
    School2,
    River;

    companion object {
        fun ofIndex(index: Int): Stations = values()[index]
    }
}