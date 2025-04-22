package ru.academits.gerasimenko.phonebookapi.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class PhoneBookInMemory implements PhoneBook {
    private final List<Contact> contacts;
    private final AtomicInteger newContactId = new AtomicInteger(1);

    public PhoneBookInMemory() {
        contacts = new ArrayList<>();
    }

    @Override
    public List<Contact> getContacts(String term) {
        synchronized (contacts) {
            String containedTerm = Objects.requireNonNullElse(term, "").toLowerCase();
            Stream<Contact> contactsStream = contacts.stream();

            if (!containedTerm.isEmpty()) {
                contactsStream = contactsStream.filter(c -> c.getName().toLowerCase().contains(containedTerm)
                        || c.getSurname().toLowerCase().contains(containedTerm)
                        || c.getPhone().toLowerCase().contains(containedTerm)
                );
            }

            return contactsStream.map(Contact::new).toList();
        }
    }

    @Override
    public void addContact(Contact contact) {
        synchronized (contacts) {
            int id = newContactId.getAndIncrement();
            contacts.add(new Contact(
                    id,
                    contact.getName(),
                    contact.getSurname(),
                    contact.getPhone()
            ));
        }
    }

    @Override
    public void updateContact(Contact contact) {
        synchronized (contacts) {
            int updatedContactId = contact.getId();

            Contact updatedContact = contacts.stream()
                    .filter(c -> c.getId() == updatedContactId)
                    .findFirst()
                    .orElse(null);

            if (updatedContact == null) {
                throw new IllegalArgumentException("Contact with id = " + updatedContactId + " not found");
            }

            updatedContact.setName(contact.getName());
            updatedContact.setSurname(contact.getSurname());
            updatedContact.setPhone(contact.getPhone());
        }
    }

    @Override
    public void removeContact(int contactId) {
        synchronized (contacts) {
            contacts.removeIf(c -> c.getId() == contactId);
        }
    }
}