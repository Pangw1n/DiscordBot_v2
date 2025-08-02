package feature3test;

import org.jointheleague.api_wrapper.ReceivedMessage;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import feature3.Feature3;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

class Feature3Test {

    private final String testChannelName = "test";
    private final Feature3 feature3 = new Feature3(testChannelName);

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
        String command = feature3.COMMAND;

        //Then

        if(!(feature3 instanceof Feature3)){
            assertNotEquals("!hangman", command);
        }

        assertNotEquals("", command);
        assertNotEquals("!", command);
        assertEquals('!', command.charAt(0));
        assertNotNull(command);
    }

    @Test
    void itShouldHandleMessagesWithCommand() {
        //Given
        HelpEmbed helpEmbed = new HelpEmbed(feature3.COMMAND, "test");
        when(receivedMessage.getMessageContent()).thenReturn(feature3.COMMAND);

        //When
        feature3.handle(receivedMessage);

        //Then
        verify(receivedMessage, times(1)).sendResponse(anyString());
    }

    @Test
    void itShouldNotHandleMessagesWithoutCommand() {
        //Given
        String command = "";
        when(receivedMessage.getMessageContent()).thenReturn(command);

        //When
        feature3.handle(receivedMessage);

        //Then
        verify(receivedMessage, never()).sendResponse("");
    }

    @Test
    void itShouldHaveAHelpEmbed() {
        //Given

        //When
        HelpEmbed actualHelpEmbed = feature3.getHelpEmbed();

        //Then
        assertNotNull(actualHelpEmbed);
    }

    @Test
    void itShouldHaveTheCommandAsTheTitleOfTheHelpEmbed() {
        //Given

        //When
        String helpEmbedTitle = feature3.getHelpEmbed().getTitle();
        String command = feature3.COMMAND;

        //Then
        assertEquals(command, helpEmbedTitle);
    }

    @Test
    void dontHandleWhenNotPlaying() {
        //Given
        when(receivedMessage.getMessageContent()).thenReturn("!hangman a");

        //When
        feature3.handle(receivedMessage);

        //Then
        verify(receivedMessage, never()).sendResponse("");
    }

    @Test
    void dontHandleWhenInputIsNot2Words() {
        //Given
		feature3.startGame(6, 1);
        when(receivedMessage.getMessageContent()).thenReturn("!hangman a a");

        //When
        feature3.handle(receivedMessage);

        //Then
        verify(receivedMessage, never()).sendResponse("");
    }

    @Test
    void dontHandleWhenGuessIsNotAChar() {
        //Given
		feature3.startGame(6, 1);
        when(receivedMessage.getMessageContent()).thenReturn("!hangman aa");

        //When
        feature3.handle(receivedMessage);

        //Then
        verify(receivedMessage, never()).sendResponse("");
    }

    @Test
    void correctGuess() {
        //Given
        when(receivedMessage.getMessageContent()).thenReturn("!hangman e");
    	feature3.startGame(6, 0);
    	feature3.setWord("hello");
    	feature3.setGuessed("_____");

        //When
        feature3.handle(receivedMessage);

        //Then
        String response = "``lives: " + feature3.getLives() + " hints: " + feature3.getHints() + "\n" + feature3.getGuessed() + "\nIncorrect guesses: ``";

        verify(receivedMessage, times(1)).sendResponse(response);
    }

    @Test
    void incorrectGuess() {
        //Given
        when(receivedMessage.getMessageContent()).thenReturn("!hangman a");
    	feature3.startGame(6, 0);
    	feature3.setWord("hello");
    	feature3.setGuessed("_____");

        //When
        feature3.handle(receivedMessage);

        //Then
        String response = "``lives: " + feature3.getLives() + " hints: " + feature3.getHints() + "\n" + feature3.getGuessed() + "\nIncorrect guesses: a``";

        verify(receivedMessage, times(1)).sendResponse(response);
    }

    @Test
    void alreadyGuessed() {
        //Given
        when(receivedMessage.getMessageContent()).thenReturn("!hangman e");
    	feature3.startGame(6, 0);
    	feature3.setWord("hello");
    	feature3.setGuessed("_____");

        //When
    	feature3.guess('e');
        feature3.handle(receivedMessage);
        
        //Given
        when(receivedMessage.getMessageContent()).thenReturn("!hangman a");

        //When
    	feature3.guess('a');
        feature3.handle(receivedMessage);

        //Then
        verify(receivedMessage, times(2)).sendResponse("``You already guessed that letter``");
    }
    
    @Test
    void getWordShouldReturnAString()
    {
    	//Given
    	
    	//When
    	String word = feature3.getNewWord();
    	
    	//Then
    	assertNotEquals(word, null);
    	assertTrue(word.length() > 0);
    }

    @Test
    void testGuess()
    {
    	//Given
    	feature3.startGame(6, 0);
    	feature3.setWord("hello");
    	feature3.setGuessed("_____");
    	
    	//When
    	boolean containsE = feature3.guess('e');
    	boolean containsA = feature3.guess('a');
    	
    	//Then
    	assertTrue(containsE);
    	assertFalse(containsA);
    }
    
    @Test
    void testHint()
    {
    	//Given
    	feature3.startGame(6, 0);
    	feature3.setWord("hello");
    	feature3.setGuessed("_____");
    	
    	//When
    	char character = feature3.hint(); 
    	
    	//Then
    	assertTrue("hello".contains("" + character));
    	
    	
    	
    	//Given
    	feature3.setWord("hello");
    	feature3.setGuessed("h_llo");
    	
    	//When
    	character = feature3.hint();
    	
    	//Then
    	assertEquals('e', character);
    }
}