
/**
 * @author Stephen Elliott
 * Class representing a plane in a RayTracing Scene.
 */
public class Plane extends Surface {
	
	private Vector3D pos;
	private Vector3D norm;
	private double pdn;
	
	public Plane(Vector3D p, Vector3D n, double[] a, double[] d, double[] s) {
		super(a, d, s);
		this.pos = p;
		this.norm = n.normalize();
		this.pdn = this.pos.dot(this.norm);
	}

	@Override
	public Vector3D getNormal(Vector3D p, HitRecord hr) {
		return this.norm;
	}

	@Override
	public double rayIntersect(Ray ray, double t0, double t1, HitRecord hr) {
		double a = ray.dir.dot(this.norm);
		if (Math.abs(a) > .00001) {
			double t = (this.pdn - ray.pos.dot(this.norm)) / a;
			return t >= t0 && t <= t1
				? t
				: Double.POSITIVE_INFINITY;
		}
		else return Double.POSITIVE_INFINITY;
	}

}
