package feature2;

public class Piece {
	private PieceType type;
	private Color color;
	
	public Piece(PieceType type, Color color) {
		this.type = type;
		this.color = color;
	}
	
	public PieceType getType()
	{
		return type;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setType(PieceType type)
	{
		this.type = type;
	}
}
