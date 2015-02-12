import java.util.Arrays;


public class FFT {
	public static void main (String[] args) {
		Complex[] a = new Complex[] {new Complex(-1,0), new Complex(1,0)};
		Complex[] b = new Complex[] {new Complex(1,0), new Complex(1,0)};
		Complex[] c = multiply(a,b);
		for (int i = 0; i < c.length; i++)
			System.out.println(c[i]);
	}
	
	// Slow Fourier Transform O(N^2) time
	public static void sft(Complex[] A, Complex omega) {
		int N = A.length;
		Complex[] ret = new Complex[N];
		Complex x = new Complex(1, 0);
		for (int i = 0; i < N; i++) {
			 // x = omega^i
			Complex y = new Complex(1, 0);
			ret[i] = new Complex(0, 0);
			for (int j = 0; j < N; j++) {
				 // y = x^j = omega^(ij)
				ret[i] = ret[i].add(A[j].multiply(y));
				y = y.multiply(x);
			}
			x = x.multiply(omega);
		}
		for (int i = 0; i < N; i++) A[i] = ret[i].copy();
	}
	
	// Fast Fourier Transform O(N log N) time
	public static void fft(Complex[] A, Complex omega) {
		int N = A.length;
		if (N == 1) return; // base case
		Complex[] even = new Complex[N/2];
		Complex[] odd = new Complex[N/2];
		// use copy, since we don't want to modify the original A values right now
		for (int i = 0; i < N/2; i++) even[i] = A[2*i].copy();
		for (int i = 0; i < N/2; i++) odd[i] = A[2*i+1].copy();
		
		// evaluate even/odd at all the roots of unity squared
		Complex omega2 = omega.multiply(omega);
		fft(even, omega2);
		fft(odd, omega2);
		
		Complex curMult = new Complex (1, 0);
		for (int i = 0; i < N/2; i++) {
			// curMult is equal to omega^i at this point
			Complex m = odd[i].multiply(curMult);
			A[i] = even[i].add(m);
			A[i+N/2] = even[i].subtract(m);
			curMult = curMult.multiply(omega);
		}
	}
	
	public static Complex[] multiply(Complex[] a, Complex[] b) {
		int N = a.length + b.length;
		N = Integer.highestOneBit(N-1) << 1; // lowest power of 2 at least as big as N.
		
		// copy over a and b, and pad with zeros
		Complex[] acopy = Arrays.copyOf(a, N);
		Complex[] bcopy = Arrays.copyOf(b, N);
		for (int i = a.length; i < N; i++) acopy[i] = new Complex(0,0);
		for (int i = b.length; i < N; i++) bcopy[i] = new Complex(0,0);
		
		// nth root of unity
		Complex unity = new Complex(Math.cos(Math.PI * 2 / N), Math.sin(Math.PI * 2 / N));
		
		fft(acopy, unity);
		fft(bcopy, unity);
		// at this point, acopy and bcopy have the polynomial 
		// evaluated at a and b at all the roots of unity
		
		Complex[] c = new Complex[N];
		for (int i = 0; i < N; i++) c[i] = acopy[i].multiply(bcopy[i]);
		
		// The inverse of the nth root of unity 
		Complex inv_unity = new Complex(unity.real, -unity.imag);
		// inverse fft is the same as regular fft, but with a different omega
		fft(c, inv_unity);
		
		// resize array
		Complex[] ret = Arrays.copyOf(c, a.length+b.length-1);
		for (int i = 0; i < ret.length; i++) ret[i] = ret[i].scale(1./N);
		return ret;
	}
	
	
	// iterative and in place
	public static void iterative_fft(Complex[] A, Complex omega) {
		int N = A.length;
		if (N == 1) return;
		Complex[] powOmega = new Complex[N/2];
		powOmega[0] = new Complex(1,0);
		for (int i = 1; i < N/2; i++) {
			powOmega[i] = powOmega[i-1].multiply(omega);
		}
		
		int levels = Integer.numberOfTrailingZeros(N);
		for (int i = 0; i < N; i++) {
			int j = Integer.reverse(i) >>> (32 - levels);
			if (j > i) {
				Complex temp = A[i].copy();
				A[i] = A[j].copy();
				A[j] = temp.copy();
			}
		}
		
		for (int size = 2; size <= N; size <<= 1) {
			int halfsize = size >> 1;
			int tablestep = N / size;
			for (int i = 0; i < N; i += size) {
				for (int j = i, k = 0; j < i + halfsize; j++, k += tablestep) {
					Complex t = A[j + halfsize].multiply(powOmega[k]);
					A[j + halfsize] = A[j].subtract(t);
					A[j] = A[j].add(t);
				}
			}
		}
	}
}
