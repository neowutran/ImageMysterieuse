================================================================================
IUT de Nice / Département informatique / Module APO-Java / 2012-2013
================================================================================

ImageMysterieuse IHM

Auteur : Martini Didier
 / Jonathan roux

Groupe : S3T G1
Readme : Version : 1.0.0 (Dernière révision 21/12/2012 19:15)


================================================================================

1. Instructions pour l'installation
================================================================================

Deux launcher sont présents pour l’exécution du .jar, un .bat (Windows) et un .sh (Linux)
Le lancement du .jar générera un dossier 'ImageMysterieuse' contenant les éléments 
nécessaires (fichiers de configuration + image) 
Le fichier de configuration est en xml. Cela rend possible la modification du fichier
de configuration avec seulement un éditeur de texte


================================================================================

2. Fonctionnalités
================================================================================

Liste non exhaustive :

- Afficher des ensembles d'images

- Decouper une image en plusieurs images plus petites
- Affichage des informations (score, pseudo, theme, temps restant(tempsG))

- Evenements audio (lecture de son, musique d'ambiance, musique de victoire)

- Decouvrir une image lors de la découverte du mot
	Deux mots sont utilisés pour la découverte complète d'une image 
	(Attention a respecter la casse) :
		- sephiroth
		- Menton


================================================================================

3. Problèmes connus
================================================================================

- Programme très lent
 (Chargement lent, parfois plus de 5 ou 6 secondes)

================================================================================

4. Idées d’améliorations possibles
================================================================================
 
- Régler les bugs d'affichages
	Assez nombreux, ceux-ci posent parfois certain problème pour afficher
	le temps dans la progressBar de TempsG
 

================================================================================

5. Notes et informations
================================================================================

- Utilisation de XML et donc de libraire pour le faire, nous avons choisi : jdom : http://www.jdom.org/, Base64Coder

- Utilisation de librairie audio : 
http://www.javazoom.net/vorbisspi/vorbisspi.html

- Les fichiers de configuration sont en XML

