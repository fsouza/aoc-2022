package dev.fsouza.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    record File(String name, int size) {
    }

    record Dir(String name, List<File> files, List<Dir> dirs) {
        public static Dir empty(String name) {
            return new Dir(name, new ArrayList<>(), new ArrayList<>());
        }

        public int size() {
            return files.stream().map(File::size).reduce(0, Integer::sum)
                    + dirs.stream().map(Dir::size).reduce(0, Integer::sum);
        }

        public Dir getOrCreateDir(String name) {
            return findDir(name).orElse(addDir(name));
        }

        public Dir addDir(String name) {
            final var dir = empty(name);
            dirs.add(dir);
            return dir;
        }

        public void addFile(String name, int size) {
            files.add(new File(name, size));
        }

        private Optional<Dir> findDir(String name) {
            return dirs.stream().filter(dir -> dir.name().equals(name)).findFirst();
        }
    }

    static class Input {
        private List<String> lines;
        private int cursor;

        public Input(Stream<String> lines) {
            this.lines = lines.collect(Collectors.toList());
            cursor = 0;
        }

        public boolean hasNext() {
            return cursor < lines.size();
        }

        public String nextLine() {
            return lines.get(cursor++);
        }

        public List<String> consumeUntil(Function<String, Boolean> predicate) {
            final List<String> output = new ArrayList<>();
            while (hasNext()) {
                final var line = lines.get(cursor);
                if (predicate.apply(line)) {
                    break;
                }
                cursor++;
                output.add(line);
            }
            return output;
        }
    }

    static class State {
        private Input input;
        private Dir root;
        private Stack<Dir> dirStack;

        public State(Input input) {
            this.input = input;
            root = Dir.empty("/");
            dirStack = new Stack<>();
            dirStack.push(root);
        }

        public Dir getRoot() {
            return root;
        }

        public void processInput() {
            while (input.hasNext()) {
                final var commandLine = input.nextLine();
                if (!commandLine.startsWith("$")) {
                    throw new IllegalStateException("tried to read something that is not a command as a command");
                }

                final var parts = commandLine.substring(2).split(" ");
                final var command = parts[0];
                if (command.equals("cd")) {
                    cd(parts[1]);
                } else {
                    ls();
                }
            }
        }

        public List<Dir> allDirs(Function<Dir, Boolean> predicate) {
            final List<Dir> output = new ArrayList<>();
            final Queue<Dir> todo = new LinkedList<>();
            todo.add(root);

            while (!todo.isEmpty()) {
                final var dir = todo.remove();
                if (predicate.apply(dir)) {
                    output.add(dir);
                }

                todo.addAll(dir.dirs());
            }

            return output;
        }

        private Dir currentDir() {
            return dirStack.peek();
        }

        private void cd(String target) {
            if (target.equals("..")) {
                dirStack.pop();
            } else if (target.equals("/")) {
                dirStack.removeAllElements();
                dirStack.push(root);
            } else {
                final var dir = currentDir().getOrCreateDir(target);
                dirStack.push(dir);
            }
        }

        private void ls() {
            final var dir = this.currentDir();
            final var entries = input.consumeUntil(line -> line.startsWith("$")).stream().map(line -> line.split(" "))
                    .collect(Collectors.partitioningBy(entry -> entry[0].equals("dir")));
            entries.get(true).stream().forEach(entry -> dir.addDir(entry[1]));
            entries.get(false).stream().forEach(entry -> dir.addFile(entry[1], Integer.parseInt(entry[0])));
        }
    }

    public static void main(String[] args) throws IOException {
        // final var isPart2 = args.length > 0 && args[0].equals("part2");
        final var reader = new BufferedReader(new InputStreamReader(System.in));
        final var input = new Input(reader.lines());
        final var state = new State(input);
        state.processInput();

        System.out.println(state.allDirs(dir -> dir.size() < 100_000).stream().map(dir -> dir.size()).reduce(0,
                (x, y) -> x + y));
    }
}
