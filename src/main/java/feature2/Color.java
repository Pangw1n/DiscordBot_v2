package feature2;

public enum Color {
	BLACK, WHITE, EMPTY;
	
	public Color opposite()
	{
		if (this == BLACK)
			return WHITE;
		if (this == WHITE)
			return BLACK;
		else
			return EMPTY;
	}
}
