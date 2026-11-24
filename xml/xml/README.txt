SOAP XML files for the emprunt project

Existing files (01-04) are preserved exactly as supplied:
01-emprunter.xml              SOAP 1.1 borrow request with a SOAP header
02-emprunter-12.xml           SOAP 1.2 borrow request
03-fault-sender-12.xml        SOAP 1.2 Sender fault example for malformed ISBN
04-rendre-invalide.xml        Return request using an invalid/nonexistent number

Added files:
05-emprunter-success-response.xml       Example successful borrow response
06-emprunter-indisponible-response.xml  Example unavailable-book SOAP fault
07-rendre-response.xml                  Example return response

Important: response XML files are documentation/examples. The actual response
must be copied from your running service if it differs. The existing 03 fault
is a SOAP 1.2 Sender fault; the 06 unavailable-book fault is a SOAP 1.1
example matching the WSDL fault bean shown in the project discussion.
