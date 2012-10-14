
/**
 * @author Stephen Elliott
 * Class representing a point light in a RayTracing Scene.
 */
public class PointLight extends Light {

	private Vector3D pos;
	
	public PointLight(Vector3D p, double i) {
		super(i);
		this.pos = p;
	}

	@Override
	public Vector3D getDir(Vector3D p) {
		return this.pos.sub(p);
	}

	@Override
	public double getMaxInterval() {
		return 1;
	}

}
