package us.quartyard.algebra;

public class DecimalField implements Field<Decimal> {


	public boolean equals(DecimalField that) {
		return true;
	}

	public long characteristic() {
		return 0L;
	}

	public int dimension() {
		return 1;
	}

	public Decimal zero() {
		return new Decimal(0);
	}

	public Decimal one() {
		return new Decimal(1);
	}

	public Decimal gen(int times) {
		return new Decimal(times);
	}

}
