package us.quartyard.algebra;

public interface Field<E extends FieldElement<E>> {

	public long characteristic();
	public int dimension();

	public E zero();
	public E one();

	public E gen(int times);


}

