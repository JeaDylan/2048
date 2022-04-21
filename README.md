# 2048
Projet pour l'UE "Programmation Orientée Objet", concevoir un jeu 2048 avec une interface graphique, et quelques extensions au choix, en respectant le modèle MVC.
Nos objets "Cases" se déplacent dans la grille, et ce ne sont pas juste la valeur de ces dernières qui est modifiée, pour insister sur l'objet.

Parmi les extensions proposées : 
  Mise en place d'un timer grâce à un thread qui affiche le temps de jeu en temps réel
  Sauvegarde du meilleur temps pour atteindre 2048 (via un fichier)
  Sauvegarde de la meilleure case atteinte (via un fichier)
  Mise en place d'un menu de navigation qui nous permet de modifier quelques paramètres
  Possibilité de redémarrer la partie en appuyant sur R
  Possibilité de remettre les records à 0 en appuyant sur B
  Possibilité de se débloquer (un certain nombre de fois, à modifier dans les paramètres du menu), en glissant une case à la place d'une autre, ce qui va       intervertir les deux cases
  Coloration des cases afin de rendre le jeu plus 'joli'
  
On déplace les cases avec les flèches directionnelles, et on utilise la souris pour intervertir deux cases et se débloquer.
