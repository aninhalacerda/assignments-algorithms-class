/**
 * Assignment 1 - Algorithms Part I, Princeton University at coursera.org
 *
 * Author:        Ana Siqueira <anaclara.lacerdas@gmail.com>
 * Written:       08/28/2013
 * Last updated:  08/29/2013
 *
 * Compilation:   javac Percolation.java
 * Execution:     java Percolation
 * 
 * Data type that models a percolation system and uses WeightedQuickUnionUF; The
 * purpose of this data type is use the Union-find algorithm to discover when a
 * system N-by-N percolates.
 * 
 * % java Percolation
 * 
 */
public class Percolation {

	private final boolean OPEN = true; // represents the OPEN site value
	private final int TOP_INDEX = 0; // virtual top site index = 0

	private int N;
	private int gridSize;
	private int BOTTOM_INDEX; // virtual bottom site index = N*N+1
	private boolean percolatedSystem;
	private boolean[] grid;
	private WeightedQuickUnionUF quickUF;
	private WeightedQuickUnionUF auxUF;

	/**
	 * create N-by-N grid, with all sites blocked (value 0); create a new
	 * WeightedQuickUnionUF instance with N*N elements plus 2 sites that will
	 * represent the virtual top site and virtual bottom site;
	 * create an auxiliar WeightedQuickUnionUF without virtual bottom site
	 * to avoid backwash problem.
	 * 
	 * @param N
	 */
	public Percolation(int N) {
		this.N = N;
		this.gridSize = N*N;
		this.BOTTOM_INDEX = gridSize + 1;
		this.percolatedSystem = false;
		this.grid = new boolean[gridSize];
		this.quickUF = new WeightedQuickUnionUF(gridSize + 2);
		this.auxUF = new WeightedQuickUnionUF(gridSize + 1);
	}

	/**
	 * open site at row i and column j if it is not already; mark the site as
	 * open and make the union with all its open neighbors;
	 * 
	 * @param i row index
	 * @param j column index
	 */
	public void open(int i, int j) {
		verifyIndex(i, j);

		if (!isOpen(i, j)) {
			grid[getSiteID(i, j)] = OPEN; // mark as open

			if (i == 1) { // connect top row sites with the virtual top
				quickUF.union(getSiteID(i, j), TOP_INDEX);
				auxUF.union(getSiteID(i, j), TOP_INDEX);
			} if (i == N) {
				quickUF.union(getSiteID(i, j), BOTTOM_INDEX);
			}
			
			// find neighbors
			int leftSite = j - 1 == 0 ? -1 : getSiteID(i, j - 1);
			int rightSite = j + 1 == N + 1 ? -1 : getSiteID(i, j + 1);
			int topSite = i - 1 == 0 ? -1 : getSiteID(i - 1, j);
			int bottomSite = i + 1 == N + 1 ? -1 : getSiteID(i + 1, j);

			// connect with open neighbors
			if (bottomSite != -1 && isOpen(i + 1, j)) {
				quickUF.union(getSiteID(i, j), bottomSite);
				auxUF.union(getSiteID(i, j), bottomSite);
			}
			if (topSite != -1 && isOpen(i - 1, j)) {
				quickUF.union(getSiteID(i, j), topSite);
				auxUF.union(getSiteID(i, j), topSite);
			}
			if (rightSite != -1 && isOpen(i, j + 1)) {
				quickUF.union(getSiteID(i, j), rightSite);
				auxUF.union(getSiteID(i, j), rightSite);
			}
			if (leftSite != -1 && isOpen(i, j - 1)) {
				quickUF.union(getSiteID(i, j), leftSite);
				auxUF.union(getSiteID(i, j), leftSite);
			}
		}
	}

	/**
	 * verify if the site at row i, column j is marked as open
	 * 
	 * @param i row index
	 * @param j column index
	 * @return if the specific site is open
	 */
	public boolean isOpen(int i, int j) {
		verifyIndex(i, j);
		return grid[getSiteID(i, j)];
	}

	/**
	 * verify if the site at row i, column j is connected with any site at the top row
	 * 
	 * @param i row index
	 * @param j column index
	 * @return if the specific site is connected with the top
	 */
	public boolean isFull(int i, int j) {
		verifyIndex(i, j);
		if (isOpen(i, j)) {
			return auxUF.connected(getSiteID(i, j), TOP_INDEX);
		}
		return false;
	}

	/**
	 * checks if exists a possible path starting from any site at the top row to
	 * any on bottom row.
	 * 
	 * @return if the system percolates
	 */
	public boolean percolates() {
		if (!percolatedSystem) {
			percolatedSystem = quickUF.connected(TOP_INDEX, BOTTOM_INDEX);
		}
		return percolatedSystem;
	}

	/**
	 * Map 2-dimensional (row, column) pair to a 1-dimensional union-find object index
	 * 
	 * @param i row index
	 * @param j column index
	 * @return the correspondent 1-dimensional id
	 */
	private int getSiteID(int i, int j) {
		return (N * i) -  j;
	}

	/**
	 * verify if the indexes is contained in the N-by-N grid
	 * 
	 * @param i row index
	 * @param j column index
	 */
	private void verifyIndex(int i, int j) {
		if (i < 1 || i > N) {
			throw new IndexOutOfBoundsException("row index i out of bounds");
		} if (j < 1 || j > N) {
			throw new IndexOutOfBoundsException("column index j out of bounds");
		}
	}
	
	public static void main(String[] args) {
		Percolation perc = new Percolation(4);

		perc.open(1,1);
		perc.open(1,2);
		perc.open(2,2);
	}
}
