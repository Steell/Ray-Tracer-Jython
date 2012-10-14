
/**
 * @author Stephen Elliott
 * Class representing a Camera.
 */
public class Camera {
	
	public Vector3D[] basis;
	public Vector3D pos;
	
	public Camera(Vector3D p, Vector3D d) {
		this.pos = p;
		
		//Setup basis
		Vector3D zc = d.neg().normalize();
		Vector3D xc = zc.neg().cross(new Vector3D(0, 1, 0)).normalize();
		Vector3D yc = zc.cross(xc);
		this.basis = new Vector3D[] { xc, yc, zc };
	}
	
}
