package ch.fhnw.comgr.citysim;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import ch.fhnw.ether.scene.attribute.IAttribute;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.IGeometryAttribute;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.util.UpdateRequest;
import ch.fhnw.util.math.Mat3;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.BoundingBox;

public class Field implements IMesh {

	private int content;
	private String name = "unnamed_field";
	private Queue queue;
	private final EnumSet<Flag> flags;
	private IMaterial material;
	private IGeometry geometry;
	private Vec3 position = Vec3.ZERO;
	private Mat4 transform = Mat4.ID;
	private BoundingBox bb;
	private int[] authorisations;

	public Field(IMaterial material, IGeometry geometry) {
		this(material, geometry, Queue.DEPTH);
	}

	public Field(IMaterial material, IGeometry geometry, Queue queue) {
		this(material, geometry, queue, NO_FLAGS);
	}

	public Field(IMaterial material, IGeometry geometry, Flag flag,
			Flag... flags) {
		this(material, geometry, Queue.DEPTH, EnumSet.of(flag, flags));
	}

	public Field(IMaterial material, IGeometry geometry, Flag flag) {
		this(material, geometry, Queue.DEPTH, EnumSet.of(flag));
	}

	public Field(IMaterial material, IGeometry geometry, EnumSet<Flag> flags) {
		this(material, geometry, Queue.DEPTH, flags);
	}

	public Field(IMaterial material, IGeometry geometry, Queue queue, Flag flag) {
		this(material, geometry, queue, EnumSet.of(flag));
	}

	public Field(IMaterial material, IGeometry geometry, Queue queue,
			Flag flag, Flag... flags) {
		this(material, geometry, queue, EnumSet.of(flag, flags));
	}

	public Field(IMaterial material, IGeometry geometry, Queue queue,
			EnumSet<Flag> flags) {
		this.material = material;
		this.geometry = geometry;
		this.queue = queue;
		this.flags = flags;
		checkAttributeConsistency(material, geometry);
	}

	private final UpdateRequest update = new UpdateRequest(true);

	// Field spezifisch
	public void setContent(int c) {
		this.content = c;
	}

	public int getContent() {
		return this.content;
	}

	public void setAuthorisations(int type) {
		// authorisation [north, south, east, west]
		authorisations = new int[4];
		switch (type) {
		case 0:
			// Gras
			this.authorisations[0] = 0;
			this.authorisations[1] = 0;
			this.authorisations[2] = 0;
			this.authorisations[3] = 0;
			break;
		case 1:
			// East West
			this.authorisations[0] = 0;
			this.authorisations[1] = 0;
			this.authorisations[2] = 1;
			this.authorisations[3] = 1;
			break;
		case 2:
			// North South
			this.authorisations[0] = 1;
			this.authorisations[1] = 1;
			this.authorisations[2] = 0;
			this.authorisations[3] = 0;
			break;
		case 3:
			// North South East West
			this.authorisations[0] = 1;
			this.authorisations[1] = 1;
			this.authorisations[2] = 1;
			this.authorisations[3] = 1;
			break;
		case 4:
			// North East
			this.authorisations[0] = 1;
			this.authorisations[1] = 0;
			this.authorisations[2] = 1;
			this.authorisations[3] = 0;
			break;
		case 5:
			// North West
			this.authorisations[0] = 1;
			this.authorisations[1] = 0;
			this.authorisations[2] = 0;
			this.authorisations[3] = 1;
			break;
		case 6:
			// South East
			this.authorisations[0] = 0;
			this.authorisations[1] = 1;
			this.authorisations[2] = 1;
			this.authorisations[3] = 0;
			break;
		case 7:
			// South West
			this.authorisations[0] = 0;
			this.authorisations[1] = 1;
			this.authorisations[2] = 0;
			this.authorisations[3] = 1;
			break;
		}
	}

	public int[] getAuthorisations() {
		return this.authorisations;
	}

