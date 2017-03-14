package edu.luc.etl.cs313.android.shapes.model;

import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

	// TODO entirely your job (except onCircle)

	/*
	The visitors need acceptors because they
	will just run the methods that the shape
	knows in order to obtain the returned
	values
	 */

	@Override
	public Location onCircle(final Circle c) {
		final int radius = c.getRadius();
		return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
	}

	@Override
	public Location onFill(final Fill f) {
		Shape shape = f.getShape();
		return shape.accept(this);
	}

	/*
	Deals with multiple objects. It has to account for
	all the objects that are passed into it and creates
	a group around it
	 */

	@Override
	public Location onGroup(final Group g) {
		List<? extends Shape> shapeList = g.getShapes();
		Location shapeLoc = shapeList.get(0).accept(this);
		Rectangle rect = (Rectangle)shapeLoc.getShape();

		int min_X = shapeLoc.getX();
		int min_Y = shapeLoc.getY();
		int max_X = min_X + rect.getWidth();
		int max_Y = min_Y + rect.getHeight();

		for(int x = 1; x < shapeList.size(); x++)
		{
			Location boxedShapeLoc = shapeList.get(x).accept(this);
			Rectangle boxingShapeList = (Rectangle)boxedShapeLoc.getShape();

			int minBox_x = boxedShapeLoc.getX();
			int minBox_y = boxedShapeLoc.getY();
			int maxBox_x = minBox_x + boxingShapeList.getWidth();
			int maxBox_y = minBox_y + boxingShapeList.getHeight();

			if(minBox_x < min_X)
				min_X = minBox_x;
			if(maxBox_y < min_Y)
				min_Y = minBox_y;
			if(max_X < maxBox_x)
				max_X = maxBox_x;
			if(max_Y < maxBox_y)
				max_Y = maxBox_y;

		}

		shapeLoc = new Location(min_X,min_Y, new Rectangle(max_X - min_X,max_Y-min_Y));

		return shapeLoc;
	}

	@Override
	public Location onLocation(final Location l) {
		Shape shape = l.getShape();
		Location loc = shape.accept(this);
		return new Location(loc.getX() + l.getX(), loc.getY() + l.getY(), loc.getShape());
	}

	@Override
	public Location onRectangle(final Rectangle r) {
		final int width = r.getWidth();
		final int height = r.getHeight();
		return new Location(0,0, new Rectangle(width, height));
	}

	@Override
	public Location onStroke(final Stroke c) {
		Shape shape = c.getShape();
		return shape.accept(this);
	}

	@Override
	public Location onOutline(final Outline o) {
		Shape shape = o.getShape();
		return shape.accept(this);
	}

	//Basically the same as onGroup so can just call

	@Override
	public Location onPolygon(final Polygon s) {
		return onGroup(s);
}
