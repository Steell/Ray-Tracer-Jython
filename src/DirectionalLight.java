
/**
 * @author Stephen Elliott
 * Class representing a directional light in a RayTracing Scene.
 */
public class DirectionalLight extends Light {

	private Vector3D dir;
	
	public DirectionalLight(Vector3D d, double i) {
		super(i);
		this.dir = d.normalize();
	}

	@Override
	protected Vector3D getDir(Vector3D p) {
		return this.dir.neg();
	}

	@Override
	protected double getMaxInterval() {
		return Double.POSITIVE_INFINITY;
	}
	
}
