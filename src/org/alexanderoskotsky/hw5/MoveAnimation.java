package org.alexanderoskotsky.hw5;

import org.shared.chess.Move;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

public class MoveAnimation extends Animation {
	private Move move;
	private Image img;
	private Callback callback;
	
	public MoveAnimation(Image[][] board, Move move, Callback callback) {
		this.move = move;
		this.callback = callback;
		
		img = board[7 - move.getFrom().getRow()][move.getFrom().getCol()];
	}
	
	@Override
	protected void onUpdate(double progress) {
		int dx = move.getFrom().getRow() - move.getTo().getRow();
		int dy = move.getFrom().getCol() - move.getTo().getCol();
		
		img.getElement().getStyle().setRight(progress * dy * 52, Unit.PX);
		img.getElement().getStyle().setTop(progress * dx * 52, Unit.PX);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		img.getElement().getStyle().setPosition(Position.RELATIVE);
	}
	
	protected void onComplete() {
		super.onComplete();
		
		callback.execute();
	}

}
