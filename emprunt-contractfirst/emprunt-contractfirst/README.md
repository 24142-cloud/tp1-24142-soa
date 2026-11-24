# emprunt-contractfirst — Partie B

## Idée
Dans cette version Contract-first, le contrat WSDL est écrit en premier.
Maven / `wsimport` génère ensuite les classes Java correspondantes.

## Ouvrir dans IntelliJ IDEA
1. Décompresser le ZIP dans le même dossier parent que `emprunt-codefirst`.
2. Dans IntelliJ, ouvrir le dossier `emprunt-contractfirst` comme projet Maven.
3. Vérifier que le Project SDK est Java 17.
4. Recharger le projet Maven.

## Générer les classes Java depuis le WSDL
Exécuter dans le terminal, depuis le dossier qui contient `pom.xml` :

```powershell
mvn clean generate-sources
```

Les classes générées apparaissent sous :

`target/generated-sources/wsimport/mr/supnum/bib/contrat/`

Ne modifiez pas les fichiers générés manوياً.

## Compiler et démarrer
Arrêter d'abord le serveur du projet `emprunt-codefirst` avec Ctrl+C, car les deux projets utilisent le port 8080.

```powershell
mvn clean compile exec:java
```

Puis ouvrir :

`http://localhost:8080/bib/EmpruntService?wsdl`

## Tests SOAP
- Livre disponible : `978-2-10-074444-5`
- Livre indisponible : `978-2-10-074444-4`
- Identifiant adhérent de test : `A001`

Le livre indisponible doit produire le Fault métier `LivreIndisponible`.

## Structure
- `src/main/resources/wsdl/EmpruntService.wsdl` : contrat écrit manuellement.
- `src/main/java/mr/supnum/bib/impl/EmpruntServiceImpl.java` : implémentation.
- `src/main/java/mr/supnum/bib/impl/Serveur.java` : publication du endpoint.
- `target/generated-sources/...` : classes Java générées par wsimport.
