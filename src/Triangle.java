
/**
 * @author Stephen Elliott
 * Class representing a curved triangle in a RayTracing Scene.
 */
public class Triangle extends Surface {

	private Vector3D v0, n0, n1, n2;
	private double a,b,c,d,e,f;

	public Triangle(Vector3D v0, Vector3D v1, Vector3D v2,
			Vector3D n0, Vector3D n1, Vector3D n2,
			double[] am, double[] dm, double[] sm) {
		super(am, dm, sm);
		this.v0 = v0;
		this.n0 = n0;
		this.n1 = n1;
		this.n2 = n2;

		Vector3D abc = v0.sub(v1);
		this.a = abc.x;
		this.b = abc.y;
		this.c = abc.z;
		Vector3D def = v0.sub(v2);
		this.d = def.x;
		this.e = def.y;
		this.f = def.z;
	}

	@Override
	public Vector3D getNormal(Vector3D p, HitRecord hr) {
		//Barycentric interpolation
		return this.n0.mul(hr.alpha)
			.add(this.n1.mul(hr.beta))
			.add(this.n2.mul(hr.gamma))
			.normalize();
	}

	@Override
	public double rayIntersect(Ray ray, double t0, double t1, HitRecord hr) {
		double g,h,i,j,k,l,t,m, eihf, gfdi, dheg, akjb, jcal, blkc;
		g = ray.dir.x;
		h = ray.dir.y;
		i = ray.dir.z;
		Vector3D jkl = this.v0.sub(ray.pos);
		j = jkl.x;
		k = jkl.y;
		l = jkl.z;

		eihf = e*i - h*f;
		gfdi = g*f - d*i;
		dheg = d*h - e*g;
		akjb = a*k - j*b;
		jcal = j*c - a*l;
		blkc = b*l - k*c;

		m = a*eihf + b*gfdi + c*dheg;	
		t = -(f*akjb + e*jcal + d*blkc) / m;
		
		if (t < t0 || t > t1)
			return Double.POSITIVE_INFINITY;
		
		double gamma = (i*akjb + h*jcal + g*blkc) / m;
		if (gamma < 0 || gamma > 1)
			return Double.POSITIVE_INFINITY;
		
		double beta = (j*eihf + k*gfdi + l*dheg) / m;
		if (beta < 0 || beta > 1 - gamma)
			return Double.POSITIVE_INFINITY;
		
        if (hr != null)
            hr.setABG(1 - beta - gamma, beta, gamma);
		return t;
	}

}
