# perceval-controller
Application permettant de controller un robot en bluetooth, édité dans un cadre scolaire (projet de conception de robot devant réaliser un parcours).

Ce programme utilise la librairie [BlueCove](http://www.bluecove.org/) pour permettre l'établissement d'une connexion bluetooth. Le programme [Arduino](https://www.arduino.cc/) contenu dans le dossier **bluetooth_arduino** doit être installé sur une carte Arduino pour fonctionner. Celui ci a été testé avec [ce module bluetooth](http://www.makeblock.com/me-bluetooth-module-dual-mode/) uniquement.

Le programme donne le choix parmi les appareils bluetooth environnant, puis envoie des données de contrôle par une connexion série bluetooth.

Les données envoyées, codées sur 3 octets correspondent à :
* Sens du moteur droit *(0 ou 1)*
* Sens du moteur gauche *(0 ou 1)*
* Vitesse des deux moteurs *(de 0 à 127)*

Deux modes de fonctionnements sont, pour le moment, contenus dans le code source :
* Fonctionnement *simulé* : la position du robot est simulée en fonction de sa vitesse, différents points pouvant être utilisés comme directions sont présents dans le dossier **data/**. Le programme détermine le point suivant à rejoindre à chaque étape. *(la mise en place de ce mode nécessite la modification du code)*
* Fonctionnement *save and restore* : les commandes sont envoyées et enregistrées lors d'un premier parcours puis restaurées dans un second temps, celles-ci sont contenues dans le fichier **data.txt**. *(c'est le mode actuellement utilisé)*
