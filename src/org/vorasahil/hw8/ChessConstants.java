package org.vorasahil.hw8;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en")
public interface ChessConstants extends Constants {
	@DefaultStringValue("Error: Could not connect to server.")
	String connectionError();
	@DefaultStringValue("Current Match:")
	String currentMatch();
	@DefaultStringValue("Logged in as:")
	String loggedInAs();
	@DefaultStringValue("Rating:")
	String rank();
	@DefaultStringValue("Challenge!")
	String challenge();
	@DefaultStringValue("Email ID is invalid!")
	String invalidEmailID();
	@DefaultStringValue("Challenge published, make your move!")
	String challengePosted();
	@DefaultStringValue("Delete Game")
	String deleteGame();
	@DefaultStringValue("Delete Failed")
	String deleteFailed();
	@DefaultStringValue("Delete Successful")
	String deleteSuccessful();
	@DefaultStringValue("Reload Game-List")
	String reloadGameList();
	@DefaultStringValue("Reloaded")
	String reloaded();
	@DefaultStringValue("Load Game")
	String loadGame();
	@DefaultStringValue("Automatch")
	String autoMatch();
	@DefaultStringValue("Finding an opponent, please be patient")
	String findingOpponent();
	@DefaultStringValue("Automatch complete, begin game")
	String autoMatchComplete();
	@DefaultStringValue("Welcome, ")
	String welcome();
	@DefaultStringValue("added to the list of games.")
	String addedToList();
	@DefaultStringValue("Match:")
	String match();
	@DefaultStringValue("changed, check it out")
	String changed();
	@DefaultStringValue("error!")
	String error();
	@DefaultStringValue("channelClosed!")
	String channelClosed();
	@DefaultStringValue("It is your turn.")
	String yourTurn();
	@DefaultStringValue("You are playing with White")
	String playingWhite();
	@DefaultStringValue("You are playing with Black")
	String playingBlack();
	@DefaultStringValue("Started on:")
	String startedOn();
	
	@DefaultStringValue("Match Drawn(FIFTY MOVE RULE)")
	String fiftyMove();
	@DefaultStringValue("Match Drawn(Stalemate)")
	String stalemate();
	@DefaultStringValue("Match Drawn(Threefold Repition Rule)")
	String threefold();
	@DefaultStringValue("You won")
	String youWon();
	@DefaultStringValue("You lost")
	String youLost();
	@DefaultStringValue("Game Over")
	String gameOver();
	@DefaultStringValue("Current Turn:White")
	String white();
	@DefaultStringValue("Current Turn:Black")
	String black();
	@DefaultStringValue("Updated")
	String updated();
	@DefaultStringValue("New Single player game as White")
	String singlePlayerWhite();
	@DefaultStringValue("New Single Player game as Black")
	String singlePlayerBlack();
	
}