	public int getEntryPointOfTaxiWithinTheField(Vec3 carPosition) {

		float distanceXWithinTheField = carPosition.x % 1;
		float distanceYWithinTheField = carPosition.y % 1;

		double fieldX = PathAlgorithm.getFields()[0][0].getBounds().getMaxX() - PathAlgorithm.getFields()[0][0].getBounds().getMinX(); 
		double fieldY = Math.abs(PathAlgorithm.getFields()[0][0].getBounds().getMaxY() - PathAlgorithm.getFields()[0][0].getBounds().getMinY()); 
		
		if (distanceXWithinTheField < (fieldX/10)) {
			// west
			return 3;
		}

		if (distanceXWithinTheField > (fieldX/10*9)) {
			// east
			return 2;
		}


		if (distanceYWithinTheField < fieldY/10) {
			// north
				return 0;
		}

		if (distanceYWithinTheField > (fieldY/10*9)) {
			// south
			return 1;
		}

		return 5;
	}

	// I3DObject implementation

	@Override
	public BoundingBox getBounds() {
		if (bb == null) {
			bb = new BoundingBox();
			float[] in = new float[3];
			float[] out = new float[3];
			getGeometry()
					.inspect(
							0,
							(attribute, data) -> {
								if (transform != Mat4.ID) {
									for (int i = 0; i < data.length; i += 3) {
										in[0] = data[i + 0];
										in[1] = data[i + 1];
										in[2] = data[i + 2];
										transform.transform(in, out);
										out[0] += position.x;
										out[1] += position.y;
										out[2] += position.z;
										bb.add(out);
									}

								} else {
									for (int i = 0; i < data.length; i += 3) {
										bb.add(data[i + 0] + position.x,
												data[i + 1] + position.y,
												data[i + 2] + position.z);
									}
								}
							});
		}
		return bb;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Field) {
			Field f = (Field) obj;
			return f.position.x == this.position.x
					&& f.position.y == this.position.y;
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public Vec3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vec3 position) {
		this.position = position;
		bb = null;
		updateRequest();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	// IMesh implementation

	@Override
	public Queue getQueue() {
		return queue;
	}

	@Override
	public EnumSet<Flag> getFlags() {
		return flags;
	}

	@Override
	public boolean hasFlag(Flag flag) {
		return flags.contains(flag);
	}

	@Override
	public IMaterial getMaterial() {
		return material;
	}

	@Override
	public IGeometry getGeometry() {
		return geometry;
	}

	@Override
	public Mat4 getTransform() {
		return transform;
	}

	@Override
	public void setTransform(Mat4 transform) {
		if (this.transform != transform) {
			this.transform = transform;
			bb = null;
			updateRequest();
		}
	}

	@Override
	public float[] getTransformedPositionData() {
		Mat4 tp = Mat4.multiply(Mat4.translate(position), transform);
		return tp.transform(geometry.getData()[0]);
	}

	@Override
	public float[][] getTransformedGeometryData() {
		float[][] src = geometry.getData();
		float[][] dst = new float[src.length][];
		IGeometryAttribute[] attrs = geometry.getAttributes();
		Mat4 tp = Mat4.multiply(Mat4.translate(position), transform);
		dst[0] = tp.transform(src[0]);
		for (int i = 1; i < src.length; ++i) {
			if (attrs[i].equals(IGeometry.NORMAL_ARRAY)) {
				Mat3 tn = new Mat3(tp).inverse().transpose();
				dst[i] = tn.transform(src[i]);
			} else {
				dst[i] = Arrays.copyOf(src[i], src[i].length);
			}
		}
		return dst;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public UpdateRequest getUpdater() {
		return update;
	}

	private void updateRequest() {
		update.request();
	}

	private static void checkAttributeConsistency(IMaterial material,
			IGeometry geometry) {
		// primitive types must match
		Primitive m = material.getType();
		Primitive g = geometry.getType();
		if (m != g)
			throw new IllegalArgumentException(
					"primitive types of material and geometry do not match: "
							+ m + " " + g);

		// geometry must provide all materials required by material
		List<IGeometryAttribute> geometryAttributes = Arrays.asList(geometry
				.getAttributes());
		for (IAttribute attr : material.getGeometryAttributes()) {
			if (!geometryAttributes.contains(attr))
				throw new IllegalArgumentException(
						"geometry does not provide required attribute: " + attr);
		}
	}

}