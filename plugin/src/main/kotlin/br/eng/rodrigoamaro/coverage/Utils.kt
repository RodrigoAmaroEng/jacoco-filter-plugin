package br.eng.rodrigoamaro.coverage

import java.io.File


fun listAllFoldersInside(folder: File): List<File> {
    return folder.folders()
        .flatMap {
            if (it.hasFoldersInside()) listAllFoldersInside(it).plus(it)
            else listOf(it)
        }
}

fun list(folder: File): List<File> {
    return folder.listFiles()
        .flatMap { if (it.isDirectory) list(it) else listOf(it) }
}

fun File.folders() = listFiles().filter { it.isDirectory }

fun File.hasFoldersInside() = isDirectory && listFiles().any { it.isDirectory }