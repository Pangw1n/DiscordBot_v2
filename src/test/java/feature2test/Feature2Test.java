package feature2test;

import org.jointheleague.api_wrapper.ReceivedMessage;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import feature2.Color;
import feature2.Feature2;
import feature2.PieceType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class Feature2Test {

    private final String testChannelName = "test";
    private final Feature2 feature2 = new Feature2(testChannelName);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Mock
    private ReceivedMessage receivedMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void itShouldNotPrintToSystemOut() {
        String expected = "";
        String actual = outContent.toString();

        assertEquals(expected, actual);
        System.setOut(originalOut);
    }

    @Test
    void itShouldHaveACommand() {
        //Given

        //When
        String command = feature2.COMMAND;

        //Then

        if(!(feature2 instanceof Feature2)){
            assertNotEquals("!chess", command);
        }

        assertNotEquals("", command);
        assertNotEquals("!", command);
        assertEquals('!', command.charAt(0));
        assertNotNull(command);
    }

    @Test
    void itShouldHandleMessagesWithCommand() {
        //Given
        HelpEmbed helpEmbed = new HelpEmbed(feature2.COMMAND, "test");
        when(receivedMessage.getMessageContent()).thenReturn(feature2.COMMAND);

        //When
        feature2.handle(receivedMessage);

        //Then
        verify(receivedMessage, times(1)).sendResponse(anyString());
    }

    @Test
    void itShouldNotHandleMessagesWithoutCommand() {
        //Given
        String command = "";
        when(receivedMessage.getMessageContent()).thenReturn(command);

        //When
        feature2.handle(receivedMessage);

        //Then
        verify(receivedMessage, never()).sendResponse("");
    }

    @Test
    void itShouldHaveAHelpEmbed() {
        //Given

        //When
        HelpEmbed actualHelpEmbed = feature2.getHelpEmbed();

        //Then
        assertNotNull(actualHelpEmbed);
    }

    @Test
    void itShouldHaveTheCommandAsTheTitleOfTheHelpEmbed() {
        //Given

        //When
        String helpEmbedTitle = feature2.getHelpEmbed().getTitle();
        String command = feature2.COMMAND;

        //Then
        assertEquals(command, helpEmbedTitle);
    }
    
    @Test
    void testCheckLine()
    {
    	//Given
    	feature2.resetBoard();
    	
    	//When
    	boolean result1 = feature2.checkLine(feature2.board, 0, 7, 1, 0, 5);
    	boolean result2 = feature2.checkLine(feature2.board, 1, 0, 1, 0, 1);
    	
    	//Then
    	assertFalse(result1);
    	assertTrue(result2);
    }
    
    @Test
    void testPawnMoves()
    {
    	//Given
    	feature2.resetBoard();
    	
    	//When
    	boolean e2e3 = feature2.isLegal(feature2.board, 1, 4, 2, 4);
    	boolean e2e4 = feature2.isLegal(feature2.board, 1, 4, 3, 4);
    	boolean e7e6 = feature2.isLegal(feature2.board, 6, 4, 5, 4);
    	boolean e7e5 = feature2.isLegal(feature2.board, 6, 4, 4, 4);
    	boolean e2f3 = feature2.isLegal(feature2.board, 1, 4, 2, 5);
    	boolean e7f6 = feature2.isLegal(feature2.board, 1, 4, 2, 5);
    	boolean e2f4 = feature2.isLegal(feature2.board, 1, 4, 3, 5);
    	boolean e7f5 = feature2.isLegal(feature2.board, 6, 4, 4, 5);
    	
    	feature2.setPiece(2, 5, Color.BLACK, PieceType.PAWN);
    	boolean e2xf3 = feature2.isLegal(feature2.board, 1, 4, 2, 5);
    	feature2.setPiece(2, 4, Color.BLACK, PieceType.PAWN);
    	boolean e2xe3 = feature2.isLegal(feature2.board, 1, 4, 2, 4);
    	feature2.setPiece(5, 5, Color.WHITE, PieceType.PAWN);
    	boolean e7xf6 = feature2.isLegal(feature2.board, 6, 4, 5, 5);
    	feature2.setPiece(5, 4, Color.WHITE, PieceType.PAWN);
    	boolean e7xe6 = feature2.isLegal(feature2.board, 6, 4, 5, 4);
    	boolean e7e5blocked = feature2.isLegal(feature2.board, 6, 4, 4, 4);
    	boolean e2e4blocked = feature2.isLegal(feature2.board, 1, 4, 3, 4);
    	feature2.setPiece(2, 4, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(5, 4, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(3, 4, Color.BLACK, PieceType.PAWN);
    	feature2.setPiece(4, 4, Color.WHITE, PieceType.PAWN);
    	boolean e7xe5 = feature2.isLegal(feature2.board, 6, 4, 4, 4);
    	boolean e2xe4 = feature2.isLegal(feature2.board, 1, 4, 3, 4);
    	
    	feature2.resetBoard();
    	
    	feature2.movePiece("e7", "e6");
    	boolean e6e4 = feature2.isLegal(feature2.board, 5, 4, 3, 4);
    	feature2.movePiece("e2", "e3");
    	boolean e3e5 = feature2.isLegal(feature2.board, 2, 4, 4, 4);
    	
    	//Then
    	assertTrue(e2e3);
    	assertTrue(e2e4);
    	assertTrue(e7e6);
    	assertTrue(e7e5);
    	assertFalse(e2xe3);
    	assertFalse(e7xe6);
    	assertFalse(e2f4);
    	assertFalse(e7f5);
    	
    	assertFalse(e2f3);
    	assertTrue(e2xf3);
    	assertFalse(e7f6);
    	assertTrue(e7xf6);
    	assertFalse(e7e5blocked);
    	assertFalse(e2e4blocked);
    	assertFalse(e2xe4);
    	assertFalse(e7xe5);
    	
    	assertFalse(e6e4);
    	assertFalse(e3e5);
    }
    @Test
    void testKingMoves()
    {
    	//Given
    	feature2.resetBoard();
    	
    	//When
    	feature2.setPiece(1, 4, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(1, 3, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(0, 6, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(0, 5, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(0, 3, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(0, 2, Color.EMPTY, PieceType.EMPTY);
    	feature2.setPiece(0, 1, Color.EMPTY, PieceType.EMPTY);
    	boolean e1e2 = feature2.isLegal(feature2.board, 0, 4, 1, 4);
    	boolean e1d2 = feature2.isLegal(feature2.board, 0, 4, 1, 3);
    	boolean e1e3 = feature2.isLegal(feature2.board, 0, 4, 2, 4);
    	boolean e1d1 = feature2.isLegal(feature2.board, 0, 4, 0, 3);
    	boolean e1c3 = feature2.isLegal(feature2.board, 0, 4, 2, 2);
    	
    	boolean kingSide = feature2.isLegal(feature2.board, 0, 4, 0, 6);
    	boolean queenSide = feature2.isLegal(feature2.board, 0, 4, 0, 2);
    	
    	feature2.setPiece(1, 3, Color.BLACK, PieceType.ROOK);
    	feature2.setPiece(1, 5, Color.BLACK, PieceType.ROOK);
    	boolean e1d1Check = feature2.isLegal(feature2.board, 0, 4, 0, 3);
    	boolean e1e2Check = feature2.isLegal(feature2.board, 0, 4, 1, 4);
    	
    	boolean kingSideCheck = feature2.isLegal(feature2.board, 0, 4, 0, 6);
    	boolean queenSideCheck = feature2.isLegal(feature2.board, 0, 4, 0, 2);
    	
    	//Then
    	assertTrue(e1e2);
    	assertTrue(e1d2);
    	assertTrue(e1d1);
    	assertFalse(e1c3);
    	assertFalse(e1e3);
    	
    	assertTrue(kingSide);
    	assertTrue(queenSide);
    	
    	assertFalse(e1d1Check);
    	assertFalse(e1e2Check);
    	
    	assertFalse(kingSideCheck);
    	assertFalse(queenSideCheck);
    }
}