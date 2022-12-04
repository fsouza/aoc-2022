package dev.fsouza.aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {
    record Interval(int start, int end) {
        public static Interval parse(String component) {
            final var parts = component.split("-");
            assert parts.length == 2;
            return new Interval(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        }

        public boolean contains(Interval other) {
            return other.start >= start && other.end <= end;
        }
    }

    record Assignment(Interval first, Interval second) {
        public static Assignment parse(String line) {
            final var parts = line.split(",");
            assert parts.length == 2;
            return new Assignment(Interval.parse(parts[0]), Interval.parse(parts[1]));
        }

        public boolean isRedundant() {
            return first.contains(second) || second.contains(first);
        }
    }

    public static void main(String[] args) {
        // final var isPart2 = args.length > 0 && args[0].equals("part2");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var lines = reader.lines();
        final var assignments = lines.map(Assignment::parse);

        System.out.println(assignments.filter(Assignment::isRedundant).count());
    }
}
