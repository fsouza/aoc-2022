package dev.fsouza.aoc

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.streams.asSequence

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val grid =
            reader.lineSequence().map { it.asSequence().map { it.digitToInt() }.toList() }.toList()
    val rows = grid.size
    val cols = grid[0].size
    var count = 0

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            val tree = grid[i][j]

            if (i == 0 || tree > maxOnNorth(grid, i, j)) {
                count++
            } else if (j == cols - 1 || tree > maxOnEast(grid, i, j)) {
                count++
            } else if (i == rows - 1 || tree > maxOnSouth(grid, i, j)) {
                count++
            } else if (j == 0 || tree > maxOnWest(grid, i, j)) {
                count++
            }
        }
    }

    println(count)
}

fun maxOnNorth(grid: List<List<Int>>, row: Int, col: Int) =
    grid.subList(0, row).asSequence().map { it[col] }.max()

fun maxOnEast(grid: List<List<Int>>, row: Int, col: Int) =
    grid[row].subList(col + 1, grid[row].size).max()

fun maxOnSouth(grid: List<List<Int>>, row: Int, col: Int) =
    grid.subList(row + 1, grid.size).asSequence().map { it[col] }.max()

fun maxOnWest(grid: List<List<Int>>, row: Int, col: Int) =
    grid[row].subList(0, col).max()
