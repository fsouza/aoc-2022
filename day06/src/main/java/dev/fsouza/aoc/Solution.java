package dev.fsouza.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) throws IOException {
        // final var isPart2 = args.length > 0 && args[0].equals("part2");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var line = reader.readLine();

        for (var i = 4; i < line.length(); i++) {
            final Set<Character> chars = line.substring(i - 4, i).chars().mapToObj(ch -> (char) ch)
                    .collect(Collectors.toSet());
            if (chars.size() == 4) {
                System.out.println(i);
                break;
            }
        }
    }
}
