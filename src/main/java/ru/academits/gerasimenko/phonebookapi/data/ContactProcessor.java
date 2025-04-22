package ru.academits.gerasimenko.phonebookapi.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContactProcessor {
    private ContactProcessor() {
    }

    public static Contact processContact(Contact contact) {
        return new Contact(
                contact.getId(),
                TextProcessor.getProcessedText(contact.getName()),
                TextProcessor.getProcessedText(contact.getSurname()),
                TextProcessor.getProcessedText(contact.getPhone())
        );
    }

    public static boolean isCorrectContact(Contact contact, ObjectNode responseBody) {
        if (contact == null) {
            ResponseBodyFiller.fill(responseBody, false, "Некорректный контакт");
            return false;
        }

        if (TextProcessor.isIncorrectField(contact.getName())) {
            ResponseBodyFiller.fill(responseBody, false, "Некорректное имя");
            return false;
        }

        if (TextProcessor.isIncorrectField(contact.getSurname())) {
            ResponseBodyFiller.fill(responseBody, false, "Некорректная фамилия");
            return false;
        }

        if (TextProcessor.isIncorrectField(contact.getPhone())) {
            ResponseBodyFiller.fill(responseBody, false, "Некорректный номер телефона");
            return false;
        }

        ResponseBodyFiller.fill(responseBody, true, null);
        return true;
    }

    public static boolean isNotExistingContact(PhoneBook phoneBook, Contact contact, ObjectNode responseBody) {
        if (phoneBook.getContacts("").stream()
                .anyMatch(c -> c.getPhone().equals(contact.getPhone()))
        ) {
            ResponseBodyFiller.fill(responseBody, false, "Контакт с таким номером уже существует");
            return false;
        }

        return true;
    }

    public static boolean isNotExistingContactOrSame(PhoneBook phoneBook, Contact contact, ObjectNode responseBody) {
        if (phoneBook.getContacts("").stream()
                .anyMatch(c -> c.getId() != contact.getId() && c.getPhone().equals(contact.getPhone()))
        ) {
            ResponseBodyFiller.fill(responseBody, false, "Контакт с таким номером уже существует");
            return false;
        }

        return true;
    }

    public static boolean isExistingContactById(PhoneBook phoneBook, int id, ObjectNode responseBody) {
        if (phoneBook.getContacts("").stream().noneMatch(c -> c.getId() == id)
        ) {
            ResponseBodyFiller.fill(responseBody, false, "Контакт не найден");
            return false;
        }

        return true;
    }
}