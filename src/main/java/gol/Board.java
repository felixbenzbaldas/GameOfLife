package gol;

import static java.util.function.Function.identity;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.IntStream.range;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Board {

	private static class Point {

		private final int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			return 31 * x + y;
		}

		@Override
		public boolean equals(Object obj) {
			Point other = (Point) obj;
			return other.x == x && other.y == y;
		}

		Stream<Point> neighbours() {
			return range(y - 1, y + 2).mapToObj(y -> range(x - 1, x + 2).mapToObj(x -> point(x, y)))
					.flatMap(identity());
		}

	}

	private Set<Point> lifeCells = new HashSet<Point>();
	private final int width;
	private final int height;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void tick() {
		this.lifeCells = cells().filter(this::alifeInNextGen).collect(toSet());
	}

	private Stream<Point> cells() {
		return range(0, getHeight()).mapToObj(y -> range(0, getWidth()).mapToObj(x -> point(x, y)))
				.flatMap(identity());
	}

	private boolean alifeInNextGen(Point point) {
		boolean alife = isAlive(point);
		long alifeNeighbours = alifeNeighbours(point);
		return alife && (alifeNeighbours == 2 || alifeNeighbours == 3) //
				|| !alife && (alifeNeighbours == 3);
	}

	private long alifeNeighbours(Point point) {
		return point.neighbours().filter(isEqual(point).negate()).filter(this::isAlive).count();
	}

	public boolean isAlive(int x, int y) {
		return isAlive(point(x, y));
	}

	private boolean isAlive(Point point) {
		return lifeCells.contains(point);
	}

	public void setAlive(int x, int y) {
		lifeCells.add(point(x, y));
	}

	private static Point point(int x, int y) {
		return new Point(x, y);
	}

}