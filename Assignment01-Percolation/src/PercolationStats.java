/**
 * Assignment 1 - Algorithms Part I, Princeton University at coursera.org
 *  
 * Author:        Ana Siqueira <anaclara.lacerdas@gmail.com>
 * Written:       08/28/2013
 * Last updated:  08/29/2013
 *
 * Compilation:   javac PercolationStats.java
 * Execution:     java PercolationStats N T
 *  
 * Performs T independent computational experiments choosing randomly the sites to open on an N-by-N
 * grid; Calculates and prints out the mean, standard deviation, and the 95% confidence
 * interval for the percolation threshold;
 *
 * % java PercolationStats 200 100
 * mean                    = 0.5916912499999998
 * stddev                  = 0.0091517560376811
 * 95% confidence interval = 0.5898975058166143, 0.5934849941833853
 */
public class PercolationStats {
	
	private int T;
	private int N;
	private double[] thresholds;

	/**
	 * perform T independent computational experiments on an N-by-N grid
	 * 
	 * @param N grid size (N-by-N)
	 * @param T number of experiments
	 */
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException();
		}
		this.thresholds = new double[T];
		this.T = T;
		this.N = N;
		execute();
	}

	private void execute() {
		for (int i = 0; i < T; i++) {
			Percolation perc = new Percolation(N);
			double openSites = 0;
			while (!perc.percolates()) {
				int row = StdRandom.uniform(1, N + 1);
				int col = StdRandom.uniform(1, N + 1);
				if (!perc.isOpen(row, col)) {
					perc.open(row, col);
					openSites++;
				}
			}
			thresholds[i] = openSites / (N*N);
		}
	}

	/**
	 * @return sample mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(thresholds);
	}

	/**
	 * @return sample standard deviation of percolation threshold
	 */
	public double stddev() {
		if (T == 1) {
			return Double.NaN;
		}
		return StdStats.stddev(thresholds);
	}

	/**
	 * @return lower bound of the 95% confidence interval
	 */
	public double confidenceLo() {
		return mean() - ((1.96 * stddev())/Math.sqrt(T));
	}

	/**
	 * @return upper bound of the 95% confidence interval
	 */
	public double confidenceHi() {
		return mean() + ((1.96 * stddev())/Math.sqrt(T));
	}

	/**
	 * This main method takes two command-line arguments N and T, performs T
	 * independent computational experiments choosing random sites to open on an N-by-N
	 * grid, and prints out the mean, standard deviation, and the 95% confidence
	 * interval for the percolation threshold
	 * 
	 * @param args N and T
	 */
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int t = Integer.parseInt(args[1]);
		PercolationStats p = new PercolationStats(n, t);
		StdOut.printf("%% java PercolationStats %d %d\n", n, t);
		StdOut.printf("mean                    = %1.16f\n", p.mean());
		StdOut.printf("stddev                  = %1.16f\n", p.stddev());
		StdOut.printf("95%% confidence interval = %1.16f, %1.16f\n", p.confidenceLo(), p.confidenceHi());
	}
}