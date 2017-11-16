package hello.controller;

import hello.domain.PersonForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
@Controller
public class WebController extends WebMvcConfigurerAdapter{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    /**
     * 它在其方法签名中包含一个PersonForm，所以form.html模板可以将表单属性与一个PersonForm相关联
     * @param personForm
     * @return
     */
    @GetMapping("/")
    public String showForm(PersonForm personForm) {
        return "form";
    }

    /**
     * personForm参数收集您将要构建的表单中填写的属性，您可以从绑定到PersonForm对象的表单中检索所有属性
     * 一个bindingResult对象用于验证参数错误
     * @param personForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/")
    public String checkPersonInfo(@Valid PersonForm personForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        return "redirect:/results";
    }
}
