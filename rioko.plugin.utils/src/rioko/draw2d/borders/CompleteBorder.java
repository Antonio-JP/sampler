package rioko.draw2d.borders;

/**
 * Special Side Border with all the lines
 * 
 * @author Antonio
 */
public class CompleteBorder extends SideBorder {
	public CompleteBorder() {
		super(SideBorder.DOWN_BORDER | SideBorder.LEFT_BORDER | SideBorder.RIGHT_BORDER | SideBorder.UP_BORDER);
	}
}
