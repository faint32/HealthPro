package mpi.cbg.fly;


import java.io.Serializable;

/**
 * SIFT feature container
 */
public class Feature implements Comparable< Feature >, Serializable
{
	
	public static  final String FEATURE_SCALE = "scale";
	public static  final String FEATURE_ORIENTATION = "orientation";
	public static  final String FEATURE_LOCATION = "location";
	public static  final String FEATURE_DESCRIPTOR = "descriptor";

	public float scale;
	public float orientation;
	public float[] location;
	public float[] descriptor;

	/** Dummy constructor for Serialization to work properly. */
	public Feature() {}
	
	public Feature( float s, float o, float[] l, float[] d )
	{
		scale = s;
		orientation = o;
		location = l;
		descriptor = d;
	}

	/**
	 * comparator for making Features sortable
	 * please note, that the comparator returns -1 for
	 * this.scale &gt; o.scale, to sort the features in a descending order  
	 */
	public int compareTo( Feature f )
	{
		return scale < f.scale ? 1 : scale == f.scale ? 0 : -1;
	}
	
	public float descriptorDistance( Feature f )
	{
		float d = 0;
		for ( int i = 0; i < descriptor.length; ++i )
		{
			float a = descriptor[ i ] - f.descriptor[ i ];
			d += a * a;
		}
		return ( float )Math.sqrt( d );
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getOrientation() {
		return orientation;
	}

	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}

	public float[] getLocation() {
		return location;
	}

	public void setLocation(float[] location) {
		this.location = location;
	}

	public float[] getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(float[] descriptor) {
		this.descriptor = descriptor;
	}
	
}

