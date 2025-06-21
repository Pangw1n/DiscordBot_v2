package feature3;

import java.util.ArrayList;

import org.jointheleague.api_wrapper.ReceivedMessage;
import org.jointheleague.features.abstract_classes.Feature;
import org.jointheleague.features.examples.third_features.plain_old_java_objects.cat_facts_api.CatWrapper;
import org.jointheleague.features.help_embed.plain_old_java_objects.help_embed.HelpEmbed;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;;

public class Feature3 extends Feature
{
    public final String COMMAND = "!hangman";

    private WebClient webClient;
    private static final String baseUrl = "https://random-word-api.vercel.app/api";
    
    private boolean playing;
    private String word;
    private String guessed;
    private ArrayList<Character> incorrect;
    private int lives;
    private int hints;
    
	public Feature3(String channelName) {
        super(channelName);
        helpEmbed = new HelpEmbed(COMMAND, "!hangman [easy/normal/hard] to start a new game. \nLeave the field blank for normal difficulty. \n!hangman [letter] to make a guess. \n!hangman hint to get a hint. ");
        
        webClient = WebClient.builder().baseUrl(baseUrl).build();
	}

	@Override
	public void handle(ReceivedMessage event) {
        String messageContent = event.getMessageContent();
        
        if (messageContent.equalsIgnoreCase(COMMAND))
        {
			startGame(6, 1);
			event.sendResponse("``lives: " + lives + " hints: " + hints + "\n" + guessed + "\nIncorrect guesses: ``");
        }
		if (messageContent.trim().indexOf(COMMAND) == 0)
		{
			String[] command = messageContent.trim().split(" ");
			if (command[0].equalsIgnoreCase(COMMAND))
			{
				if (playing && command.length == 2 && command[1].length() == 1)
				{
					char guess = command[1].toLowerCase().charAt(0);
					if (guessed.contains("" + guess) || incorrect.contains(guess))
					{
						event.sendResponse("``You already guessed that letter``");
						return;
					}
					boolean correct = guess(guess);
					
					if (guessed.equalsIgnoreCase(word))
					{
						event.sendResponse("``You guessed the word! \nThe word was " + word + "``");
						playing = false;
					}
					else if (lives <= 0)
					{
						event.sendResponse("``You ran out of lives! \nThe word was " + word + "``");
						playing = false;
					}
					else
					{
						String response = "``lives: " + lives + " hints: " + hints + "\n" + guessed + "\nIncorrect guesses: ";
						
						for (int i = 0; i < incorrect.size(); i++)
						{
							response += incorrect.get(i);
							if (i < incorrect.size() - 1)
							{
								response += ", ";
							}
						}
						response += "``";
					
						event.sendResponse(response);
					}
				}
				else if (playing && command.length == 2 && command[1].equalsIgnoreCase("hint"))
				{
					if (hints > 0)
					{
						hint();
						hints--;
						
						String response = "``lives: " + lives + " hints: " + hints + "\n" + guessed + "\nIncorrect guesses: ";
						
						for (int i = 0; i < incorrect.size(); i++)
						{
							response += incorrect.get(i);
							if (i < incorrect.size() - 1)
							{
								response += ", ";
							}
						}
						response += "``";
					
						event.sendResponse(response);
					}
					else
					{
						event.sendResponse("``No hints remaining``");
					}
				}
				else if (command.length == 2 && command[1].length() != 1)
				{
					if (command[1].equalsIgnoreCase("easy"))
					{
						startGame(8, 3);
						event.sendResponse("``lives: " + lives + " hints: " + hints + "\n" + guessed + "\nIncorrect guesses: ``");
					}
					else if (command[1].equalsIgnoreCase("normal"))
					{
						startGame(6, 1);
						event.sendResponse("``lives: " + lives + " hints: " + hints + "\n" + guessed + "\nIncorrect guesses: ``");
					}
					else if (command[1].equalsIgnoreCase("hard"))
					{
						startGame(4, 0);
						event.sendResponse("``lives: " + lives + " hints: " + hints + "\n" + guessed + "\nIncorrect guesses: ``");
					}
				}
			}
		}
	}
	
	public void startGame(int l, int h)
	{
		word = getWord().toLowerCase();
		lives = l;
		hints = h;
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
		if (!correct)
		{
			incorrect.add(guess);
			lives -= 1;
		}
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
	
	public void hint()
	{
		ArrayList<Character> letters = new ArrayList<Character>();
		for (int i = 0; i < word.length(); i++)
		{
			if (guessed.charAt(i) != word.charAt(i) && !letters.contains(word.charAt(i)))
			{
				letters.add(word.charAt(i));
			}
		}
		char character = word.charAt((int)(Math.random() * letters.size()));
		StringBuilder builder = new StringBuilder(guessed);
		for (int i = 0; i < word.length(); i++)
		{
			if (character == word.charAt(i))
			{
				builder.setCharAt(i, character);
			}
		}
		guessed = builder.toString();
	}
}
