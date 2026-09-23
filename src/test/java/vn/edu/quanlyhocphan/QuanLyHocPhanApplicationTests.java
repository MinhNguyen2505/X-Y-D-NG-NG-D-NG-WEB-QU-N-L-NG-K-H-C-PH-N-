package vn.edu.quanlyhocphan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.ConcurrentModel;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.spring6.view.ThymeleafView;
import vn.edu.quanlyhocphan.controller.QuanLyController;

import java.util.Locale;

@SpringBootTest
class QuanLyHocPhanApplicationTests {

    @Autowired
    private QuanLyController quanLyController;

    @Autowired
    private ThymeleafViewResolver viewResolver;

    @Test
    void contextLoads() {
    }

    @Test
    void testRenderQuanLyThiLai() throws Exception {
        ConcurrentModel model = new ConcurrentModel();
        String viewName = quanLyController.danhSachThiLai("CHO_DUYET", model);
        ThymeleafView view = (ThymeleafView) viewResolver.resolveViewName(viewName, Locale.getDefault());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        view.render(model, request, response);
    }
}