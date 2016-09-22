package by.pvt.controller;

import by.pvt.DTO.LoginDTO;
import by.pvt.constants.Message;
import by.pvt.constants.Pages;
import by.pvt.constants.WebErrorMessages;
import by.pvt.exception.ServiceException;
import by.pvt.pojo.Client;
import by.pvt.pojo.Roles;
import by.pvt.pojo.StatusOfClient;
import by.pvt.service.impl.ClientService;
import by.pvt.service.impl.RoleService;
import by.pvt.service.impl.StatusOfClientService;
import by.pvt.util.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static by.pvt.constants.Constants.*;
import static by.pvt.constants.UIParams.*;
import static by.pvt.constants.Pages.*;
import static by.pvt.constants.Message.*;

@org.springframework.stereotype.Controller
public class UserController {
    private static final String REGEX_EMAIL = "^[0-9a-zA-Zа-яА-я]*[@][a-z]*[.][a-z]{1,3}$";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final int MIN_PASSWORD = 7;

    @Autowired
    ClientService clientServices;
    @Autowired
    StatusOfClientService statusOfClientService;
    @Autowired
    RoleService roleService;

    @ExceptionHandler(NullPointerException.class)
    public String handleIOException(HttpServletRequest request) {
        request.setAttribute(WebErrorMessages.EXCEPTION_MESSAGE, MessageManager.getInstance().getValue(EXCEPTION_MESSAGE_I18N, Locale.getDefault()));
        return PAGE_ERROR;
    }

    @RequestMapping(value = VALUE_CLIENT_PAGE, method = RequestMethod.GET)
    public String clientPage() {
        return PAGE_CLIENT;
    }

    @RequestMapping(value = VALUE_ADMIN_PAGE, method = RequestMethod.GET)
    public String adminPage() {
        return PAGE_ADMIN;
    }

    @RequestMapping(value = VALUE_GO_TO_REGISTRATION)
    public String GoToRegistration(ModelMap model) {
        model.put(CLIENT, new Client());
        return PAGE_REGISTRATION;
    }

    @RequestMapping(value = VALUE_FORGOT_PASSWORD)
    public String ForgotPassword() {
        return PAGE_FORGOT_PASSWORD;
    }

    @RequestMapping(value = VALUE_LOGIN, method = RequestMethod.POST)
    public String login(LoginDTO login, Model model, HttpServletRequest request) throws ServiceException {
        //check email and password for regexp
        if (login.getEmail().matches(REGEX_EMAIL)) {
            if (login.getPassword().length() >= 4) {
                //get client with entered params
                Client clientUI = clientServices.login(login.getEmail(), login.getPassword());
               if(clientUI == null){
                    model.addAttribute(WRONG_USER_LOGIN, MessageManager.getInstance().getValue(ERROR_LOGIN, Locale.getDefault()));
                    return PAGE_INDEX;
                }
                model.addAttribute(CLIENT, clientUI);
                HttpSession session = request.getSession();
                session.setAttribute(CLIENT, clientUI);
                return PAGE_USER;
            }
            model.addAttribute(PASSWORD_ERROR, MessageManager.getInstance().getValue(PASSWORD_ERROR_I18N, Locale.getDefault()));
            return PAGE_INDEX;
        }
        model.addAttribute(EMAIL_ERROR, MessageManager.getInstance().getValue(EMAIL_ERROR_I18N, Locale.getDefault()));
        return PAGE_INDEX;
    }

