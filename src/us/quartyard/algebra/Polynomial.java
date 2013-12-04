package us.quartyard.algebra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


interface PolyLike<E> {
	public E get(int i);
	public int degree();
}

public class Polynomial<E extends FieldElement<E>> implements PolyLike<E> {

	private Field<E> _field;
	private List<E> _coeffs;
	private int _degree;

	private Polynomial(Field<E> field, int maxDeg) {
		this._field = field;
		this._coeffs = new ArrayList<E>(maxDeg+1);
		E zero = field.zero();
		for(int i = 0; i <= maxDeg; i++) append(zero);

	}

	private Polynomial(Field<E> field, int degree, E... coeffs) {
		// TODO: assert that the Es are in the F
		this._field = field;
		this._coeffs = Arrays.asList(coeffs);
		this._degree = degree;
	}

	public Polynomial(Field<E> f, E... coeffs) {
		this(f, 0, coeffs);
		this.computeDegree();
	}

	public Polynomial<E> copy() {
		return new Polynomial<E>(_field, _degree, (E[])(_coeffs.toArray()));
	}

	private void append(E... coeffs) {
		for (E e : coeffs) {
			this._coeffs.add(e);
		}
	}

	private void computeDegree() {
		E zero = this._field.zero();
		// what is the degree of the zero polynomial?
		// This says -1.
		for(int i = this.len()-1; i >= 0;){
			if(!this.get(i).equals(zero)){
				this._degree = i;
				return;
			}
			i--;
		}
	}

	public boolean equals(Polynomial<E> that) {
		if (!_field.equals(that._field)) {
			return false;
		}
		if (_degree != that._degree) {
			return false;
		}
		for (int i = 0; i <= _degree; i++) {
			if (!this.get(i).equals(that.get(i))) {
				return false;
			}
		}
		return true;
	}

	public int degree() {
		return this._degree;
	}

	public Field<E> field() {
		return this._field;
	}

	public E get(int i) {
		if (i < 0 || i > this.len()) {
			return _field.zero();
		}
		return this._coeffs.get(i);
	}

	private void set(int i, E coeff) {
		this._coeffs.set(i, coeff);
	}

	private int len() {
		return this._coeffs.size();
	}


	public String toString(){
		return toString(this);
	}

	private String toString(PolyLike<E> p) {
		StringBuffer sb = new StringBuffer();
		for(int i = p.degree(); i > 0; i--){
			sb.append(p.get(i));
			if(i > 0) {
				sb.append('X');
			}
			if(i > 1) {
				sb.append('^');
				sb.append(i);
			}
			sb.append(" + ");
		}
		sb.append(p.get(0));
		return sb.toString();

	}

	public Polynomial<E> add(PolyLike<E> that) {
		PolyLike<E> p = this;
		PolyLike<E> q = that;
		if (this.degree() < that.degree()){
			p = that;
			q = this;
		}
		Polynomial<E> r = new Polynomial<E>(_field, p.degree());
		for(int i = 0; i <= q.degree(); i++){
			r.set(i, p.get(i).add(q.get(i)));
		}
		for(int i = q.degree() + 1; i <= p.degree(); i++){
			r.set(i, p.get(i));
		}
		r._degree = p.degree();
		r.computeDegree();
		System.out.println();
		return r;
	}

	public Polynomial<E> subtract(PolyLike<E> that) {

		PolyLike<E> p = this;
		PolyLike<E> q = that;
		if (this.degree() < that.degree()){
			p = that;
			q = this;
		}
		Polynomial<E> r = new Polynomial<E>(_field, p.degree());
		for(int i = 0; i <= q.degree(); i++){
			r.set(i, p.get(i).subtract(q.get(i)));
		}
		for(int i = q.degree() + 1; i <= p.degree(); i++){
			r.set(i, p.get(i));
		}
		r.computeDegree();
		return r;
	}

