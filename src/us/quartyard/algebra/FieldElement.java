package us.quartyard.algebra;

public interface FieldElement<E> {

	public E add(E that);
	public E subtract(E that);
	public E multiply(E that);
	public E divide(E that);
	public E negate();

}
