package dev.fsouza.aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    record Entry(int index, Set<Character> items) {
    }

    record Rucksack(Set<Character> left, Set<Character> right) {
        public static Rucksack parse(String line) {
            final var left = line.substring(0, line.length() / 2);
            final var right = line.substring(line.length() / 2);
            final var leftSet = left.chars().mapToObj((ch) -> (char) ch).collect(Collectors.toSet());
            final var rightSet = right.chars().mapToObj((ch) -> (char) ch).collect(Collectors.toSet());
            return new Rucksack(leftSet, rightSet);
        }

        public Set<Character> allItems() {
            final var union = new HashSet<>(left);
            union.addAll(right);
            return union;
        }

        public Character commonItemType() {
            final var intersection = new HashSet<>(left);
            intersection.retainAll(right);
            assert intersection.size() == 1;
            return intersection.iterator().next();
        }
    }

    public static void main(String[] args) {
        final var isPart2 = args.length > 0 && args[0].equals("part2");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var lines = reader.lines();
        final var items = isPart2 ? part2Items(lines) : part1Items(lines);

        System.out.println(items.map(Solution::priority).reduce(0, (x, y) -> x + y));
    }

    public static Stream<Character> part1Items(Stream<String> lines) {
        return lines.map(Rucksack::parse).map(r -> r.commonItemType());
    }

    public static Stream<Character> part2Items(Stream<String> lines) {
        var items = new AtomicInteger(0);
        return lines.map(Rucksack::parse).map(r -> r.allItems())
                .map(set -> {
                    final var index = items.get();
                    items.incrementAndGet();
                    return new Entry(index, set);
                }).collect(Collectors.groupingBy(entry -> entry.index / 3)).values().stream()
                .map(entries -> entries.stream().reduce(null, (acc, entry) -> {
                    if (acc == null) {
                        return new Entry(0, new HashSet<Character>(entry.items));
                    }
                    acc.items.retainAll(entry.items);
                    return acc;
                }))
                .map(entry -> entry.items.iterator().next());
    }

    public static int priority(Character item) {
        final var aCode = (int) 'a';
        final var ACode = (int) 'A';
        final var code = (int) item.charValue();
        if (Character.isUpperCase(item)) {
            return code - ACode + 27;
        } else {
            return code - aCode + 1;
        }
    }
}
