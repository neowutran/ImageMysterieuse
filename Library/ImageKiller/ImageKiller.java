package library;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

/**
 * @author sephiroth
 */
public class ImageKiller {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {

		final ImageKiller im = new ImageKiller(
						"/home/sephiroth/Images/background/p16dagii4kpn01bmveq7nens9se.jpg",
						"/home/sephiroth/Images/decoupe/", "jpg");

		im.configuration(2, 5);
		im.decoupe();
		im.save();
	}

	private final String				outputFolder;
	private final String				inputFile;
	private BufferedImage				inputBuffer;
	private final int					widthInput;
	private final int					heightInput;
	private int							widthOutput;
	private int							heightOutput;
	private int							nbSubImage;

	private int							nbColonne;
	private int							nbLigne;
	private LinkedList<BufferedImage>	outputBuffer;

	private final String				fileType;

	/**
	 * @param file
	 * @param outputFolder
	 * @param fileType
	 * @throws IOException
	 */
	public ImageKiller(final String file, final String outputFolder,
					final String fileType) throws IOException {

		this.outputFolder = outputFolder;
		this.inputFile = file;
		final File img = new File(this.inputFile);
		try {
			this.inputBuffer = ImageIO.read(img);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.heightInput = this.inputBuffer.getHeight();
		this.widthInput = this.inputBuffer.getWidth();
		this.fileType = fileType;

	}

	/**
	 * @param nbColonne
	 * @param nbLigne
	 */
	public void configuration(final int nbColonne, final int nbLigne) {

		this.heightOutput = this.heightInput / nbLigne;
		this.widthOutput = this.widthInput / nbColonne;
		this.setNbSubImage(nbColonne * nbLigne);
		this.nbColonne = nbColonne;
		this.nbLigne = nbLigne;

	}

	/**
*
*/
	public void decoupe() {

		this.outputBuffer = new LinkedList<BufferedImage>();

		int i, j;
		for (i = 0; i < this.nbLigne; i++) {

			for (j = 0; j < this.nbColonne; j++) {

				this.outputBuffer.add(this.inputBuffer.getSubimage(
								this.widthOutput * j, this.heightOutput * i,
								this.widthOutput, this.heightOutput));

			}

		}

	}

	/**
	 * @return String
	 */
	public String getFileType() {

		return this.fileType;
	}

	/**
	 * @return int
	 */
	public int getHeightInput() {

		return this.heightInput;
	}

	/**
	 * @return int
	 */
	public int getHeightOutput() {

		return this.heightOutput;
	}

	/**
	 * @return BufferedImage
	 */
	public BufferedImage getInputBuffer() {

		return this.inputBuffer;
	}

	/**
	 * @return String
	 */
	public String getInputFile() {

		return this.inputFile;
	}

	/**
	 * @return int
	 */
	public int getNbColonne() {

		return this.nbColonne;
	}

	/**
	 * @return int
	 */
	public int getNbLigne() {

		return this.nbLigne;
	}

	/**
	 * @return the nbSubImage
	 */
	public int getNbSubImage() {

		return this.nbSubImage;
	}

	/**
	 * @return LinkedList<BufferedImage>
	 */
	public LinkedList<BufferedImage> getOutputBuffer() {

		return this.outputBuffer;
	}

	/**
	 * @return String
	 */
	public String getOutputFolder() {

		return this.outputFolder;
	}

	/**
	 * @return int
	 */
	public int getWidthInput() {

		return this.widthInput;
	}

	/**
	 * @return int
	 */
	public int getWidthOutput() {

		return this.widthOutput;
	}

	/**
*
*/
	public void save() {

		int compteur = 0;
		for (final BufferedImage subImage : this.outputBuffer) {

			compteur++;
			final File outputfile = new File(this.outputFolder + compteur + "."
							+ this.fileType);
			try {
				ImageIO.write(subImage, this.fileType, outputfile);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * @param nbSubImage
	 *            the nbSubImage to set
	 */
	public void setNbSubImage(final int nbSubImage) {

		this.nbSubImage = nbSubImage;
	}
}