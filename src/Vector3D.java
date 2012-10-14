
/**
 * @author Stephen Elliott
 * Class representing a 3D Vector.
 */
public class Vector3D {

	double x;
	double y;
	double z;
	private Double mag;

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private Vector3D(double x, double y, double z, double m) {
		this(x, y, z);
		this.mag = m;
	}
	
	public Vector3D(double[] xyz) {
		this(xyz[0], xyz[1], xyz[2]);
	}

	/**
	 * @return This Vector3D as a 3D Array of Doubles.
	 */
	public double[] toArray() {
		return new double[] { this.x, this.y, this.z };
	}
	
	/**
	 * @param matrix 3x3 Matrix
	 * @return This Vector3D transformed by the given matrix.
	 */
	public Vector3D transform(Vector3D[] matrix) {
		return new Vector3D(
				this.x * matrix[0].x + this.y * matrix[1].x + this.z * matrix[2].x,
				this.x * matrix[0].y + this.y * matrix[1].y + this.z * matrix[2].y,
				this.x * matrix[0].z + this.y * matrix[1].z + this.z * matrix[2].z
		);
	}
	
	/**
	 * @return The magnitude of this Vector3D. 
	 */
	public double magnitude() {
		if (this.mag == null)
			this.mag = Math.sqrt(
					this.x * this.x +
					this.y * this.y +
					this.z * this.z
			);
		return this.mag;
		
	}
	
	/**
	 * @return The unit vector pointing in the same direction as this Vector3D.
	 */
	public Vector3D normalize() {
		double m = this.magnitude();
		return new Vector3D(
				this.x / m,
				this.y / m,
				this.z / m,
				1
		);
	}
	
	/**
	 * @return The dot-product between the two Vector3Ds.
	 */
	public double dot(Vector3D o) {
		return this.x * o.x + this.y * o.y + this.z * o.z;
	}
	
	/**
	 * @return This Vector3D, negated.
	 */
	public Vector3D neg() {
		return new Vector3D(-this.x, -this.y, -this.z);
	}
	
	/**
	 * @return This Vector3D divided by the given scalar o.
	 */
	public Vector3D div(double o) {
		return new Vector3D(
				this.x / o,
				this.y / o,
				this.z / o
		);
	}
	
	/**
	 * @return This Vector3D multiplied by the given scalar o.
	 */
	public Vector3D mul(double o) {
		return new Vector3D(
				this.x * o,
				this.y * o,
				this.z * o
		);
	}
	
	/** HACK FOR COLORS
	 * @return This Vector3D multiplied by the given Vector3D o.
	 */
	public Vector3D mul(Vector3D o) {
		return new Vector3D(
				this.x * o.x,
				this.y * o.y,
				this.z * o.z
		);
	}
	
	/**
	 * @return The cross product of the two Vector3Ds.
	 */
	public Vector3D cross(Vector3D o) {
		return new Vector3D (
				this.y * o.z - this.z * o.y,
				this.z * o.x - this.x * o.z,
				this.x * o.y - this.y * o.x
		);
	}
	
	/**
	 * @return Vector addition.
	 */
	public Vector3D add(Vector3D o) {
		return new Vector3D(
				this.x + o.x,
				this.y + o.y,
				this.z + o.z
		);
	}
	
	/**
	 * @return Vector addition.
	 */
	public Vector3D add(double[] o) {
		return new Vector3D(
				this.x + o[0],
				this.y + o[1],
				this.z + o[2]
		);
	}
	
	/**
	 * @return Vector subtraction.
	 */
	public Vector3D sub(Vector3D o) {
		return this.add(o.neg());
	}

}
