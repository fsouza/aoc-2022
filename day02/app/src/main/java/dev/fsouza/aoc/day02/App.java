package dev.fsouza.aoc.day02;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
	enum Result {
		WIN,
		LOSE,
		DRAW;

		public int score() {
			return switch (this) {
				case WIN -> 6;
				case LOSE -> 0;
				case DRAW -> 3;
			};
		}

	}

	enum Hand {
		ROCK,
		PAPER,
		SCISSORS;

		public static Hand parse(char option) {
			return switch (option) {
				case 'A', 'X' -> ROCK;
				case 'B', 'Y' -> PAPER;
				case 'C', 'Z' -> SCISSORS;
				default -> throw new IllegalStateException("invalid hand");
			};
		}

		public Result play(Hand other) {
			if (this == other) {
				return Result.DRAW;
			}

			return (this.beats(other)) ? Result.WIN : Result.LOSE;
		}

		public int score() {
			return switch (this) {
				case ROCK -> 1;
				case PAPER -> 2;
				case SCISSORS -> 3;
			};
		}

		private boolean beats(Hand other) {
			return switch (this) {
				case ROCK -> other == SCISSORS;
				case PAPER -> other == ROCK;
				case SCISSORS -> other == PAPER;
			};
		}
	}

	record Round(Hand npc, Hand player) {
		public static Round parse(String line) {
			return new Round(Hand.parse(line.charAt(0)), Hand.parse(line.charAt(2)));
		}

		public int score() {
			return player.score() + player.play(npc).score();
		}
	}

	public static void main(String[] args) {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		var lines = reader.lines();
		var scores = lines.map((line) -> Round.parse(line)).map((round) -> round.score());

		System.out.printf("Part 1: %d\n", scores.reduce(0, (x, y) -> x + y));
	}
}
