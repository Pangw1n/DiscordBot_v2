package feature2;

public class Piece {
	private PieceType type;
	private Color color;
	private boolean firstMove;
	
	public Piece(PieceType type, Color color) {
		if (type == PieceType.EMPTY || color == Color.EMPTY) {
			this.type = PieceType.EMPTY;
			this.color = Color.EMPTY;
		}
		else {
			this.type = type;
			this.color = color;
		}
		firstMove = true;
	}
	
	public PieceType getType()
	{
		return type;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public boolean isFirstMove()
	{
		return firstMove;
	}
	
	public void moved()
	{
		firstMove = false;
	}
}
