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
			if (command.length == 3)
			{
				String from = command[0];
				String toColor = command[1];
				String toPiece = command[2];
				setPiece(from, toColor, toPiece);
				event.sendResponse(getBoard());
			}
			else if (command.length == 2)
			{
				String from = command[0];
				String to = command[1];
				movePiece(from, to);
				event.sendResponse(getBoard());
			}
		}
	}
	
	private void movePiece(String from, String to) {
		// TODO Auto-generated method stub
		int fromCol = from.charAt(0) - 'a';
		int fromRow = from.charAt(0) - '1';
		int toCol = from.charAt(0) - 'a';
		int toRow = from.charAt(0) - '1';
		
		board[toRow][toCol] = board[fromRow][fromCol];
		board[fromRow][fromCol] = new Piece(PieceType.EMPTY, Color.EMPTY);
	}
	
	private void setPiece(String from, String toColor, String toPiece)
	{
		int fromCol = from.charAt(0) - 'a';
		int fromRow = from.charAt(0) - '1';
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
			default:
				type = PieceType.EMPTY;
		}
		
		board[fromRow][fromCol] = new Piece(type, color);
	}

	private String getBoard() {
		System.out.println("get board");
		String result = "```  a b c d e f g h\n";
		for (int r = 0; r < 8; r++)
		{
			result += (8 - r) + " ";
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
		System.out.println(result);
		return result;
	}

	public void resetBoard()
	{
		System.out.println("reset board");
		board = new Piece[8][8];
		
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[r].length; c++)
			{
				board[r][c] = new Piece(PieceType.EMPTY, Color.EMPTY);
			}
		}
		
		board[0][0] = new Piece(PieceType.ROOK, Color.BLACK);
		board[0][1] = new Piece(PieceType.KNIGHT, Color.BLACK);
		board[0][2] = new Piece(PieceType.BISHOP, Color.BLACK);
		board[0][3] = new Piece(PieceType.QUEEN, Color.BLACK);
		board[0][4] = new Piece(PieceType.KING, Color.BLACK);
		board[0][5] = new Piece(PieceType.BISHOP, Color.BLACK);
		board[0][6] = new Piece(PieceType.KNIGHT, Color.BLACK);
		board[0][7] = new Piece(PieceType.ROOK, Color.BLACK);

		board[1][0] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][1] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][2] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][3] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][4] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][5] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][6] = new Piece(PieceType.PAWN, Color.BLACK);
		board[1][7] = new Piece(PieceType.PAWN, Color.BLACK);
		

		
		board[7][0] = new Piece(PieceType.ROOK, Color.WHITE);
		board[7][1] = new Piece(PieceType.KNIGHT, Color.WHITE);
		board[7][2] = new Piece(PieceType.BISHOP, Color.WHITE);
		board[7][3] = new Piece(PieceType.QUEEN, Color.WHITE);
		board[7][4] = new Piece(PieceType.KING, Color.WHITE);
		board[7][5] = new Piece(PieceType.BISHOP, Color.WHITE);
		board[7][6] = new Piece(PieceType.KNIGHT, Color.WHITE);
		board[7][7] = new Piece(PieceType.ROOK, Color.WHITE);

		board[6][0] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][1] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][2] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][3] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][4] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][5] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][6] = new Piece(PieceType.PAWN, Color.WHITE);
		board[6][7] = new Piece(PieceType.PAWN, Color.WHITE);
	}

}
