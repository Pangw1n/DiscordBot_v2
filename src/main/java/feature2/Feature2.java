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
	}
	
	private String getBoard() {
		String result = "";
		for (int r = 0; r < 8; r++)
		{
			for (int c = 0; c < 8; c++)
			{
				result += "♔";
			}
			result += "\n";
		}
		return result;
	}

	public void resetBoard()
	{
		board = new Piece[8][8];
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