	public Polynomial<E> multiply(PolyLike<E> that) {
		int rdeg = this.degree() + that.degree();
		Polynomial<E> r = new Polynomial<E>(_field, rdeg);
		for(int i = 0; i <= this.degree(); i++){
			for(int j = 0; j <= that.degree(); j++){
				E a = this.get(i);
				E b = that.get(j);
				E c = r.get(i+j);
				c = c.add(a.multiply(b));
				r.set(i+j, c);
			}
		}
		r.computeDegree();
		return r;
	}

	private class AxToTheB implements PolyLike<E> {
		E _a, _zero;
		int _b;

		//The single-term, degree b polynomial: aX^b
		AxToTheB(E a, int b) {
			_a = a;
			_b = b;
			_zero = _field.zero();
		}

		public int degree() {
			return _b;
		}

		public E get(int i) {
			return (i == _b)? _a : _zero;
		}

	}

	private PolyLike<E> expt(E a, int b) {
		return new AxToTheB(a, b);
	}

	public Polynomial<E> plusXtoThe(int a) {
		return this.add(expt(_field.one(), a));
	}

	public DivisionResult<Polynomial<E>> divmod(Polynomial<E> that) {
		int k = this.degree() - that.degree();
		Polynomial<E> q = new Polynomial<E>(_field, k);
		Polynomial<E> r = this.copy();
		E eThat = that.get(that.degree());
		while(that.degree() <= r.degree()) {
			E e = r.get(r.degree());
			q.set(k, e.divide(eThat));
			r = r.subtract(that.multiply(expt(q.get(k), k)));
			k--;
		}
		q.computeDegree();
		r.computeDegree();
		return new DivisionResult<Polynomial<E>>(q, r);
	}

	public Polynomial<E> divide(Polynomial<E> that) {
		return this.divmod(that).quotient();
	}

	public Polynomial<E> mod(Polynomial<E> that) {
		return this.divmod(that).remainder();
	}

	public Polynomial<E> gcd(Polynomial<E> that) {
		Polynomial<E> p = this;
		Polynomial<E> q = that;
		if (p.degree() < q.degree()) {
			p = that;
			q = this;
		}
		while(q.degree() >= 0) {
			Polynomial<E> t = p;
			p = q;
			q = p.mod(q);
		}
		return p;
	}

	public static <T> Polynomial<T> x(Field<T> f, int degree) {
		Polynomial<T> p = new Polynomial<T>(f, degree);
		for(int i = 0; i <= degree; i++) {
			p.set(i, f.random());
		}
		return p;
	}


	public static void main(String[] args) throws Exception {
		System.out.println("Hello poly!\n");
		DecimalField Q = new DecimalField();
		Decimal zero = Q.zero();
		Decimal one = Q.gen(1);
		if (!Q.one().equals(one)){
			throw new Exception("WTF?!");
		}
		Decimal minusOne = Q.gen(-1);
		Decimal two = Q.gen(2);
		Decimal three = Q.gen(3);
		Decimal five = Q.gen(5);
		Decimal seven = Q.gen(7);
		Polynomial<Decimal> p = new Polynomial<Decimal>(Q,
								three,
								two,
								five,
								seven,
								one);
		Polynomial<Decimal> q = new Polynomial<Decimal>(Q,
								one,
								two);

		System.out.println("P[X] = "+ p);
		System.out.println("Q[X] = "+ q);
		System.out.println();
		
		System.out.println("(P + Q)[X] = "+ p.add(q));
		System.out.println("Also         "+ q.add(p));
		System.out.println();

		System.out.println("(P - Q)[X] = "+ p.subtract(q));
		System.out.println("Also         "+ q.subtract(p));
		System.out.println();
		System.out.println("(P * Q)[X] = "+ p.multiply(q));
		System.out.println("Also         "+ q.multiply(p));
		System.out.println();

		p = new Polynomial(Q, one, minusOne, one, three);
		q = new Polynomial(Q, minusOne, two, one);
		DivisionResult<Polynomial<Decimal>> r = p.divmod(q);
		System.out.println("(P / Q)[X] = "+ r.quotient());
		System.out.println("(P % Q)[X] = "+ r.remainder());
		System.out.println();




	}
}

