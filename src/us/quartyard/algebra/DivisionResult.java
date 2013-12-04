package us.quartyard.algebra;

public class DivisionResult<T> {

	private T _q;
	private T _r;

	DivisionResult(T q, T r) {
		_q = q;
		_r = r;
	}

	public T quotient() {
		return _q;
	}

	public T remainder() {
		return _r;
	}

}

