package main

import (
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"

	"golang.org/x/exp/slices"
)

type elf []int

func (e elf) total() int {
	return sliceSum(e)
}

func sliceSum(s []int) int {
	var sum int
	for _, v := range s {
		sum += v
	}
	return sum
}

func main() {
	elves := readElves(os.Stdin)
	part1(elves)
	part2(elves)
}

func readElves(r io.Reader) []elf {
	var elves []elf
	data, err := io.ReadAll(r)
	if err != nil {
		panic(err)
	}
	rawElves := strings.Split(string(data), "\n\n")
	for _, rawElf := range rawElves {
		elves = append(elves, parseElf(rawElf))
	}
	return elves
}

func part1(elves []elf) {
	var largest int
	for _, elf := range elves {
		if total := elf.total(); total > largest {
			largest = total
		}
	}
	fmt.Printf("Part 1: %d\n", largest)
}

func part2(elves []elf) {
	// this function could use a heap with only the top 3 elements, but
	// given the volume of data, sorting is fine.
	var totals []int
	for _, elf := range elves {
		totals = append(totals, elf.total())
	}
	slices.Sort(totals)
	top3 := totals[len(totals)-3:]
	fmt.Printf("Part 2: %d\n", sliceSum(top3))
}

func parseElf(input string) elf {
	lines := strings.Split(input, "\n")
	var output []int
	for _, line := range lines {
		if cals, err := strconv.Atoi(line); err == nil {
			output = append(output, cals)
		}
	}
	slices.Sort(output)
	return elf(output)
}
