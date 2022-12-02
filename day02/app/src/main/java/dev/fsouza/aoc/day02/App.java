package dev.fsouza.aoc.day02;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
	enum Result {
		WIN,
		LOSE,
		DRAW;

		public static Result parseDesired(char option) {
			return switch (option) {
				case 'X' -> LOSE;
				case 'Y' -> DRAW;
				case 'Z' -> WIN;
				default -> throw new IllegalStateException("invalid hand");
			};
		}

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

			return (this.beats() == other) ? Result.WIN : Result.LOSE;
		}

		public Hand resolve(Result result) {
			return switch (result) {
				case DRAW -> this;
				case WIN -> this.losesTo();
				case LOSE -> this.beats();
			};
		}

		public int score() {
			return switch (this) {
				case ROCK -> 1;
				case PAPER -> 2;
				case SCISSORS -> 3;
			};
		}

		private Hand beats() {
			return switch (this) {
				case ROCK -> SCISSORS;
				case PAPER -> ROCK;
				case SCISSORS -> PAPER;
			};
		}

		private Hand losesTo() {
			return switch (this) {
				case ROCK -> PAPER;
				case PAPER -> SCISSORS;
				case SCISSORS -> ROCK;
			};
		}
	}

	record Round(Hand player, Result result) {
		public static Round parsePart1(String line) {
			final var otherHand = Hand.parse(line.charAt(0));
			final var playerHand = Hand.parse(line.charAt(2));
			final var result = playerHand.play(otherHand);
			return new Round(playerHand, result);
		}

		public static Round parsePart2(String line) {
			final var otherHand = Hand.parse(line.charAt(0));
			final var result = Result.parseDesired(line.charAt(2));
			final var playerHand = otherHand.resolve(result);
			return new Round(playerHand, result);
		}

		public int score() {
			return player.score() + result.score();
		}
	}

	public static void main(String[] args) {
		final var isPart2 = args.length > 0 && args[0].equals("part2");
		final var reader = new BufferedReader(new InputStreamReader(System.in));
		final var lines = reader.lines();
		final var scores = lines.map((line) -> isPart2 ? Round.parsePart2(line) : Round.parsePart1(line))
				.map((round) -> round.score());

		System.out.println(scores.reduce(0, (x, y) -> x + y));
	}
}
