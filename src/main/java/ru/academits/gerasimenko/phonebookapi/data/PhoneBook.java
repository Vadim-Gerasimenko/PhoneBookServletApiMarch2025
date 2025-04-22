package ru.academits.gerasimenko.phonebookapi.data;

import java.util.List;

public interface PhoneBook {
    List<Contact> getContacts(String term);

    void addContact(Contact contact);

    void updateContact(Contact contact);

    void removeContact(int contactId);
}