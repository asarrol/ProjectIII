package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import java.util.List;

import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

	// TODO entirely your job (except onCircle)

	private final Canvas canvas;

	private final Paint paint;

	public Draw(final Canvas canvas, final Paint paint) {
		this.canvas = canvas; // FIXME
		this.paint = paint; // FIXME
		paint.setStyle(Style.STROKE);
	}

	@Override
	public Void onCircle(final Circle c) {
		canvas.drawCircle(0, 0, c.getRadius(), paint);
		return null;
	}

	@Override
	public Void onStroke(final Stroke c) {
		int stroke = paint.getColor();
		paint.setColor(c.getColor());
		Shape shape = c.getShape();
		shape.accept(this);
		paint.setColor(stroke);
		return null;
	}

	@Override
	public Void onFill(final Fill f) {
		Style fill = paint.getStyle();
		paint.setStyle(Style.FILL_AND_STROKE);
		f.getShape().accept(this);
		paint.setStyle(fill);
		return null;
	}

	@Override
	public Void onGroup(final Group g) {
		List <? extends Shape> shapeList = g.getShapes();
		for(Shape getShapes : shapeList)
		{
			getShapes.accept(this);
		}
		return null;
	}

	@Override
	public Void onLocation(final Location l) {
		canvas.translate(l.getX(),l.getY());
		Shape shape = l.getShape();
		shape.accept(this);
		canvas.translate(-l.getX(),-l.getY());
		return null;
	}

	@Override
	public Void onRectangle(final Rectangle r) {
		canvas.drawRect(0,0,r.getWidth(),r.getHeight(),paint);
		return null;
	}

	@Override
	public Void onOutline(Outline o) {
		Style shapeOutline = paint.getStyle();
		paint.setStyle(Style.STROKE);
		o.getShape().accept(this);
		paint.setStyle(shapeOutline);
		return null;
	}

	/*
	Complicated form of code because it draws
	the polygon by connecting the points that
	is the passed through and creates that
	polygon
	 */

	@Override
	public Void onPolygon(final Polygon s) {
		int polyPoints = (s.getPoints().size());
		final float[] pts = new float[4*polyPoints];

		for(int x = 0; x < polyPoints-1; x++)
		{
			pts[4*x] = s.getPoints().get(x).getX();
			pts[4*x+1] = s.getPoints().get(x).getY();
			pts[4*x+2] = s.getPoints().get(x+1).getY();
			pts[4*x+3] = s.getPoints().get(x+1).getY();
		}

		pts[4*(polyPoints-1)] = s.getPoints().get(polyPoints-1).getX();
		pts[4*(polyPoints-1)+1] = s.getPoints().get(polyPoints-1).getY();
		pts[4*(polyPoints-1)+2] = s.getPoints().get(0).getY();
		pts[4*(polyPoints-1)+3] = s.getPoints().get(0).getY();

		canvas.drawLines(pts, paint);
		return null;
	}
}
