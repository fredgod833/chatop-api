package fr.fredgodard.chatop.service;

import fr.fredgodard.chatop.exceptions.MessageException;
import fr.fredgodard.chatop.model.Message;
import fr.fredgodard.chatop.repository.MessageRepository;
import fr.fredgodard.chatop.repository.RentalsRepository;
import fr.fredgodard.chatop.repository.entities.MessageEntity;
import fr.fredgodard.chatop.repository.entities.RentalEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@Slf4j
public class MessageService {

    private MessageRepository messageRepository;
    private RentalsRepository rentalRepository;

    private PasswordEncoder encoder;
    public MessageService(MessageRepository messageRepository, RentalsRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.rentalRepository = rentalRepository;
    }

    private static Message entity2Bean(MessageEntity entity) {
        if (entity == null) {
            return null;
        }
        Message result = new Message();
        result.setOwnerId(entity.getUserId());
        result.setRentalId(entity.getRentalId());
        return result;
    }

    private MessageEntity message2Entity(Message message) {
        MessageEntity result = new MessageEntity();
        result.setUserId(message.getOwnerId());
        result.setRentalId(message.getOwnerId());
        result.setMessage(message.getMessage());
        return result;
    }

    public Message saveMessage(final Message message) throws MessageException {
        if (message.getOwnerId() == null) {
            Optional<RentalEntity> rental = rentalRepository.findById(message.getRentalId());
            if (! rental.isPresent()) {
                throw new MessageException("Rental not found !");
            }
            message.setOwnerId(rental.get().getOwnerId());
        }
        return entity2Bean(messageRepository.save(message2Entity(message)));
    }


}
