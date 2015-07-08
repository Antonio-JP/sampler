package rioko.layouts.graphImpl;

import rioko.grapht.VertexFactory;
import rioko.layouts.geometry.Point;

public class LayoutVertexFactory implements VertexFactory<LayoutVertex> {

	@Override
	public LayoutVertex createVertex(Object... args) {
		if(args.length == 2 && args[0] instanceof Double && args[1] instanceof Double) {
			return new LayoutVertex((Double)args[0], (Double)args[1]);
		} else if(args.length == 3 && args[0] instanceof Point && args[1] instanceof Double && args[2] instanceof Double){
			return new LayoutVertex((Point)args[0], (Double)args[1], (Double)args[2]);
		} else if(args.length == 4 && args[0] instanceof Double && args[1] instanceof Double && args[2] instanceof Double && args[3] instanceof Double){
			return new LayoutVertex((Double)args[0], (Double)args[1], (Double)args[2], (Double)args[3]);
		}
		
		return null;
	}

}
