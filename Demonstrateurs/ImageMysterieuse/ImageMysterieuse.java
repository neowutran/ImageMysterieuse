package demonstrateurs;

import java.io.File;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import config.GenerateFile;
import config.GenerateXML;
import controllers.ImageMysterieuseController;

/**
 * Cette classe est le point d'entrer de imageMysterieuse, elle generera le
 * dossier de configuration et son contenu, puis creera le controller
 * 
 * @author Martini Didier
 * @version 3.0
 */

public class ImageMysterieuse {

	private static Element						config;
	private final ImageMysterieuseController	controller;
	public static final String					FOLDER					= "imageMysterieuse"
																						+ File.separator;
	public static final String					CONFIG					= "config_1.0.0.xml";
	public static final String					FOLDER_IMG				= "images"
																						+ File.separator;
	public static final String					FOLDER_GAME				= "game"
																						+ File.separator;
	public static final String					BLUE_CIRCLE				= "blue.png";
	public static final String					RED_CIRCLE				= "red.png";
	public static final String					GREEN_CIRCLE			= "green.png";
	public static final String					ORANGE_CIRCLE			= "orange.png";
	public static final String					YELLOW_CIRCLE			= "yellow.png";
	public static final String					GREY_CIRCLE				= "grey.png";
	public static final String					VICTORY					= "victory.jpg";
	public static final String                  EXEMPLE1                = "a.jpg";
	public static final String                  EXEMPLE2                = "aa.jpg";
	public static final String                  EXEMPLE3                = "aaa.jpg";
	public static final String                  EXEMPLE4                = "aaaa.jpg";
	public static final String					FOLDER_SOUND			= "sound"
																						+ File.separator;
	public static final String					SOUND_VICTORY			= "victory.ogg";
	public static final String					SOUND_BIP				= "bip.ogg";
	public static final String					SOUND_GONG				= "gong.ogg";
	public static final String					SOUND_FOND				= "fond.ogg";

	private static final String					CONFIG_LOGGER_CONFIG	= "config";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(final String[] args) {

		String pathConfig = null;

		if (args.length == 1) {

			pathConfig = args[0];

		}

		new ImageMysterieuse(pathConfig);

	}

	/**
	 * @param path
	 * @throws IOException 
	 */
	public ImageMysterieuse(final String path) {

		if (!(new File(ImageMysterieuse.FOLDER)).isDirectory()) {

			this.createConfigFolder();

		}

		if (!new File(ImageMysterieuse.FOLDER + ImageMysterieuse.CONFIG)
						.exists()) {

			final GenerateXML generer = new GenerateXML();
			generer.setPathFile(ImageMysterieuse.FOLDER
							+ ImageMysterieuse.CONFIG);
			generer.genererXML();

		}

		this.genererImage(ImageMysterieuse.BLUE_CIRCLE);
		this.genererImage(ImageMysterieuse.GREEN_CIRCLE);
		this.genererImage(ImageMysterieuse.ORANGE_CIRCLE);
		this.genererImage(ImageMysterieuse.RED_CIRCLE);
		this.genererImage(ImageMysterieuse.YELLOW_CIRCLE);
		this.genererImage(ImageMysterieuse.GREY_CIRCLE);
		this.genererImage(ImageMysterieuse.VICTORY);
		this.genererImage(ImageMysterieuse.EXEMPLE1);
		this.genererImage(ImageMysterieuse.EXEMPLE2);
		this.genererImage(ImageMysterieuse.EXEMPLE3);
		this.genererImage(ImageMysterieuse.EXEMPLE4);
		//copier les images aussi a la racine du dossier src, puis cree le jar et envoie moi ok
		this.genererSon(ImageMysterieuse.SOUND_BIP);
		this.genererSon(ImageMysterieuse.SOUND_VICTORY);
		this.genererSon(ImageMysterieuse.SOUND_FOND);
		this.genererSon(ImageMysterieuse.SOUND_GONG);

		String pathConfig;
		if ((path == null) || (!(new File(path)).exists())) {

			pathConfig = ImageMysterieuse.FOLDER + ImageMysterieuse.CONFIG;

		} else {

			pathConfig = path;

		}

		this.chargerFichierConf(pathConfig);

		if (ImageMysterieuse.config == null) {

			pathConfig = ImageMysterieuse.FOLDER + ImageMysterieuse.CONFIG;
			this.chargerFichierConf(pathConfig);

		}

		this.controller = new ImageMysterieuseController(
						ImageMysterieuse.config, pathConfig);
	}

	private void chargerFichierConf(final String xmlPath) {

		final SAXBuilder sxb = new SAXBuilder();

		try {

			ImageMysterieuse.config = sxb.build(new File(xmlPath))
							.getRootElement();

		} catch (final Exception e) {

			System.out.println(e.getMessage());

		}

	}

	private void createConfigFolder() {

		(new File(ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_IMG))
						.mkdirs();
		(new File(ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_SOUND))
						.mkdirs();
		(new File(ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_GAME))
						.mkdirs();

	}

	/**
	 * @param image
	 */
	public void genererImage(final String image) {

		if (!new File(ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_IMG
						+ image).exists()) {

			final GenerateFile generer = new GenerateFile();
			generer.copyfile(ImageMysterieuse.FOLDER
							+ ImageMysterieuse.FOLDER_IMG + image,
							this.getClass().getClassLoader()
											.getResourceAsStream(image));

		}

	}

	/**
	 * @param image
	 */
	public void genererSon(final String son) {

		if (!new File(ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_SOUND
						+ son).exists()) {

			final GenerateFile generer = new GenerateFile();
			generer.copyfile(ImageMysterieuse.FOLDER
							+ ImageMysterieuse.FOLDER_SOUND + son,
							this.getClass().getClassLoader()
											.getResourceAsStream(son));

		}

	}

	/**
	 * @return the controller
	 */
	public ImageMysterieuseController getController() {

		return this.controller;
	}

}
