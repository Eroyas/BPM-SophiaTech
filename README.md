# BPM : Sophia Tech Forum

Ce projet est une implémentation du notre proposition sous forme de Business Process de l'organisation du Sophia Tech Forum.
Un rapport sur ce Business Process est disponible [ici](rapport.pdf).

Ce projet ne correspond pas à une implémentation totale du Business Process ni de tout ses indicateurs de performance. Les tâches systèmes sont implémentés sous forme de bouchons. 
En effet, le but de ce projet est de fournir une implémentation à gros grains du Business Process parcourant toutes les tâches.
L'implémentation est découpée en sous process indépendants et leurs intéractions s'effectuent par le biais d'évènements.

## Points positifs
Ce projet permet d'exécuter notre proposition du Business Process du Sophia Tech Forum. Il est également possible d'automatiser totalement son exécution pour récuperer des metrics et ainsi potentiellement améliorer ce Business Process.

## Points négatifs
Ce projet ne dispose pas d'implémentations poussés au niveau des tâches systèmes. 
L'interaction avec les utilisateurs s'effectue seulement par le biais d'une console et est donc peu utilisable. 

## Dépendences
Ce projet nécessite Java 8 et Docker

## Construction
```./build.sh``` permet de construire le projet

## Démarrage
```./start.sh``` permet de lancer le projet


## Auteurs
Ce projet a été réalisé par Maxime Carlier, Yasin Eroglu, Khadim Gning et Günther Jungbluth
