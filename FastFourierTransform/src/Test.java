
public class Test {
	public static void main (String[] args) {
		for (int pow = 0; pow <= 15; pow++) {
			int length = 1 << pow;
			System.out.println(length);
			Complex[] a = generateRandomPolynomial(length);
			Complex[] b = generateRandomPolynomial(length);
			
			long start = System.currentTimeMillis();
			Complex[] c1 = FFT.multiply(a, b);
			long time1 = System.currentTimeMillis()-start;
			System.out.println(time1);
			
			start = System.currentTimeMillis();
			Complex[] c2 = Naive.multiply(a, b);
			long time2 = System.currentTimeMillis()-start;
			System.out.println(time2);
			
			// check that they're equal
			for (int i = 0; i < c1.length; i++) {
				if (!c1[i].equals(c2[i])) {
					System.out.println("DIFFERENT");
					break;
				}
			}
			
			System.out.println();
		}
		System.out.println("DONE");
	}
	
	public static Complex[] generateRandomPolynomial(int length) {
		Complex[] ret = new Complex[length];
		for (int i = 0; i < length; i++) {
			ret[i] = new Complex(Math.random()*10-5, Math.random()*10-5);
		}
		return ret;
	}
}
