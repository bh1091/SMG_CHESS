package org.yuanjia.hw3;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GameImages extends ClientBundle {
	
	@Source("empty.png")
	ImageResource empty();

	@Source("Red B.png")
	ImageResource blackBishop();

	@Source("Red K.png")
	ImageResource blackKing();

	@Source("Red N.png")
	ImageResource blackKnight();

	@Source("Red Q.png")
	ImageResource blackQueen();

	@Source("Red P.png")
	ImageResource blackPawn();

	@Source("Red R.png")
	ImageResource blackRook();

	@Source("Red2 B.png")
	ImageResource whiteBishop();

	@Source("Red2 K.png")
	ImageResource whiteKing();

	@Source("Red2 N.png")
	ImageResource whiteKnight();

	@Source("Red2 Q.png")
	ImageResource whiteQueen();

	@Source("Red2 P.png")
	ImageResource whitePawn();

	@Source("Red2 R.png")
	ImageResource whiteRook();


}