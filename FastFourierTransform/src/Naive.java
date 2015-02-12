
public class Naive {
	public static Complex[] multiply(Complex[] a, Complex[] b) {
		Complex[] ret = new Complex[a.length + b.length - 1];
		for (int i = 0; i < ret.length; i++) ret[i] = new Complex(0,0);
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b.length; j++) {
				ret[i+j] = ret[i+j].add(a[i].multiply(b[j]));
			}
		}
		return ret;
	}

}
