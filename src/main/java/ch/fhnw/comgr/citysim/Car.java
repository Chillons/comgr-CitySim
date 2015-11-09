package ch.fhnw.comgr.citysim;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import ch.fhnw.ether.scene.attribute.IAttribute;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshLibrary;
import ch.fhnw.ether.scene.mesh.IMesh.Flag;
import ch.fhnw.ether.scene.mesh.IMesh.Queue;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.IGeometryAttribute;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.util.UpdateRequest;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.BoundingBox;

public class Car implements IMesh{
	private String name = "unnamed_field";
	private Queue queue;
	private Set<Flag> flags;
	private IMaterial material;
	private IGeometry geometry;
	private Vec3 position = Vec3.ZERO;
	private Mat4 transform = Mat4.ID;
	private BoundingBox bb;
	
	
	public Car(){			
		 this(new ColorMaterial(RGBA.WHITE), Util.getDefaultCar());
	}

		
	public Car(IMaterial material, IGeometry geometry) {
		this(material, geometry, Queue.DEPTH);
	}

	public Car(IMaterial material, IGeometry geometry, Queue queue) {
		this(material, geometry, queue, NO_FLAGS);
	}

	public Car(IMaterial material, IGeometry geometry, Flag flag, Flag... flags) {
		this(material, geometry, Queue.DEPTH, EnumSet.of(flag, flags));
	}

	public Car(IMaterial material, IGeometry geometry, Flag flag) {
		this(material, geometry, Queue.DEPTH, EnumSet.of(flag));
	}

	public Car(IMaterial material, IGeometry geometry, EnumSet<Flag> flags) {
		this(material, geometry, Queue.DEPTH, flags);
	}

	public Car(IMaterial material, IGeometry geometry, Queue queue, Flag flag) {
		this(material, geometry, queue, EnumSet.of(flag));
	}

	public Car(IMaterial material, IGeometry geometry, Queue queue, Flag flag, Flag... flags) {
		this(material, geometry, queue, EnumSet.of(flag, flags));
	}

	public Car(IMaterial material, IGeometry geometry, Queue queue, EnumSet<Flag> flags) {
		this.material = material;
		this.geometry = geometry;
		this.queue = queue;
		this.flags = Collections.unmodifiableSet(flags);
		checkAttributeConsistency(material, geometry);
	}
		
	private final UpdateRequest update = new UpdateRequest(true);



	// I3DObject implementation

	@Override
	public BoundingBox getBounds() {
		if (bb == null) {
			bb = new BoundingBox();
			float[] in = new float[3];
			float[] out = new float[3];
			getGeometry().inspect(0, (attribute, data) -> {
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
						bb.add(data[i + 0] + position.x, data[i + 1] + position.y, data[i + 2] + position.z);
					}
				}
			});
		}
		return bb;
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
	public Set<Flag> getFlags() {
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

	private static void checkAttributeConsistency(IMaterial material, IGeometry geometry) {
		// primitive types must match
		Primitive m = material.getType();
		Primitive g = geometry.getType();
		if (m != g)
			throw new IllegalArgumentException("primitive types of material and geometry do not match: " + m + " " + g);

		// geometry must provide all materials required by material
		List<IGeometryAttribute> ga = Arrays.asList(geometry.getAttributes());
		List<IAttribute> ma = material.getRequiredAttributes();
		if (!ga.containsAll(ma))
			throw new IllegalArgumentException("primitive types of material and geometry do not match: " + m + " " + g);
	}

}
