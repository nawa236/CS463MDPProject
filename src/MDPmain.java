import java.util.Arrays;

public class MDPmain {
	static double[][] rewardFunc = new double[][] { {0, 5, -2, 10},
													{ 0, 5, 0, 15},
													{-5, 10, 5, 0},
													{60, 0, 0, 5} }; 							   
	static double[] policy = new double[] { .7, .2, .1};
	static double[][][] V6 = new double[7][4][4];
	static double[][][] Vinf = new double[100000][4][4];
	static char[][] action = new char[4][4];
	
	public static void main(String[] args) {
		valuIter(V6, 1, 6);
		for(int i = 0; i < 7; i++) {
			System.out.println("V6(" + i + "): " + Arrays.deepToString(V6[i]));
		}
		System.out.println("\nV6 Policy: " + Arrays.deepToString(action) + "\n");
		
		valuIter(Vinf, .96,-1);
		for(int i = 0; i < 7; i++) {
			System.out.println("V*(" + i + "): " + Arrays.deepToString(Vinf[i]));
		}
		System.out.println("\nV* Policy: " + Arrays.deepToString(action));
	}
	
	
	//use -1 for horizon to do infinite horizon
	static int valuIter(double[][][] valFunc, double discount, int horizon) {
		boolean done = false;
		int count = 1;
		double[] actions = new double[4];
		double max;
		valFunc[0] = rewardFunc;//first layer is the same as the reward function since no utility exists yet
		while(!done) {
			for(int r = 0; r < 4; r++) {//row iterator
				for(int c = 0; c < 4; c++) {//column iterator
					//action value creation
					for(int a = 0; a < 4; a++) {
						if(a == 0) { //up action
							if(r == 0) {//wall up
								actions[0] = policy[0]*valFunc[count-1][r][c] + policy[1]*valFunc[count-1][r+1][c] + policy[2]*valFunc[count-1][r][c];
							} else if(r == 3) {//wall down
								actions[0] = policy[0]*valFunc[count-1][r-1][c] + policy[1]*valFunc[count-1][r][c] + policy[2]*valFunc[count-1][r][c];
							} else {//no wall
								actions[0] = policy[0]*valFunc[count-1][r-1][c] + policy[1]*valFunc[count-1][r+1][c] + policy[2]*valFunc[count-1][r][c];
							}
						} else if(a == 1) { //right action
							if(c == 3) {//wall right
								actions[1] = policy[0]*valFunc[count-1][r][c] + policy[1]*valFunc[count-1][r][c-1] + policy[2]*valFunc[count-1][r][c];
							} else if(c == 0) {//wall left
								actions[1] = policy[0]*valFunc[count-1][r][c+1] + policy[1]*valFunc[count-1][r][c] + policy[2]*valFunc[count-1][r][c];
							} else {//no wall
								actions[1] = policy[0]*valFunc[count-1][r][c+1] + policy[1]*valFunc[count-1][r][c-1] + policy[2]*valFunc[count-1][r][c];
							}
						} else if(a == 2) { //down action
							if(r == 3) {//wall down
								actions[2] = policy[0]*valFunc[count-1][r][c] + policy[1]*valFunc[count-1][r-1][c] + policy[2]*valFunc[count-1][r][c];
							} else if(r == 0) {//wall up
								actions[2] = policy[0]*valFunc[count-1][r+1][c] + policy[1]*valFunc[count-1][r][c] + policy[2]*valFunc[count-1][r][c];
							} else {//no wall
								actions[2] = policy[0]*valFunc[count-1][r+1][c] + policy[1]*valFunc[count-1][r-1][c] + policy[2]*valFunc[count-1][r][c];
							}
						} else { //left action
							if(c == 0) {//wall left
								actions[3] = policy[0]*valFunc[count-1][r][c] + policy[1]*valFunc[count-1][r][c+1] + policy[2]*valFunc[count-1][r][c];
							} else if(c == 3) {//wall right
								actions[3] = policy[0]*valFunc[count-1][r][c-1] + policy[1]*valFunc[count-1][r][c] + policy[2]*valFunc[count-1][r][c];
							} else {//no wall
								actions[3] = policy[0]*valFunc[count-1][r][c-1] + policy[1]*valFunc[count-1][r][c+1] + policy[2]*valFunc[count-1][r][c];
							}
						}
					}
					
					//determine max action and set 
					max = Math.max(Math.max(Math.max(actions[0], actions[1]), actions[2]), actions[3]);
					if(max == actions[0]) {
						action[r][c] = '^';
					}else if(max == actions[1]) {
						action[r][c] = '>';
					}else if(max == actions[2]) {
						action[r][c] = 'v';
					} else {
						action[r][c] = '<';
					}
					valFunc[count][r][c] = rewardFunc[r][c] + (discount * max);
				}
			}
			if(count == horizon) {
				done = true;
			} else if(horizon == -1) {
				if(count > 0) {
					if(Math.abs(valFunc[count-1][2][2] - valFunc[count][2][2]) < .005) {//bellman residue < .005
						done = true;
					}
				}
			}
			count++;
		}
		return count;
	}
		
}
