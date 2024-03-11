#!/bin/sh

#PORT D'ECOUTE DE L'API
export LISTEN_PORT="3001"

#CLEF de CHIFFRAGE POUR LES TOKENS JWT
export JWT_KEY="7PLTpbz5CRH6WK2m4iphkCY6my5xu1fF"

#URL DE CONNECTION JDBC VERS MYSQL
export DS_URL="jdbc:mysql://localhost:3306/Chatop"

#AUTHENTIFICATION CONNECTION BDD
#LOGIN
export DS_CRED_LI="OC"
#PASSWORD
export DS_CRED_PW="OCProjectsUser2024"

#Chemin racine du dépo des images
export PIC_ROOT_DIR="file:/opt/files/oc2024"
