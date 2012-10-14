
/**
 * @author Stephen Elliott
 * Class representing a sphere in a RayTracing Scene.
 */
public class Sphere extends Surface {

	private Vector3D center;
	private double r2;
	
	public Sphere(Vector3D c, double r, double[] a, double[] d, double[] s) {
		super(a, d, s);
		this.center = c;
		this.r2 = r * r;
	}
	
	@Override
	public Vector3D getNormal(Vector3D p, HitRecord hr) {
		return p.sub(this.center).normalize();
	}

	@Override
	public double rayIntersect(Ray ray, double t0, double t1, HitRecord hr) {
		Vector3D dst = ray.pos.sub(this.center);
		double a = ray.dir.dot(ray.dir);
		double b = ray.dir.mul(2).dot(dst);
		double c = dst.dot(dst) - this.r2;
		double d = (b*b) - (4*a*c);
		if (d > .0001) {
			//First intersection
			double t = (-b - Math.sqrt(d)) / (a * 2);
			if (t >= t0 && t <= t1) 
				return t;
			//Second intersection
			t = (-b + Math.sqrt(d)) / (a * 2);
			return t >= t0 && t <= t1
				? t
				: Double.POSITIVE_INFINITY;
		} 
		else return Double.POSITIVE_INFINITY;
	}

}
