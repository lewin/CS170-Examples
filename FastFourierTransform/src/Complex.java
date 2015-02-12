
public class Complex {
	public final double real, imag;
	
	public Complex(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public Complex add(Complex other) {
		return new Complex(this.real + other.real, this.imag + other.imag);
	}
	
	public Complex subtract(Complex other) {
		return new Complex(this.real - other.real, this.imag - other.imag);
	}
	
	public Complex multiply(Complex other) {
		return new Complex(this.real * other.real - this.imag * other.imag, this.real * other.imag + this.imag * other.real);
	}
	
	public Complex scale(double scale) {
		return new Complex(this.real * scale, this.imag * scale);
	}
	
	public Complex copy() {
		return new Complex(this.real, this.imag);
	}
	
	public boolean equals(Complex other) {
		return Math.abs(this.real - other.real) < 1e-6 && Math.abs(this.imag - other.imag) < 1e-6;
		
	}
	
	public String toString() {
		return real+"+"+imag+"i";
	}
}
