package org.peigenyou.hw8;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en")
public interface ChessMessages extends Constants {
        @DefaultStringValue("Auto-Match")
        String AutoMatchButton();
        @DefaultStringValue("Match With Him!")
        String MatchWithButton();
        @DefaultStringValue("Load Match!")
        String LoadMatchButton();
        @DefaultStringValue("Delete Match!")
        String DelMatchButton();
        
        
        @DefaultStringValue("Error: Fail to connect server!")
        String connectionError();
        @DefaultStringValue("Error: Fail to refresh content!")
        String updateError();
        @DefaultStringValue("Error: Fail to delete match!")
        String delError();
        @DefaultStringValue("Error: Fail to match with him!")
        String createError();
        @DefaultStringValue("Error: Fail to load Match!")
        String loadMatchError();
        @DefaultStringValue("Error: Fail to Fail to auto-Match!")
        String autoMatchError();
        @DefaultStringValue("Error: Fail to submit move!")
        String submitMoveError();
        

        @DefaultStringValue("There are no waiting players. You are added to the waiting queue!")
        String noOpponentInformation();
        @DefaultStringValue("You don't have any saved matches!")
        String noMatchInformation();

        @DefaultStringValue("Black")
        String black();
        @DefaultStringValue("White")
        String white();

        @DefaultStringValue("Draw! Fifty move rule.")
        String fiftyMoveRule();
        @DefaultStringValue("Draw! Stalemate.")
        String stalemate();
        @DefaultStringValue("Draw! Threefold repetition rule.")
        String threefoldRepRule();
        @DefaultStringValue("Checkmate.")
        String checkMate();
        @DefaultStringValue("White player win!")
        String whiteWin();
        @DefaultStringValue("Black player win!")
        String blackWin();
        

        @DefaultStringValue("Your Turn!")
        String playerTurn();
        @DefaultStringValue("Wait for your opponent's move!")
        String opponentTurn();
        
        @DefaultStringValue("Delete success!")
        String sucDel();
        @DefaultStringValue("Channel has closed!")
        String channelClose();
        @DefaultStringValue("Auto Match Found!")
        String autoSuccess();
        
        @DefaultStringValue("Please select one kind of piece to promote the pawn!")
        String promoteInfo();
        
        @DefaultStringValue("Playing with: ")
        String playWith();
        @DefaultStringValue("Match ID: ")
        String MatchID();
        @DefaultStringValue("Username: ")
        String userName();
        @DefaultStringValue("Email: ")
        String email();
        @DefaultStringValue("Rank: ")
        String rank();
        @DefaultStringValue("Year: ")
        String year();
        @DefaultStringValue("Month: ")
        String month();
        @DefaultStringValue("Day: ")
        String day();
        @DefaultStringValue("Hour: ")
        String hour();
        @DefaultStringValue("Minitue: ")
        String minitue();
        @DefaultStringValue("Play vs AI")
        String playwithai();
        
}