package us.quartyard.algebra;

public class GF implements Field<GFElement> {

	private long _p;
	private int _dim;
	
	public GF(long p, int k) {
		this._p = p;
		this._dim = k;
	}

	public GF(long p) {
		this(p, 1);
	}

	public boolean equals(GF that) {
		return (this.characteristic() == that.characteristic() &&
			this.dimension() == that.dimension());
	}

	public long characteristic() {
		return this._p;
	}

	public int dimension() {
		return this._dim;
	}

	public GFElement zero() {
		throw new UnsupportedOperationException();
	}

	public GFElement one() {
		throw new UnsupportedOperationException();
	}

	public GFElement gen(int times) {
		throw new UnsupportedOperationException();
	}

}
