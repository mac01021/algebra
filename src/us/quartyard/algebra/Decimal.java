package us.quartyard.algebra;

import java.math.BigDecimal;

public class Decimal extends BigDecimal implements FieldElement<Decimal> {
	
	public Decimal(BigDecimal bd) {
		super(bd.unscaledValue(), bd.scale());
	}

	public Decimal(int i) {
		super(i);
	}

	public Decimal add(Decimal that) {
		return new Decimal(super.add(that));
	}

	public Decimal subtract(Decimal that) {
		return new Decimal(super.subtract(that));
	}

	public Decimal multiply(Decimal that) {
		return new Decimal(super.multiply(that));
	}

	public Decimal divide(Decimal that) {
		return new Decimal(super.divide(that));
	}

	public Decimal negate() {
		return new Decimal(super.negate());
	}

}

