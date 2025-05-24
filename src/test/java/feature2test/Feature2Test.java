package feature2test;

import org.jointheleague.api_wrapper.ReceivedMessage;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import feature2.Feature2;

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
            assertNotEquals("!count", command);
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
}