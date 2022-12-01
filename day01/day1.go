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
	var sum int
	for _, food := range e {
		sum += food
	}
	return sum
}

func main() {
	part1(os.Stdin)
}

func part1(r io.Reader) {
	data, err := io.ReadAll(r)
	if err != nil {
		panic(err)
	}
	rawElves := strings.Split(string(data), "\n\n")
	var largest int
	for _, rawElf := range rawElves {
		elf := parseElf(rawElf)
		if total := elf.total(); total > largest {
			largest = total
		}
	}
	fmt.Println(largest)
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
