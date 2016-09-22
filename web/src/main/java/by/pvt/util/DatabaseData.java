package by.pvt.util;

import by.pvt.VO.CarDTO;
import by.pvt.VO.CarSortingDTO;
import by.pvt.constants.Constants;
import by.pvt.constants.Message;
import by.pvt.constants.UIParams;
import by.pvt.exception.ServiceException;
import by.pvt.pojo.BodyType;
import by.pvt.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

import static by.pvt.constants.UIParams.MESSAGE_NULL_LIST;

@Component("dataBase")
public class DatabaseData {
    private static final int MIN_PER_PAGE = 5;
    private static final int PAGE_FOR_PAGINATION = 1;
    private static final int START_ROW = 0;
    private static final int ROWS_PER_PAGE = 100;

    @Autowired
    private Filter filter;
    @Autowired
    private BrandsService brandsService;
    @Autowired
    private EngineTypeService engineTypeService;
    @Autowired
    private BodyTypeService bodyTypeService;
    @Autowired
    private TransmissionTypeService transmissionTypeService;
    @Autowired
    private CarService carService;

    //the method get params for cars and put it to session
    public void setToSessionCarParams(HttpServletRequest request, Model model) {
        List brands;
        List<BodyType> bodyType;
        List engineType;
        List transmissionType;
        try {
            HttpSession session = request.getSession();
            brands = brandsService.getAll(START_ROW, ROWS_PER_PAGE);
            session.setAttribute(UIParams.REQUEST_ALL_BRANDS, brands);
            bodyType = bodyTypeService.getAll(START_ROW, ROWS_PER_PAGE);
            session.setAttribute(UIParams.REQUEST_ALL_BODY_TYPES, bodyType);
            engineType = engineTypeService.getAll(START_ROW, ROWS_PER_PAGE);
            session.setAttribute(UIParams.REQUEST_ALL_ENGINE_TYPES, engineType);
            transmissionType = transmissionTypeService.getAll(START_ROW, ROWS_PER_PAGE);
            session.setAttribute(UIParams.REQUEST_ALL_TRANSMISSION_TYPES, transmissionType);
        } catch (ServiceException e) {
            model.addAttribute(MESSAGE_NULL_LIST, MessageManager.getInstance().getValue(Message.ERROR_NULL_LIST, Locale.getDefault()));
        }
    }

    //the method get cars by filter and sort params
    public void getCarsListByFilter(HttpServletRequest request, Model model) {
        Pagination pagination = Pagination.getInstance();
        Sorting sorting = Sorting.getInstance();
        CarDTO carDTO = filter.getCarFilter(request);
        //get pagination params
        int pagesCount = 0;
        try {
            pagesCount = (int) (carService.getCountCars(carDTO) / pagination.getPerPage(request));
        } catch (ServiceException e) {
            model.addAttribute(UIParams.MESSAGE_GET_COUNT,MessageManager.getInstance().getValue(Message.ERROR_GET_COUNT, Locale.getDefault()));
        }
        pagesCount = pagination.getPagesCount(pagesCount);
        //put sorting params
        CarSortingDTO sortingDTO = sorting.getSortingParam(request);
        sortingDTO.setASC(sorting.getSorting(request));
        //get the resulting list after filtering and sorting
        try {
            List allCar = carService.getCarByFilter(carDTO, pagination.getStartRow(request) - PAGE_FOR_PAGINATION, pagination.getPerPage(request), sortingDTO);
            model.addAttribute(UIParams.REQUEST_GET_CARS, allCar);
        } catch (ServiceException e) {
            model.addAttribute(UIParams.MESSAGE_GET_LIST_CARS,MessageManager.getInstance().getValue(Message.ERROR_GET_ALL_ORDERS, Locale.getDefault()));
        }
        request.setAttribute(Constants.TOTAL_PAGE, pagesCount);

    }


}