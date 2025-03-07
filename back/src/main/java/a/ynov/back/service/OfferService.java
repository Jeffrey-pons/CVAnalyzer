package a.ynov.back.service;


import a.ynov.back.dto.OfferDto;
import a.ynov.back.entity.Offer;
import a.ynov.back.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferService {

        private final OfferRepository offreRepository;

        /**
         * Sauvegarde une offre dans la base de données.
         * @param offreDto DTO contenant les données de l'offre.
         * @return L'offre sauvegardée.
         */
        public Offer save(OfferDto offreDto) {
                // Vérification de la validité des données avant création de l'offre
                if (offreDto == null || offreDto.description() == null || offreDto.description().isEmpty()) {
                        throw new IllegalArgumentException("La description de l'offre ne peut pas être vide ou nulle.");
                }

                // Création de l'entité Offre à partir du DTO
                Offer offre = new Offer();
                offre.setDescription(offreDto.description());

                // Sauvegarde de l'offre et retour de l'entité sauvegardée
                return offreRepository.save(offre);
        }
}
