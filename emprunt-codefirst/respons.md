# TP1 — Services Web SOAP et WSDL
## Compte rendu des étapes et réponses aux questions

---

# Étape 1 — Découverte du service Web et des annotations Java

### Question 1 — Rôle des annotations `@WebService` et `@WebFault`

L'annotation `@WebService` permet de déclarer la classe `EmpruntService` comme un service Web SOAP.

L'annotation `@WebFault`, placée sur `LivreIndisponibleException`, permet de déclarer cette exception comme une erreur dans le contrat WSDL du service.

### Question 2 — Rôle du constructeur vide dans les classes de données

Les classes de données doivent avoir un constructeur vide (sans paramètres) pour permettre à JAXB de créer des instances des objets Java lors de la conversion des messages XML en objets Java (désérialisation).

JAXB peut ensuite remplir les attributs de ces objets avec les données reçues dans le message SOAP.

---

# Étape 2 — Analyse du contrat WSDL

### Question 1 — URL et opérations proposées par le service

L'URL du service est :

`http://localhost:8080/bib/EmpruntService`

Le service propose deux opérations :

- **emprunterLivre**
    - Message d'entrée : `idAdherent` et `isbn`.
    - Message de sortie : `emprunt`, contenant le numéro d'emprunt et la date de retour.

- **rendreLivre**
    - Message d'entrée : `numeroEmprunt`.
    - Message de sortie : `penalite`, de type `BigDecimal`.

### Question 2 — Style, use et version SOAP du binding

Le binding déclare le style `document` et le use `literal`.

Le service utilise SOAP 1.1, car le binding généré par défaut utilise l'espace de noms SOAP 1.1 :

`http://schemas.xmlsoap.org/wsdl/soap/`

Il s'agit donc d'un service SOAP 1.1 de style document/literal.

### Question 3 — Types XSD générés pour les propriétés

Le type XSD généré pour `isbn` est `xs:string`.

Le type XSD généré pour `dateRetour` est également `xs:string`, car les deux propriétés sont déclarées comme `String` en Java.

La règle de format de l'ISBN définie avec `Pattern.compile()` dans le code Java n'apparaît pas automatiquement dans le schéma XSD généré.

### Question 4 — Identification des paramètres et des espaces de noms

Les paramètres `idAdherent` et `isbn` doivent être examinés dans la requête SOAP générée par SoapUI.

Le préfixe utilisé permet d'identifier l'espace de noms auquel appartiennent les éléments XML.

### Question 5 — Rôle de HTTP, XML, SOAP et WSDL dans l'onglet Raw

Dans l'onglet Raw :

- **HTTP** apparaît dans la ligne de requête et les en-têtes HTTP. Il permet le transport des messages entre le client et le serveur.
- **XML** apparaît dans la structure des messages et permet de représenter les données sous forme de balises.
- **SOAP** apparaît dans l'enveloppe du message (`Envelope`, `Header` et `Body`). Il définit la structure des messages échangés entre le client et le service Web.
- **WSDL** décrit le contrat du service Web : ses opérations, ses messages, ses types de données et son adresse. Il permet au client de connaître la manière de communiquer avec le service.

---

# Étape 3 — Envoi et analyse des messages SOAP

### Question 1 — Pourquoi la requête utilise-t-elle POST, même pour lire une information ?

La requête utilise la méthode HTTP POST parce que le client envoie un message SOAP contenant une enveloppe XML dans le corps de la requête HTTP.

Même si l'opération consiste à lire une information, SOAP utilise POST pour transmettre les messages au service.

### Question 2 — Le bloc jeton du Header a-t-il empêché le traitement ? Pourquoi ?

Non, le bloc jeton n'a pas empêché le traitement de la requête, car le service ne vérifie pas ce jeton. Aucun mécanisme de sécurité n'est configuré pour le traiter. Il est donc ignoré.

Le Header peut contenir des informations supplémentaires, mais leur présence ne signifie pas automatiquement qu'elles seront vérifiées par le serveur.

### Question 3 — Que répond le service au message SOAP 1.2 ? Expliquez avec le binding lu à l'étape 2.

Le service retourne une erreur HTTP 500 avec une SOAP Fault de code `VersionMismatch`.

