package games.difference;

import java.awt.Point;

public class DifferencePicture {
	private String correctPic;
	private String wrongPic;
	private Point errorCoordinates;
	public DifferencePicture(String correctPic, String wrongPic, Point errorCoordinates){
		this.setCorrectPic(correctPic);
		this.setWrongPic(wrongPic);
		this.setErrorCoordinates(errorCoordinates);
	}
	public Point getErrorCoordinates() {
		return errorCoordinates;
	}
	public void setErrorCoordinates(Point errorCoordinates) {
		this.errorCoordinates = errorCoordinates;
	}
	public String getCorrectPic() {
		return correctPic;
	}
	public void setCorrectPic(String correctPic) {
		this.correctPic = correctPic;
	}
	public String getWrongPic() {
		return wrongPic;
	}
	public void setWrongPic(String wrongPic) {
		this.wrongPic = wrongPic;
	}
}
