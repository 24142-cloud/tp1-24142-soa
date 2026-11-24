
package mr.supnum.bib.impl;

import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceException;

import mr.supnum.bib.contrat.Emprunt;
import mr.supnum.bib.contrat.EmpruntPortType;
import mr.supnum.bib.contrat.LivreIndisponible;
import mr.supnum.bib.contrat.LivreIndisponibleFault;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@WebService(
        serviceName = "EmpruntService",
        portName = "EmpruntPort",
        targetNamespace = "http://supnum.mr/bib",
        endpointInterface = "mr.supnum.bib.contrat.EmpruntPortType"
)
public class EmpruntServiceImpl implements EmpruntPortType {

    private final AtomicInteger compteur = new AtomicInteger(812);

    @Override
    public Emprunt emprunterLivre(String idAdherent, String isbn)
            throws LivreIndisponibleFault {

        if (isbn == null || !isbn.matches("97[89]-[0-9]-[0-9]{2,5}-[0-9]{2,6}-[0-9]")) {
            throw new WebServiceException("ISBN mal forme : " + isbn);
        }

        if ("978-2-10-074444-4".equals(isbn)) {

            LivreIndisponible info = new LivreIndisponible();
            info.setIsbn(isbn);
            info.setRetourPrevu(
                    toXmlDate(LocalDate.of(2026, 12, 2))
            );

            throw new LivreIndisponibleFault(
                    "Livre indisponible",
                    info
            );
        }

        Emprunt emprunt = new Emprunt();

        emprunt.setNumero(
                String.format("E-2026-%04d", compteur.getAndIncrement())
        );

        emprunt.setDateRetour(
                toXmlDate(LocalDate.now().plusDays(14))
        );

        return emprunt;
    }

    @Override
    public BigDecimal rendreLivre(String numeroEmprunt) {
        return BigDecimal.ZERO;
    }

    private static XMLGregorianCalendar toXmlDate(LocalDate date) {
        try {
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(date.toString());

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Impossible de convertir la date", e
            );
        }
    }
}