    @RequestMapping(value = VALUE_LOGOUT)
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return PAGE_INDEX;
    }

    @RequestMapping(value = VALUE_GO_TO_EDIT_CLIENT, method = RequestMethod.GET)
    public String editClientData(HttpServletRequest request, ModelMap model) {
        model.put(CLIENT, request.getSession().getAttribute(CLIENT));
        return PAGE_EDIT_CLIENT;
    }

    @RequestMapping(value = VALUE_CHANGE_DATA, method = RequestMethod.POST)
    public String changeClientData(@Valid @ModelAttribute(CLIENT) Client client, BindingResult result, Model model, HttpServletRequest request) throws ServiceException {
        if (result.hasErrors()) {
            model.addAttribute(DATE_ERROR, MessageManager.getInstance().getValue(DATE_ERROR_I18N, Locale.getDefault()));
            return PAGE_EDIT_CLIENT;
        }
        if (client.getPassports().getPassport() == null || client.getPassports().getPassport().length() < MIN_PASSWORD) {
            model.addAttribute(PASSPORT_ERROR, MessageManager.getInstance().getValue(PASSPORT_ERROR_I18N, Locale.getDefault()));
            return PAGE_EDIT_CLIENT;
        }
        Client clientUI = (Client) request.getSession().getAttribute(CLIENT);
        client.setId(clientUI.getId());
        client.setRole(clientUI.getRole());
        client.setStatusOfClient(clientUI.getStatusOfClient());
        clientServices.update(client);
        model.addAttribute(CLIENT, client);

        HttpSession session = request.getSession();
        session.setAttribute(CLIENT, client);
        return PAGE_CLIENT;
    }

    @RequestMapping(value = VALUE_GET_PASSWORD, method = RequestMethod.POST)
    public String getPassword(HttpServletRequest request, @RequestParam String email, Model model) {
        String password = null;
        try {
            password = clientServices.forgotPassword(email);
        } catch (ServiceException e) {
            model.addAttribute(MESSAGE_ERROR_GET_PASSWORD,MessageManager.getInstance().getValue(ERROR_GET_PASSWORD, Locale.getDefault()));
        }
        request.setAttribute(REQUEST_PASSWORD, password);
        return PAGE_FORGOT_PASSWORD;
    }

    @RequestMapping(value = VALUE_NEW_USER, method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute(CLIENT) Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(DATE_ERROR, MessageManager.getInstance().getValue(DATE_ERROR_I18N, Locale.getDefault()));
            return PAGE_REGISTRATION;
        }
        if (client.getPassports().getPassport() == null || client.getPassports().getPassport().length() < MIN_PASSWORD) {
            model.addAttribute(PASSPORT_ERROR, MessageManager.getInstance().getValue(PASSPORT_ERROR_I18N, Locale.getDefault()));
            return PAGE_REGISTRATION;
        }

        try {
            client.setStatusOfClient(statusOfClientService.get(StatusOfClient.class, 1));
            client.setRole(roleService.get(Roles.class, 1));
            clientServices.save(client);
            model.addAttribute(CLIENT, client);
            model.addAttribute(REQUEST_SUCCESS_REGISTRY,
                    MessageManager.getInstance().getValue(Message.SUCCESS_REGISTRY, Locale.getDefault()));
        } catch (ServiceException e) {
            model.addAttribute(MESSAGE_ERROR_SAVE_USER,MessageManager.getInstance().getValue(Message.ERROR_SAVE_OBJECT, Locale.getDefault()));
            model.addAttribute(MESSAGE_NULL_PARAM, e.getMessage());
        }
        return PAGE_INDEX;

    }


    @RequestMapping(value = Pages.VALUE_GET_ALL_USERS, method = RequestMethod.GET)
    public String getAllUserGet() {
        return PAGE_ALL_USERS;
    }

    @RequestMapping(value = VALUE_GET_ALL_USERS, method = RequestMethod.POST)
    public String getAllUsersPost(HttpServletRequest request, Model model) {
        int perPages = Integer.valueOf(request.getParameter(PER_PAGES));
        int page = Integer.valueOf(request.getParameter(PAGES));
        List users = null;
        try {
            users = clientServices.getAll(page, perPages);
        } catch (ServiceException e) {
            model.addAttribute(MESSAGE_ERROR_GET_USERS, MessageManager.getInstance().getValue(Message.ERROR_GET_USERS, Locale.getDefault()));
        }
        model.addAttribute(REQUEST_GET_ALL_USERS, users);
        return PAGE_ALL_USERS;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}