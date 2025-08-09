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
			event.sendResponse(getResponse());
        }
		if (messageContent.trim().indexOf(COMMAND) == 0)
		{
			String[] command = messageContent.trim().split(" ");
			if (playing && command.length == 2 && command[1].length() == 1)
			{
				char guess = command[1].toLowerCase().charAt(0);
				if (getGuessed().contains("" + guess) || getIncorrect().contains(guess))
				{
					event.sendResponse("``You already guessed that letter``");
					return;
				}
				guess(guess);
				
				if (getGuessed().equalsIgnoreCase(word))
				{
					event.sendResponse("``You guessed the word! \nThe word was " + word + "``");
					playing = false;
				}
				else if (getLives() <= 0)
				{
					event.sendResponse("``You ran out of lives! \nThe word was " + word + "``");
					playing = false;
				}
				else
				{
					event.sendResponse(getResponse());
				}
			}
			else if (playing && command.length == 2 && command[1].equalsIgnoreCase("hint"))
			{
				if (getHints() > 0)
				{
					hint();
					hints = getHints() - 1;
				
					event.sendResponse(getResponse());
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
				}
				else if (command[1].equalsIgnoreCase("normal"))
				{
					startGame(6, 1);
				}
				else if (command[1].equalsIgnoreCase("hard"))
				{
					startGame(4, 0);
				}
				else
				{
					return;
				}
				event.sendResponse(getResponse());
			}
		}
	}
	
	public String getResponse()
	{
		String response = "``lives: " + getLives() + " hints: " + getHints() + "\n" + getGuessed() + "\nIncorrect guesses: ";
		
		for (int i = 0; i < getIncorrect().size(); i++)
		{
			response += getIncorrect().get(i);
			if (i < getIncorrect().size() - 1)
			{
				response += ", ";
			}
		}
		response += "``";
		
		return response;
	}
	
	public void startGame(int l, int h)
	{
		word = getNewWord().toLowerCase();
		lives = l;
		hints = h;
		guessed = "";
		for (int i = 0; i < word.length(); i++)
		{
			guessed = getGuessed() + "_";
		}
		incorrect = new ArrayList<Character>();
		playing = true;
	}
	
	public boolean guess(char guess)
	{
		boolean correct = false;
		StringBuilder builder = new StringBuilder(getGuessed());
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
			getIncorrect().add(guess);
			lives = getLives() - 1;
		}
		return correct;
	}
	
	public String getNewWord()
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
	
	public char hint()
	{
		ArrayList<Character> letters = new ArrayList<Character>();
		for (int i = 0; i < word.length(); i++)
		{
			if (getGuessed().charAt(i) != word.charAt(i) && !letters.contains(word.charAt(i)))
			{
				letters.add(word.charAt(i));
			}
		}
		char character = letters.get((int)(Math.random() * letters.size()));
		StringBuilder builder = new StringBuilder(getGuessed());
		for (int i = 0; i < word.length(); i++)
		{
			if (character == word.charAt(i))
			{
				builder.setCharAt(i, character);
			}
		}
		guessed = builder.toString();
		return character;
	}
	
	public void setWord(String newWord)
	{
		word = newWord;
	}

	public void setGuessed(String guessed) {
		this.guessed = guessed;
	}

	public int getLives() {
		return lives;
	}

	public int getHints() {
		return hints;
	}

	public String getGuessed() {
		return guessed;
	}

	public ArrayList<Character> getIncorrect() {
		return incorrect;
	}
	
	public String getWord() {
		return word;
	}
}
