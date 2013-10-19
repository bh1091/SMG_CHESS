package org.alexanderoskotsky.hw3;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GameImages extends ClientBundle {
	@Source("Black P.png")
	ImageResource blackPawn();

	@Source("Black N.png")
	ImageResource blackKnight();

	@Source("Black K.png")
	ImageResource blackKing();

	@Source("Black R.png")
	ImageResource blackRook();

	@Source("Black Q.png")
	ImageResource blackQueen();

	@Source("Black B.png")
	ImageResource blackBishop();

	@Source("White P.png")
	ImageResource whitePawn();

	@Source("White N.png")
	ImageResource whiteKnight();

	@Source("White K.png")
	ImageResource whiteKing();

	@Source("White R.png")
	ImageResource whiteRook();

	@Source("White Q.png")
	ImageResource whiteQueen();

	@Source("White B.png")
	ImageResource whiteBishop();
	
	@Source("spinner.gif")
	ImageResource spinner();
	
}
