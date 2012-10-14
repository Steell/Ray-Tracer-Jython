
/**
 * @author Stephen Elliott
 * Class representing a Ray.
 */
public class Ray {

	public Vector3D pos;
	public Vector3D dir;
	
	public Ray(Vector3D p, Vector3D d) {
		this.pos = p;
		this.dir = d;
	}
	
	public Vector3D getPoint(double t) {
		return this.pos.add(this.dir.mul(t));
	}
	
}
