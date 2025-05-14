package feature2;

import org.jointheleague.api_wrapper.ReceivedMessage;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;

public class Feature2 extends Feature
{
    public final String COMMAND = "!chess";
    private Piece[][] board;
    
	public Feature2(String channelName) {
        super(channelName);
        helpEmbed = new HelpEmbed(COMMAND, "");
	}

	@Override
	public void handle(ReceivedMessage event) {
        String messageContent = event.getMessageContent();
        
		if (messageContent.equals(COMMAND))
		{
			resetBoard();
			event.sendResponse(getBoard());
		}
		else if (messageContent.contains(COMMAND))
		{
			String[] command = messageContent.replace(COMMAND, "").trim().split(" ");
			if (command.length == 1)
			{
				String from = command[0];
				setPiece(from, "", "");
				event.sendResponse(getBoard());
			}
			if (command.length == 2)
			{
				String from = command[0];
				String to = command[1];
				event.sendResponse(movePiece(from, to));
			}
			else if (command.length == 3)
			{
				String from = command[0];
				String toColor = command[1];
				String toPiece = command[2];
				setPiece(from, toColor, toPiece);
				event.sendResponse(getBoard());
			}
		}
	}
	
	public String movePiece(String from, String to) {
		// TODO Auto-generated method stub
		int fromCol = from.charAt(0) - 'a';
		int fromRow = from.charAt(1) - '1';
		int toCol = to.charAt(0) - 'a';
		int toRow = to.charAt(1) - '1';
		
		if (fromCol >= 8 || fromRow >= 8 || toCol >= 8 || toRow >= 8)
			return "Out of bounds";
		if (board[toRow][toCol].getColor() == board[fromRow][fromCol].getColor())
			return "You cannot take a piece of the same color";
		if (!isLegal(board, fromRow, fromCol, toRow, toCol))
			return "Illegal move";
		Piece[][] simBoard = simMove(fromRow, fromCol, toRow, toCol);
		int kingRow = 0;
		int kingCol = 0;
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				if (simBoard[r][c].getType() == PieceType.KING && simBoard[r][c].getColor() == board[fromRow][fromCol].getColor())
				{
					kingRow = r;
					kingCol = c;
				}
			}
		}
		if (checkAttack(simBoard, kingRow, kingCol, board[fromRow][fromCol].getColor() == Color.WHITE ? Color.BLACK : Color.WHITE))
			return "Endangers King";
		
		Piece temp = board[fromRow][fromCol];
		board[fromRow][fromCol] = new Piece(PieceType.EMPTY, Color.EMPTY);
		board[toRow][toCol] = temp;
		board[toRow][toCol].moved();
		return getBoard();
	}
	
	public void setPiece(String from, String toColor, String toPiece)
	{
		int fromCol = from.charAt(0) - 'a';
		int fromRow = from.charAt(1) - '1';
		if (fromCol >= 8 || fromRow >= 8)
			return;
		Color color;
		switch (toColor.toLowerCase().trim())
		{
			case "black":
				color = Color.BLACK;
				break;
			case "white":
				color = Color.WHITE;
				break;
			default:
				color = Color.EMPTY;
				break;
		}
		PieceType type;
		switch (toPiece.toLowerCase().trim())
		{
			case "king":
				type = PieceType.KING;
				break;
			case "queen":
				type = PieceType.QUEEN;
				break;
			case "rook":
				type = PieceType.ROOK;
				break;
			case "knight":
				type = PieceType.KNIGHT;
				break;
			case "bishop":
				type = PieceType.BISHOP;
				break;
			case "pawn":
				type = PieceType.PAWN;
				break;
			default:
				type = PieceType.EMPTY;
		}
		
		board[fromRow][fromCol] = new Piece(type, color);
	}

	public String getBoard() {
		String result = "```  a b c d e f g h\n";
		for (int r = 7; r >= 0; r--)
		{
			result += (r + 1) + " ";
			for (int c = 0; c < 8; c++)
			{
				Color color = board[r][c].getColor();
				switch(board[r][c].getType())
				{
					case KING:
						if (color == Color.BLACK)
							result += "♚";
						else
							result += "♔";
						break;
					case QUEEN:
						if (color == Color.BLACK)
							result += "♛";
						else
							result += "♕";
						break;
					case ROOK:
						if (color == Color.BLACK)
							result += "♜";
						else
							result += "♖";
						break;
					case KNIGHT:
						if (color == Color.BLACK)
							result += "♞";
						else
							result += "♘";
						break;
					case BISHOP:
						if (color == Color.BLACK)
							result += "♝";
						else
							result += "♗";
						break;
					case PAWN:
						if (color == Color.BLACK)
							result += "♟";
						else
							result += "♙";
						break;
					default:
						result += " ";
						break;
				}
				result += " ";
			}
			result += "\n";
		}
		result += "```";
		boolean check = false;
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				if (board[r][c].getType() == PieceType.KING) {
					if (checkAttack(board, r, c, board[r][c].getColor() == Color.WHITE ? Color.BLACK : Color.WHITE)) {
						check = true;
					}
				}
			}
		}
		if (check)
		{
			result += "\nCheck";
		}
		
		return result;
	}

	public void resetBoard()
	{
		board = new Piece[8][8];
		
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[r].length; c++)
			{
				board[r][c] = new Piece(PieceType.EMPTY, Color.EMPTY);
			}
		}
		
		board[7][0] = new Piece(PieceType.ROOK, Color.BLACK);
		board[7][1] = new Piece(PieceType.KNIGHT, Color.BLACK);
		board[7][2] = new Piece(PieceType.BISHOP, Color.BLACK);
		board[7][3] = new Piece(PieceType.QUEEN, Color.BLACK);
		board[7][4] = new Piece(PieceType.KING, Color.BLACK);
		board[7][5] = new Piece(PieceType.BISHOP, Color.BLACK);
		board[7][6] = new Piece(PieceType.KNIGHT, Color.BLACK);
		board[7][7] = new Piece(PieceType.ROOK, Color.BLACK);

		board[6][0] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][1] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][2] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][3] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][4] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][5] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][6] = new Piece(PieceType.PAWN, Color.BLACK);
		board[6][7] = new Piece(PieceType.PAWN, Color.BLACK);
		

		
		board[0][0] = new Piece(PieceType.ROOK, Color.WHITE);
		board[0][1] = new Piece(PieceType.KNIGHT, Color.WHITE);
		board[0][2] = new Piece(PieceType.BISHOP, Color.WHITE);
		board[0][3] = new Piece(PieceType.QUEEN, Color.WHITE);
		board[0][4] = new Piece(PieceType.KING, Color.WHITE);
		board[0][5] = new Piece(PieceType.BISHOP, Color.WHITE);
		board[0][6] = new Piece(PieceType.KNIGHT, Color.WHITE);
		board[0][7] = new Piece(PieceType.ROOK, Color.WHITE);

		board[1][0] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][1] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][2] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][3] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][4] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][5] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][6] = new Piece(PieceType.PAWN, Color.WHITE);
		board[1][7] = new Piece(PieceType.PAWN, Color.WHITE);
	}
	
	
	private boolean isLegal(Piece[][] board, int fromRow, int fromCol, int toRow, int toCol)
	{
		int direction;
		int rowDir;
		int colDir;
		Piece piece = board[fromRow][fromCol];
		switch (piece.getType())
		{
			case PAWN:
				direction = (piece.getColor() == Color.WHITE) ? 1 : -1;
				if (toRow - fromRow == direction)
				{
					if (Math.abs(toCol - fromCol) == 1 && board[toRow][toCol].getColor() != Color.EMPTY)
					{
						return true;
					}
					else if (toCol == fromCol && board[toRow][toCol].getColor() == Color.EMPTY)
					{
						return true;
					}
				}
				else if (piece.isFirstMove() && toRow - fromRow == 2 * direction)
				{
					if (checkLine(board, fromRow, fromCol, direction, 0, 2))
					{
						return true;
					}
				}
				return false;
			case KING:
				if (Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1 && !checkAttack(board, toRow, toCol, (piece.getColor() == Color.WHITE) ? Color.BLACK : Color.WHITE))
				{
					return true;
				}
				else if (piece.isFirstMove() && toRow == fromRow && Math.abs(toCol - fromCol) == 2)
				{
					direction = (toCol - fromCol) / 2;
					if (direction == -1)
					{
						if (checkLine(board, fromRow, fromCol, 0, direction, 4))
						{
							for (int c = fromCol; c >= toCol; c--)
							{
								if (checkAttack(board, fromRow, c, (piece.getColor() == Color.WHITE) ? Color.BLACK : Color.WHITE))
								{
									return false;
								}
							}
							if (board[toRow][0].getType() == PieceType.ROOK && board[toRow][0].isFirstMove())
							{
								movePiece("a" + (toRow + 1), "d" + (toRow + 1));
								return true;
							}
						}
					}
					else if (direction == 1)
					{
						if (checkLine(board, fromRow, fromCol, 0, direction, 3))
						{
							for (int c = fromCol; c <= toCol; c++)
							{
								if (checkAttack(board, fromRow, c, (piece.getColor() == Color.WHITE) ? Color.BLACK : Color.WHITE))
								{
									return false;
								}
							}
							if (board[toRow][7].getType() == PieceType.ROOK && board[toRow][7].isFirstMove())
							{
								movePiece("h" + (toRow + 1), "f" + (toRow + 1));
								return true;
							}
						}
					}
				}
				return false;
			case QUEEN:
				rowDir = (toRow == fromRow) ? 0 : (Math.abs(toRow - fromRow) / (toRow - fromRow));
				colDir = (toCol == fromCol) ? 0 : (Math.abs(toCol - fromCol) / (toCol - fromCol));
				if (rowDir != 0 && colDir != 0 && Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol))
				{
					if (checkLine(board, fromRow, fromCol, rowDir, colDir, Math.abs(toRow - fromRow)))
					{
						return true;
					}
				}
				else if (rowDir != 0 && colDir == 0)
				{
					if (checkLine(board, fromRow, fromCol, rowDir, 0, Math.abs(toRow - fromRow)))
					{
						return true;
					}
				}
				else if (rowDir == 0 && colDir != 0)
				{
					if (checkLine(board, fromRow, fromCol, 0, colDir, Math.abs(toCol - fromCol)))
					{
						return true;
					}
				}
				return false;
			case ROOK:
				rowDir = (toRow == fromRow) ? 0 : (Math.abs(toRow - fromRow) / (toRow - fromRow));
				colDir = (toCol == fromCol) ? 0 : (Math.abs(toCol - fromCol) / (toCol - fromCol));
				if (rowDir != 0 && colDir == 0)
				{
					if (checkLine(board, fromRow, fromCol, rowDir, 0, Math.abs(toRow - fromRow)))
					{
						return true;
					}
				}
				else if (rowDir == 0 && colDir != 0)
				{
					if (checkLine(board, fromRow, fromCol, 0, colDir, Math.abs(toCol - fromCol)))
					{
						return true;
					}
				}
				return false;
			case KNIGHT:
				if ((Math.abs(toRow - fromRow) == 2 && Math.abs(toCol - fromCol) == 1) || (Math.abs(toRow - fromRow) == 1 && Math.abs(toCol - fromCol) == 2))
				{
					return true;
				}
				return false;
			case BISHOP:
				rowDir = (toRow == fromRow) ? 0 : (Math.abs(toRow - fromRow) / (toRow - fromRow));
				colDir = (toCol == fromCol) ? 0 : (Math.abs(toCol - fromCol) / (toCol - fromCol));
				if (rowDir != 0 && colDir != 0 && Math.abs(toRow - fromRow) == Math.abs(toCol - fromCol))
				{
					if (checkLine(board, fromRow, fromCol, rowDir, colDir, Math.abs(toRow - fromRow)))
					{
						return true;
					}
				}
				return false;
			default:
				return false;
		}
	}
	
	private boolean checkLine(Piece[][] board, int row, int col, int rowDir, int colDir, int steps)
	{
		for (int i = 1; i < steps; i++)
		{
			if (board[row + rowDir * i][col + colDir * i].getType() != PieceType.EMPTY)
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean checkAttack(Piece[][] board, int row, int col, Color color)
	{
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				if (board[r][c].getColor() == color)
				{
					if (isLegal(board, r, c, row, col))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean checkMate(Color color)
	{
		for (int fromRow = 0; fromRow < 8; fromRow++)
			for (int fromCol = 0; fromCol < 8; fromCol++)
				if (board[fromRow][fromCol].getColor() == color)
					for (int toRow = 0; toRow < 8; toRow++)
						for (int toCol = 0; toCol < 8; toCol++)
							if (isLegal(board, fromRow, fromCol, toRow, toCol))
							{
								Piece[][] simBoard = simMove(fromRow, fromCol, toRow, toCol);
								int kingRow = 0;
								int kingCol = 0;
								for (int r = 0; r < 8; r++)
								{
									for (int c = 0; c < 8; c++)
									{
										if (simBoard[r][c].getType() == PieceType.KING && simBoard[r][c].getColor() == board[fromRow][fromCol].getColor())
										{
											kingRow = r;
											kingCol = c;
										}
									}
								}
								if (!checkAttack(simBoard, kingRow, kingCol, board[fromRow][fromCol].getColor() == Color.WHITE ? Color.BLACK : Color.WHITE))
									return false;
							}
		return true;
	}
	
	private Piece[][] simMove(int fromRow, int fromCol, int toRow, int toCol)
	{
		Piece[][] simBoard = new Piece[8][8];
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				if (r == toRow && c == toCol)
					simBoard[r][c] = board[fromRow][fromCol];
				else if (r == fromRow && c == fromCol)
					simBoard[r][c] = new Piece(PieceType.EMPTY, Color.EMPTY);
				else
					simBoard[r][c] = board[r][c];
			}
		}
		return simBoard;
	}
}