Cela se produit parce que le binding du service est configuré en SOAP 1.1, alors que la requête envoyée utilise SOAP 1.2. Le serveur attend donc une enveloppe SOAP 1.1 et refuse l'enveloppe SOAP 1.2.

### Question 4 — Quelles sont les cinq erreurs du dernier message et comment les corriger ?

Les cinq erreurs sont :

| N° | Erreur | Correction |
|---|---|---|
| 1 | `soap:envelope` en minuscule | `soap:Envelope` |
| 2 | Mauvais namespace : `http://supnum.mr/bibliotheque` | `http://supnum.mr/bib` |
| 3 | Nom du paramètre incorrect : `numero` | `numeroEmprunt` |
| 4 | Préfixe `bib:` ajouté au paramètre | Utiliser `numeroEmprunt` sans préfixe |
| 5 | Header placé après Body | Placer Header avant Body |

**Message SOAP corrigé :**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope
    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:bib="http://supnum.mr/bib">

    <soap:Header>
        <bib:jeton>abc123</bib:jeton>
    </soap:Header>

    <soap:Body>
        <bib:rendreLivre>
            <numeroEmprunt>E-2026-0812</numeroEmprunt>
        </bib:rendreLivre>
    </soap:Body>

</soap:Envelope>
```

Certaines erreurs peuvent ne pas déclencher d'erreur côté serveur, notamment le jeton ignoré, car aucun mécanisme ne vérifie sa valeur.

Un paramètre incorrect peut également ne pas être rejeté immédiatement si la validation du schéma XML n'est pas activée.

---

# Étape 4 — Provoquer et construire des SOAP Faults

### Question 1 — Pour les cas 1 et 2, quels faultcode et faultstring recevez-vous, et quel code HTTP ?

**Cas 1 : ISBN mal formé**

- Code HTTP : `500 Internal Server Error`
- `faultcode` : `S:Client`
- `faultstring` : `ISBN mal formé : 978-2-10`

**Cas 2 : Livre déjà emprunté**

- Code HTTP : `500 Internal Server Error`
- `faultcode` : `S:Server`
- `faultstring` : `Livre indisponible`

### Question 2 — Dans le cas 2, que contient `<detail>` ? Où sa structure est-elle décrite dans le WSDL ?

Le bloc `<detail>` contient une exception structurée `LivreIndisponible`.

Il contient :

- L'ISBN du livre.
- La date de retour prévue.

La structure de cette exception est décrite dans le WSDL, dans la déclaration de `LivreIndisponible` et de ses éléments.

### Question 3 — Quel code JAX-WS a-t-il choisi pour le livre indisponible ? Est-ce une erreur du client ou du serveur ? Qu'auriez-vous choisi ?

JAX-WS a choisi le code `S:Server`.

Cette Fault est renvoyée par le serveur parce que le livre est indisponible et que l'opération demandée ne peut pas être effectuée.

J'aurais choisi une Fault métier spécifique pour signaler que le livre est déjà emprunté, afin de distinguer cette situation d'une véritable erreur technique du serveur.

### Question 4 — Que se passe-t-il avec `mustUnderstand="1"` ? Et avec `"0"` ?

Avec `mustUnderstand="1"`, le serveur renvoie une Fault `S:MustUnderstand`, car il ne comprend pas le Header `jeton` marqué comme obligatoire.

Avec `mustUnderstand="0"`, le serveur peut ignorer le Header. Le traitement continue et renvoie la Fault métier `Livre indisponible`.

### Question 5 — Quelle différence voyez-vous entre votre Fault 1.2 et les Faults 1.1 reçues ?

La Fault SOAP 1.1 utilise les éléments :

- `<faultcode>`
- `<faultstring>`
- `<detail>`

La Fault SOAP 1.2 utilise une structure différente :

- `<soap:Code>` et `<soap:Value>`
- `<soap:Reason>` et `<soap:Text>`
- `<soap:Detail>`

SOAP 1.2 permet également de préciser la langue du message avec l'attribut `xml:lang`, par exemple `xml:lang="fr"`.

La Fault SOAP 1.2 utilise le code `soap:Sender` pour indiquer une erreur venant du client, tandis que SOAP 1.1 utilise ici `S:Client`.

---

# Fin du compte rendu

Ce document regroupe les réponses des quatre étapes du TP1 : découverte du service Web, analyse du WSDL, envoi des messages SOAP et gestion des SOAP Faults.