package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Size implements Visitor<Integer> {

	// TODO entirely your job

	@Override
	public Integer onPolygon(final Polygon p) {
		return 1;
	}

	@Override
	public Integer onCircle(final Circle c) {
		Circle circle = new Circle(c.getRadius());
		if(circle.getRadius() == c.getRadius())
			return 1;
		return -1;
	}

	@Override
	public Integer onGroup(final Group g) {
		int totGroup = 0;
		for(Shape shape : g.getShapes())
			totGroup += shape.accept(this);
		return totGroup;
	}

	@Override
	public Integer onRectangle(final Rectangle q) {
		Rectangle rect = new Rectangle(q.getWidth(), q.getHeight());
		if(rect.getHeight() == q.getHeight() && rect.getWidth() == q.getWidth())
			return 1;
		return -1;
	}

	/*
	Since these are visitors they just need to
	accept the objects and will run the objects
	method
	 */

	@Override
	public Integer onOutline(final Outline o) {
		Shape outLine = o.getShape();
		return outLine.accept(this);
	}

	@Override
	public Integer onFill(final Fill c) {
		Shape fill = c.getShape();
		return fill.accept(this);
	}

	@Override
	public Integer onLocation(final Location l) {
		Shape loc = l.getShape();
		return loc.accept(this);
	}

	@Override
	public Integer onStroke(final Stroke c) {
		Shape stroke = c.getShape();
		return stroke.accept(this);
	}
}
