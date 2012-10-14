
/**
 * @author Stephen Elliott
 * Abstract class representing a Light Source in a RayTracing Scene.
 */
public abstract class Light {

	protected double intensity;

	public Light(double i) {
		this.intensity = i;
	}

	/**
	 * @return The maximum interval (t0) used in shadow intersection calculations.
	 */
	protected abstract double getMaxInterval();
	/**
	 * @param p A Point in the Scene.
	 * @return A Vector3D giving the direction to this Light from the given point.
	 */
	protected abstract Vector3D getDir(Vector3D p);

	/**
	 * @param ray The ray which hit the surface.
	 * @param p The Point of intersection.
	 * @param n The Normal of the surface at the point of collision.
	 * @param s The Surface which was hit by the ray.
	 * @param shapes The list of all the shapes which can cast shadows.
	 * @param diff Flag indicating if diffuse shading is calculated.
	 * @param spec Flag indicating if specular shading is calculated.
	 * @param shadows Flag indicating if shadows are calculated.
	 * 
	 * @return The resulting color of the collision from this light.
	 */
	public Vector3D resultColor(Ray ray, Vector3D p, Vector3D n, Surface s,
			Surface[] shapes, boolean diff, boolean spec, boolean shadows) {
		Vector3D result = new Vector3D(0, 0, 0);
		Vector3D l = this.getDir(p);

		//Shadows
		if (shadows) {
			for (Surface shape : shapes) {
				if (shape.rayIntersect(new Ray(p, l), .001, this.getMaxInterval(), null) < Double.POSITIVE_INFINITY)
					return result;
			}
		}

		l = l.normalize();
		double amt = n.dot(l);
		//Is the surface facing the light?
		if (amt > 0) {
			//Diffuse
			if (diff)
				result = result.add(s.dm).mul(this.intensity * amt);
		}
		//Specular
		if (spec) {
			Vector3D h = l.sub(ray.dir);
			if (h.magnitude() > .0001) {
				double a = n.dot(h.normalize());
				if (a >= 0)
					result = result.add(
							new Vector3D(s.sm[0], s.sm[1], s.sm[2])
								.mul(Math.pow(a, s.sm[3]) * this.intensity)
					);
			}
		}
		return result;//.mul(this.intensity * amt);
	}

}
