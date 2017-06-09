# perceval-controller
Application permettant de controller un robot en bluetooth, édité dans un cadre scolaire (projet de conception de robot devant réaliser un parcours).

Ce programme utilise la librairie [BlueCove](http://www.bluecove.org/) pour permettre l'établissement d'une connexion bluetooth.

Deux modes de fonctionnements sont pour le moment contenus dans le code source :
* Fonctionnement en fonction de la simulation de la position du robot (différents points pouvant être utilisés comme directions sont présents dans le dossier **data/**. *(la mise en place de ce mode nécessite la modification du code)*
* Fonctionnement *save and restore* : les commandes sont envoyées et enregistrées lors d'un premier parcours puis restaurées dans un second temps, celles-ci sont contenues dans le fichier **data.txt**. *(c'est le mode actuellement utilisé)
