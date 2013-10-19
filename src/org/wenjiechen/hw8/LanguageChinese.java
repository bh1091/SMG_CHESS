package org.wenjiechen.hw8;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en")
public interface LanguageChinese extends Constants {
	
	@DefaultStringValue("Auto Match")
	String matchButton();

	@DefaultStringValue("Logout")
	String logOutLink();

	@DefaultStringValue("Delete Match")
	String deleteMatchButton();

	@DefaultStringValue("Load Match!")
	String LoadMatchButton();

	@DefaultStringValue("Invite")
	String emailMatchButton();

	@DefaultStringValue("Game Star")
	String gameStar();

	@DefaultStringValue("Black's turn")
	String blackTurn();

	@DefaultStringValue("White's turn")
	String whiteTurn();

	@DefaultStringValue("Black is winner")
	String blackWinner();

	@DefaultStringValue("White is winner")
	String whiteWinner();

	@DefaultStringValue("You must first choose a piece kind for promotion")
	String promotionPromt();

	@DefaultStringValue("Error: Cannot log in server!")
	String errorCannotLogInServer();

	@DefaultStringValue("remove user from waiting list")
	String romoveUserFromWaitingList();

	@DefaultStringValue("Error: Cannot invite player via email!")
	String errorCannotInvitePlayerViaEmail();

	@DefaultStringValue("Game is Over. Winner is ")
	String resultWinner();

	@DefaultStringValue(", reason: ")
	String resultResaon();

	@DefaultStringValue(" ")
	String promotionPromtClean();

	@DefaultStringValue("You are ")
	String setPlayerName();

	@DefaultStringValue(", color is ")
	String setPlayerColor();

	@DefaultStringValue(", rank: ")
	String setPlayerRank();

	@DefaultStringValue("Opponent is ")
	String setOppPlayerName();

	@DefaultStringValue(", color is ")
	String setOppPlayerColor();

	@DefaultStringValue(", rank: ")
	String setOppPlayerRank();

	@DefaultStringValue("If not matched, please press automatch button, invite other player via Email or choose match list")
	String ifNotMatched();

	@DefaultStringValue("unset")
	String setPlayerUnset();

	@DefaultStringValue("Error: Cannot fetch match list from server!")
	String errorCannotFetchMatchList();

	@DefaultStringValue("your color: ")
	String showlineColor();

	@DefaultStringValue(", opponent: ")
	String showlineOppNmae();

	@DefaultStringValue(", rank: ")
	String showlineRank();

	@DefaultStringValue(", start date: ")
	String showlinedate();

	@DefaultStringValue("You have an on going game with this player.please finish this round.")
	String youHaveAoOnGoingGame();

	@DefaultStringValue("Match faild, becasue of no other player.\nPlease wait for a moment")
	String matchFaild();
	
	@DefaultStringValue("unset")
	String unset();
}