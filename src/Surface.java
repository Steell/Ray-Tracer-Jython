
public abstract class Surface {
	
	public double[] am;
	public double[] dm;
	public double[] sm;
	
	public Surface(double[] a, double[] d, double[] s) {
		this.am = a;
		this.dm = d;
		this.sm = s;
	}
	
	/**
	 * @param ray Intersection ray
	 * @param t0 Minimum t
	 * @param t1 Maximum t
	 * 
	 * @return The t value when the Ray will collide with this Surface.
	 * 	       Returns infinity if it doesn't collide.
	 */
	public abstract double rayIntersect(Ray ray, double t0, double t1, HitRecord hr);
	/**
	 * @param p A point on the Surface
	 * @return The Surface Normal at the given point.
	 */
	public abstract Vector3D getNormal(Vector3D p, HitRecord hr);
}
