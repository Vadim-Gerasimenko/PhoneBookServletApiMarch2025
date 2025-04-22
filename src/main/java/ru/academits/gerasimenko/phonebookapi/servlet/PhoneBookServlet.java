package ru.academits.gerasimenko.phonebookapi.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.academits.gerasimenko.phonebookapi.data.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/contacts/*")
public class PhoneBookServlet extends HttpServlet {
    private PhoneBook phoneBook;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        phoneBook = new PhoneBookInMemory();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configureRequestAndResponse(req, resp);

        List<Contact> contacts = phoneBook.getContacts(req.getParameter("term"));
        mapper.writeValue(resp.getWriter(), contacts);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configureRequestAndResponse(req, resp);

        Contact newContact = ContactProcessor.processContact(mapper.readValue(req.getReader(), Contact.class));
        ObjectNode responseBody = mapper.createObjectNode();

        if (ContactProcessor.isCorrectContact(newContact, responseBody)
                & ContactProcessor.isNotExistingContact(phoneBook, newContact, responseBody)
        ) {
            phoneBook.addContact(newContact);
        }

        mapper.writeValue(resp.getWriter(), responseBody);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configureRequestAndResponse(req, resp);

        Contact newContact = ContactProcessor.processContact(mapper.readValue(req.getReader(), Contact.class));
        ObjectNode responseBody = mapper.createObjectNode();

        if (ContactProcessor.isCorrectContact(newContact, responseBody)
                & ContactProcessor.isExistingContactById(phoneBook, newContact.getId(), responseBody)
                & ContactProcessor.isNotExistingContactOrSame(phoneBook, newContact, responseBody)
        ) {
            phoneBook.updateContact(newContact);
        }

        mapper.writeValue(resp.getWriter(), responseBody);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        configureRequestAndResponse(req, resp);

        ObjectNode responseBody = mapper.createObjectNode();

        try {
            int contactId = Integer.parseInt(req.getHttpServletMapping().getMatchValue());

            if (ContactProcessor.isExistingContactById(phoneBook, contactId, responseBody)) {
                phoneBook.removeContact(contactId);
                ResponseBodyFiller.fill(responseBody, true, null);
            }
        } catch (NumberFormatException e) {
            ResponseBodyFiller.fill(responseBody, false, "Некорректный id");
        }

        mapper.writeValue(resp.getWriter(), responseBody);
    }

    private static void configureRequestAndResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
    }
}