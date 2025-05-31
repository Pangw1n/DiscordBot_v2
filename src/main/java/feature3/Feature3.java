package feature3;

import java.util.ArrayList;

import org.jointheleague.api_wrapper.ReceivedMessage;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.examples.third_features.plain_old_java_objects.cat_facts_api.CatWrapper;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class Feature3 extends Feature
{
    public final String COMMAND = "!hangman";

    private WebClient webClient;
    private static final String baseUrl = "https://random-word-api.herokuapp.com/word";
    
    private boolean playing;
    private String word;
    private String guessed;
    private ArrayList<Character> incorrect;
    private int lives;
    
	public Feature3(String channelName) {
        super(channelName);
        helpEmbed = new HelpEmbed(COMMAND, "");
        
        webClient = WebClient.builder().baseUrl(baseUrl).build();
	}

	@Override
	public void handle(ReceivedMessage event) {
        String messageContent = event.getMessageContent();
        
		if (messageContent.equals(COMMAND))
		{
			startGame();
			event.sendResponse("``" + guessed + "``");
		}
		else if (messageContent.trim().indexOf(COMMAND) == 0)
		{
			String[] command = messageContent.trim().split(" ");
			if (playing && command.length == 2 && command[1].length() == 1)
			{
				boolean correct = guess(command[1].charAt(0));
				event.sendResponse("``" + guessed + "``");
			}
		}
	}
	
	public void startGame()
	{
		word = getWord();
		lives = 5;
		guessed = "";
		for (int i = 0; i < word.length(); i++)
		{
			guessed += "_";
		}
		incorrect = new ArrayList<Character>();
		playing = true;
	}
	
	public boolean guess(char guess)
	{
		boolean correct = false;
		StringBuilder builder = new StringBuilder(guessed);
		for (int i = 0; i < word.length(); i++)
		{
			if (guess == word.charAt(i))
			{
				builder.setCharAt(i, guess);
				correct = true;
			}
		}
		guessed = builder.toString();
		return correct;
	}
	
	public String getWord()
	{
		//Make the request, accepting the response as a plain old java object you created
        Mono<String> stringMono = webClient.get()
                .retrieve()
                .bodyToMono(String.class);

        //collect the response into a plain old java object
        String randomWord = stringMono.block();

        String message = randomWord.substring(2, randomWord.length() - 2);
        
        //send the message
        return message;
	}
}
