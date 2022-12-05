package dev.fsouza.aoc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    record Instruction(int count, int fromIndex, int toIndex) {

        private static final Pattern INSTRUCTION = Pattern.compile("^move ([0-9]+) from ([0-9]+) to ([0-9]+)$");

        public static Instruction parse(String line) {
            final var matcher = INSTRUCTION.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalStateException("invalid input");
            }
            final var count = Integer.parseInt(matcher.group(1));
            final var fromIndex = Integer.parseInt(matcher.group(2)) - 1;
            final var toIndex = Integer.parseInt(matcher.group(3)) - 1;
            return new Instruction(count, fromIndex, toIndex);
        }
    }

    record State(ArrayList<Stack<Character>> stacks) {
        public static State parse(Stream<String> lines) {
            final ArrayList<LinkedList<Character>> stacks = new ArrayList<>();
            lines.map(line -> {
                var builder = new StringBuilder();
                for (var i = 1; i < line.length(); i += 4) {
                    builder = builder.append(line.charAt(i));
                }
                return builder.toString();
            }).forEach(line -> {
                for (var i = 0; i < line.length(); i++) {
                    if (i == stacks.size()) {
                        stacks.add(new LinkedList<>());
                    }

                    final var ch = line.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        stacks.get(i).add(ch);
                    }
                }
            });
            return new State(stacks.stream().map(items -> {
                final var stack = new Stack<Character>();
                items.descendingIterator().forEachRemaining(stack::push);
                return stack;
            }).collect(Collectors.toCollection(ArrayList::new)));
        }

        public String result() {
            return stacks.stream().map(stack -> stack.peek()).map(ch -> ch.toString()).reduce("",
                    (c1, c2) -> c1 + c2);
        }

        public void execute(Instruction instruction) {
            final var sourceStack = stacks.get(instruction.fromIndex());
            final var targetStack = stacks.get(instruction.toIndex());
            for (var i = 0; i < instruction.count(); i++) {
                targetStack.push(sourceStack.pop());
            }
        }
    }

    public static void main(String[] args) {
        // final var isPart2 = args.length > 0 && args[0].equals("part2");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var stateLines = reader.lines().takeWhile(line -> !line.equals(""));
        final var state = State.parse(stateLines);
        reader.lines().dropWhile(line -> line.equals("")).map(Instruction::parse).forEach(state::execute);
        System.out.println(state.result());
    }
}
