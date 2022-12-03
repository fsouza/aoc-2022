package dev.fsouza.aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
    record Rucksack(Set<Character> left, Set<Character> right) {
        public static Rucksack parse(String line) {
            final var left = line.substring(0, line.length() / 2);
            final var right = line.substring(line.length() / 2);
            final var leftSet = left.chars().mapToObj((ch) -> (char) ch).collect(Collectors.toSet());
            final var rightSet = right.chars().mapToObj((ch) -> (char) ch).collect(Collectors.toSet());
            return new Rucksack(leftSet, rightSet);
        }

        public Character commonItemType() {
            final var intersection = new HashSet<>(left);
            intersection.retainAll(right);
            assert intersection.size() == 1;
            return intersection.iterator().next();
        }
    }

    public static void main(String[] args) {
        // final var isPart2 = args.length > 0 && args[0].equals("part2");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var lines = reader.lines();
        final var items = lines.map(line -> Rucksack.parse(line)).map(r -> r.commonItemType());

        System.out.println(items.map(item -> priority(item)).reduce(0, (x, y) -> x + y));
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
