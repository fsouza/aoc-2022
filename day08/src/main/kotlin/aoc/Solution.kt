package dev.fsouza.aoc

import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val grid =
        reader.lineSequence().map { it.asSequence().map { it.digitToInt() }.toList() }.toList()

    println("Part 1: ${calcPart1(grid)}")
    println("Part 2: ${calcPart2(grid)}")
}

fun calcPart1(grid: List<List<Int>>): Int {
    val rows = grid.size
    val cols = grid[0].size

    return indices(grid)
        .map {
            val tree = grid[it.first][it.second]

            when {
                it.first == 0 || tree > maxOnNorth(grid, it.first, it.second) -> 1
                it.second == cols - 1 || tree > maxOnEast(grid, it.first, it.second) -> 1
                it.first == rows - 1 || tree > maxOnSouth(grid, it.first, it.second) -> 1
                it.second == 0 || tree > maxOnWest(grid, it.first, it.second) -> 1
                else -> 0
            }
        }
        .sum()
}

fun calcPart2(grid: List<List<Int>>) =
    indices(grid).map { scenicScore(grid, it.first, it.second) }.max()

fun indices(grid: List<List<Int>>): Sequence<Pair<Int, Int>> {
    val rows = grid.size
    val cols = grid[0].size

    return generateSequence(Pair(0, 0)) { (row, col) ->
        if (row == rows - 1 && col == cols - 1) null
        else if (col == cols - 1) Pair(row + 1, 0) else Pair(row, col + 1)
    }
}

fun max(x: Int, y: Int) = if (x > y) x else y

fun maxOnNorth(grid: List<List<Int>>, row: Int, col: Int) =
    grid.subList(0, row).asSequence().map { it[col] }.max()

fun maxOnEast(grid: List<List<Int>>, row: Int, col: Int) =
    grid[row].subList(col + 1, grid[row].size).max()

fun maxOnSouth(grid: List<List<Int>>, row: Int, col: Int) =
    grid.subList(row + 1, grid.size).asSequence().map { it[col] }.max()

fun maxOnWest(grid: List<List<Int>>, row: Int, col: Int) = grid[row].subList(0, col).max()

fun scenicScore(grid: List<List<Int>>, row: Int, col: Int): Int {
    val tree = grid[row][col]
    val rows = grid.size
    val cols = grid[0].size

    val northIndices =
        generateSequence(row - 1) { if (it > 0) it - 1 else null }
            .filter { it >= 0 }
            .takeWhile { grid[it][col] < tree }
            .toList()
    val eastIndices =
        generateSequence(col + 1) { if (it < cols - 1) it + 1 else null }
            .filter { it < cols }
            .takeWhile { grid[row][it] < tree }
            .toList()
    val southIndices =
        generateSequence(row + 1) { if (it < rows - 1) it + 1 else null }
            .filter { it < rows }
            .takeWhile { grid[it][col] < tree }
            .toList()
    val westIndices =
        generateSequence(col - 1) { if (it > 0) it - 1 else null }
            .filter { it >= 0 }
            .takeWhile { grid[row][it] < tree }
            .toList()

    val northValue =
        if (northIndices.size > 0 && northIndices.last() > 0) northIndices.size + 1
        else northIndices.size
    val eastValue =
        if (eastIndices.size > 0 && eastIndices.last() < cols - 1) eastIndices.size + 1
        else eastIndices.size
    val southValue =
        if (southIndices.size > 0 && southIndices.last() < rows - 1) southIndices.size + 1
        else southIndices.size
    val westValue =
        if (westIndices.size > 0 && westIndices.last() > 0) westIndices.size + 1
        else westIndices.size

    return northValue * eastValue * southValue * westValue
}
