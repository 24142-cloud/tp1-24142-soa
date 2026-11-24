TP1 SOAP et WSDL - Partie A (Code-first)

1. Ouvrir ce dossier dans IntelliJ IDEA comme projet Maven.
2. Vérifier que le Project SDK et le Maven runner utilisent JDK 17.
3. Attendre la fin du téléchargement des dépendances Maven.
4. Lancer dans le terminal IntelliJ :
   mvn compile exec:java
5. Ouvrir dans le navigateur :
   http://localhost:8080/bib/EmpruntService?wsdl

Le dossier xml est réservé aux fichiers XML créés aux étapes suivantes du TP.
La méthode rendreLivre retourne temporairement BigDecimal.ZERO, comme dans l'énoncé ;
elle sera modifiée à l'étape 4.
