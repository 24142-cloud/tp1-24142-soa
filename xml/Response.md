# SOAP Web Service – Test Responses

## Files included

The `xml/` directory contains the four XML files supplied in the original archive, unchanged, plus three response examples.

| File | Purpose |
|---|---|
| `01-emprunter.xml` | SOAP 1.1 borrow request with header token |
| `02-emprunter-12.xml` | SOAP 1.2 borrow request |
| `03-fault-sender-12.xml` | SOAP 1.2 Sender fault example for malformed ISBN |
| `04-rendre-invalide.xml` | Return request with an invalid/nonexistent loan number |
| `05-emprunter-success-response.xml` | Successful borrow response example |
| `06-emprunter-indisponible-response.xml` | Unavailable-book fault example |
| `07-rendre-response.xml` | Return response example |

## Successful borrow

A successful borrow response contains an `emprunt` element with the loan number and expected return date. A sample response is provided in `xml/05-emprunter-success-response.xml`.

## Unavailable book

The unavailable-book case is represented by a SOAP fault with ISBN and expected return date in its detail. See `xml/06-emprunter-indisponible-response.xml`.

## Malformed ISBN

The supplied `xml/03-fault-sender-12.xml` is a SOAP 1.2 Sender fault example for an incorrectly formatted ISBN.

## Return

The supplied `xml/04-rendre-invalide.xml` is the return request. The example response in `xml/07-rendre-response.xml` shows a penalty value of `0`, consistent with the successful return test previously shared.

## Note

The response files are examples for submission documentation. Use the exact XML returned by your own running server if it differs. The SOAP version and envelope namespace must match the endpoint binding: SOAP 1.1 uses `http://schemas.xmlsoap.org/soap/envelope/`, while SOAP 1.2 uses `http://www.w3.org/2003/05/soap-envelope`.
